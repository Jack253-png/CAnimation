package com.mcreater.canimation.mixin;

import com.mcreater.canimation.client.CAnimationClient;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.screen.CommandSuggestor;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CommandSuggestor.class)
public abstract class CommandSuggesterMixin {
    @Shadow @Final @Mutable int color;
    @Inject(at = @At("RETURN"), method = "<init>")
    private void init(MinecraftClient client, Screen owner, TextFieldWidget textField, TextRenderer textRenderer, boolean slashOptional, boolean suggestingWhenEmpty, int inWindowIndexOffset, int maxSuggestionSize, boolean chatScreenSized, int color, CallbackInfo ci){
        CAnimationClient.Java_net_minecraft_client_gui_screen_ChatScreen_commandSuggestor_color = color;
        CAnimationClient.Java_net_minecraft_client_gui_screen_ChatScreen_commandSuggestor_maxSuggestionSize = maxSuggestionSize;
        CAnimationClient.Java_net_minecraft_client_gui_screen_ChatScreen_commandSuggestor_textRenderer = textRenderer;
        CAnimationClient.Java_net_minecraft_client_gui_screen_ChatScreen_commandSuggestor_owner = owner;
    }

    @Inject(at = @At("HEAD"), method = "showSuggestions")
    private void showSuggestions(boolean narrateFirstSuggestion, CallbackInfo ci) {
        color = 16777215 + (1 << 24);
    }
}
