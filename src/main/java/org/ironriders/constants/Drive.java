package org.ironriders.constants;

import edu.wpi.first.math.util.Units;
import swervelib.math.SwerveMath;

public class Drive {
    public static final String SWERVE_CONFIG_LOCATION = "swerve";
    public static final double MAX_SPEED = 4.5;

    public static class Wheels {
        public static double STEERING_CONVERSION_FACTOR =
                SwerveMath.calculateDegreesPerSteeringRotation(21.4285714286, 1);
        public static double DRIVE_CONVERSION_FACTOR =
                SwerveMath.calculateMetersPerRotation(Units.inchesToMeters(3.951), 6.75, 1);
    }
}
