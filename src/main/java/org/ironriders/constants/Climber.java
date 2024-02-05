package org.ironriders.constants;

public class Climber {
    public static final int CURRENT_LIMIT = 20;
    public static final int GEARING = 500;
    public static final double SPEED = 0.7;

    public static final String DASHBOARD_PREFIX = "climber/";

    public static class RollController {

        public static final double P = 0.001;
        public static final double I = 0;
        public static final double D = 0;
    }

    public static class Limit {
        public static final float REVERSE = 0;
        public static final float FORWARD = 30;
    }
}
