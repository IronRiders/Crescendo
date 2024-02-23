package org.ironriders.constants;

import edu.wpi.first.math.trajectory.TrapezoidProfile;

public class Pivot {
    public static final double TOLERANCE = 10;
    public static final double ENCODER_OFFSET = 150;
    public static final int CURRENT_LIMIT = 40;

    public static final String DASHBOARD_PREFIX = "pivot/";

    public enum State {
        GROUND(30),
        AMP(30),
        STOWED_TO_PERIMETER(169),
        LAUNCHER(246);

        final double position;

        State(int position) {
            this.position = position;
        }

        public double getPosition() {
            return position;
        }
    }

    public static class Control {
        public static final double P = 0.02;
        public static final double I = 0;
        public static final double D = 0;

        public static final TrapezoidProfile.Constraints PROFILE =
                new TrapezoidProfile.Constraints(360, 410);
    }
}
