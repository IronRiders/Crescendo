// Copyright (c) FIRST and other WPILib contributors.

// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package org.ironriders.robot;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.button.CommandJoystick;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import org.ironriders.constants.Ports;
import org.ironriders.constants.Teleop;
import org.ironriders.lib.Utils;
import org.ironriders.subsystems.DriveSubsystem;

import static org.ironriders.constants.Teleop.Speed.DEADBAND;
import static org.ironriders.constants.Teleop.Speed.MIN_MULTIPLIER;

public class RobotContainer {
    private final DriveSubsystem drive = new DriveSubsystem();

    private final CommandXboxController primaryController =
            new CommandXboxController(Ports.Controllers.PRIMARY_CONTROLLER);
    private final CommandJoystick secondaryController =
            new CommandJoystick(Ports.Controllers.SECONDARY_CONTROLLER);

    public RobotContainer() {
        configureBindings();
    }

    private void configureBindings() {
        // Primary Driver
        drive.setDefaultCommand(
                drive.getCommands().teleopCommand(
                        () -> -controlCurve(primaryController.getLeftY()),
                        () -> -controlCurve(primaryController.getLeftX()),
                        () -> -controlCurve(primaryController.getRightX())
                )
        );
    }

    private double controlCurve(double input) {
        // Multiplier based on trigger axis (whichever one is larger) then scaled to start at 0.35
        return Utils.controlCurve(input, Teleop.Controllers.Joystick.EXPONENT, Teleop.Controllers.Joystick.DEADBAND) * (
                Utils.controlCurve(
                        Math.max(primaryController.getLeftTriggerAxis(), primaryController.getRightTriggerAxis()),
                        Teleop.Speed.EXPONENT,
                        DEADBAND
                ) * (1 - MIN_MULTIPLIER) + MIN_MULTIPLIER
        );
    }

    public Command getAutonomousCommand() {
        return null;
    }
}
