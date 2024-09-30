package org.ironriders.subsystems;

import edu.wpi.first.wpilibj.AddressableLED;
import edu.wpi.first.wpilibj.AddressableLEDBuffer;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import org.ironriders.constants.Identifiers;

import static org.ironriders.constants.Lighting.*;

public class LightingSubsystem extends SubsystemBase {
    private final AddressableLED addressableLED = new AddressableLED(Identifiers.Lighting.PORT);
    private final AddressableLEDBuffer buffer = new AddressableLEDBuffer(STRIP_LENGTH);
    
    private LightState lightState = LightState.OFF;

    public LightingSubsystem() {
        addressableLED.setLength(buffer.getLength());
        
        for (int i = 0; i < buffer.getLength(); i++) { 
            buffer.setRGB(i, lightState.r, lightState.g, lightState.b); 
        }

        addressableLED.setData(buffer);
        addressableLED.start();
    }

    public void setLighting(LightState state) {
        lightState = state;
    }

    @Override
    public void periodic() {
        for (int i = 0; i < buffer.getLength(); i++) {
            buffer.setRGB(
                i, 
                lightState.r,
                lightState.g,
                lightState.b
            );
       }

       addressableLED.setData(buffer);
    }
}
