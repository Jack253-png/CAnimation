package com.mcreater.canimation.mixin;

import com.google.common.collect.Ordering;
import com.mcreater.canimation.CAnimation;
import com.mcreater.canimation.client.CAnimationClient;
import com.mcreater.canimation.utils.FrictionsGenerator;
import com.mojang.authlib.GameProfile;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.gui.hud.PlayerListHud;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.PlayerModelPart;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.scoreboard.ScoreboardCriterion;
import net.minecraft.scoreboard.ScoreboardObjective;
import net.minecraft.scoreboard.ScoreboardPlayerScore;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Text;
import net.minecraft.util.Util;
import net.minecraft.world.GameMode;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Iterator;
import java.util.List;
import java.util.Objects;

@Mixin(value = PlayerListHud.class, priority = 2147483647)
public abstract class PlayerListHudMixin extends DrawableHelper {
    @Shadow @Final private static Ordering<PlayerListEntry> ENTRY_ORDERING;
    @Shadow @Final private MinecraftClient client;
    @Shadow @Final private InGameHud inGameHud;
    @Shadow @Nullable private Text footer;
    @Shadow @Nullable private Text header;
    @Shadow private long showTime;
    @Shadow private boolean visible;

    @Shadow public abstract Text getPlayerName(PlayerListEntry entry);
    @Shadow protected abstract void renderScoreboardObjective(ScoreboardObjective objective, int y, String player, int startX, int endX, PlayerListEntry entry, MatrixStack matrices);
    @Shadow protected abstract void renderLatencyIcon(MatrixStack matrices, int width, int x, int y, PlayerListEntry entry);
    private static final double[] frictions = FrictionsGenerator.generate1(1000);
    private static int index = 0;
    private static boolean isInScreen = true;

    /**
     * @author Jack253-png
     * @reason overwrite for outer animations
     */
    @Overwrite
    public void setVisible(boolean visible) {
        if (visible && !this.visible) {
            this.showTime = Util.getMeasuringTimeMs();
        }

        isInScreen = visible;
        this.visible = visible;
    }
    private static void fillInternal(MatrixStack matrices, int x1, int y1, int x2, int y2, int color) {
        if (CAnimationClient.playerListConfig.enable_player_list_background) fill(matrices, x1, y1, x2, y2, color);
    }
    /**
     * @author Jack253-png
     * @reason overwrite for transparent and animations
     */
    @Overwrite
    public void render(MatrixStack matrices, int scaledWindowWidth, Scoreboard scoreboard, @Nullable ScoreboardObjective objective) {
        int offset = (int) (MinecraftClient.getInstance().getWindow().getHeight() * frictions[CAnimationClient.animationConfig.enable_player_list_animation ? index : frictions.length - 1]);

        ClientPlayNetworkHandler clientPlayNetworkHandler = this.client.player.networkHandler;
        List<PlayerListEntry> list = ENTRY_ORDERING.sortedCopy(clientPlayNetworkHandler.getPlayerList());
        int i = 0;
        int j = 0;
        Iterator<PlayerListEntry> var9 = list.iterator();

        int k;
        while(var9.hasNext()) {
            PlayerListEntry playerListEntry = var9.next();
            k = this.client.textRenderer.getWidth(this.getPlayerName(playerListEntry));
            i = Math.max(i, k);
            if (objective != null && objective.getRenderType() != ScoreboardCriterion.RenderType.HEARTS) {
                TextRenderer var10000 = this.client.textRenderer;
                ScoreboardPlayerScore var10001 = scoreboard.getPlayerScore(playerListEntry.getProfile().getName(), objective);
                k = var10000.getWidth(" " + var10001.getScore());
                j = Math.max(j, k);
            }
        }

        list = list.subList(0, Math.min(list.size(), 80));
        int l = list.size();
        int m = l;

        for(k = 1; m > 20; m = (l + k - 1) / k) {
            ++k;
        }

        boolean bl = this.client.isInSingleplayer() || this.client.getNetworkHandler().getConnection().isEncrypted();
        int n;
        if (objective != null) {
            if (objective.getRenderType() == ScoreboardCriterion.RenderType.HEARTS) {
                n = 90;
            } else {
                n = j;
            }
        } else {
            n = 0;
        }

        int o = Math.min(k * ((bl ? 9 : 0) + i + n + 13), scaledWindowWidth - 50) / k;
        int p = scaledWindowWidth / 2 - (o * k + (k - 1) * 5) / 2;
        int q = 10;
        int r = o * k + (k - 1) * 5;
        List<OrderedText> list2 = null;
        if (this.header != null) {
            list2 = this.client.textRenderer.wrapLines(this.header, scaledWindowWidth - 50);

            OrderedText orderedText;
            for(Iterator<OrderedText> var19 = list2.iterator(); var19.hasNext(); r = Math.max(r, this.client.textRenderer.getWidth(orderedText))) {
                orderedText = var19.next();
            }
        }

        List<OrderedText> list3 = null;
        OrderedText orderedText2;
        Iterator<OrderedText> var38;
        if (this.footer != null) {
            list3 = this.client.textRenderer.wrapLines(this.footer, scaledWindowWidth - 50);

            for(var38 = list3.iterator(); var38.hasNext(); r = Math.max(r, this.client.textRenderer.getWidth(orderedText2))) {
                orderedText2 = var38.next();
            }
        }

        int var10002;
        int var10003;
        int var10005;
        int s;
        int var36;
        if (list2 != null) {
            var36 = scaledWindowWidth / 2 - r / 2 - 1;
            var10002 = q - 1;
            var10003 = scaledWindowWidth / 2 + r / 2 + 1;
            var10005 = list2.size();
            Objects.requireNonNull(this.client.textRenderer);
            fillInternal(matrices, var36, var10002 - offset, var10003, q + var10005 * 9 - offset, Integer.MIN_VALUE);

            for(var38 = list2.iterator(); var38.hasNext(); q += 9) {
                orderedText2 = var38.next();
                s = this.client.textRenderer.getWidth(orderedText2);
                this.client.textRenderer.drawWithShadow(matrices, orderedText2, (float)(scaledWindowWidth / 2 - s / 2), (float)q - offset, -1);
                Objects.requireNonNull(this.client.textRenderer);
            }

            ++q;
        }

        fillInternal(matrices, scaledWindowWidth / 2 - r / 2 - 1, q - 1 - offset, scaledWindowWidth / 2 + r / 2 + 1, q + m * 9 - offset, Integer.MIN_VALUE);
        int t = this.client.options.getTextBackgroundColor(553648127);

        int v;
        for(int u = 0; u < l; ++u) {
            s = u / m;
            v = u % m;
            int w = p + s * o + s * 5;
            int x = q + v * 9;
            fill(matrices, w, x - offset, w + o, x + 8 - offset, t);
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
            if (u < list.size()) {
                PlayerListEntry playerListEntry2 = list.get(u);
                GameProfile gameProfile = playerListEntry2.getProfile();
                if (bl) {
                    PlayerEntity playerEntity = this.client.world.getPlayerByUuid(gameProfile.getId());
                    boolean bl2 = playerEntity != null && LivingEntityRenderer.shouldFlipUpsideDown(playerEntity);
                    RenderSystem.setShaderTexture(0, playerListEntry2.getSkinTexture());
                    int y = 8 + (bl2 ? 8 : 0);
                    int z = 8 * (bl2 ? -1 : 1);
                    DrawableHelper.drawTexture(matrices, w, x - offset, 8, 8, 8.0F, (float)y, 8, z, 64, 64);
                    if (playerEntity != null && playerEntity.isPartVisible(PlayerModelPart.HAT)) {
                        int aa = 8 + (bl2 ? 8 : 0);
                        int ab = 8 * (bl2 ? -1 : 1);
                        DrawableHelper.drawTexture(matrices, w, x - offset, 8, 8, 40.0F, (float)aa, 8, ab, 64, 64);
                    }

                    w += 9;
                }

                this.client.textRenderer.drawWithShadow(matrices, this.getPlayerName(playerListEntry2), (float)w, (float)x - offset, playerListEntry2.getGameMode() == GameMode.SPECTATOR ? -1862270977 : -1);
                if (objective != null && playerListEntry2.getGameMode() != GameMode.SPECTATOR) {
                    int ac = w + i + 1;
                    int ad = ac + n;
                    if (ad - ac > 5) {
                        this.renderScoreboardObjective(objective, x - offset, gameProfile.getName(), ac, ad, playerListEntry2, matrices);
                    }
                }

                this.renderLatencyIcon(matrices, o, w - (bl ? 9 : 0), x - offset, playerListEntry2);
            }
        }

        if (list3 != null) {
            q += m * 9 + 1;
            var36 = scaledWindowWidth / 2 - r / 2 - 1;
            var10002 = q - 1;
            var10003 = scaledWindowWidth / 2 + r / 2 + 1;
            var10005 = list3.size();
            Objects.requireNonNull(this.client.textRenderer);
            fillInternal(matrices, var36, var10002 - offset, var10003, q + var10005 * 9 - offset, Integer.MIN_VALUE);

            for(Iterator<OrderedText> var41 = list3.iterator(); var41.hasNext(); q += 9) {
                OrderedText orderedText3 = var41.next();
                v = this.client.textRenderer.getWidth(orderedText3);
                this.client.textRenderer.drawWithShadow(matrices, orderedText3, (float)(scaledWindowWidth / 2 - v / 2), (float)q - offset, -1);
                Objects.requireNonNull(this.client.textRenderer);
            }
        }
        if (isInScreen) {
            if (index < frictions.length - 1) {
                index++;
            }
        }
        else {
            if (index > 0) {
                index--;
            }
        }
    }
}
