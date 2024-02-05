package org.ironriders.subsystems;

import com.revrobotics.CANSparkMax;
import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import org.ironriders.commands.ClimberCommands;
import org.ironriders.constants.Identifiers;

import static com.revrobotics.CANSparkBase.IdleMode.kBrake;
import static com.revrobotics.CANSparkBase.SoftLimitDirection.kForward;
import static com.revrobotics.CANSparkBase.SoftLimitDirection.kReverse;
import static com.revrobotics.CANSparkLowLevel.MotorType.kBrushless;
import static org.ironriders.constants.Climber.*;
import static org.ironriders.constants.Climber.RollController.*;

public class ClimberSubsystem extends SubsystemBase {
    private final ClimberCommands commands;

    private final DriveSubsystem drive;

    private final CANSparkMax right = new CANSparkMax(Identifiers.Climber.RIGHT, kBrushless);
    private final CANSparkMax left = new CANSparkMax(Identifiers.Climber.LEFT, kBrushless);

    private final PIDController pid = new PIDController(P, I, D);
    private double rightPower = 0;
    private double leftPower = 0;
    private boolean climbingMode = false;

    public ClimberSubsystem(DriveSubsystem drive) {
        this.drive = drive;

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
        if (climbingMode) {
            double calculation = pid.calculate(drive.getSwerveDrive().getRoll().getDegrees(), 0);

            right.set(MathUtil.clamp(rightPower - calculation, -SPEED, SPEED));
            left.set(MathUtil.clamp(rightPower + calculation, -SPEED, SPEED));
        } else {
            right.stopMotor();
            left.stopMotor();
        }

        SmartDashboard.putData(DASHBOARD_PREFIX + "rollPID", pid);
        SmartDashboard.putNumber(DASHBOARD_PREFIX + "rightPosition", right.getEncoder().getPosition());
        SmartDashboard.putNumber(DASHBOARD_PREFIX + "leftPosition", left.getEncoder().getPosition());
        SmartDashboard.putNumber(DASHBOARD_PREFIX + "rightPower", rightPower);
        SmartDashboard.putNumber(DASHBOARD_PREFIX + "leftPower", leftPower);
    }

    public void setLeftPower(double leftPower) {
        this.leftPower = leftPower;
    }

    public void setRightPower(double rightPower) {
        this.rightPower = rightPower;
    }

    public void toggleClimbingMode() {
        climbingMode = !climbingMode;
    }

    public boolean getClimbingMode() {
        return climbingMode;
    }

    public ClimberCommands getCommands() {
        return commands;
    }
}
