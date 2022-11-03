package com.mcreater.canimation.utils;

import net.minecraft.text.BaseText;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Language;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public final class FormatUtils {
    public static BaseText format(String resKey) {
        return new TranslatableText("ui.log.base", Language.getInstance().get(resKey));
    }
    public static BaseText format(String resKey, Object... args) {
        return new TranslatableText("ui.log.base", Language.getInstance().get(resKey), args);
    }
    public static File getDir(File src1) {
        String src = src1.getAbsolutePath();
        src = src.replace("\\", "/");
        List<String> s = new ArrayList<>(List.of(src.split("/")));
        s.remove(s.get(s.size() - 1));
        return new File(String.join("/", s));
    }
}
