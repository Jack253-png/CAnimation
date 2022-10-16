package com.mcreater.canimation.client;

import com.mcreater.canimation.config.AnimationConfig;
import com.mcreater.canimation.config.CommandSuggesterConfig;
import com.mcreater.canimation.config.PlayerListConfig;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.screen.Screen;

@Environment(EnvType.CLIENT)
public class CAnimationClient implements ClientModInitializer {
    public static final CommandSuggesterConfig commandSuggesterConfig = new CommandSuggesterConfig();
    public static final AnimationConfig animationConfig = new AnimationConfig();
    public static final PlayerListConfig playerListConfig = new PlayerListConfig();

    public void onInitializeClient() {

    }
}
