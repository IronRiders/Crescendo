package org.ironriders.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.SparkLimitSwitch;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import org.ironriders.commands.ManipulatorCommands;
import org.ironriders.constants.Identifiers;

import static com.revrobotics.CANSparkBase.IdleMode.kBrake;
import static com.revrobotics.CANSparkLowLevel.MotorType.kBrushless;
import static com.revrobotics.SparkLimitSwitch.Type.kNormallyOpen;
import static org.ironriders.constants.Manipulator.*;
import static org.ironriders.constants.Robot.COMPENSATED_VOLTAGE;

public class ManipulatorSubsystem extends SubsystemBase {
    private final ManipulatorCommands commands;

    private final CANSparkMax motor = new CANSparkMax(Identifiers.Manipulator.MOTOR, kBrushless);

    private final SparkLimitSwitch limitSwitch = motor.getForwardLimitSwitch(kNormallyOpen);

    private State state = State.STOP;

    public ManipulatorSubsystem() {
        motor.setSmartCurrentLimit(CURRENT_LIMIT);
        motor.enableVoltageCompensation(COMPENSATED_VOLTAGE);
        motor.setIdleMode(kBrake);
        motor.setControlFramePeriodMs(VELOCITY_FILTERING);

        limitSwitch.enableLimitSwitch(false);

        SmartDashboard.putString(DASHBOARD_PREFIX + "state", "STOP");

        commands = new ManipulatorCommands(this);
    }

    @Override
    public void periodic() {
        if (limitSwitch.isPressed() && state.equals(State.GRAB)) stop();

        SmartDashboard.putNumber(DASHBOARD_PREFIX + "velocity", getVelocity());
    }

    public void set(State state) {
        switch (state) {
            case GRAB -> grab();
            case EJECT_TO_LAUNCHER -> dischargeForLauncher();
            case EJECT_TO_AMP -> dischargeForAmp();
            case STOP -> stop();
        }
        this.state = state;
        SmartDashboard.putString(DASHBOARD_PREFIX + "state", state.name());
    }

    private void grab() {
        motor.set(GRAB_SPEED);
    }

    private void dischargeForLauncher() {
        motor.set(DISCHARGE_FOR_LAUNCHER_SPEED);
    }

    private void dischargeForAmp() {
        motor.set(DISCHARGE_FOR_AMP_SPEED);
    }

    private void stop() {
        motor.stopMotor();
    }

    public double getVelocity() {
        return motor.getEncoder().getVelocity();
    }

    public ManipulatorCommands getCommands() {
        return commands;
    }
}
