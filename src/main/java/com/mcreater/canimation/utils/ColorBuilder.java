package com.mcreater.canimation.utils;

import net.minecraft.text.TextColor;

import java.util.Objects;

public class ColorBuilder {
    public static int getColor(int r, int g, int b, int a) {
        return r + g << 8 + b << 16 + a << 24;
    }
    public static int getColor(String r) {
        return Objects.requireNonNull(TextColor.parse(r)).getRgb();
    }
}
