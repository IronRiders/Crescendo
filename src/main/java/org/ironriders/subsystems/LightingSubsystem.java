package org.ironriders.subsystems;


import edu.wpi.first.wpilibj.PWM;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import static org.ironriders.constants.Lighting.*;


public class LightingSubsystem extends SubsystemBase {

    public PWM redChannel = new PWM(LIGHTCHANNELRED, false);
    public PWM greenChannel = new PWM(LIGHTCHANNELGREEN, false);
    public PWM blueChannel = new PWM(LIGHTCHANNELBLUE, false);

    public LightingSubsystem() {
        setColorRGB(RED,GREEN,BLUE);
    }


    public void setColorRGB(int r, int g, int b) {
        redChannel.setPulseTimeMicroseconds((int) Math.round(r*RGBTOPULSEWIDTH));
        greenChannel.setPulseTimeMicroseconds((int) Math.round(g*RGBTOPULSEWIDTH));
        blueChannel.setPulseTimeMicroseconds((int) Math.round(b*RGBTOPULSEWIDTH));

    }



}
