package org.ironriders.constants;

import edu.wpi.first.math.geometry.Rotation3d;
import edu.wpi.first.math.geometry.Transform3d;
import edu.wpi.first.math.util.Units;

public class Vision {
    public static final String VISION_CAMERA = "VISION_CAMERA";

    public static final String DASHBOARD_PREFIX = "vision/";

    public static final Transform3d CAMERA_POSITION =
            new Transform3d(
                    Units.inchesToMeters(13.5),
                    0,
                    Units.inchesToMeters(20),
                    new Rotation3d(0, Units.degreesToRadians(-20), 0));
}
