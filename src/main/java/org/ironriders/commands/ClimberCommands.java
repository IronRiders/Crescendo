package org.ironriders.commands;

import edu.wpi.first.wpilibj2.command.Command;
import org.ironriders.subsystems.ClimberSubsystem;

import java.util.function.DoubleSupplier;

public class ClimberCommands {
    private final ClimberSubsystem climber;

    public ClimberCommands(ClimberSubsystem climber) {
        this.climber = climber;
    }

    public Command set(DoubleSupplier right, DoubleSupplier left) {
        return climber.runOnce(() -> climber.set(right.getAsDouble(), left.getAsDouble()));
    }
}
