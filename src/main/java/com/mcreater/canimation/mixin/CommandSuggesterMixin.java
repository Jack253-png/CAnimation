package com.mcreater.canimation.mixin;

import com.mcreater.canimation.client.CAnimationClient;
import com.mcreater.canimation.config.CommandSuggesterConfig;
import com.mojang.brigadier.Message;
import com.mojang.brigadier.suggestion.Suggestion;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.screen.CommandSuggestor;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.util.math.Rect2i;
import net.minecraft.text.Texts;
import net.minecraft.util.math.Vec2f;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(value = CommandSuggestor.class, priority = 2147483647)
public abstract class CommandSuggesterMixin {
    @Shadow @Final Screen owner;
    @Inject(at = @At("RETURN"), method = "<init>")
    private void init(MinecraftClient client, Screen owner, TextFieldWidget textField, TextRenderer textRenderer, boolean slashOptional, boolean suggestingWhenEmpty, int inWindowIndexOffset, int maxSuggestionSize, boolean chatScreenSized, int color, CallbackInfo ci){
        CAnimationClient.default_Java_net_minecraft_client_gui_screen_ChatScreen_commandSuggestor_owner = owner;
    }

    @Mixin(value = CommandSuggestor.SuggestionWindow.class, priority = 2147483647)
    public abstract static class SuggestionWindowMixin {
        @Mutable @Final @Shadow private Rect2i area;
        @Mutable @Final @Shadow private List<Suggestion> suggestions;
        @Shadow private int inWindowIndex;
        @Shadow private int selection;
        @Shadow private Vec2f mouse;
        @Shadow public abstract void select(int index);

        /**
         * @author Jack253-png
         * @reason Text color control
         */
        @Overwrite
        public void render(MatrixStack matrices, int mouseX, int mouseY) {
            int d = CAnimationClient.commandSuggesterConfig.suggestion_background;

            int i = Math.min(this.suggestions.size(), CommandSuggesterConfig.DEFAULT_MAX_SUGGESTION_SIZE);
            boolean bl = this.inWindowIndex > 0;
            boolean bl2 = this.suggestions.size() > this.inWindowIndex + i;
            boolean bl3 = bl || bl2;
            boolean bl4 = this.mouse.x != (float)mouseX || this.mouse.y != (float)mouseY;
            if (bl4) {
                this.mouse = new Vec2f((float)mouseX, (float)mouseY);
            }

            if (bl3) {
                DrawableHelper.fill(matrices, this.area.getX(), this.area.getY() - 1, this.area.getX() + this.area.getWidth(), this.area.getY(), d);
                DrawableHelper.fill(matrices, this.area.getX(), this.area.getY() + this.area.getHeight(), this.area.getX() + this.area.getWidth(), this.area.getY() + this.area.getHeight() + 1, d);
                int k;
                if (bl) {
                    for(k = 0; k < this.area.getWidth(); ++k) {
                        if (k % 2 == 0) {
                            DrawableHelper.fill(matrices, this.area.getX() + k, this.area.getY() - 1, this.area.getX() + k + 1, this.area.getY(), -1);
                        }
                    }
                }

                if (bl2) {
                    for(k = 0; k < this.area.getWidth(); ++k) {
                        if (k % 2 == 0) {
                            DrawableHelper.fill(matrices, this.area.getX() + k, this.area.getY() + this.area.getHeight(), this.area.getX() + k + 1, this.area.getY() + this.area.getHeight() + 1, -1);
                        }
                    }
                }
            }

            boolean bl5 = false;

            for(int l = 0; l < i; ++l) {
                Suggestion suggestion = this.suggestions.get(l + this.inWindowIndex);
                DrawableHelper.fill(matrices, this.area.getX(), this.area.getY() + 12 * l, this.area.getX() + this.area.getWidth(), this.area.getY() + 12 * l + 12, d);
                if (mouseX > this.area.getX() && mouseX < this.area.getX() + this.area.getWidth() && mouseY > this.area.getY() + 12 * l && mouseY < this.area.getY() + 12 * l + 12) {
                    if (bl4) {
                        this.select(l + this.inWindowIndex);
                    }

                    bl5 = true;
                }
                MinecraftClient.getInstance().textRenderer.drawWithShadow(matrices, suggestion.getText(), (float)(this.area.getX() + 1), (float)(this.area.getY() + 2 + 12 * l), l + this.inWindowIndex == this.selection ? CAnimationClient.commandSuggesterConfig.suggestion_selected_text_fill : CAnimationClient.commandSuggesterConfig.suggestion_text_fill);
            }

            if (bl5) {
                Message message = this.suggestions.get(this.selection).getTooltip();
                if (message != null) {
                    CAnimationClient.default_Java_net_minecraft_client_gui_screen_ChatScreen_commandSuggestor_owner.renderTooltip(matrices, Texts.toText(message), mouseX, mouseY);
                }
            }
        }
    }
}
