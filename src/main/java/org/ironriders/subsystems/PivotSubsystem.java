package org.ironriders.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.SparkLimitSwitch;
import edu.wpi.first.math.controller.ProfiledPIDController;
import edu.wpi.first.wpilibj.DutyCycleEncoder;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import org.ironriders.commands.PivotCommands;
import org.ironriders.constants.Identifiers;
import org.ironriders.lib.Utils;

import static com.revrobotics.CANSparkBase.IdleMode.kBrake;
import static com.revrobotics.CANSparkLowLevel.MotorType.kBrushless;
import static com.revrobotics.SparkLimitSwitch.Type.kNormallyClosed;
import static org.ironriders.constants.Pivot.*;
import static org.ironriders.constants.Pivot.Control.*;
import static org.ironriders.constants.Robot.COMPENSATED_VOLTAGE;

public class PivotSubsystem extends SubsystemBase {
    private final PivotCommands commands;

    private final CANSparkMax motor = new CANSparkMax(Identifiers.Pivot.MOTOR, kBrushless);
    private final ProfiledPIDController pid = new ProfiledPIDController(P, I, D, PROFILE);
    private final DutyCycleEncoder absoluteEncoder = new DutyCycleEncoder(Identifiers.Pivot.ENCODER);

    private final SparkLimitSwitch forwardSwitch = motor.getForwardLimitSwitch(kNormallyClosed);
    private final SparkLimitSwitch reverseSwitch = motor.getReverseLimitSwitch(kNormallyClosed);

    public PivotSubsystem() {
        motor.restoreFactoryDefaults();

        motor.setSmartCurrentLimit(CURRENT_LIMIT);
        motor.enableVoltageCompensation(COMPENSATED_VOLTAGE);
        motor.setIdleMode(kBrake);

        forwardSwitch.enableLimitSwitch(true);
        reverseSwitch.enableLimitSwitch(true);

        set(getRotation());

        commands = new PivotCommands(this);
    }

    @Override
    public void periodic() {
        motor.set(pid.calculate(getRotation()));

        SmartDashboard.putNumber(DASHBOARD_PREFIX + "rotation", getRotation());
        SmartDashboard.putNumber(DASHBOARD_PREFIX + "setPoint", pid.getGoal().position);
        SmartDashboard.putBoolean(DASHBOARD_PREFIX + "forwardSwitch", forwardSwitch.isPressed());
        SmartDashboard.putBoolean(DASHBOARD_PREFIX + "reverseSwitch", reverseSwitch.isPressed());
    }

    public void set(State state) {
        set(state.getPosition());
    }

    public void set(double position) {
        reset();
        pid.setGoal(position);
    }

    public void reset() {
        pid.setGoal(getRotation());
        pid.reset(getRotation());
    }

    public boolean atPosition() {
        return Utils.isWithinTolerance(getRotation(), pid.getGoal().position, TOLERANCE);
    }

    private double getRotation() {
        return Utils.absoluteRotation(absoluteEncoder.getAbsolutePosition() * 360 - ENCODER_OFFSET);
    }

    public PivotCommands getCommands() {
        return commands;
    }
}
