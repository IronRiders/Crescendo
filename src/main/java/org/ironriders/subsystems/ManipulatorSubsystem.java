package org.ironriders.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.SparkLimitSwitch;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import org.ironriders.commands.ManipulatorCommands;
import org.ironriders.constants.Identifiers;

import static com.revrobotics.CANSparkBase.IdleMode.kBrake;
import static com.revrobotics.CANSparkLowLevel.MotorType.kBrushless;
import static com.revrobotics.SparkLimitSwitch.Type.kNormallyOpen;
import static org.ironriders.constants.Manipulator.*;
import static org.ironriders.constants.Manipulator.State.STOP;
import static org.ironriders.constants.Robot.COMPENSATED_VOLTAGE;

public class ManipulatorSubsystem extends SubsystemBase {
    private final ManipulatorCommands commands;

    private final CANSparkMax motor = new CANSparkMax(Identifiers.Manipulator.MOTOR, kBrushless);

    private final SparkLimitSwitch limitSwitch = motor.getForwardLimitSwitch(kNormallyOpen);

    private State state = STOP;

    public ManipulatorSubsystem() {
        motor.restoreFactoryDefaults();

        motor.setSmartCurrentLimit(CURRENT_LIMIT);
        motor.enableVoltageCompensation(COMPENSATED_VOLTAGE);
        motor.setIdleMode(kBrake);
        motor.setControlFramePeriodMs(VELOCITY_FILTERING);

        limitSwitch.enableLimitSwitch(false);

        SmartDashboard.putString(DASHBOARD_PREFIX + "state", "STOP");

        commands = new ManipulatorCommands(this);
    }

    @Override
    public void periodic() {
        if (hasNote() && state.equals(State.GRAB)) set(STOP);

        SmartDashboard.putNumber(DASHBOARD_PREFIX + "velocity", getVelocity());
    }

    public void set(State state) {
        motor.set(state.getSpeed());
        this.state = state;

        SmartDashboard.putString(DASHBOARD_PREFIX + "state", state.name());
    }

    public boolean hasNote() {
        return limitSwitch.isPressed();
    }

    public double getVelocity() {
        return motor.getEncoder().getVelocity();
    }

    public ManipulatorCommands getCommands() {
        return commands;
    }
}
