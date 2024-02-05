package org.ironriders.commands;

import edu.wpi.first.wpilibj2.command.Command;
import org.ironriders.subsystems.ClimberSubsystem;

public class ClimberCommands {
    private final ClimberSubsystem climber;

    public ClimberCommands(ClimberSubsystem climber) {
        this.climber = climber;
    }

    public Command left() {
        return climber.startEnd(() -> climber.setLeftPower(1), () -> climber.setLeftPower(0));
    }

    public Command right() {
        return climber.startEnd(() -> climber.setRightPower(1), () -> climber.setRightPower(0));
    }

    public Command toggleClimbingMode() {
        return climber.runOnce(climber::toggleClimbingMode);
    }

    public ClimberSubsystem getClimber() {
        return climber;
    }
}
