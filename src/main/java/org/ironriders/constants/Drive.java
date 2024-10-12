package org.ironriders.constants;

import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj.DriverStation;
import org.ironriders.lib.Utils;
import swervelib.math.SwerveMath;

public class Drive {
    public static final String SWERVE_CONFIG_LOCATION = "swerve";
    public static final String DASHBOARD_PREFIX = "drive/";
    public static final double MAX_SPEED = 7.6; // We can figure this out later
    public static final double MAX_ROTATION_SPEED = 9.0;
    public static final double CLIMBING_MODE_SPEED = 0.5;
    public static final double ANGLE_TOLERANCE = 3.0; // Degrees

    public enum Heading {
        AMP(-180.0),
        STRAIGHT(0.0),
        SPEAKER_LEFT(58.0),
        SPEAKER_RIGHT(-58.0),
        STAGE_LEFT(-60.0),
        STAGE_RIGHT(60.0);

        private final Double heading;

        Heading(Double heading) {
            this.heading = heading;
        }

        public Double getHeading() {
            return heading * (Utils.getAlliance() == DriverStation.Alliance.Red ? -1 : 1);
        }

        public boolean isNotFree() {
            return heading != null;
        }
    }

    public static class HeadingController {
        public static final double SPEED_CAP = 3.5;

        public static final double P = 0.1;
        public static final double I = 0;
        public static final double D = 0;
    }

    public static class Wheels {
        public static double STEERING_CONVERSION_FACTOR = 16.8;
        public static double DRIVE_CONVERSION_FACTOR = 0.047286787200699704;
    }
}
