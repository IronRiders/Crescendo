package org.ironriders.commands;

import com.pathplanner.lib.auto.NamedCommands;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import org.ironriders.constants.Manipulator;
import org.ironriders.subsystems.ManipulatorSubsystem;

import static org.ironriders.constants.Manipulator.DISCHARGE_TIMEOUT;
import static org.ironriders.constants.Manipulator.State.*;

public class ManipulatorCommands {
    private final ManipulatorSubsystem manipulator;

    public ManipulatorCommands(ManipulatorSubsystem manipulator) {
        this.manipulator = manipulator;

        NamedCommands.registerCommand("Manipulator Grab", set(GRAB));
        NamedCommands.registerCommand("Manipulator Eject to Shooter", set(EJECT_TO_SHOOTER));
        NamedCommands.registerCommand("Manipulator Eject to Amp", set(EJECT_TO_AMP));
        NamedCommands.registerCommand("Manipulator Stop", set(STOP));
    }

    public Command set(Manipulator.State state) {
        Command command = manipulator.run(() -> manipulator.set(state));

        if (state.equals(EJECT_TO_SHOOTER) || state.equals(EJECT_TO_AMP)) {
            return command
                    .deadlineWith(Commands.waitSeconds(DISCHARGE_TIMEOUT))
                    .finallyDo(() -> manipulator.set(STOP)
                    );
        }

        return manipulator
                .run(() -> manipulator.set(state))
                .finallyDo(() -> manipulator.set(STOP));
    }
}
