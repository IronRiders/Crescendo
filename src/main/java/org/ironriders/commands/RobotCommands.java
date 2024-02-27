package org.ironriders.commands;

import com.pathplanner.lib.auto.NamedCommands;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import org.ironriders.constants.Drive;
import org.ironriders.constants.Manipulator;
import org.ironriders.constants.Pivot;
import org.ironriders.subsystems.DriveSubsystem;
import org.ironriders.subsystems.LauncherSubsystem;
import org.ironriders.subsystems.ManipulatorSubsystem;
import org.ironriders.subsystems.PivotSubsystem;

public class RobotCommands {
    private final DriveCommands drive;
    private final LauncherCommands launcher;
    private final PivotCommands pivot;
    private final ManipulatorCommands manipulator;

    public RobotCommands(DriveSubsystem drive, LauncherSubsystem launcher, PivotSubsystem pivot,
                         ManipulatorSubsystem manipulator) {
        this.drive = drive.getCommands();
        this.launcher = launcher.getCommands();
        this.pivot = pivot.getCommands();
        this.manipulator = manipulator.getCommands();

        NamedCommands.registerCommand("Apm", amp());
        NamedCommands.registerCommand("Launch", launch());
        NamedCommands.registerCommand("Start Ground Pickup", startGroundPickup());
        NamedCommands.registerCommand("End Ground Pickup", endGroundPickup());
    }

    public Command amp() {
        return Commands.sequence(
                drive.setHeadingMode(Drive.HeadingMode.AMP),
                pivot.set(Pivot.State.AMP),
                manipulator.set(Manipulator.State.EJECT_TO_AMP)
        );
    }

    /**
     * Manual override command
     */
    public Command ejectNote() {
        return Commands.sequence(
                manipulator.setHasNote(false),
                manipulator.set(Manipulator.State.EJECT)
        );
    }

    /**
     * Manual override command
     */
    public Command deployPivot() {
        return Commands.sequence(
                manipulator.setHasNote(false),
                pivot.set(Pivot.State.GROUND)
        );
    }

    public Command launch() {
        return Commands.sequence(
                Commands.parallel(
                        launcher.initialize(true),
                        pivot.set(Pivot.State.LAUNCHER)
                ),
                manipulator.set(Manipulator.State.EJECT_TO_LAUNCHER),
                manipulator.setHasNote(false),
                launcher.deactivate()
        ).onlyIf(pivot.getPivot()::atPosition);
    }

    public Command startGroundPickup() {
        return Commands.sequence(
                Commands.deadline(
                        Commands.waitUntil(manipulator.getManipulator()::hasNoteSwitchTriggered),
                        drive.setHeadingMode(Drive.HeadingMode.FREE),
                        pivot.set(Pivot.State.GROUND),
                        manipulator.set(Manipulator.State.GRAB)
                ),
                manipulator.setHasNote(true),
                Commands.parallel(
                        Commands.either(
                                Commands.sequence(
                                        manipulator.centerNote(),
                                        manipulator.centerNote(),
                                        manipulator.centerNote()
                                ),
                                manipulator.set(Manipulator.State.STOP),
                                manipulator.getManipulator()::hasNote
                        ),
                        endGroundPickup()
                )
        );
    }

    public Command endGroundPickup() {
        return Commands.either(
                Commands.parallel(
                        pivot.set(Pivot.State.LAUNCHER),
                        drive.setHeadingMode(Drive.HeadingMode.STRAIGHT)
                ),
                pivot.set(Pivot.State.STOWED_TO_PERIMETER),
                manipulator.getManipulator()::hasNote
        );
    }
}
