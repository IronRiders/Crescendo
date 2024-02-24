package org.ironriders.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.SparkLimitSwitch;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import org.ironriders.commands.ManipulatorCommands;
import org.ironriders.constants.Identifiers;

import static com.revrobotics.CANSparkBase.IdleMode.kBrake;
import static com.revrobotics.CANSparkLowLevel.MotorType.kBrushless;
import static com.revrobotics.SparkLimitSwitch.Type.kNormallyClosed;
import static org.ironriders.constants.Manipulator.*;
import static org.ironriders.constants.Robot.COMPENSATED_VOLTAGE;

public class ManipulatorSubsystem extends SubsystemBase {
    private final ManipulatorCommands commands;

    private final CANSparkMax motor = new CANSparkMax(Identifiers.Manipulator.MOTOR, kBrushless);

    private final SparkLimitSwitch limitSwitch = motor.getForwardLimitSwitch(kNormallyClosed);

    private boolean hasNote = false;

    public ManipulatorSubsystem() {
        motor.restoreFactoryDefaults();

        motor.setSmartCurrentLimit(CURRENT_LIMIT);
        motor.enableVoltageCompensation(COMPENSATED_VOLTAGE);
        motor.setIdleMode(kBrake);
        motor.setControlFramePeriodMs(VELOCITY_FILTERING);

        SmartDashboard.putString(DASHBOARD_PREFIX + "state", "STOP");

        commands = new ManipulatorCommands(this);
    }

    @Override
    public void periodic() {
        SmartDashboard.putNumber(DASHBOARD_PREFIX + "velocity", getVelocity());
        SmartDashboard.putBoolean(DASHBOARD_PREFIX + "hasNote", hasNote);
        SmartDashboard.putBoolean(DASHBOARD_PREFIX + "hasNoteSwitchTriggered", hasNoteSwitchTriggered());
    }

    public void set(State state) {
        motor.set(state.getSpeed());

        SmartDashboard.putString(DASHBOARD_PREFIX + "state", state.name());
    }

    public void setHasNote(boolean hasNote) {
        this.hasNote = hasNote;
    }

    public boolean hasNote() {
        return hasNote;
    }

    public boolean hasNoteSwitchTriggered() {
        return limitSwitch.isPressed();
    }

    public double getVelocity() {
        return motor.getEncoder().getVelocity();
    }

    public ManipulatorCommands getCommands() {
        return commands;
    }
}
