package com.mcreater.canimation.mixin;

import com.mcreater.canimation.client.CAnimationClient;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.gui.screen.CommandSuggestor;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = ChatScreen.class, priority = 2147483647)
public abstract class ChatScreenMixin extends Screen {
    private int tempY = -1;
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

    @Inject(at = @At("HEAD"), method = "resize")
    private void resize(MinecraftClient client, int width, int height, CallbackInfo ci) {
        tempY = -1;
    }
    /**
     * @author Jack253-png
     * @reason rewrite for animation
     */
    @Overwrite
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        if (tempY == -1) tempY = height + 1;
        if (tempY > height - 12) tempY -= 1;
        if (!CAnimationClient.config.model.animationControl.chatScreen) tempY = height - 12;
        chatField.y = tempY;

        this.setFocused(this.chatField);
        this.chatField.setTextFieldFocused(true);
        fill(matrices, 2, tempY - 2, this.width - 2, tempY + 10, MinecraftClient.getInstance().options.getTextBackgroundColor(Integer.MIN_VALUE));
        this.chatField.render(matrices, mouseX, mouseY, delta);
        this.commandSuggestor.render(matrices, mouseX, mouseY);
        Style style = MinecraftClient.getInstance().inGameHud.getChatHud().getText(mouseX, mouseY);
        if (style != null && style.getHoverEvent() != null) {
            this.renderTextHoverEffect(matrices, style, mouseX, mouseY);
        }
        super.render(matrices, mouseX, mouseY, delta);
    }
}
