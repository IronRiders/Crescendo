package org.ironriders.subsystems;

import com.fasterxml.jackson.core.TreeNode;
import com.pathplanner.lib.auto.AutoBuilder;
import com.pathplanner.lib.util.HolonomicPathFollowerConfig;
import com.pathplanner.lib.util.PathPlannerLogging;
import com.pathplanner.lib.util.ReplanningConfig;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.trajectory.Trajectory;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import edu.wpi.first.wpilibj.Filesystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import org.ironriders.commands.DriveCommands;
import org.ironriders.constants.Auto;
import org.ironriders.constants.Drive.*;
import org.ironriders.constants.Drive;
import org.ironriders.lib.Utils;
import org.ironriders.lib.sendable_choosers.EnumSendableChooser;
import swervelib.SwerveDrive;
import swervelib.SwerveDrive.*;
import swervelib.parser.SwerveParser;
import edu.wpi.first.math.MathUtil;
import swervelib.telemetry.SwerveDriveTelemetry;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.ironriders.constants.Auto.PathfindingConstraintProfile;
import static org.ironriders.constants.Drive.DASHBOARD_PREFIX;
import static org.ironriders.constants.Drive.HeadingController.*;
import static org.ironriders.constants.Drive.MAX_SPEED;
import static org.ironriders.constants.Drive.Wheels.DRIVE_CONVERSION_FACTOR;
import static org.ironriders.constants.Drive.Wheels.STEERING_CONVERSION_FACTOR;
import static org.ironriders.constants.Robot.Dimensions;

public class DriveSubsystem extends SubsystemBase {
    private final DriveCommands commands;
    private final VisionSubsystem vision;
    private final SwerveDrive swerveDrive;

    private final PIDController headingPID = new PIDController(P, I, D);

    // stuff for secondary driver control
    private boolean primaryControlEnabled = true;
    private Drive.Heading targetHeading;

    private final EnumSendableChooser<PathfindingConstraintProfile> constraintProfile = new EnumSendableChooser<>(
            PathfindingConstraintProfile.class,
            PathfindingConstraintProfile.getDefault(),
            Auto.DASHBOARD_PREFIX + "pathfindingConstraintProfile"
    );

    public DriveSubsystem(VisionSubsystem vision) {
        try {
            swerveDrive = new SwerveParser(
                    new File(Filesystem.getDeployDirectory(), Drive.SWERVE_CONFIG_LOCATION)
            ).createSwerveDrive(MAX_SPEED, STEERING_CONVERSION_FACTOR, DRIVE_CONVERSION_FACTOR);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        this.vision = vision;

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
                () -> Utils.getAlliance() == Alliance.Red,
                this
        );

        commands = new DriveCommands(this);
    }

    @Override
    public void periodic() {
        getVision().getPoseEstimate().ifPresent(estimatedRobotPose -> swerveDrive.addVisionMeasurement(
                estimatedRobotPose.estimatedPose.toPose2d(),
                estimatedRobotPose.timestampSeconds
        ));

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

        // Check if reached desired angle from sec. driver
        if (!primaryControlEnabled) {
            SmartDashboard.putNumber("! targetHeading", targetHeading.getHeading());
            SmartDashboard.putNumber("! currentHeading", swerveDrive.getOdometryHeading().getDegrees());
            if (
                Math.abs(
                    targetHeading.getHeading() - 
                    swerveDrive.getOdometryHeading().getDegrees()
                )
                <= Drive.ANGLE_TOLERANCE
            ) {
                primaryControlEnabled = true;
            }
        }

        headingPID.enableContinuousInput(0, 360);

        SmartDashboard.putNumber(DASHBOARD_PREFIX + "heading", swerveDrive.getOdometryHeading().getDegrees());
    }

    public void drive(Translation2d translation, double radiansPerSecond, boolean fieldRelative) {
        translation = Utils.getAlliance().equals(Alliance.Blue) ? Utils.invertTranslation(translation) : translation;
        swerveDrive.drive(translation, radiansPerSecond, fieldRelative, true);
    }

    public boolean isPrimaryControlEnabled() {
        return primaryControlEnabled;
    }

    public void setTargetHeading(Drive.Heading heading) {
        primaryControlEnabled = false;
        targetHeading = heading;
    }

    public double getDesiredHeading() {
        return targetHeading.getHeading();
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
