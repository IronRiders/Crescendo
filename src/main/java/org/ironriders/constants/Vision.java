package org.ironriders.constants;

import edu.wpi.first.math.geometry.Rotation3d;
import edu.wpi.first.math.geometry.Transform3d;
import edu.wpi.first.math.util.Units;

public class Vision {
    public static final String VISION_CAMERA = "VISION_CAMERA";

    public static final Transform3d LIMELIGHT_POSITION =
            new Transform3d(Units.inchesToMeters(0), 0, Units.inchesToMeters(0), new Rotation3d());
}
