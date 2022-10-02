package com.mcreater.canimation.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.screen.Screen;

@Environment(EnvType.CLIENT)
public class CAnimationClient implements ClientModInitializer {
    public static int Java_net_minecraft_client_gui_screen_ChatScreen_commandSuggestor_color;
    public static int Java_net_minecraft_client_gui_screen_ChatScreen_commandSuggestor_maxSuggestionSize;
    public static TextRenderer Java_net_minecraft_client_gui_screen_ChatScreen_commandSuggestor_textRenderer;
    public static Screen Java_net_minecraft_client_gui_screen_ChatScreen_commandSuggestor_owner;
    public void onInitializeClient() {

    }
}
