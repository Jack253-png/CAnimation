package com.mcreater.canimation.client;

import com.mcreater.canimation.AnimatedChatHud;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public class CAnimationClient implements ClientModInitializer {
    public static AnimatedChatHud c = null;
    @Override
    public void onInitializeClient() {

    }
}
