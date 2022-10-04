package com.mcreater.canimation.utils;

import net.minecraft.text.TextColor;

import java.util.Objects;

public class ColorCompiler {
    public static int decompile(String raw) throws Exception {
        return Objects.requireNonNull(TextColor.parse(raw)).getRgb();
    }
    public static String compile(int raw) {
        return "#" + Integer.toHexString(raw);
    }
}
