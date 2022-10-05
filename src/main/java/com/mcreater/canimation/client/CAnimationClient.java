package com.mcreater.canimation.client;

import com.mcreater.canimation.config.AnimationConfig;
import com.mcreater.canimation.config.CommandSuggesterConfig;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.screen.Screen;

@Environment(EnvType.CLIENT)
public class CAnimationClient implements ClientModInitializer {
    public static Screen default_Java_net_minecraft_client_gui_screen_ChatScreen_commandSuggestor_owner;

    public static final CommandSuggesterConfig commandSuggesterConfig = new CommandSuggesterConfig();
    public static final AnimationConfig animationConfig = new AnimationConfig();

    public void onInitializeClient() {

    }
}
