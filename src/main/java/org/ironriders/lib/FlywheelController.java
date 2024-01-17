package org.ironriders.lib;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.controller.SimpleMotorFeedforward;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class FlywheelController {
    private final PIDController pid;
    private final SimpleMotorFeedforward feedforward;
    private double setPoint = 0;

    public FlywheelController(double P, double I, double D, double S, double V, double A, String dashboardPrefix) {
        pid = new PIDController(P, I, D);
        feedforward = new SimpleMotorFeedforward(S, V, A);

        SmartDashboard.putData(dashboardPrefix + "pid", pid);
    }

    public double calculate(double measurement) {
        return pid.calculate(measurement, setPoint) + feedforward.calculate(measurement);
    }

    public void setPoint(double setPoint) {
        this.setPoint = setPoint;
    }
}
