package org.ironriders.subsystems;

import com.revrobotics.spark.SparkMax;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import org.ironriders.commands.LauncherCommands;
import org.ironriders.constants.Climber.Limit;
import org.ironriders.constants.Identifiers;

import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.config.SoftLimitConfig;
import com.revrobotics.spark.config.SparkMaxConfig;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;
import static org.ironriders.constants.Launcher.*;
import static org.ironriders.constants.Launcher.PID.*;
import static org.ironriders.constants.Robot.COMPENSATED_VOLTAGE;

public class LauncherSubsystem extends SubsystemBase {
    private final LauncherCommands commands;

    private final SparkMax right = new SparkMax(Identifiers.Launcher.RIGHT, MotorType.kBrushless);
    private final PIDController rightPID = new PIDController(P, I, D);
    private final SparkMax left = new SparkMax(Identifiers.Launcher.LEFT, MotorType.kBrushless);
    private final PIDController leftPID = new PIDController(P, I, D);

    private double setPoint = 0;
    private boolean isInitialized = false;

    @SuppressWarnings("deprecation")
    public LauncherSubsystem() {
        var config= new SparkMaxConfig().idleMode(IdleMode.kCoast).smartCurrentLimit(40).apply(new SoftLimitConfig().forwardSoftLimit(Limit.FORWARD).reverseSoftLimit(Limit.REVERSE)).voltageCompensation(COMPENSATED_VOLTAGE);
        left.configure(config, null,null);
        right.configure(config, null, null);
        right.setInverted(true);
        left.setControlFramePeriodMs(VELOCITY_FILTERING);
        right.setControlFramePeriodMs(VELOCITY_FILTERING);

        SmartDashboard.putBoolean(DASHBOARD_PREFIX + "isRunning", false);

        commands = new LauncherCommands(this);
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
        if (!isInitialized) {
            set(LAUNCH_VELOCITY);
        }
        isInitialized = true;
        SmartDashboard.putBoolean(DASHBOARD_PREFIX + "isRunning", true);
    }

    public void deactivate() {
        set(0);
        isInitialized = false;
        SmartDashboard.putBoolean(DASHBOARD_PREFIX + "isRunning", false);
    }

    private void set(double setPoint) { // Here you're running one then running the other, that doesn't make sense bc it means that it will curve. TODO: make r & left seperate commands and run in parralell
        this.setPoint = setPoint;
        rightPID.reset();
        rightPID.setSetpoint(setPoint);
        leftPID.reset();
        leftPID.setSetpoint(setPoint);
    }

    public boolean isNotInitialized() {
        return !isInitialized;
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
