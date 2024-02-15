package org.ironriders.constants;

public class Launcher {
    public static final double INITIATION_TIMEOUT = 1.5;
    public static final double LAUNCH_VELOCITY = 5000;
    public static final int CURRENT_LIMIT = 40;
    public static final int VELOCITY_FILTERING = 5;

    public static final String DASHBOARD_PREFIX = "launcher/";

    public static class PID {
        public static final double P = 0.00017;
        public static final double I = 0.0006;
        public static final double D = 0;
    }
}
