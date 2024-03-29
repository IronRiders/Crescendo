package org.ironriders.subsystems;

import com.revrobotics.CANSparkMax;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import org.ironriders.commands.ClimberCommands;
import org.ironriders.constants.Identifiers;

import static com.revrobotics.CANSparkBase.IdleMode.kBrake;
import static com.revrobotics.CANSparkBase.SoftLimitDirection.kForward;
import static com.revrobotics.CANSparkBase.SoftLimitDirection.kReverse;
import static com.revrobotics.CANSparkLowLevel.MotorType.kBrushless;
import static org.ironriders.constants.Climber.*;

public class ClimberSubsystem extends SubsystemBase {
    private final ClimberCommands commands;

    private final CANSparkMax right = new CANSparkMax(Identifiers.Climber.RIGHT, kBrushless);
    private final CANSparkMax left = new CANSparkMax(Identifiers.Climber.LEFT, kBrushless);

    private double input = 0;
    private boolean climbingMode = false;

    public ClimberSubsystem() {
        applyConfig(right);
        applyConfig(left);

        left.follow(right, true);

        commands = new ClimberCommands(this);
    }

    private void applyConfig(CANSparkMax motor) {
        motor.restoreFactoryDefaults();

        motor.setSmartCurrentLimit(CURRENT_LIMIT);
        motor.setIdleMode(kBrake);

        motor.getEncoder().setPositionConversionFactor(1.0 / GEARING);

        motor.setSoftLimit(kReverse, Limit.REVERSE);
        motor.enableSoftLimit(kReverse, true);
        motor.setSoftLimit(kForward, Limit.FORWARD);
        motor.enableSoftLimit(kForward, true);
    }

    @Override
    public void periodic() {
        if (climbingMode) {
            right.set(input * SPEED);
        } else {
            right.stopMotor();
            left.stopMotor();
        }

        SmartDashboard.putBoolean(DASHBOARD_PREFIX + "climbingModeEnabled", climbingMode);
        SmartDashboard.putNumber(DASHBOARD_PREFIX + "rightPosition", right.getEncoder().getPosition());
        SmartDashboard.putNumber(DASHBOARD_PREFIX + "leftPosition", left.getEncoder().getPosition());
        SmartDashboard.putNumber(DASHBOARD_PREFIX + "input", input);
    }

    public void set(double input) {
        this.input = input;
    }

    public void setClimbingMode(boolean isEnabled) {
        climbingMode = isEnabled;
    }

    public boolean getClimbingMode() {
        return climbingMode;
    }

    public ClimberCommands getCommands() {
        return commands;
    }
}
