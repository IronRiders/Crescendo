package org.ironriders.constants;

public class Teleop {
    public static class Speed {
        public static final double MIN_MULTIPLIER = 0.35;
        public static final double EXPONENT = 3;
        public static final double DEADBAND = 0.1;
    }

    public static class Controllers {
        public static class Joystick {
            public static final double EXPONENT = 3;
            public static final double DEADBAND = 0.15;
        }
    }
}
