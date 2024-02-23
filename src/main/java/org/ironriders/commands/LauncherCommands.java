package org.ironriders.commands;

import com.pathplanner.lib.auto.NamedCommands;
import edu.wpi.first.wpilibj2.command.Command;
import org.ironriders.subsystems.LauncherSubsystem;

import static org.ironriders.constants.Launcher.INITIATION_TIMEOUT;

public class LauncherCommands {
    private final LauncherSubsystem launcher;

    public LauncherCommands(LauncherSubsystem launcher) {
        this.launcher = launcher;

        NamedCommands.registerCommand("Launcher Initialize", initialize(false));
        NamedCommands.registerCommand("Launcher Deactivate", deactivate());
    }

    /**
     * Initializes the launcher and ends when the launcher's velocity passes the velocity threshold. The launcher is ready to
     * launch when this command is over. Should be used in sequence with other commands that launch the note. Only
     * stops the launcher if the command is interrupted.
     */
    public Command initialize(boolean forLaunching) {
        Command command = launcher
                .run(launcher::run)
                .onlyIf(launcher::isNotInitialized);
        if (forLaunching && launcher.isNotInitialized()) {
            return command.withTimeout(INITIATION_TIMEOUT);
        } else {
            return command;
        }
    }

    public Command deactivate() {
        return launcher.runOnce(launcher::deactivate);
    }

    public LauncherSubsystem getLauncher() {
        return launcher;
    }
}
