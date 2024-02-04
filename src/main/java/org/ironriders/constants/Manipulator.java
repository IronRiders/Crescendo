package org.ironriders.constants;

public class Manipulator {
    public static final int CURRENT_LIMIT = 20;
    public static final double GRAB_SPEED = 0.5;
    public static final double DISCHARGE_FOR_LAUNCHER_SPEED = 1;
    public static final double DISCHARGE_FOR_AMP_SPEED = 1;
    public static final double DISCHARGE_TIMEOUT = 0.5;
    public static final int VELOCITY_FILTERING = 16;
    public static final String DASHBOARD_PREFIX = "manipulator/";

    public enum State {
        GRAB,
        EJECT_TO_LAUNCHER,
        EJECT_TO_AMP,
        STOP
    }
}
