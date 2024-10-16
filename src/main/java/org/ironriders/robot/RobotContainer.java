// Copyright (c) FIRST and other WPILib contributors.

// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package org.ironriders.robot;

import com.pathplanner.lib.auto.AutoBuilder;
import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.RobotBase;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
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
import edu.wpi.first.wpilibj.AddressableLED;
import edu.wpi.first.wpilibj.AddressableLEDBuffer;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import org.ironriders.constants.Identifiers;

import static org.ironriders.constants.Lighting.*;
import static org.ironriders.constants.Auto.DEFAULT_AUTO;
import static org.ironriders.constants.Teleop.Controllers.Joystick;

public class RobotContainer {
    private final VisionSubsystem vision = new VisionSubsystem();
    private final DriveSubsystem drive = new DriveSubsystem(vision);
    private final DriveCommands driveCommands = drive.getCommands();
    private final LauncherSubsystem launcher = new LauncherSubsystem(vision);
    private final LauncherCommands launcherCommands = launcher.getCommands();
    private final PivotSubsystem pivot = new PivotSubsystem();
    private final ManipulatorSubsystem manipulator = new ManipulatorSubsystem();
    private final ClimberSubsystem climber = new ClimberSubsystem();
    private final ClimberCommands climberCommands = climber.getCommands();
    private final LightingSubsystem lighting = new LightingSubsystem();

    private final CommandXboxController primaryController =
            new CommandXboxController(Identifiers.Controllers.PRIMARY_CONTROLLER);
    private final CommandGenericHID secondaryController =
            new CommandGenericHID(Identifiers.Controllers.SECONDARY_CONTROLLER);

    private final RobotCommands commands =
            new RobotCommands(drive, launcher, pivot, manipulator, lighting, primaryController.getHID());
    private final SendableChooser<String> autoOptionsSelector = new SendableChooser<>();

    public RobotContainer() {
        for (String auto : AutoBuilder.getAllAutoNames()) {
            if (auto.equals("REGISTERED_COMMANDS")) continue;
            autoOptionsSelector.addOption(auto, auto);
        }
        autoOptionsSelector.setDefaultOption(DEFAULT_AUTO, DEFAULT_AUTO);
        SmartDashboard.putData("auto/Auto Option", autoOptionsSelector);

        configureBindings();
    }

    private void configureBindings() {
        if (RobotBase.isSimulation()) return;

        // Primary Driver
        primaryController.getHID().setRumble(GenericHID.RumbleType.kBothRumble, 0);

        drive.setDefaultCommand(
                driveCommands.teleopCommand(
                        () -> controlCurve(primaryController.getLeftY()),
                        () -> controlCurve(primaryController.getLeftX()),
                        () -> controlCurve(primaryController.getRightX()),
                        () -> controlCurve(primaryController.getRightY())
                )
        );

        primaryController.leftTrigger().onTrue(commands.launch());
        primaryController.rightTrigger()
                .onTrue(commands.startGroundPickup())
                .onFalse(commands.endGroundPickup().unless(manipulator::hasNote));

        primaryController.leftBumper().whileTrue(climberCommands.set(-1));
        primaryController.rightBumper().whileTrue(climberCommands.set(1));

        primaryController.a().onTrue(climberCommands.setClimbingMode(true));
        primaryController.b().onTrue(climberCommands.setClimbingMode(false));
        primaryController.x().onTrue(commands.ejectNoteToGround());

        // Secondary Controller
        secondaryController.button(1).onTrue(driveCommands.setHeading(Drive.Heading.SPEAKER_LEFT));
        secondaryController.button(2).onTrue(driveCommands.setHeading(Drive.Heading.STRAIGHT));
        secondaryController.button(4).onTrue(driveCommands.setHeading(Drive.Heading.SPEAKER_RIGHT));

        secondaryController.button(5).onTrue(driveCommands.setHeading(Drive.Heading.STAGE_LEFT));
        secondaryController.button(6).onTrue(driveCommands.setHeading(Drive.Heading.STRAIGHT));
        secondaryController.button(8).onTrue(driveCommands.setHeading(Drive.Heading.STAGE_RIGHT));

        secondaryController.button(9).onTrue(climberCommands.setClimbingMode(false));
        secondaryController.button(10).onTrue(climberCommands.setClimbingMode(true));

        secondaryController.button(11).onTrue(commands.ejectNote());
        secondaryController.button(12).onTrue(commands.deployPivot());

        secondaryController.button(13).onTrue(launcherCommands.deactivate());
        secondaryController.button(16).onTrue(launcherCommands.initialize(false));
    }

    private double controlCurve(double input) {
        return Utils.controlCurve(input, Joystick.EXPONENT, Joystick.DEADBAND);
    }

    public Command getEnableCommand() {
        return Commands.parallel(
                driveCommands.setAngleScalar(Drive.Heading.STRAIGHT),
                pivot.getCommands().reset(),
                commands.rumble()
        );
    }

    public Command getAutonomousCommand() {
        if (autoOptionsSelector.getSelected() == null) {
            return Commands.none();
        }

        return Commands.sequence(
                Commands.parallel(
                        pivot.getCommands().reset()
                ),
                driveCommands.useVisionForPoseEstimation(
                        AutoBuilder.buildAuto(autoOptionsSelector.getSelected())
                )
        );
    }
}
