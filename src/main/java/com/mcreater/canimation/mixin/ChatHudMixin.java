package com.mcreater.canimation.mixin;

import com.mcreater.canimation.client.CAnimationClient;
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
    private final double[] frictions = FrictionsGenerator.generate1(1000);
    private final Map<ChatHudLine.Visible, Integer> cachedMap = new HashMap<>();
    private static double getMessageOpacityMultiplier(int age) {
        double d = (double)age / 200.0;
        d = 1.0 - d;
        d *= 10.0;
        d = MathHelper.clamp(d, 0.0, 1.0);
        d *= d;
        return d;
    }
    /**
     * @author Jack253-png
     * @reason overwrite for animations
     */
    @Overwrite
    public void render(MatrixStack matrices, int currentTick) {
        if (!this.isChatHidden()) {
            int i = this.getVisibleLineCount();
            int j = this.visibleMessages.size();
            if (j > 0) {
                boolean bl = this.isChatFocused();
                float f = (float)this.getChatScale();
                int k = MathHelper.ceil((float)this.getWidth() / f);
                matrices.push();
                matrices.translate(4.0, 8.0, 0.0);
                matrices.scale(f, f, 1.0F);
                double d = this.client.options.getChatOpacity().getValue() * 0.8999999761581421 + 0.10000000149011612;
                double e = this.client.options.getTextBackgroundOpacity().getValue();
                double g = this.client.options.getChatLineSpacing().getValue();
                int l = this.getLineHeight();
                double h = -8.0 * (g + 1.0) + 4.0 * g;
                int m = 0;

                int o;
                int q;
                int r;
                int t;
                int u;
                for(int n = 0; n + this.scrolledLines < this.visibleMessages.size() && n < i; ++n) {
                    ChatHudLine.Visible visible = this.visibleMessages.get(n + this.scrolledLines);
                    if (visible != null) {
                        o = currentTick - visible.addedTime();
                        if (o < 200 || bl) {
                            double p = bl ? 1.0 : getMessageOpacityMultiplier(o);
                            q = (int)(255.0 * p * d);
                            r = (int)(255.0 * p * e);
                            ++m;
                            if (q > 3) {
                                if (!cachedMap.containsKey(visible)) {
                                    cachedMap.put(visible, CAnimationClient.config.model.animationControl.chatHUD ? 0 : frictions.length - 1);
                                }
                                int Ind = cachedMap.get(visible);

                                int offset = (int) (this.getWidth() * frictions[Ind]);

                                t = -n * l;
                                u = (int) ((double) t + h);

                                int finalT = t;
                                int finalR = r;
                                int finalU = u;
                                int finalQ = q;
                                Runnable r2 = () -> {
                                    int off = 0;
                                    matrices.push();
                                    matrices.translate(0.0, 0.0, 50.0);
                                    fill(matrices, -4 - off, finalT - l, k + 4 + 4 - off, finalT, finalR << 24);
                                    MessageIndicator messageIndicator = visible.indicator();
                                    if (messageIndicator != null) {
                                        int v = messageIndicator.indicatorColor() | finalQ << 24;
                                        fill(matrices, -4 - off, finalT - l, -2 - off, finalT, v);
                                        if (bl && visible.endOfEntry() && messageIndicator.icon() != null) {
                                            int w = this.getIndicatorX(visible);
                                            Objects.requireNonNull(this.client.textRenderer);
                                            int x = finalU + 9;
                                            this.drawIndicatorIcon(matrices, w - off, x, messageIndicator.icon());
                                        }
                                    }

                                    RenderSystem.enableBlend();
                                    matrices.translate(0.0, 0.0, 50.0);
                                    this.client.textRenderer.drawWithShadow(matrices, visible.content(), (float) 0 - off, (float) finalU, 16777215 + (finalQ << 24));
                                    RenderSystem.disableBlend();
                                    matrices.pop();
                                };

                                if (MinecraftClient.getInstance().currentScreen == null || cachedMap.get(visible) != frictions.length - 1) {
                                    matrices.push();
                                    matrices.translate(0.0, 0.0, 50.0);
                                    fill(matrices, -4 - offset, t - l, k + 4 + 4 - offset, t, r << 24);
                                    MessageIndicator messageIndicator = visible.indicator();
                                    if (messageIndicator != null) {
                                        int v = messageIndicator.indicatorColor() | q << 24;
                                        fill(matrices, -4 - offset, t - l, -2 - offset, t, v);
                                        if (bl && visible.endOfEntry() && messageIndicator.icon() != null) {
                                            int w = this.getIndicatorX(visible);
                                            Objects.requireNonNull(this.client.textRenderer);
                                            int x = u + 9;
                                            this.drawIndicatorIcon(matrices, w - offset, x, messageIndicator.icon());
                                        }
                                    }

                                    RenderSystem.enableBlend();
                                    matrices.translate(0.0, 0.0, 50.0);
                                    this.client.textRenderer.drawWithShadow(matrices, visible.content(), (float) 0 - offset, (float) u, 16777215 + (q << 24));
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

                long y = this.client.getMessageHandler().getUnprocessedMessageCount();
                int z;
                if (y > 0L) {
                    o = (int)(128.0 * d);
                    z = (int)(255.0 * e);
                    matrices.push();
                    matrices.translate(0.0, 0.0, 50.0);
                    fill(matrices, -2, 0, k + 4, 9, z << 24);
                    RenderSystem.enableBlend();
                    matrices.translate(0.0, 0.0, 50.0);
                    this.client.textRenderer.drawWithShadow(matrices, Text.translatable("chat.queue", y), 0.0F, 1.0F, 16777215 + (o << 24));
                    matrices.pop();
                    RenderSystem.disableBlend();
                }

                if (bl) {
                    o = this.getLineHeight();
                    z = j * o;
                    int aa = m * o;
                    q = this.scrolledLines * aa / j;
                    r = aa * aa / z;
                    if (z != aa) {
                        int s = q > 0 ? 170 : 96;
                        t = this.hasUnreadNewMessages ? 13382451 : 3355562;
                        u = k + 4;
                        fill(matrices, u, -q, u + 2, -q - r, t + (s << 24));
                        fill(matrices, u + 2, -q, u + 1, -q - r, 13421772 + (s << 24));
                    }
                }

                matrices.pop();
            }
        }
    }
}
