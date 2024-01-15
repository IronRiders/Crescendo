package org.ironriders.subsystems;

import com.revrobotics.CANSparkMax;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import org.ironriders.commands.ShooterCommands;
import org.ironriders.constants.Identifiers;

import static com.revrobotics.CANSparkMax.IdleMode.kCoast;
import static com.revrobotics.CANSparkMaxLowLevel.MotorType.kBrushless;
import static org.ironriders.constants.Robot.COMPENSATED_VOLTAGE;
import static org.ironriders.constants.Shooter.*;

public class ShooterSubsystem extends SubsystemBase {
    private final ShooterCommands commands;

    private final CANSparkMax leader = new CANSparkMax(Identifiers.Shooter.LEADER, kBrushless);
    @SuppressWarnings("FieldCanBeLocal")
    private final CANSparkMax follower = new CANSparkMax(Identifiers.Shooter.FOLLOWER, kBrushless);

    public ShooterSubsystem() {
        leader.setSmartCurrentLimit(CURRENT_LIMIT);
        follower.setSmartCurrentLimit(CURRENT_LIMIT);
        leader.enableVoltageCompensation(COMPENSATED_VOLTAGE);
        follower.enableVoltageCompensation(COMPENSATED_VOLTAGE);
        leader.setIdleMode(kCoast);
        follower.setIdleMode(kCoast);

        follower.follow(leader, true);

        SmartDashboard.putBoolean(DASHBOARD_PREFIX + "isRunning", false);
        SmartDashboard.putNumber(DASHBOARD_PREFIX + "velocity", getVelocity());

        commands = new ShooterCommands(this);
    }

    public void run() {
        leader.set(LAUNCH_SPEED);
        SmartDashboard.putBoolean(DASHBOARD_PREFIX + "isRunning", true);
    }

    public void stop() {
        leader.set(0);
        SmartDashboard.putBoolean(DASHBOARD_PREFIX + "isRunning", false);
    }

    public boolean isWithinInitiationVelocityThreshold() {
        return getVelocity() > INITIATION_VELOCITY_THRESHOLD;
    }

    public double getVelocity() {
        return leader.getEncoder().getVelocity();
    }

    public ShooterCommands getCommands() {
        return commands;
    }
}
