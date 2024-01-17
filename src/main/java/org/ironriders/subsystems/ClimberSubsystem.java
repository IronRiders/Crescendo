package org.ironriders.subsystems;

import com.revrobotics.CANSparkMax;
import edu.wpi.first.math.MathUtil;
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

    private final CANSparkMax right = new CANSparkMax(Identifiers.Climber.LEADER, kBrushless);
    private final CANSparkMax left = new CANSparkMax(Identifiers.Climber.FOLLOWER, kBrushless);

    public ClimberSubsystem() {
        right.setSmartCurrentLimit(CURRENT_LIMIT);
        left.setSmartCurrentLimit(CURRENT_LIMIT);
        right.setIdleMode(kBrake);
        left.setIdleMode(kBrake);

        right.getEncoder().setPositionConversionFactor(1.0 / GEARING);
        left.getEncoder().setPositionConversionFactor(1.0 / GEARING);

        right.setSoftLimit(kReverse, Limit.REVERSE);
        right.enableSoftLimit(kReverse, true);
        right.setSoftLimit(kForward, Limit.FORWARD);
        right.enableSoftLimit(kForward, true);
        left.setSoftLimit(kReverse, Limit.REVERSE);
        left.enableSoftLimit(kReverse, true);
        left.setSoftLimit(kForward, Limit.FORWARD);
        left.enableSoftLimit(kForward, true);

        commands = new ClimberCommands(this);
    }

    @Override
    public void periodic() {
        SmartDashboard.putNumber(DASHBOARD_PREFIX + "rightPosition", right.getEncoder().getPosition());
        SmartDashboard.putNumber(DASHBOARD_PREFIX + "leftPosition", left.getEncoder().getPosition());
    }

    public void set(double right, double left) {
        this.right.set(MathUtil.clamp(right * SPEED, -1, 1));
        this.left.set(MathUtil.clamp(left * SPEED, -1, 1));
    }

    public ClimberCommands getCommands() {
        return commands;
    }
}
