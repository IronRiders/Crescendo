package org.ironriders.commands;

import com.pathplanner.lib.auto.NamedCommands;
import edu.wpi.first.wpilibj2.command.Command;
import org.ironriders.constants.Pivot.State;
import org.ironriders.subsystems.PivotSubsystem;

public class PivotCommands {
    private final PivotSubsystem pivot;

    public PivotCommands(PivotSubsystem pivot) {
        this.pivot = pivot;

        NamedCommands.registerCommand("Pivot Ground", set(State.GROUND));
        NamedCommands.registerCommand("Pivot Amp", set(State.AMP));
        NamedCommands.registerCommand("Pivot Launcher", set(State.LAUNCHER));
    }

    public Command set(State state) {
        return pivot
                .run(() -> pivot.set(state))
                .until(pivot::atPosition)
                .handleInterrupt(pivot::reset);
    }

    public PivotSubsystem getPivot() {
        return pivot;
    }
}
