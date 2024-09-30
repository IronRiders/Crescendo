package org.ironriders.constants;



public class Lighting {
    public static final String DASHBOARD_PREFIX = "lighting/";
    public static final int STRIP_LENGTH = 29;

    public enum LightState {
        OFF   (0, 0, 0),
        GREEN (0, 255, 0);

        public int r;
        public int g;
        public int b;

        LightState(int r, int g, int b) {
            this.r = r;
            this.g = g;
            this.b = b;
        }
    }
}
