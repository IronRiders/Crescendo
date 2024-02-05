package org.ironriders.constants;

import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj.DriverStation;
import org.ironriders.lib.Utils;
import swervelib.math.SwerveMath;

public class Drive {
    public static final String SWERVE_CONFIG_LOCATION = "swerve";
    public static final double MAX_SPEED = 4.5;

    public enum HeadingMode {
        FREE(null),
        AMP(-180.0),
        STRAIGHT(0.0),
        SPEAKER_LEFT(-77.5),
        SPEAKER_RIGHT(77.5),
        STAGE_LEFT(60.0),
        STAGE_RIGHT(-60.0);

        private final Double heading;

        HeadingMode(Double heading) {
            this.heading = heading;
        }

        @SuppressWarnings("DataFlowIssue")
        public Double getHeading() {
            return Utils.getAlliance().equals(DriverStation.Alliance.Red) && heading != null ? heading + 180 : heading;
        }

        public boolean isFree() {
            return heading == null;
        }
    }

    public static class HeadingController {
        public static final double SPEED_CAP = 0.3;

        public static final double P = 0.001;
        public static final double I = 0;
        public static final double D = 0;
    }

    public static class Wheels {
        public static double STEERING_CONVERSION_FACTOR =
                SwerveMath.calculateDegreesPerSteeringRotation(21.4285714286, 1);
        public static double DRIVE_CONVERSION_FACTOR =
                SwerveMath.calculateMetersPerRotation(Units.inchesToMeters(3.951), 6.75, 1);
    }
}
