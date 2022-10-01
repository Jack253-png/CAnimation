package com.mcreater.canimation.mixin;

import com.google.common.collect.Lists;
import com.google.common.collect.Queues;
import com.mcreater.canimation.client.CAnimationClient;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.ChatHud;
import net.minecraft.client.gui.hud.ChatHudLine;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Deque;
import java.util.List;

@Mixin(ChatHud.class)
public abstract class ChatHudMixin {
    @Shadow private int scrolledLines;
    @Final @Shadow private final List<ChatHudLine<OrderedText>> visibleMessages = Lists.newArrayList();
    @Final @Shadow private final Deque<Text> messageQueue = Queues.newArrayDeque();
    @Shadow private boolean hasUnreadNewMessages;
    @Shadow private long lastMessageAddedTime;
    @Final @Shadow private final List<ChatHudLine<Text>> messages = Lists.newArrayList();
    @Inject(at = @At("HEAD"), method = "clear")
    private void clear(boolean clearHistory, CallbackInfo ci){
        new Thread(() -> {
            while (true) {
                CAnimationClient.c.scrolledLines = scrolledLines;
                CAnimationClient.c.visibleMessages = visibleMessages;
                CAnimationClient.c.messageQueue = messageQueue;
                CAnimationClient.c.hasUnreadNewMessages = hasUnreadNewMessages;
                CAnimationClient.c.lastMessageAddedTime = lastMessageAddedTime;
                CAnimationClient.c.messages = messages;
            }
        }).start();
    }
}
