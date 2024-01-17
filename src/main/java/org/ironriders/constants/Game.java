package org.ironriders.constants;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import org.ironriders.lib.Utils;

/**
 * Contains game specific constants.
 * <p>
 * M = Meters
 * CM = Centimeters
 * MM = Millimeters
 * <p>
 * Y = Yards
 * FT = Feet
 * IN = Inches
 */
public class Game {
    public static class KeyLocations {
        private static final double OFFSET_TO_SPEAKER = 0;
        private static final double OFFSET_TO_AMP = 0;
        public static final Location SPEAKER_LEFT =
                new Location(0, 0, 0, OFFSET_TO_AMP);
        public static final Location SPEAKER_CENTER =
                new Location(1, 5.547867999999999, 0, OFFSET_TO_AMP);
        public static final Location SPEAKER_RIGHT =
                new Location(0, 0, 0, OFFSET_TO_AMP);
        public static final Location AMP =
                new Location(1.8415, 8.2042, -90, OFFSET_TO_AMP);
        public static final Location STAGE_LEFT =
                new Location(4.641342, 4.49834, 120, OFFSET_TO_AMP);
        public static final Location CENTER_STAGE =
                new Location(5.320792, 4.105148, 0, OFFSET_TO_AMP);
        public static final Location STAGE_RIGHT =
                new Location(4.641342, 3.7132259999999997, -120, OFFSET_TO_AMP);
        private static final double OFFSET_TO_STAGE = 0;
    }

    /**
     * Contains a key pose and the distance to it. Useful for when you have some pose, and you want to easily
     * change the distance to a key location.
     */
    public static class Location {
        private final Pose2d pose;
        private final double offset;

        public Location(double x, double y, double rotation, double offset) {
            pose = new Pose2d(x, y, Rotation2d.fromDegrees(rotation));
            this.offset = offset;
        }

        /**
         * With offset applied.
         */
        public Pose2d getRobotPose() {
            return Utils.accountedPose(pose, offset);
        }
    }
}
