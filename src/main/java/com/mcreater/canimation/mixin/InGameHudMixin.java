package com.mcreater.canimation.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.gui.hud.PlayerListHud;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.scoreboard.ScoreboardObjective;
import net.minecraft.scoreboard.Team;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = InGameHud.class, priority = 2147483647)
public abstract class InGameHudMixin extends DrawableHelper {
    @Shadow @Final private MinecraftClient client;
    @Shadow private int scaledWidth;
    @Shadow @Final private PlayerListHud playerListHud;
    @Inject(at = @At("RETURN"), method = "render")
    private void render(MatrixStack matrices, float tickDelta, CallbackInfo ci) {
        Scoreboard scoreboard = this.client.world.getScoreboard();
        ScoreboardObjective scoreboardObjective = null;
        Team team = scoreboard.getPlayerTeam(this.client.player.getEntityName());
        if (team != null) {
            int m = team.getColor().getColorIndex();
            if (m >= 0) {
                scoreboardObjective = scoreboard.getObjectiveForSlot(3 + m);
            }
        }

        ScoreboardObjective scoreboardObjective2 = scoreboardObjective != null ? scoreboardObjective : scoreboard.getObjectiveForSlot(1);

        if (!this.client.options.hudHidden) {
            if (!this.client.options.playerListKey.isPressed() || this.client.isInSingleplayer() && this.client.player.networkHandler.getPlayerList().size() <= 1 && scoreboardObjective2 == null) {
                this.playerListHud.render(matrices, this.scaledWidth, scoreboard, scoreboardObjective2);
            }
        }


    }
}
