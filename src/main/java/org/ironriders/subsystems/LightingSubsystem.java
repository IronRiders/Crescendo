package org.ironriders.subsystems;


import edu.wpi.first.math.geometry.Pose3d;
import edu.wpi.first.wpilibj.PWM;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import org.photonvision.EstimatedRobotPose;

import java.util.Optional;

import static org.ironriders.constants.Lighting.Color.*;
import static org.ironriders.constants.Lighting.DASHBOARD_PREFIX;
import static org.ironriders.constants.Lighting.PULSE_WIDTH;

public class LightingSubsystem extends SubsystemBase {
    private final VisionSubsystem vision = new VisionSubsystem();
    public PWM redChannel = new PWM(1, false);
    public PWM greenChannel = new PWM(2, false);
    public PWM blueChannel = new PWM(3, false);

    public LightingSubsystem() {
        SmartDashboard.putData(DASHBOARD_PREFIX + "redChannel", redChannel);
        SmartDashboard.putData(DASHBOARD_PREFIX + "greenChannel", greenChannel);
        SmartDashboard.putData(DASHBOARD_PREFIX + "blueChannel", blueChannel);

        setColorRGB(R, G, B);
    }

    public void setColorRGB(int r, int g, int b) {
        redChannel.setPulseTimeMicroseconds((int) Math.round(r * PULSE_WIDTH));
        greenChannel.setPulseTimeMicroseconds((int) Math.round(g * PULSE_WIDTH));
        blueChannel.setPulseTimeMicroseconds((int) Math.round(b * PULSE_WIDTH));
    }
    public void periodic(){

        vision.getPoseEstimate().ifPresent(estimatedRobotPose->setColorRGB(255,0,0));
    }

}
