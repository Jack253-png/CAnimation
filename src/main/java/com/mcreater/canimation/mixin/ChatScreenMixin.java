package com.mcreater.canimation.mixin;

import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.gui.screen.CommandSuggestor;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = ChatScreen.class, priority = 2147483647)
public abstract class ChatScreenMixin extends Screen  {
    private double tempY = -1;
    @Shadow protected TextFieldWidget chatField;
    @Shadow CommandSuggestor commandSuggestor;
    protected ChatScreenMixin(Text title) {
        super(title);
    }
    @Inject(at = @At("RETURN"), method = "keyPressed")
    private void keyPressed(int keyCode, int scanCode, int modifiers, CallbackInfoReturnable<Boolean> cir) {
        if (this.commandSuggestor.keyPressed(keyCode, scanCode, modifiers)) {}
        else if (super.keyPressed(keyCode, scanCode, modifiers)) {}
        else if (keyCode == 256) {}
        else if (keyCode != 257 && keyCode != 335) {}
        else {
            tempY = -1;
        }
    }

    @Inject(at = @At("RETURN"), method = "render")
    private void render(MatrixStack matrices, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        if (tempY == -1) tempY = height + 1;
        if (tempY > height - 12) tempY -= 1;
        chatField.y = (int) tempY;
    }
}
