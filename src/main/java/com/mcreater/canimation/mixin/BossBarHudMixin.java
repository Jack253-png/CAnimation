package com.mcreater.canimation.mixin;

import com.mcreater.canimation.utils.FrictionsGenerator;
import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.loader.impl.lib.sat4j.core.Vec;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.Vector;
import java.util.WeakHashMap;
import java.util.function.Consumer;

@Mixin(value = BossBarHud.class, priority = 2147483647)
public abstract class BossBarHudMixin {
    @Shadow @Final private static Identifier BARS_TEXTURE;
    @Shadow @Final private MinecraftClient client;
    @Shadow @Final Map<UUID, ClientBossBar> bossBars;
    @Shadow protected abstract void renderBossBar(MatrixStack matrices, int x, int y, BossBar bossBar);
    private final Map<UUID, ClientBossBar> cachedBossBars = new HashMap<>();
    private final Map<ClientBossBar, Integer> cachedMap = new WeakHashMap<>();
    private final Map<ClientBossBar, Integer> baseYCachedMap = new WeakHashMap<>();
    private final double[] frictions = FrictionsGenerator.generate1(1000);
    private boolean bossBarExists(ClientBossBar bar) {
        return bossBars.containsValue(bar);
    }
    /**
     * @author Jack253-png
     * @reason overwrite for animations
     */
    @Overwrite
    public void render(MatrixStack matrices) {
        cachedBossBars.putAll(bossBars);
        if (!this.cachedBossBars.isEmpty()) {
            int i = this.client.getWindow().getScaledWidth();
            int j = 12;

            for (ClientBossBar clientBossBar : this.cachedBossBars.values()) {
                if (!cachedMap.containsKey(clientBossBar)) {
                    cachedMap.put(clientBossBar, 0);
                }

                int y = j;
                if (baseYCachedMap.containsKey(clientBossBar)) y = baseYCachedMap.get(clientBossBar);

                int offset = (int) ((y + 50) * frictions[cachedMap.get(clientBossBar)]);

                int k = i / 2 - 91;
                RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
                RenderSystem.setShaderTexture(0, BARS_TEXTURE);
                this.renderBossBar(matrices, k, y - offset, clientBossBar);
                Text text = clientBossBar.getName();
                int m = this.client.textRenderer.getWidth(text);
                int n = i / 2 - m / 2;
                int o = y - 9;
                this.client.textRenderer.drawWithShadow(matrices, text, (float) n, (float) o - offset, 16777215);
                if (bossBarExists(clientBossBar)) {
                    if (cachedMap.get(clientBossBar) < frictions.length - 1)
                        cachedMap.put(clientBossBar, cachedMap.get(clientBossBar) + 1);
                }
                else {
                    if (!baseYCachedMap.containsKey(clientBossBar)) baseYCachedMap.put(clientBossBar, j);
                    if (cachedMap.get(clientBossBar) > 0) cachedMap.put(clientBossBar, cachedMap.get(clientBossBar) - 1);
                }

                Objects.requireNonNull(this.client.textRenderer);
                j += 10 + 9;
                if (j >= this.client.getWindow().getScaledHeight() / 3) {
                    break;
                }
            }
        }

        List<UUID> removableKeys = new ArrayList<>();
        cachedBossBars.forEach((uuid, clientBossBar) -> {
            if (!bossBarExists(clientBossBar) && cachedMap.get(clientBossBar) == 0) {
                removableKeys.add(uuid);
            }
        });
        removableKeys.forEach(cachedBossBars::remove);
    }
}
