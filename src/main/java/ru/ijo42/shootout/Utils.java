package ru.ijo42.shootout;

import java.io.InputStream;

public class Utils {
    public static InputStream getResource(String name) {
        return ShootoutTweaks.class.getResourceAsStream(name);
    }

    public static int toIntRGB(String color) {
        return Integer.parseInt(color.replace("0x", "").replace("#", ""),16);
    }
}
