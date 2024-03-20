package org.ironriders.subsystems;

import edu.wpi.first.wpilibj.AddressableLED;
import edu.wpi.first.wpilibj.AddressableLEDBuffer;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import org.ironriders.constants.Identifiers;

import static org.ironriders.constants.Lighting.*;

public class LightingSubsystem extends SubsystemBase {
    private final AddressableLED addressableLED = new AddressableLED(Identifiers.Lighting.PORT);
    private final AddressableLEDBuffer buffer = new AddressableLEDBuffer(STRIP_LENGTH);

    public LightingSubsystem() {
        addressableLED.setLength(buffer.getLength());
        srtLighting(false);
        addressableLED.start();
    }

    public void srtLighting(boolean hasNote) {
        int r = hasNote ? HAS_NOTE.R : DOES_NOT_HAVE_NOTE.R;
        int g = hasNote ? HAS_NOTE.G : DOES_NOT_HAVE_NOTE.G;
        int b = hasNote ? HAS_NOTE.B : DOES_NOT_HAVE_NOTE.B;

        for (int i = 0; i < buffer.getLength(); i++) {
            buffer.setRGB(i, r, g, b);
        }

        addressableLED.setData(buffer);
    }
}
