package org.ironriders.constants;

public class Manipulator {
    public static final int CURRENT_LIMIT = 20;
    public static final double CENTER_NOTE_SPEED = 1;
    public static final int VELOCITY_FILTERING = 16;
    public static final int DISCHARGE_TIMEOUT = 1;
    public static final String DASHBOARD_PREFIX = "manipulator/";

    public enum State {
        GRAB(1.0),
        EJECT_TO_LAUNCHER(-1.0),
        EJECT_TO_AMP(-0.5),
        STOP(0.0),
        CENTER_NOTE(null);

        private final Double speed;

        State(Double speed) {
            this.speed = speed;
        }

        public Double getSpeed() {
            return speed;
        }
    }
}
