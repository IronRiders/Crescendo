package org.ironriders.constants;

import edu.wpi.first.math.trajectory.TrapezoidProfile;

public class Pivot {
    public static final double TOLERANCE = 10;
    public static final double ENCODER_OFFSET = 260;
    public static final int CURRENT_LIMIT = 100;

    public static final String DASHBOARD_PREFIX = "pivot/";

    public enum State {
        GROUND(42),
        AMP(42),
        STOWED(173),
        LAUNCHER(256);

        final double position;

        State(int position) {
            this.position = position;
        }

        public double getPosition() {
            return position;
        }
    }

    public static class Control {
        public static final double P = 0.01;
        public static final double I = 0;
        public static final double D = 0;

        public static final TrapezoidProfile.Constraints PROFILE =
                new TrapezoidProfile.Constraints(500, 850);
    }
}
