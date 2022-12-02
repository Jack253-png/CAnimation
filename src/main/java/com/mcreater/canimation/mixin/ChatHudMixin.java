package com.mcreater.canimation.mixin;

import com.mcreater.canimation.client.CAnimationClient;
import com.mcreater.canimation.utils.ChatLogUtils;
import com.mcreater.canimation.utils.FrictionsGenerator;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.hud.ChatHud;
import net.minecraft.client.gui.hud.ChatHudLine;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Deque;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.WeakHashMap;

@Mixin(value = ChatHud.class, priority = 2147483647)
public abstract class ChatHudMixin extends DrawableHelper {
    @Shadow private int scrolledLines;
    @Final @Shadow private List<ChatHudLine<OrderedText>> visibleMessages;
    @Final @Shadow private Deque<Text> messageQueue;
    @Shadow private boolean hasUnreadNewMessages;

    @Final @Shadow private List<ChatHudLine<Text>> messages;

    private static final double[] frictions = FrictionsGenerator.generate1(1000);
    private static final Map<OrderedText, Integer> cachedMap = new WeakHashMap<>();

    @Shadow protected abstract boolean isChatHidden();
    @Shadow protected abstract void processMessageQueue();
    @Shadow public abstract int getVisibleLineCount();
    @Shadow protected abstract boolean isChatFocused();
    @Shadow public abstract double getChatScale();
    @Shadow public abstract int getWidth();
    private boolean flag = false;
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
    public void render(MatrixStack matrices, int tickDelta) {
        if (!flag) {
            flag = true;
            new Thread(ChatLogUtils::printDebugLog).start();
        }
        if (!this.isChatHidden()) {
            this.processMessageQueue();
            int i = this.getVisibleLineCount();
            int j = this.visibleMessages.size();
            if (j > 0) {
                boolean bl = this.isChatFocused();

                float f = (float) this.getChatScale();
                int k = MathHelper.ceil((float) getWidth() / f);
                matrices.push();
                matrices.translate(4.0, 8.0, 0.0);
                matrices.scale(f, f, 1.0F);
                double d = MinecraftClient.getInstance().options.chatOpacity * 0.8999999761581421 + 0.10000000149011612;
                double e = MinecraftClient.getInstance().options.textBackgroundOpacity;
                double g = 9.0 * (MinecraftClient.getInstance().options.chatLineSpacing + 1.0);
                double h = -8.0 * (MinecraftClient.getInstance().options.chatLineSpacing + 1.0) + 4.0 * MinecraftClient.getInstance().options.chatLineSpacing;
                int l = 0;

                int messageIndex;
                int messageLock;
                int colorArg1;
                int colorArg2;
                for (messageIndex = 0; messageIndex + this.scrolledLines < this.visibleMessages.size() && messageIndex < i; ++messageIndex) {
                    ChatHudLine<OrderedText> chatHudLine = this.visibleMessages.get(messageIndex + this.scrolledLines);
                    if (chatHudLine != null) {
                        messageLock = tickDelta - chatHudLine.getCreationTick();
                        if (messageLock < 200 || bl) {
                            double o = bl ? 1.0 : getMessageOpacityMultiplier(messageLock);
                            colorArg1 = (int) (255.0 * o * d);
                            colorArg2 = (int) (255.0 * o * e);

                            int tempArg = (int) (255.0 * getMessageOpacityMultiplier(messageLock) * d);
                            ++l;
                            if (colorArg1 > 3) {

                                if (!cachedMap.containsKey(chatHudLine.getText())) {
                                    cachedMap.put(chatHudLine.getText(), CAnimationClient.config.model.animationControl.chatHUD ? 0 : frictions.length - 1);
                                }

                                int Ind = cachedMap.get(chatHudLine.getText());
                                double s = (double) (-messageIndex) * g;

                                int finalColorArg = colorArg2;
                                int finalColorArg1 = colorArg1;
                                Runnable r = () -> {
                                    matrices.push();
                                    matrices.translate(0.0, 0.0, 50.0);
                                    fill(matrices, -4, (int) (s - g), k + 4, (int) s, finalColorArg << 24);
                                    matrices.translate(0.0, 0.0, 50.0);
                                    RenderSystem.enableBlend();
                                    MinecraftClient.getInstance().textRenderer.drawWithShadow(
                                            matrices,
                                            chatHudLine.getText(),
                                            0F,
                                            (float) ((int) (s + h)),
                                            16777215 + (finalColorArg1 << 24)
                                    );
                                    RenderSystem.disableBlend();
                                };

                                if (MinecraftClient.getInstance().currentScreen == null || cachedMap.get(chatHudLine.getText()) != frictions.length - 1) {
                                    if (tempArg > 3) {
                                        double baseWidth = this.getWidth();
                                        double temp2 = baseWidth * frictions[Ind];
                                        matrices.push();
                                        matrices.translate(0.0, 0.0, 50.0);
                                        fill(matrices, (int) (-4 - temp2), (int) (s - g), (int) (k + 4 - temp2), (int) s, colorArg2 << 24);
                                        matrices.translate(0.0, 0.0, 50.0);
                                        RenderSystem.enableBlend();
                                        MinecraftClient.getInstance().textRenderer.drawWithShadow(
                                                matrices,
                                                chatHudLine.getText(),
                                                (float) (0 - baseWidth * frictions[Ind]),
                                                (float) ((int) (s + h)),
                                                16777215 + (colorArg1 << 24)
                                        );
                                        RenderSystem.disableBlend();
                                    }
                                    else {
                                        r.run();
                                    }
                                }
                                else {
                                    r.run();
                                }

                                if (cachedMap.get(chatHudLine.getText()) < frictions.length - 1) {
                                    cachedMap.put(chatHudLine.getText(), cachedMap.get(chatHudLine.getText()) + 1);
                                }

                                matrices.pop();
                            }
                            else {
                                cachedMap.remove(chatHudLine.getText());
                            }
                        }
                        else {
                            cachedMap.remove(chatHudLine.getText());
                        }
                    }
                }

                int t;
                if (!this.messageQueue.isEmpty()) {
                    messageIndex = (int) (128.0 * d);
                    t = (int) (255.0 * e);
                    matrices.push();
                    matrices.translate(0.0, 0.0, 50.0);
                    fill(matrices, -2, 0, k + 4, 9, t << 24);
                    RenderSystem.enableBlend();
                    matrices.translate(0.0, 0.0, 50.0);
                    MinecraftClient.getInstance().textRenderer.drawWithShadow(matrices, new TranslatableText("chat.queue", this.messageQueue.size()), 0.0F, 1.0F, 16777215 + (messageIndex << 24));

                    matrices.pop();
                    RenderSystem.disableBlend();
                }

                if (bl) {
                    Objects.requireNonNull(MinecraftClient.getInstance().textRenderer);
                    int m1 = 9;
                    t = j * m1;
                    messageLock = l * m1;
                    int u = this.scrolledLines * messageLock / j;
                    int v = messageLock * messageLock / t;
                    if (t != messageLock) {
                        colorArg1 = u > 0 ? 170 : 96;
                        colorArg2 = this.hasUnreadNewMessages ? 13382451 : 3355562;
                        matrices.translate(-4.0, 0.0, 0.0);
                        fill(matrices, 0, -u, 2, -u - v, colorArg2 + (colorArg1 << 24));
                        fill(matrices, 2, -u, 1, -u - v, 13421772 + (colorArg1 << 24));
                    }
                }

                matrices.pop();
            }
        }
    }
}
