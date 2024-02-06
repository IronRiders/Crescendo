package org.ironriders.constants;

public class Manipulator {
    public static final int CURRENT_LIMIT = 20;
    public static final int VELOCITY_FILTERING = 16;
    public static final int DISCHARGE_TIMEOUT = 1;
    public static final String DASHBOARD_PREFIX = "manipulator/";

    public enum State {
        GRAB(0.5),
        EJECT_TO_LAUNCHER(1),
        EJECT_TO_AMP(1),
        STOP(0);

        private final double speed;

        State(double speed) {
            this.speed = speed;
        }

        public double getSpeed() {
            return speed;
        }
    }
}
