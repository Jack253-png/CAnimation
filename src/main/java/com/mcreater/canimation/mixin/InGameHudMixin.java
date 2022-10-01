package com.mcreater.canimation.mixin;

import com.mcreater.canimation.AnimatedChatHud;
import com.mcreater.canimation.client.CAnimationClient;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.hud.ChatHud;
import net.minecraft.client.gui.hud.InGameHud;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public abstract class InGameHudMixin extends DrawableHelper {
    @Mutable @Final @Shadow private ChatHud chatHud = null;

    @Inject(at = @At("HEAD"), method = "setDefaultTitleFade")
    private void setDefaultTitleFade(CallbackInfo info) {
        AnimatedChatHud inst = new AnimatedChatHud(MinecraftClient.getInstance());
        chatHud = inst;
        CAnimationClient.c = inst;
    }
}
