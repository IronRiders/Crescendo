// Copyright (c) FIRST and other WPILib contributors.

// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package org.ironriders.robot;

import com.pathplanner.lib.auto.AutoBuilder;
import edu.wpi.first.wpilibj.RobotBase;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.button.CommandJoystick;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import org.ironriders.commands.RobotCommands;
import org.ironriders.constants.Identifiers;
import org.ironriders.constants.Teleop;
import org.ironriders.lib.Utils;
import org.ironriders.subsystems.*;

import static org.ironriders.constants.Game.KeyLocations.*;
import static org.ironriders.constants.Teleop.Controllers.Joystick;
import static org.ironriders.constants.Teleop.Speed.DEADBAND;
import static org.ironriders.constants.Teleop.Speed.MIN_MULTIPLIER;

public class RobotContainer {
    private final DriveSubsystem drive = new DriveSubsystem();
    private final LauncherSubsystem launcher = new LauncherSubsystem();
    private final PivotSubsystem pivot = new PivotSubsystem();
    private final ManipulatorSubsystem manipulator = new ManipulatorSubsystem();
    private final ClimberSubsystem climber = new ClimberSubsystem();
    @SuppressWarnings("unused")
    private final LightingSubsystem lighting = new LightingSubsystem();
    private final RobotCommands commands = new RobotCommands(drive, launcher, pivot, manipulator, climber);

    private final CommandXboxController primaryController =
            new CommandXboxController(Identifiers.Controllers.PRIMARY_CONTROLLER);
    private final CommandJoystick secondaryController =
            new CommandJoystick(Identifiers.Controllers.SECONDARY_CONTROLLER);

    public RobotContainer() {
        configureBindings();
    }

    private void configureBindings() {
        drive.setDefaultCommand(
                drive.getCommands().teleopCommand(
                        () -> -driveControlCurve(primaryController.getLeftY()),
                        () -> -driveControlCurve(primaryController.getLeftX()),
                        () -> -driveControlCurve(primaryController.getRightX())
                )
        );

        if (RobotBase.isSimulation()) return;

        Command speakerRight = commands.launchAt(SPEAKER_RIGHT);
        Command speakerCenter = commands.launchAt(SPEAKER_CENTER);
        Command speakerLeft = commands.launchAt(SPEAKER_LEFT);

        Command stageRight = commands.onStage(STAGE_RIGHT);
        Command centerStage = commands.onStage(CENTER_STAGE);
        Command stageLeft = commands.onStage(STAGE_LEFT);

        Command amp = commands.amp();

        Command cancelAuto = Commands.runOnce(() -> {
            speakerRight.cancel();
            speakerCenter.cancel();
            speakerLeft.cancel();
            stageRight.cancel();
            centerStage.cancel();
            stageLeft.cancel();
            amp.cancel();
        });

        // Primary Driver
        primaryController.rightBumper().onTrue(cancelAuto);
        primaryController.leftBumper().onTrue(cancelAuto);

        primaryController.a().onTrue(commands.startGroundPickup()).onFalse(commands.endGroundPickup());
        primaryController.b().onTrue(speakerRight);
        primaryController.x().onTrue(speakerRight);
        primaryController.y().onTrue(speakerCenter);

        // Secondary Controller
        climber.setDefaultCommand(
                climber.getCommands().set(
                        () -> (climberControlCurve(secondaryController.getX()) * 0.5 + 0.5) *
                                climberControlCurve(secondaryController.getY()),
                        () -> (climberControlCurve(secondaryController.getX()) * -0.5 + 0.5) *
                                climberControlCurve(secondaryController.getY())
                )
        );

        secondaryController.button(7).onTrue(speakerRight);
        secondaryController.button(9).onTrue(speakerCenter);
        secondaryController.button(11).onTrue(speakerLeft);

        secondaryController.button(8).onTrue(stageRight);
        secondaryController.button(10).onTrue(stageLeft);

        secondaryController.button(12).onTrue(cancelAuto);
    }

    private double driveControlCurve(double input) {
        // Multiplier based on trigger axis (whichever one is larger) then scaled to start at 0.35
        return Utils.controlCurve(input, Joystick.EXPONENT, Joystick.DEADBAND) * (
                Utils.controlCurve(
                        Math.max(primaryController.getLeftTriggerAxis(), primaryController.getRightTriggerAxis()),
                        Teleop.Speed.EXPONENT,
                        DEADBAND
                ) * (1 - MIN_MULTIPLIER) + MIN_MULTIPLIER
        );
    }

    private double climberControlCurve(double input) {
        return Utils.controlCurve(input, Joystick.EXPONENT, Joystick.DEADBAND);
    }

    public Command getEnableCommand() {
        return pivot.getCommands().reset();
    }

    public Command getAutonomousCommand() {
        return AutoBuilder.buildAuto("5_1C_1A_1B_2A").repeatedly();
    }
}
