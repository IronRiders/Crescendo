package org.ironriders.subsystems;

import com.pathplanner.lib.auto.AutoBuilder;
import com.pathplanner.lib.config.PIDConstants;
import com.pathplanner.lib.config.RobotConfig;
import com.pathplanner.lib.controllers.PPHolonomicDriveController;
import com.pathplanner.lib.util.PathPlannerLogging;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.trajectory.Trajectory;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import edu.wpi.first.wpilibj.Filesystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import org.ironriders.commands.DriveCommands;
import org.ironriders.constants.Auto;
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
import static org.ironriders.constants.Drive.DASHBOARD_PREFIX;
import static org.ironriders.constants.Drive.HeadingController.*;
import static org.ironriders.constants.Drive.MAX_SPEED;
import static org.ironriders.constants.Drive.Wheels.DRIVE_CONVERSION_FACTOR;
import static org.ironriders.constants.Drive.Wheels.STEERING_CONVERSION_FACTOR;

public class DriveSubsystem extends SubsystemBase {
    private final DriveCommands commands;
    private final SwerveDrive swerveDrive;

    private final PIDController headingPID = new PIDController(P, I, D);

    private final EnumSendableChooser<PathfindingConstraintProfile> constraintProfile = new EnumSendableChooser<>(
            PathfindingConstraintProfile.class,
            PathfindingConstraintProfile.getDefault(),
            Auto.DASHBOARD_PREFIX + "pathfindingConstraintProfile"
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
        var HOLONOMIC_CONFIG = new PPHolonomicDriveController( // PPHolonomicController
                                                                                                      // is the built in
                                                                                                      // path following
                                                                                                      // controller for
                                                                                                      // holonomic drive
                                                                                                      // trains
            new PIDConstants(5.0, 0.0, 0.0), // Translation PID
            new PIDConstants(5.0, 0.0, 0.0) // Rotation PID
         );
        RobotConfig robotConfig = null;
		try {
			robotConfig = RobotConfig.fromGUISettings();
		} catch (Exception e) {
			throw new RuntimeException("Could not load path planner config", e);
		}
        AutoBuilder.configure(
				swerveDrive::getPose,
				swerveDrive::resetOdometry,
				swerveDrive::getRobotVelocity,
				(speeds, feedforwards) -> swerveDrive.setChassisSpeeds(speeds),
				HOLONOMIC_CONFIG,
				robotConfig,
				() -> {
					var alliance = DriverStation.getAlliance();
					if (alliance.isPresent()) {
						return alliance.get() == DriverStation.Alliance.Red;
					}
					return false;
				},
				this);
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

        headingPID.enableContinuousInput(0, 360);

        SmartDashboard.putNumber(DASHBOARD_PREFIX + "heading", swerveDrive.getOdometryHeading().getDegrees());
    }

    public void drive(Translation2d translation, double radiansPerSecond, boolean fieldRelative) {
        translation = Utils.getAlliance().equals(Alliance.Blue) ? Utils.invertTranslation(translation) : translation;

        swerveDrive.drive(translation, radiansPerSecond, fieldRelative, false);
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
