package org.ironriders.commands;

import edu.wpi.first.wpilibj2.command.Command;
import org.ironriders.subsystems.ShooterSubsystem;

import static org.ironriders.constants.Shooter.INITIATION_TIMEOUT;

public class ShooterCommands {
    private final ShooterSubsystem shooter;

    public ShooterCommands(ShooterSubsystem shooter) {
        this.shooter = shooter;
    }

    /**
     * Initializes shooter and ends when the shooter's velocity passes the velocity threshold. The shooter is ready to
     * launch when this command is over. Should be used in sequence with other commands that launch the note. Only
     * stops the shooter if the command is interrupted.
     */
    public Command initialize() {
        return shooter
                .run(shooter::run)
                .until(shooter::isWithinInitiationVelocityThreshold)
                .handleInterrupt(shooter::stop)
                .withTimeout(INITIATION_TIMEOUT);
    }

    public Command stop() {
        return shooter.runOnce(shooter::stop);
    }
}
