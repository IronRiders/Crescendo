package org.ironriders.commands;

import com.pathplanner.lib.auto.AutoBuilder;
import edu.wpi.first.math.geometry.*;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import org.ironriders.constants.Drive;
import org.ironriders.lib.Utils;
import org.ironriders.subsystems.DriveSubsystem;
import swervelib.SwerveController;
import swervelib.SwerveDrive;

import java.util.function.DoubleSupplier;

import static org.ironriders.constants.Drive.MAX_SPEED;

public class DriveCommands {
    private final DriveSubsystem drive;
    private final SwerveDrive swerve;


    public DriveCommands(DriveSubsystem drive) {
        this.drive = drive;

        this.swerve = drive.getSwerveDrive();
    }

    public Command teleopCommand(DoubleSupplier x, DoubleSupplier y, DoubleSupplier hX, DoubleSupplier hY) {
        return drive.runOnce(() -> {
            if (DriverStation.isAutonomous()) return;

            double invert = Utils.getAlliance().equals(DriverStation.Alliance.Blue) ? -1 : 1;

            ChassisSpeeds desiredSpeeds = swerve.getSwerveController().getTargetSpeeds(
                    x.getAsDouble(),
                    y.getAsDouble(),
                    hX.getAsDouble() * invert,
                    hY.getAsDouble() * invert,
                    swerve.getOdometryHeading().getRadians(),
                    MAX_SPEED
            );

            drive.drive(
                    SwerveController.getTranslation2d(desiredSpeeds),
                    desiredSpeeds.omegaRadiansPerSecond,
                    true
            );
        });
    }

    public Command setHeading(Drive.Heading heading) {
        return Commands.runOnce(() -> swerve.getSwerveController().lastAngleScalar = Units.degreesToRadians(heading.getHeading()));
    }

    public Command zeroGyro() {
        return drive.runOnce(swerve::zeroGyro);
    }

    /**
     * Generates a path to the specified destination using default settings false for velocity control which will stop
     * the robot abruptly when it reaches the target.
     *
     * @param target The destination pose represented by a Pose2d object.
     * @return A Command object representing the generated path to the destination.
     */
    public Command pathFindTo(Pose2d target) {
        return pathFindTo(target, false);
    }

    /**
     * Generates a path to the specified destination with options for velocity control.
     *
     * @param target              The destination pose represented by a Pose2d object.
     * @param preserveEndVelocity A boolean flag indicating whether to preserve velocity at the end of the path.
     *                            If true, the path will end with the robot going the max velocity; if false, it will
     *                            stop abruptly.
     * @return A Command object representing the generated path to the destination.
     */
    public Command pathFindTo(Pose2d target, boolean preserveEndVelocity) {
        return AutoBuilder.pathfindToPose(
                target,
                drive.getPathfindingConstraint().getConstraints(),
                preserveEndVelocity ? drive.getPathfindingConstraint().getConstraints().maxVelocityMPS() : 0
        );
    }

    /**
     * Generates a path to a specified target identified by a vision tag. This will run only if the id is provided if
     * the id is not provided it will return Command that does nothing and immediately closes itself.
     *
     * @param id     The identifier of the vision tag.
     * @param offset The transformation to be applied to the identified target's pose.
     * @return A Command object representing the generated path to the identified target.
     */

    public Command lockPose() {
        return drive.runOnce(swerve::lockPose);
    }



    public Command resetOdometry() {
        return resetOdometry(new Pose2d());
    }

    public Command resetOdometry(Pose2d pose) {
        return drive.runOnce(() -> drive.getSwerveDrive().resetOdometry(pose));
    }
}
