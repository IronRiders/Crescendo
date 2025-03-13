package org.ironriders.subsystems;

import com.revrobotics.spark.SparkLimitSwitch;
import com.revrobotics.spark.SparkMax;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import org.ironriders.commands.ManipulatorCommands;
import org.ironriders.constants.Climber.Limit;
import org.ironriders.constants.Identifiers;
import org.ironriders.constants.Manipulator.State;

import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.config.SoftLimitConfig;
import com.revrobotics.spark.config.SparkMaxConfig;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;
import static org.ironriders.constants.Manipulator.*;
import static org.ironriders.constants.Robot.COMPENSATED_VOLTAGE;

public class ManipulatorSubsystem extends SubsystemBase {
    private final ManipulatorCommands commands;

    private final SparkMax motor = new SparkMax(Identifiers.Manipulator.MOTOR, MotorType.kBrushless);

    private final SparkLimitSwitch limitSwitch = motor.getForwardLimitSwitch();

    private boolean hasNote = false;

    public ManipulatorSubsystem() {
        var config= new SparkMaxConfig().idleMode(IdleMode.kBrake).smartCurrentLimit(CURRENT_LIMIT).apply(new SoftLimitConfig().forwardSoftLimit(Limit.FORWARD).reverseSoftLimit(Limit.REVERSE)).voltageCompensation(COMPENSATED_VOLTAGE);
        motor.configure(config, null, null);
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
        return !limitSwitch.isPressed();//bad fix proably broken
    }

    public double getVelocity() {
        return motor.getEncoder().getVelocity();
    }

    public ManipulatorCommands getCommands() {
        return commands;
    }
}
