package org.ironriders.subsystems;

import edu.wpi.first.wpilibj.AddressableLED;
import edu.wpi.first.wpilibj.AddressableLEDBuffer;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import org.ironriders.constants.Identifiers;

import static org.ironriders.constants.Lighting.DOES_NOT_HAVE_NOTE;
import static org.ironriders.constants.Lighting.STRIP_LENGTH;

public class LightingSubsystem extends SubsystemBase {
    public AddressableLED addressableLED = new AddressableLED(Identifiers.Lighting.PORT);

    public LightingSubsystem() {
        addressableLED.setLength(STRIP_LENGTH);
        setRGB(DOES_NOT_HAVE_NOTE.R, DOES_NOT_HAVE_NOTE.G, DOES_NOT_HAVE_NOTE.B);
        addressableLED.start();
    }

    public void setRGB(int r, int g, int b) {
        AddressableLEDBuffer buffer = new AddressableLEDBuffer(STRIP_LENGTH);
        for (int i = 0; i < STRIP_LENGTH; i++) {
            buffer.setRGB(i, r, g, b);
        }

        addressableLED.setData(buffer);
    }
}
