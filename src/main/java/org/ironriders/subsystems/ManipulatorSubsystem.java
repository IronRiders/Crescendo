package org.ironriders.subsystems;

import com.revrobotics.CANSparkMax;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import org.ironriders.commands.ManipulatorCommands;
import org.ironriders.constants.Identifiers;

import static com.revrobotics.CANSparkBase.IdleMode.kBrake;
import static com.revrobotics.CANSparkLowLevel.MotorType.kBrushless;
import static org.ironriders.constants.Manipulator.*;
import static org.ironriders.constants.Robot.COMPENSATED_VOLTAGE;

public class ManipulatorSubsystem extends SubsystemBase {
    private final ManipulatorCommands commands;

    private final CANSparkMax motor = new CANSparkMax(Identifiers.Manipulator.MOTOR, kBrushless);

    public ManipulatorSubsystem() {
        motor.setSmartCurrentLimit(CURRENT_LIMIT);
        motor.enableVoltageCompensation(COMPENSATED_VOLTAGE);
        motor.setIdleMode(kBrake);
        motor.setControlFramePeriodMs(1);

        SmartDashboard.putString(DASHBOARD_PREFIX + "state", "STOP");

        commands = new ManipulatorCommands(this);
    }

    @Override
    public void periodic() {
        SmartDashboard.putNumber(DASHBOARD_PREFIX + "velocity", getVelocity());
    }

    public void set(State state) {
        switch (state) {
            case GRAB -> intake();
            case EJECT_TO_LAUNCHER -> dischargeForLauncher();
            case EJECT_TO_AMP -> dischargeForAmp();
            case STOP -> stop();
        }
        SmartDashboard.putString(DASHBOARD_PREFIX + "state", state.name());
    }

    private void intake() {
        motor.set(INTAKE_SPEED);
    }

    private void dischargeForLauncher() {
        motor.set(DISCHARGE_FOR_LAUNCHER_SPEED);
    }

    private void dischargeForAmp() {
        motor.set(DISCHARGE_FOR_AMP_SPEED);
    }

    private void stop() {
        motor.set(0);
    }

    public double getVelocity() {
        return motor.getEncoder().getVelocity();
    }

    public ManipulatorCommands getCommands() {
        return commands;
    }
}
