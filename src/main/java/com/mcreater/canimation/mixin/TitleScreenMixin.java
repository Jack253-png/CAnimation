package com.mcreater.canimation.mixin;

import com.mcreater.canimation.utils.ChatLogUtils;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = TitleScreen.class, priority = 2147483647)
public abstract class TitleScreenMixin {
    @Inject(at = @At("HEAD"), method = "render")
    private void render(MatrixStack matrices, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        ChatLogUtils.debugShowed = false;
        ChatLogUtils.updateShowed = false;
    }
}
