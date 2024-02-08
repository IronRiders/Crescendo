package org.ironriders.constants;

public class Launcher {
    public static final double INITIATION_TIMEOUT = 100;
    public static final double LAUNCH_VELOCITY = 3000;
    public static final int CURRENT_LIMIT = 40;
    public static final int TOLERANCE = 1;
    public static final int VELOCITY_FILTERING = 5;

    public static final String DASHBOARD_PREFIX = "launcher/";

    public static class PID {
        public static final double P = 0.0002;
        public static final double I = 0.0001;
        public static final double D = 0;
    }
}
