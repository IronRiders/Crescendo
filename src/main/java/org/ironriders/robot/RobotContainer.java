// Copyright (c) FIRST and other WPILib contributors.

// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package org.ironriders.robot;

import com.pathplanner.lib.auto.AutoBuilder;
import edu.wpi.first.wpilibj.RobotBase;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.button.CommandGenericHID;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import org.ironriders.commands.ClimberCommands;
import org.ironriders.commands.DriveCommands;
import org.ironriders.commands.LauncherCommands;
import org.ironriders.commands.RobotCommands;
import org.ironriders.constants.Drive;
import org.ironriders.constants.Identifiers;
import org.ironriders.lib.Utils;
import org.ironriders.subsystems.*;

import static org.ironriders.constants.Drive.CLIMBING_MODE_SPEED;
import static org.ironriders.constants.Teleop.Controllers.Joystick;

public class RobotContainer {
    private final DriveSubsystem drive = new DriveSubsystem();
    private final DriveCommands driveCommands = drive.getCommands();
    private final LauncherSubsystem launcher = new LauncherSubsystem();
    private final LauncherCommands launcherCommands = launcher.getCommands();
    private final PivotSubsystem pivot = new PivotSubsystem();
    private final ManipulatorSubsystem manipulator = new ManipulatorSubsystem();
    private final ClimberSubsystem climber = new ClimberSubsystem(drive);
    private final ClimberCommands climberCommands = climber.getCommands();
    @SuppressWarnings("unused")
    private final LightingSubsystem lighting = new LightingSubsystem();
    private final RobotCommands commands = new RobotCommands(drive, launcher, pivot, manipulator, climber);

    private final CommandXboxController primaryController =
            new CommandXboxController(Identifiers.Controllers.PRIMARY_CONTROLLER);
    private final CommandGenericHID secondaryController =
            new CommandGenericHID(Identifiers.Controllers.SECONDARY_CONTROLLER);



    public RobotContainer() {
        configureBindings();
    }

    private void configureBindings() {
        if (RobotBase.isSimulation()) return;

        // Primary Driver
        drive.setDefaultCommand(
                drive.getCommands().teleopCommand(
                        () -> -controlCurve(primaryController.getLeftY()),
                        () -> -controlCurve(primaryController.getLeftX()),
                        () -> -controlCurve(primaryController.getRightX())
                )
        );

        primaryController.leftTrigger().onTrue(commands.launch());
        primaryController.rightTrigger().onTrue(commands.startGroundPickup()).onFalse(commands.endGroundPickup());

        primaryController.leftBumper().whileTrue(climberCommands.left());
        primaryController.rightBumper().whileTrue(climberCommands.right());

        primaryController.x().onTrue(driveCommands.headingMode(Drive.HeadingMode.SPEAKER_LEFT));
        primaryController.y().onTrue(driveCommands.headingMode(Drive.HeadingMode.STRAIGHT));
        primaryController.b().onTrue(driveCommands.headingMode(Drive.HeadingMode.SPEAKER_RIGHT));
        primaryController.a().onTrue(commands.toggleClimbingMode());

        // Secondary Controller
        secondaryController.button(1).onTrue(driveCommands.headingMode(Drive.HeadingMode.SPEAKER_LEFT));
        secondaryController.button(2).onTrue(driveCommands.headingMode(Drive.HeadingMode.STRAIGHT));
        secondaryController.button(4).onTrue(driveCommands.headingMode(Drive.HeadingMode.SPEAKER_RIGHT));

        secondaryController.button(5).onTrue(driveCommands.headingMode(Drive.HeadingMode.STAGE_LEFT));
        secondaryController.button(6).onTrue(driveCommands.headingMode(Drive.HeadingMode.STRAIGHT));
        secondaryController.button(8).onTrue(driveCommands.headingMode(Drive.HeadingMode.STAGE_RIGHT));

        secondaryController.button(13).onTrue(launcherCommands.deactivate());
        secondaryController.button(17).onTrue(launcherCommands.initialize());
    }

    private double controlCurve(double input) {
        return Utils.controlCurve(input, Joystick.EXPONENT, Joystick.DEADBAND) *
                (climber.getClimbingMode() ? CLIMBING_MODE_SPEED : 1);
    }

    public Command getAutonomousCommand() {
        return AutoBuilder.buildAuto("5_1C_1A_1B_2A").repeatedly();
    }
}
