package org.ironriders.constants;

import edu.wpi.first.math.util.Units;

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
    public static final double COMPENSATED_VOLTAGE = 10;

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
