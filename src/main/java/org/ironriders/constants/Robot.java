package org.ironriders.constants;

import edu.wpi.first.math.geometry.Rotation3d;
import edu.wpi.first.math.geometry.Transform3d;
import edu.wpi.first.math.util.Units;
import swervelib.math.SwerveMath;

/**
 * Robot specific constants.
 * <p>
 * M = Meters
 * CM = Centimeters
 * MM = Millimeters
 * <p>
 * Y = Yards
 * FT = Feet
 * IN = Inches
 */
public class Robot {
    public static final Transform3d LIMELIGHT_POSITION =
            new Transform3d(Units.inchesToMeters(0), 0, Units.inchesToMeters(0), new Rotation3d());

    public static class Wheels {
        public static double STEERING_CONVERSION_FACTOR =
                SwerveMath.calculateDegreesPerSteeringRotation(21.4285714286, 1);
        public static double DRIVE_CONVERSION_FACTOR =
                SwerveMath.calculateMetersPerRotation(Units.inchesToMeters(3.951), 6.75, 1);
    }

    /**
     * Robot dimensions in IN (including bumpers).
     */
    public static class Dimensions {
        public static final double LENGTH = 31;
        public static final double WIDTH = 31;
        public static final double HEIGHT = -1;

        public static final double DRIVEBASE_RADIUS = 0.3048;
        /**
         * Length in M.
         */
        public static final double DISTANCE_FROM_ORIGIN_TO_BUMPER = Units.inchesToMeters(LENGTH / 2.0);
    }
}
