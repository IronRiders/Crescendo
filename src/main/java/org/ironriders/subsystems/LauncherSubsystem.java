package org.ironriders.subsystems;

import com.revrobotics.CANSparkMax;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import org.ironriders.commands.LauncherCommands;
import org.ironriders.constants.Identifiers;
import org.ironriders.lib.Utils;

import static com.revrobotics.CANSparkBase.IdleMode.kCoast;
import static com.revrobotics.CANSparkLowLevel.MotorType.kBrushless;
import static org.ironriders.constants.Launcher.*;
import static org.ironriders.constants.Launcher.PID.*;
import static org.ironriders.constants.Robot.COMPENSATED_VOLTAGE;

public class LauncherSubsystem extends SubsystemBase {
    private final LauncherCommands commands;

    private final CANSparkMax right = new CANSparkMax(Identifiers.Launcher.LEADER, kBrushless);
    private final PIDController rightPID = new PIDController(P, I, D);
    @SuppressWarnings("FieldCanBeLocal")
    private final CANSparkMax left = new CANSparkMax(Identifiers.Launcher.FOLLOWER, kBrushless);
    private final PIDController leftPID = new PIDController(P, I, D);

    private double setPoint = 0;

    public LauncherSubsystem() {
        right.setSmartCurrentLimit(CURRENT_LIMIT);
        left.setSmartCurrentLimit(CURRENT_LIMIT);
        right.enableVoltageCompensation(COMPENSATED_VOLTAGE);
        left.enableVoltageCompensation(COMPENSATED_VOLTAGE);
        right.setIdleMode(kCoast);
        left.setIdleMode(kCoast);
        right.setControlFramePeriodMs(1);
        left.setControlFramePeriodMs(1);

        SmartDashboard.putData(DASHBOARD_PREFIX + "rightPID", rightPID);
        SmartDashboard.putData(DASHBOARD_PREFIX + "leftPID", leftPID);
        SmartDashboard.putBoolean(DASHBOARD_PREFIX + "isRunning", false);

        commands = new LauncherCommands(this);
    }

    @Override
    public void periodic() {
        right.set(rightPID.calculate(getRightVelocity()));
        left.set(leftPID.calculate(getLeftVelocity()));

        SmartDashboard.putNumber(DASHBOARD_PREFIX + "rightVelocity", getRightVelocity());
        SmartDashboard.putNumber(DASHBOARD_PREFIX + "leftVelocity", getLeftVelocity());
        SmartDashboard.putNumber(DASHBOARD_PREFIX + "minVelocity", getMinVelocity());
    }

    public void run() {
        set(LAUNCH_SPEED);
        SmartDashboard.putBoolean(DASHBOARD_PREFIX + "isRunning", true);
    }

    public void stop() {
        set(0);
        SmartDashboard.putBoolean(DASHBOARD_PREFIX + "isRunning", false);
    }

    private void set(double setPoint) {
        this.setPoint = setPoint;
        rightPID.setSetpoint(setPoint);
        leftPID.setSetpoint(setPoint);
    }

    public boolean atSpeed() {
        return Utils.isWithinTolerance(getMinVelocity(), setPoint, TOLERANCE);
    }

    private double getRightVelocity() {
        return right.getEncoder().getVelocity();
    }

    private double getLeftVelocity() {
        return left.getEncoder().getVelocity();
    }

    private double getMinVelocity() {
        return Math.min(getRightVelocity(), getLeftVelocity());
    }

    public LauncherCommands getCommands() {
        return commands;
    }
}
