package org.ironriders.constants;



public class Lighting {
    public static final String DASHBOARD_PREFIX = "lights/";
    // 4095/255 RGB values go from 0 to 295 while the pulse width 0 to 4095
    public static final double PULSE_WIDTH = 4095 / 255.0;

    public static class Color {
        public static final int R = 0;
        public static final int G = 255;
        public static final int B = 0;
    }
}
