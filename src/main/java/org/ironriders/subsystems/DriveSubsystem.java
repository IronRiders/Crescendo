package org.ironriders.subsystems;

import com.pathplanner.lib.auto.AutoBuilder;
import com.pathplanner.lib.util.HolonomicPathFollowerConfig;
import com.pathplanner.lib.util.PathPlannerLogging;
import com.pathplanner.lib.util.ReplanningConfig;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation3d;
import edu.wpi.first.math.trajectory.Trajectory;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import edu.wpi.first.wpilibj.Filesystem;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import org.ironriders.commands.DriveCommands;
import org.ironriders.constants.Drive;
import org.ironriders.lib.sendable_choosers.EnumSendableChooser;
import swervelib.SwerveDrive;
import swervelib.parser.SwerveParser;
import swervelib.telemetry.SwerveDriveTelemetry;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.ironriders.constants.Auto.DASHBOARD_PREFIX;
import static org.ironriders.constants.Auto.PathfindingConstraintProfile;
import static org.ironriders.constants.Drive.MAX_SPEED;
import static org.ironriders.constants.Drive.Wheels.DRIVE_CONVERSION_FACTOR;
import static org.ironriders.constants.Drive.Wheels.STEERING_CONVERSION_FACTOR;
import static org.ironriders.constants.Robot.Dimensions;

public class DriveSubsystem extends SubsystemBase {
    private final DriveCommands commands;
    private final VisionSubsystem vision = new VisionSubsystem();
    private final SwerveDrive swerveDrive;
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
                () -> DriverStation.getAlliance().orElse(Alliance.Blue).equals(Alliance.Red),
                this
        );

        commands = new DriveCommands(this);
    }

    @Override
    public void periodic() {
        PathPlannerLogging.setLogActivePathCallback((poses) -> {
            List<Trajectory.State> states = new ArrayList<>();
            for (Pose2d pose : poses) {
                Trajectory.State state = new Trajectory.State();
                state.poseMeters = pose;
                states.add(state);
            }

            swerveDrive.postTrajectory(new Trajectory(states));
        });

        getVision().getPoseEstimate().ifPresent(estimatedRobotPose -> {
            swerveDrive.resetOdometry(estimatedRobotPose.estimatedPose.toPose2d());
            swerveDrive.setGyro(estimatedRobotPose.estimatedPose.getRotation());
//            swerveDrive.addVisionMeasurement(
//                    estimatedRobotPose.estimatedPose.toPose2d(),
//                    estimatedRobotPose.timestampSeconds
//            );
//            swerveDrive.setGyro(estimatedRobotPose.estimatedPose.getRotation());
        });
    }

    public DriveCommands getCommands() {
        return commands;
    }

    public void setGyro(Rotation3d rotation) {
        swerveDrive.setGyro(new Rotation3d());
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
}
