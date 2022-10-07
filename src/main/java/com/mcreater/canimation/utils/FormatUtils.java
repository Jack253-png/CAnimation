package com.mcreater.canimation.utils;

import com.mojang.logging.LogUtils;
import net.minecraft.text.BaseText;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Language;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class FormatUtils {
    public static BaseText format(String resKey) {
        return new TranslatableText("ui.log.base", Language.getInstance().get(resKey));
    }
    public static File getDir(String src) {
        src = src.replace("\\", "/");
        List<String> s = new ArrayList<>(List.of(src.split("/")));
        s.remove(s.get(s.size() - 1));
        return new File(String.join("/", s));
    }
}
