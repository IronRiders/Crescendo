package org.ironriders.subsystems;

import com.revrobotics.spark.SparkMax;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import org.ironriders.commands.ClimberCommands;
import org.ironriders.constants.Identifiers;

import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.config.SoftLimitConfig;
import com.revrobotics.spark.config.SparkMaxConfig;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;

import  org.ironriders.constants.Climber.*;

public class ClimberSubsystem extends SubsystemBase {
    private final ClimberCommands commands;

    private final SparkMax right = new SparkMax(Identifiers.Climber.RIGHT, MotorType.kBrushless);
    private final SparkMax left = new SparkMax(Identifiers.Climber.LEFT, MotorType.kBrushless);

    private double input = 0;
    private boolean climbingMode = false;

    public ClimberSubsystem() {

        var config= new SparkMaxConfig().idleMode(IdleMode.kBrake).smartCurrentLimit(40).apply(new SoftLimitConfig().forwardSoftLimit(Limit.FORWARD).reverseSoftLimit(Limit.REVERSE));
        right.configure(config, null, null);
        config.follow(right,true);
        left.configure(config, null,null);
        

        commands = new ClimberCommands(this);
    }

    
    @Override
    public void periodic() {
        if (climbingMode) {
            right.set(input * 1);
        } else {
            right.stopMotor();
            left.stopMotor();
        }

        SmartDashboard.putBoolean("climber/" + "climbingModeEnabled", climbingMode);
        SmartDashboard.putNumber("climber/" + "rightPosition", right.getEncoder().getPosition());
        SmartDashboard.putNumber("climber/" + "leftPosition", left.getEncoder().getPosition());
        SmartDashboard.putNumber("climber/" + "input", input);
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
