package org.ironriders.constants;

import com.pathplanner.lib.path.PathConstraints;
import edu.wpi.first.math.util.Units;

public class Auto {
    public static final String DEFAULT_AUTO = "TEST";

    public enum PathfindingConstraintProfile {
        SLOW(new PathConstraints(
                1,
                0.5,
                Units.degreesToRadians(360),
                Units.degreesToRadians(180)
        )),
        MEDIUM(new PathConstraints(
                2,
                1.5,
                Units.degreesToRadians(360),
                Units.degreesToRadians(180)
        )),
        FAST(new PathConstraints(
                2.5,
                2,
                Units.degreesToRadians(360),
                Units.degreesToRadians(180)
        ));

        private final PathConstraints constraints;

        PathfindingConstraintProfile(PathConstraints constraints) {
            this.constraints = constraints;
        }

        public static PathfindingConstraintProfile getDefault() {
            return SLOW;
        }

        public PathConstraints getConstraints() {
            return constraints;
        }
    }
}
