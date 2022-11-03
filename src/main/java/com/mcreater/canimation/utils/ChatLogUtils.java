package com.mcreater.canimation.utils;

import com.mcreater.canimation.CAnimation;
import net.fabricmc.loader.impl.FabricLoaderImpl;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Objects;

public class ChatLogUtils {
    public static final StackWalker walker = StackWalker.getInstance(StackWalker.Option.RETAIN_CLASS_REFERENCE);
    public static boolean debugShowed = false;
    public static boolean updateShowed = false;
    public static void printDebugLog() {
        if (!debugShowed) {
            Text t = FormatUtils.format("ui.debug");
            if (FabricLoaderImpl.INSTANCE.isDevelopmentEnvironment())
                MinecraftClient.getInstance().inGameHud.getChatHud().addMessage(t);
            debugShowed = true;
        }
        try {
            if (!updateShowed) {
                if (!Objects.equals(CAnimation.checkUpdate(), CAnimation.getCurrentModVer())) {
                    Text t = FormatUtils.format("ui.update");
                    MinecraftClient.getInstance().inGameHud.getChatHud().addMessage(t);
                    updateShowed = true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static Logger getLogger() {
        return LogManager.getLogger(walker.getCallerClass());
    }
}
