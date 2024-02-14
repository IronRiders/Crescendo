package org.ironriders.subsystems;

import com.revrobotics.CANSparkMax;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import org.ironriders.commands.LauncherCommands;
import org.ironriders.constants.Identifiers;

import static com.revrobotics.CANSparkBase.IdleMode.kCoast;
import static com.revrobotics.CANSparkLowLevel.MotorType.kBrushless;
import static org.ironriders.constants.Launcher.*;
import static org.ironriders.constants.Launcher.PID.*;
import static org.ironriders.constants.Robot.COMPENSATED_VOLTAGE;

public class LauncherSubsystem extends SubsystemBase {
    private final LauncherCommands commands;

    private final CANSparkMax right = new CANSparkMax(Identifiers.Launcher.RIGHT, kBrushless);
    private final PIDController rightPID = new PIDController(P, I, D);
    private final CANSparkMax left = new CANSparkMax(Identifiers.Launcher.LEFT, kBrushless);
    private final PIDController leftPID = new PIDController(P, I, D);

    private double setPoint = 0;

    public LauncherSubsystem() {
        applyConfig(right);
        applyConfig(left);

        right.setInverted(true);

        SmartDashboard.putBoolean(DASHBOARD_PREFIX + "isRunning", false);

        commands = new LauncherCommands(this);
    }

    private void applyConfig(CANSparkMax motor) {
        motor.restoreFactoryDefaults();

        motor.setSmartCurrentLimit(CURRENT_LIMIT);
        motor.enableVoltageCompensation(COMPENSATED_VOLTAGE);
        motor.setIdleMode(kCoast);
        motor.setControlFramePeriodMs(VELOCITY_FILTERING);
    }

    @Override
    public void periodic() {
        if (setPoint == 0) {
            right.set(0);
            left.set(0);
        } else {
            right.set(rightPID.calculate(getRightVelocity()));
            left.set(leftPID.calculate(getLeftVelocity()));
        }

        SmartDashboard.putNumber(DASHBOARD_PREFIX + "rightVelocity", getRightVelocity());
        SmartDashboard.putNumber(DASHBOARD_PREFIX + "leftVelocity", getLeftVelocity());
        SmartDashboard.putNumber(DASHBOARD_PREFIX + "setPoint", setPoint);
    }

    public void run() {
        set(LAUNCH_VELOCITY);
        SmartDashboard.putBoolean(DASHBOARD_PREFIX + "isRunning", true);
    }

    public void deactivate() {
        set(0);
        SmartDashboard.putBoolean(DASHBOARD_PREFIX + "isRunning", false);
    }

    private void set(double setPoint) {
        this.setPoint = setPoint;
        rightPID.reset();
        rightPID.setSetpoint(setPoint);
        leftPID.reset();
        leftPID.setSetpoint(setPoint);
    }

    private double getRightVelocity() {
        return right.getEncoder().getVelocity();
    }

    private double getLeftVelocity() {
        return left.getEncoder().getVelocity();
    }

    public LauncherCommands getCommands() {
        return commands;
    }
}
