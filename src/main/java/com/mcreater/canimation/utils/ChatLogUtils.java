package com.mcreater.canimation.utils;

import net.fabricmc.loader.impl.FabricLoaderImpl;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ChatLogUtils {
    public static final StackWalker walker = StackWalker.getInstance(StackWalker.Option.RETAIN_CLASS_REFERENCE);
    public static boolean debugShowed = false;
    public static void printDebugLog() {
        if (!debugShowed) {
            Text t = FormatUtils.format("ui.debug");
            if (FabricLoaderImpl.INSTANCE.isDevelopmentEnvironment())
                MinecraftClient.getInstance().inGameHud.getChatHud().addMessage(t);
            debugShowed = true;
        }
    }
    public static Logger getLogger(){
        Logger logger = LogManager.getLogger(walker.getCallerClass());
        logger.info(String.format("Init for class %s", walker.getCallerClass()));
        return logger;
    }
}
