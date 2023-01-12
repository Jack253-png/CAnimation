package com.mcreater.canimation.client;

import com.mcreater.canimation.config.CommonConfig;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public class CAnimationClient implements ClientModInitializer {
    public static final CommonConfig config = new CommonConfig();
    public void onInitializeClient() {

    }
}
