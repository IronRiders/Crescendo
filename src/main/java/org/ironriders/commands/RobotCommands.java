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
                drive.headingMode(Drive.HeadingMode.AMP),
                pivot.set(Pivot.State.AMP),
                manipulator.set(Manipulator.State.EJECT_TO_AMP)
        );
    }

    public Command launch() {
        return Commands.sequence(
                launcher.initialize(),
                manipulator.set(Manipulator.State.EJECT_TO_LAUNCHER),
                launcher.deactivate()
        );
    }

    public Command startGroundPickup() {
        return Commands.parallel(
                drive.headingMode(Drive.HeadingMode.FREE),
                pivot.set(Pivot.State.GROUND),
                manipulator.set(Manipulator.State.GRAB),
                launcher.initialize()
        );
    }

    public Command endGroundPickup() {
        return Commands.parallel(
                pivot.set(Pivot.State.LAUNCHER),
                manipulator.set(Manipulator.State.STOP)
        );
    }

    public Command setClimbingMode(boolean isEnabled) {
        return Commands.parallel(
                drive.headingMode(Drive.HeadingMode.FREE),
                climber.setClimbingMode(isEnabled)
        );
    }
}
