package org.ironriders.commands;

import edu.wpi.first.wpilibj2.command.Command;
import org.ironriders.subsystems.ClimberSubsystem;

public class ClimberCommands {
    private final ClimberSubsystem climber;

    public ClimberCommands(ClimberSubsystem climber) {
        this.climber = climber;
    }

    public Command set(double input) {
        return climber.startEnd(() -> climber.set(input), () -> climber.set(0));
    }

    public Command setClimbingMode(boolean isEnabled) {
        return climber.runOnce(() -> climber.setClimbingMode(isEnabled));
    }

    public ClimberSubsystem getClimber() {
        return climber;
    }
}
