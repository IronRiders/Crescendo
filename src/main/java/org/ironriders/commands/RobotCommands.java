package org.ironriders.commands;

import com.pathplanner.lib.auto.NamedCommands;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import org.ironriders.constants.Drive;
import org.ironriders.constants.Manipulator;
import org.ironriders.constants.Pivot;
import org.ironriders.subsystems.*;

public class RobotCommands {
    private final DriveCommands drive;
    private final LauncherCommands launcher;
    private final PivotCommands pivot;
    private final ManipulatorCommands manipulator;
    private final ClimberCommands climber;

    public RobotCommands(DriveSubsystem drive, LauncherSubsystem launcher, PivotSubsystem pivot,
                         ManipulatorSubsystem manipulator, ClimberSubsystem climber) {
        this.drive = drive.getCommands();
        this.launcher = launcher.getCommands();
        this.pivot = pivot.getCommands();
        this.manipulator = manipulator.getCommands();
        this.climber = climber.getCommands();

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

    public Command launch() {
        return Commands.sequence(
                Commands.parallel(
                        launcher.initialize(),
                        pivot.set(Pivot.State.LAUNCHER)
                ),
                manipulator.set(Manipulator.State.EJECT_TO_LAUNCHER),
                launcher.deactivate()
        );
    }

    public Command startGroundPickup() {
        return Commands.sequence(
                Commands.deadline(
                        Commands.waitUntil(manipulator.getManipulator()::hasNote),
                        Commands.parallel(
                                drive.setHeadingMode(Drive.HeadingMode.FREE),
                                pivot.set(Pivot.State.GROUND),
                                manipulator.set(Manipulator.State.GRAB)
                        )
                ),
                endGroundPickup()
        );
    }

    public Command endGroundPickup() {
        return Commands.parallel(
                drive.setHeadingMode(Drive.HeadingMode.STRAIGHT).onlyIf(manipulator.getManipulator()::hasNote),
                pivot.set(Pivot.State.LAUNCHER),
                manipulator.set(Manipulator.State.STOP)
        );
    }

    public Command setClimbingMode(boolean isEnabled) {
        if (isEnabled) {
            return Commands.parallel(
                    drive.setHeadingMode(Drive.HeadingMode.FREE),
                    climber.setClimbingMode(true)
            );
        } else {
            return Commands.parallel(
                    drive.setHeadingMode(Drive.HeadingMode.STRAIGHT),
                    climber.setClimbingMode(false)
            );
        }
    }
}
