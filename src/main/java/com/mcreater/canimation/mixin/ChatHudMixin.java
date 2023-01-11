package com.mcreater.canimation.mixin;

import com.mcreater.canimation.client.CAnimationClient;
import com.mcreater.canimation.utils.ChatLogUtils;
import com.mcreater.canimation.utils.FrictionsGenerator;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.hud.ChatHud;
import net.minecraft.client.gui.hud.ChatHudLine;
import net.minecraft.client.gui.hud.MessageIndicator;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Mixin(value = ChatHud.class, priority = 2147483647)
public abstract class ChatHudMixin extends DrawableHelper {
    @Shadow @Final private MinecraftClient client;
    @Shadow @Final private List<ChatHudLine.Visible> visibleMessages;
    @Shadow private int scrolledLines;
    @Shadow private boolean hasUnreadNewMessages;
    @Shadow protected abstract boolean isChatHidden();
    @Shadow public abstract int getVisibleLineCount();
    @Shadow protected abstract boolean isChatFocused();
    @Shadow public abstract double getChatScale();
    @Shadow public abstract int getWidth();
    @Shadow protected abstract int getLineHeight();
    @Shadow protected abstract int getIndicatorX(ChatHudLine.Visible line);
    @Shadow protected abstract void drawIndicatorIcon(MatrixStack matrices, int x, int y, MessageIndicator.Icon icon);
    @Shadow protected abstract int getMessageIndex(double chatLineX, double chatLineY);
    @Shadow protected abstract double toChatLineX(double x);
    @Shadow protected abstract double toChatLineY(double y);
    private final double[] frictions = FrictionsGenerator.generate1(1000);
    private final Map<ChatHudLine.Visible, Integer> cachedMap = new HashMap<>();
    private boolean flag = false;
    private static double getMessageOpacityMultiplier(int age) {
        double d = (double)age / 200.0;
        d = 1.0 - d;
        d *= 10.0;
        d = MathHelper.clamp(d, 0.0, 1.0);
        d *= d;
        return d;
    }
    @Inject(at = @At("RETURN"), method = "<init>")
    public void init(MinecraftClient client, CallbackInfo ci) {
        if (!flag) {
            flag = true;
            ChatLogUtils.printDebugLog();
        }
    }
    /**
     * @author Jack253-png
     * @reason overwrite for animations
     */
    @Overwrite
    public void render(MatrixStack matrices, int currentTick, int mouseX, int mouseY) {
        if (!this.isChatHidden()) {
            int i = this.getVisibleLineCount();
            int j = this.visibleMessages.size();
            if (j > 0) {
                boolean bl = this.isChatFocused();
                float f = (float)this.getChatScale();
                int k = MathHelper.ceil((float)this.getWidth() / f);
                int l = this.client.getWindow().getScaledHeight();
                matrices.push();
                matrices.scale(f, f, 1.0F);
                matrices.translate(4.0F, 0.0F, 0.0F);
                int m = MathHelper.floor((float)(l - 40) / f);
                int n = this.getMessageIndex(this.toChatLineX(mouseX), this.toChatLineY(mouseY));
                double d = this.client.options.getChatOpacity().getValue() * 0.8999999761581421 + 0.10000000149011612;
                double e = this.client.options.getTextBackgroundOpacity().getValue();
                double g = this.client.options.getChatLineSpacing().getValue();
                int o = this.getLineHeight();
                int p = (int)Math.round(-8.0 * (g + 1.0) + 4.0 * g);
                int q = 0;

                int t;
                int u;
                int v;
                int x;
                for(int r = 0; r + this.scrolledLines < this.visibleMessages.size() && r < i; ++r) {
                    int s = r + this.scrolledLines;
                    ChatHudLine.Visible visible = this.visibleMessages.get(s);
                    if (visible != null) {
                        t = currentTick - visible.addedTime();
                        if (t < 200 || bl) {
                            double h = bl ? 1.0 : getMessageOpacityMultiplier(t);
                            u = (int)(255.0 * h * d);
                            v = (int)(255.0 * h * e);
                            ++q;
                            if (u > 3) {
                                if (!cachedMap.containsKey(visible)) {
                                    cachedMap.put(visible, CAnimationClient.config.model.animationControl.chatHUD ? 0 : frictions.length - 1);
                                }
                                int Ind = cachedMap.get(visible);
                                int offset = (int) (this.getWidth() * frictions[Ind]);

                                int finalR = r;
                                int finalV = v;
                                int finalU = u;
                                Runnable r2 = () -> {
                                    int x2 = m - finalR * o;
                                    int y = x2 + p;
                                    matrices.push();
                                    matrices.translate(0.0F, 0.0F, 50.0F);
                                    fill(matrices, -4, x2 - o, k + 4 + 4, x2, finalV << 24);
                                    MessageIndicator messageIndicator = visible.indicator();
                                    if (messageIndicator != null) {
                                        int z = messageIndicator.indicatorColor() | finalU << 24;
                                        fill(matrices, -4, x2 - o, -2, x2, z);
                                        if (s == n && messageIndicator.icon() != null) {
                                            int aa = this.getIndicatorX(visible);
                                            Objects.requireNonNull(this.client.textRenderer);
                                            int ab = y + 9;
                                            this.drawIndicatorIcon(matrices, aa, ab, messageIndicator.icon());
                                        }
                                    }

                                    RenderSystem.enableBlend();
                                    matrices.translate(0.0F, 0.0F, 50.0F);
                                    this.client.textRenderer.drawWithShadow(matrices, visible.content(), 0.0F, (float)y, 16777215 + (finalU << 24));
                                    RenderSystem.disableBlend();
                                    matrices.pop();
                                };


                                if (MinecraftClient.getInstance().currentScreen == null || cachedMap.get(visible) != frictions.length - 1) {
                                    x = m - r * o;
                                    int y = x + p;
                                    matrices.push();
                                    matrices.translate(0.0F, 0.0F, 50.0F);
                                    fill(matrices, -4 - offset, x - o, k + 4 + 4 - offset, x, v << 24);
                                    MessageIndicator messageIndicator = visible.indicator();
                                    if (messageIndicator != null) {
                                        int z = messageIndicator.indicatorColor() | u << 24;
                                        fill(matrices, -4 - offset, x - o, -2 - offset, x, z);
                                        if (s == n && messageIndicator.icon() != null) {
                                            int aa = this.getIndicatorX(visible);
                                            Objects.requireNonNull(this.client.textRenderer);
                                            int ab = y + 9;
                                            this.drawIndicatorIcon(matrices, aa - offset, ab, messageIndicator.icon());
                                        }
                                    }

                                    RenderSystem.enableBlend();
                                    matrices.translate(0.0F, 0.0F, 50.0F);
                                    this.client.textRenderer.drawWithShadow(matrices, visible.content(), 0.0F - offset, (float) y, 16777215 + (u << 24));
                                    RenderSystem.disableBlend();
                                    matrices.pop();

                                    if (cachedMap.get(visible) < frictions.length - 1) {
                                        cachedMap.put(visible, cachedMap.get(visible) + 1);
                                    }
                                }
                                else {
                                    r2.run();
                                }
                            }
                            else {
                                cachedMap.remove(visible);
                            }
                        }
                        else {
                            cachedMap.remove(visible);
                        }
                    }
                }

                long ac = this.client.getMessageHandler().getUnprocessedMessageCount();
                int ad;
                if (ac > 0L) {
                    ad = (int)(128.0 * d);
                    t = (int)(255.0 * e);
                    matrices.push();
                    matrices.translate(0.0F, (float)m, 50.0F);
                    fill(matrices, -2, 0, k + 4, 9, t << 24);
                    RenderSystem.enableBlend();
                    matrices.translate(0.0F, 0.0F, 50.0F);
                    this.client.textRenderer.drawWithShadow(matrices, Text.translatable("chat.queue", ac), 0.0F, 1.0F, 16777215 + (ad << 24));
                    matrices.pop();
                    RenderSystem.disableBlend();
                }

                if (bl) {
                    ad = this.getLineHeight();
                    t = j * ad;
                    int ae = q * ad;
                    int af = this.scrolledLines * ae / j - m;
                    u = ae * ae / t;
                    if (t != ae) {
                        v = af > 0 ? 170 : 96;
                        int w = this.hasUnreadNewMessages ? 13382451 : 3355562;
                        x = k + 4;
                        fill(matrices, x, -af, x + 2, -af - u, w + (v << 24));
                        fill(matrices, x + 2, -af, x + 1, -af - u, 13421772 + (v << 24));
                    }
                }

                matrices.pop();
            }
        }
    }
}
