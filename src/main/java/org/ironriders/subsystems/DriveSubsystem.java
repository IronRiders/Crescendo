package org.ironriders.subsystems;

import com.pathplanner.lib.auto.AutoBuilder;
import com.pathplanner.lib.util.HolonomicPathFollowerConfig;
import com.pathplanner.lib.util.PathPlannerLogging;
import com.pathplanner.lib.util.ReplanningConfig;
import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.trajectory.Trajectory;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import edu.wpi.first.wpilibj.Filesystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import org.ironriders.commands.DriveCommands;
import org.ironriders.constants.Drive;
import org.ironriders.lib.Utils;
import org.ironriders.lib.sendable_choosers.EnumSendableChooser;
import swervelib.SwerveDrive;
import swervelib.parser.SwerveParser;
import swervelib.telemetry.SwerveDriveTelemetry;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.ironriders.constants.Auto.PathfindingConstraintProfile;
import static org.ironriders.constants.Drive.*;
import static org.ironriders.constants.Drive.HeadingController.*;
import static org.ironriders.constants.Drive.Wheels.DRIVE_CONVERSION_FACTOR;
import static org.ironriders.constants.Drive.Wheels.STEERING_CONVERSION_FACTOR;
import static org.ironriders.constants.Robot.Dimensions;

public class DriveSubsystem extends SubsystemBase {
    private final DriveCommands commands;
    private final VisionSubsystem vision = new VisionSubsystem();
    private final SwerveDrive swerveDrive;

    private final PIDController headingPID = new PIDController(P, I, D);
    private HeadingMode headingMode = HeadingMode.STRAIGHT;

    private final EnumSendableChooser<PathfindingConstraintProfile> constraintProfile = new EnumSendableChooser<>(
            PathfindingConstraintProfile.class,
            PathfindingConstraintProfile.getDefault(),
            DASHBOARD_PREFIX + "pathfindingConstraintProfile"
    );

    public DriveSubsystem() {
        try {
            swerveDrive = new SwerveParser(
                    new File(Filesystem.getDeployDirectory(), Drive.SWERVE_CONFIG_LOCATION)
            ).createSwerveDrive(MAX_SPEED, STEERING_CONVERSION_FACTOR, DRIVE_CONVERSION_FACTOR);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        SwerveDriveTelemetry.verbosity = SwerveDriveTelemetry.TelemetryVerbosity.HIGH;

        AutoBuilder.configureHolonomic(
                swerveDrive::getPose,
                swerveDrive::resetOdometry,
                swerveDrive::getRobotVelocity,
                swerveDrive::setChassisSpeeds,
                new HolonomicPathFollowerConfig(
                        4.5,
                        Dimensions.DRIVEBASE_RADIUS,
                        new ReplanningConfig()
                ),
                () -> Utils.getAlliance().equals(Alliance.Red),
                this
        );

        commands = new DriveCommands(this);
    }

    @Override
    public void periodic() {
        PathPlannerLogging.setLogActivePathCallback((poses) -> {
            if (poses.isEmpty()) return;

            List<Trajectory.State> states = new ArrayList<>();
            for (Pose2d pose : poses) {
                Trajectory.State state = new Trajectory.State();
                state.poseMeters = pose;
                states.add(state);
            }

            swerveDrive.postTrajectory(new Trajectory(states));
        });

        getVision().getPoseEstimate().ifPresent(estimatedRobotPose -> swerveDrive.addVisionMeasurement(
                estimatedRobotPose.estimatedPose.toPose2d(),
                estimatedRobotPose.timestampSeconds
        ));

        SmartDashboard.putData(DASHBOARD_PREFIX + "headingPID", headingPID);
        SmartDashboard.putString(DASHBOARD_PREFIX + "headingMode", headingMode.toString());
    }

    public void drive(Translation2d translation, double rotation, boolean fieldRelative, boolean isOpenLoop) {
        if (!headingMode.isFree()) {
            rotation = MathUtil.clamp(
                    headingPID.calculate(
                            Utils.rotationalError(headingMode.getHeading(), swerveDrive.getYaw().getDegrees()),
                            0
                    ),
                    -SPEED_CAP,
                    SPEED_CAP
            );
        }

        swerveDrive.drive(translation, rotation, fieldRelative, isOpenLoop);
    }

    public void setHeadingMode(HeadingMode headingMode) {
        this.headingMode = headingMode;
    }

    public VisionSubsystem getVision() {
        return vision;
    }

    public PathfindingConstraintProfile getPathfindingConstraint() {
        return constraintProfile.getSelected();
    }

    public SwerveDrive getSwerveDrive() {
        return swerveDrive;
    }

    public DriveCommands getCommands() {
        return commands;
    }
}
