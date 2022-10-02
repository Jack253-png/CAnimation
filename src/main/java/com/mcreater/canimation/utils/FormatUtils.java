package com.mcreater.canimation.utils;

import net.minecraft.text.BaseText;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Language;

public final class FormatUtils {
    public static BaseText format(String resKey) {
        return new TranslatableText("ui.log.base", Language.getInstance().get(resKey));
    }
}
