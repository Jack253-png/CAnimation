package com.mcreater.canimation.mixin;

import com.mcreater.canimation.utils.FrictionsGenerator;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.BossBarHud;
import net.minecraft.client.gui.hud.ClientBossBar;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.boss.BossBar;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.WeakHashMap;

@Mixin(value = BossBarHud.class, priority = 2147483647)
public abstract class BossBarHudMixin {
    @Shadow @Final private static Identifier BARS_TEXTURE;
    @Shadow @Final private MinecraftClient client;
    @Shadow @Final Map<UUID, ClientBossBar> bossBars;
    @Shadow protected abstract void renderBossBar(MatrixStack matrices, int x, int y, BossBar bossBar);
    private Map<ClientBossBar, Integer> cachedMap = new WeakHashMap<>();
    private final double[] frictions = FrictionsGenerator.generate1(1000);
    /**
     * @author Jack253-png
     * @reason overwrite for animations
     */
    @Overwrite
    public void render(MatrixStack matrices) {
        if (!this.bossBars.isEmpty()) {
            int i = this.client.getWindow().getScaledWidth();
            int j = 12;

            for (ClientBossBar clientBossBar : this.bossBars.values()) {
                int k = i / 2 - 91;
                RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
                RenderSystem.setShaderTexture(0, BARS_TEXTURE);
                this.renderBossBar(matrices, k, j, clientBossBar);
                Text text = clientBossBar.getName();
                int m = this.client.textRenderer.getWidth(text);
                int n = i / 2 - m / 2;
                int o = j - 9;
                this.client.textRenderer.drawWithShadow(matrices, text, (float) n, (float) o, 16777215);
                Objects.requireNonNull(this.client.textRenderer);
                j += 10 + 9;
                if (j >= this.client.getWindow().getScaledHeight() / 3) {
                    break;
                }
            }

        }
    }
}
