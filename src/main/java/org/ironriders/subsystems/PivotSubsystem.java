package org.ironriders.subsystems;

import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkLimitSwitch;
import edu.wpi.first.math.controller.ProfiledPIDController;
import edu.wpi.first.wpilibj.DutyCycleEncoder;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import org.ironriders.commands.PivotCommands;
import org.ironriders.constants.Climber.Limit;
import org.ironriders.constants.Identifiers;
import org.ironriders.lib.Utils;

import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.config.LimitSwitchConfig;
import com.revrobotics.spark.config.SoftLimitConfig;
import com.revrobotics.spark.config.SparkMaxConfig;
import com.revrobotics.spark.config.LimitSwitchConfig.Type;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;
import static org.ironriders.constants.Pivot.*;
import static org.ironriders.constants.Pivot.Control.*;
import static org.ironriders.constants.Robot.COMPENSATED_VOLTAGE;

public class PivotSubsystem extends SubsystemBase {
    private final PivotCommands commands;

    private final SparkMax motor = new SparkMax(Identifiers.Pivot.MOTOR, MotorType.kBrushless);
    private final ProfiledPIDController pid = new ProfiledPIDController(P, I, D, PROFILE);
    private final DutyCycleEncoder absoluteEncoder = new DutyCycleEncoder(Identifiers.Pivot.ENCODER);

    private final SparkLimitSwitch forwardSwitch = motor.getForwardLimitSwitch();
    private final SparkLimitSwitch reverseSwitch = motor.getReverseLimitSwitch();

    public PivotSubsystem() {
        var config= new SparkMaxConfig().idleMode(IdleMode.kBrake).smartCurrentLimit(CURRENT_LIMIT).apply(new SoftLimitConfig().forwardSoftLimit(Limit.FORWARD).reverseSoftLimit(Limit.REVERSE)).voltageCompensation(COMPENSATED_VOLTAGE).apply(new LimitSwitchConfig().forwardLimitSwitchEnabled(true).forwardLimitSwitchType(Type.kNormallyClosed));
        motor.configure(config, null, null);

        set(getRotation());

        pid.setTolerance(TOLERANCE);

        commands = new PivotCommands(this);
    }

    @Override
    public void periodic() {
        double output = pid.calculate(getRotation());
        motor.set(output);

        SmartDashboard.putNumber(DASHBOARD_PREFIX + "rotation", getRotation());
        SmartDashboard.putNumber(DASHBOARD_PREFIX + "output", output);
        SmartDashboard.putNumber(DASHBOARD_PREFIX + "setPoint", pid.getGoal().position);
        SmartDashboard.putBoolean(DASHBOARD_PREFIX + "forwardSwitch", !forwardSwitch.isPressed());
        SmartDashboard.putBoolean(DASHBOARD_PREFIX + "reverseSwitch", !reverseSwitch.isPressed());
    }

    public void set(double position) {
        pid.setGoal(position);
    }

    public void reset() {
        pid.setGoal(getRotation());
        pid.reset(getRotation());
    }

    public boolean atPosition() {
        return pid.atGoal();
    }

    private double getRotation() {
        return Utils.absoluteRotation(absoluteEncoder.get() * 360 - ENCODER_OFFSET);
    }

    public PivotCommands getCommands() {
        return commands;
    }

    public boolean forwardLimitSwitchPressed() {
        return forwardSwitch.isPressed();
    }

    public boolean reverseLimitSwitchPressed() {
        return reverseSwitch.isPressed();
    }
}
