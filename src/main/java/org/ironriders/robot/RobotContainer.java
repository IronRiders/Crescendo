// Copyright (c) FIRST and other WPILib contributors.

// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package org.ironriders.robot;

import com.pathplanner.lib.auto.AutoBuilder;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.button.CommandJoystick;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import org.ironriders.constants.Identifiers;
import org.ironriders.constants.Teleop;
import org.ironriders.lib.Utils;
import org.ironriders.subsystems.*;

import static org.ironriders.constants.Teleop.Controllers.Joystick;
import static org.ironriders.constants.Teleop.Speed.DEADBAND;
import static org.ironriders.constants.Teleop.Speed.MIN_MULTIPLIER;

public class RobotContainer {
    private final DriveSubsystem drive = new DriveSubsystem();
    private final ShooterSubsystem shooter = new ShooterSubsystem();
    private final PivotSubsystem pivot = new PivotSubsystem();
    private final ManipulatorSubsystem manipulator = new ManipulatorSubsystem();
    private final ClimberSubsystem climber = new ClimberSubsystem();

    private final CommandXboxController primaryController =
            new CommandXboxController(Identifiers.Controllers.PRIMARY_CONTROLLER);
    private final CommandJoystick secondaryController =
            new CommandJoystick(Identifiers.Controllers.SECONDARY_CONTROLLER);

    public RobotContainer() {
        configureBindings();
    }

    private void configureBindings() {
        // Primary Driver
        drive.setDefaultCommand(
                drive.getCommands().teleopCommand(
                        () -> -driveControlCurve(primaryController.getLeftY()),
                        () -> -driveControlCurve(primaryController.getLeftX()),
                        () -> -driveControlCurve(primaryController.getRightX())
                )
        );

        // Secondary Controller
        climber.setDefaultCommand(
                climber.getCommands().set(
                        () -> climberControlCurve(secondaryController.getX() * 0.5 + 0.5) *
                                climberControlCurve(secondaryController.getY()),
                        () -> climberControlCurve(secondaryController.getX() * -0.5 + 0.5) *
                                climberControlCurve(secondaryController.getY())
                )
        );
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
        return AutoBuilder.buildAuto("New Auto");
    }
}
