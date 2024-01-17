package org.ironriders.subsystems;

import edu.wpi.first.wpilibj.AddressableLED;
import edu.wpi.first.wpilibj.AddressableLEDBuffer;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import static org.ironriders.constants.Identifiers.Lighting.PORT;
import static org.ironriders.constants.Lighting.COLOR;
import static org.ironriders.constants.Lighting.LENGTH;

public class LightingSubsystem extends SubsystemBase {
    @SuppressWarnings("FieldCanBeLocal")
    private final AddressableLED ledStrip = new AddressableLED(PORT);

    public LightingSubsystem() {
        AddressableLEDBuffer buffer = new AddressableLEDBuffer(LENGTH);
        for (int i = 0; i < LENGTH; i++) {
            buffer.setLED(i, COLOR);
        }

        ledStrip.setData(buffer);
        ledStrip.start();
    }
}
