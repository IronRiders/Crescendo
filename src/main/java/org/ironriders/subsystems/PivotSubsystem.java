package org.ironriders.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.SparkLimitSwitch;
import edu.wpi.first.math.controller.ProfiledPIDController;
import edu.wpi.first.wpilibj.DutyCycleEncoder;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import org.ironriders.commands.PivotCommands;
import org.ironriders.constants.Identifiers;
import org.ironriders.lib.Utils;

import static com.revrobotics.CANSparkBase.IdleMode.kBrake;
import static com.revrobotics.CANSparkBase.SoftLimitDirection.kForward;
import static com.revrobotics.CANSparkBase.SoftLimitDirection.kReverse;
import static com.revrobotics.CANSparkLowLevel.MotorType.kBrushless;
import static com.revrobotics.SparkLimitSwitch.Type.kNormallyOpen;
import static org.ironriders.constants.Pivot.*;
import static org.ironriders.constants.Pivot.Control.*;
import static org.ironriders.constants.Robot.COMPENSATED_VOLTAGE;

public class PivotSubsystem extends SubsystemBase {
    private final PivotCommands commands;

    private final CANSparkMax motor = new CANSparkMax(Identifiers.Pivot.MOTOR, kBrushless);
    private final ProfiledPIDController pid = new ProfiledPIDController(P, I, D, PROFILE);
    @SuppressWarnings("FieldCanBeLocal")
    private final DutyCycleEncoder absoluteEncoder = new DutyCycleEncoder(Identifiers.Pivot.ENCODER);
    private final RelativeEncoder encoder = motor.getEncoder();

    private final SparkLimitSwitch forwardSwitch = motor.getForwardLimitSwitch(kNormallyOpen);
    private final SparkLimitSwitch reverseSwitch = motor.getReverseLimitSwitch(kNormallyOpen);

    public PivotSubsystem() {
        motor.restoreFactoryDefaults();

        motor.setSmartCurrentLimit(CURRENT_LIMIT);
        motor.enableVoltageCompensation(COMPENSATED_VOLTAGE);
        motor.setIdleMode(kBrake);

        encoder.setPositionConversionFactor(360.0 / GEARING);
        encoder.setPosition(absoluteEncoder.getDistance() + ENCODER_OFFSET);

        motor.setSoftLimit(kReverse, Limit.REVERSE);
        motor.enableSoftLimit(kReverse, true);
        motor.setSoftLimit(kForward, Limit.FORWARD);
        motor.enableSoftLimit(kForward, true);

        forwardSwitch.enableLimitSwitch(true);
        reverseSwitch.enableLimitSwitch(true);

        SmartDashboard.putData(DASHBOARD_PREFIX + "pid", pid);

        reset();

        commands = new PivotCommands(this);
    }

    @Override
    public void periodic() {
        motor.set(pid.calculate(getRotation()));

        SmartDashboard.putNumber(DASHBOARD_PREFIX + "rotation", getRotation());
        SmartDashboard.putBoolean(DASHBOARD_PREFIX + "forwardSwitch", forwardSwitch.isPressed());
        SmartDashboard.putBoolean(DASHBOARD_PREFIX + "reverseSwitch", reverseSwitch.isPressed());
    }

    public void set(State state) {
        pid.reset(state.getPosition());
    }

    public void reset() {
        pid.reset(getRotation());
    }

    public boolean atPosition() {
        return Utils.isWithinTolerance(getRotation(), pid.getSetpoint().position, TOLERANCE);
    }

    private double getRotation() {
        return encoder.getPosition();
    }

    public PivotCommands getCommands() {
        return commands;
    }
}
