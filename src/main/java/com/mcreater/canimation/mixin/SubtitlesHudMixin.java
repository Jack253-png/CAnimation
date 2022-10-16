package com.mcreater.canimation.mixin;

import com.mcreater.canimation.client.CAnimationClient;
import com.mcreater.canimation.utils.FrictionsGenerator;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.hud.SubtitlesHud;
import net.minecraft.client.sound.SoundInstanceListener;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.Util;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Mixin(value = SubtitlesHud.class, priority = 2147483647)
public abstract class SubtitlesHudMixin extends DrawableHelper implements SoundInstanceListener {
    @Final @Shadow private MinecraftClient client;
    @Final @Shadow private List<SubtitlesHud.SubtitleEntry> entries;
    @Shadow private boolean enabled;

    private static final double[] frictions = FrictionsGenerator.generate1(1000);
    private static final Map<SubtitlesHud.SubtitleEntry, Double> loadedMap = new HashMap<>();
    /**
     * @author Jack253-png
     * @reason overwrite for animation
     */
    @Overwrite
    public void render(MatrixStack matrices) {
        if (!this.enabled && this.client.options.showSubtitles) {
            this.client.getSoundManager().registerListener(this);
            this.enabled = true;
        } else if (this.enabled && !this.client.options.showSubtitles) {
            this.client.getSoundManager().unregisterListener(this);
            this.enabled = false;
        }

        if (this.enabled && !this.entries.isEmpty()) {
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
            Vec3d vec3d = new Vec3d(this.client.player.getX(), this.client.player.getEyeY(), this.client.player.getZ());
            Vec3d vec3d2 = new Vec3d(0.0, 0.0, -1.0).rotateX(-this.client.player.getPitch() * 0.017453292F).rotateY(-this.client.player.getYaw() * 0.017453292F);
            Vec3d vec3d3 = new Vec3d(0.0, 1.0, 0.0).rotateX(-this.client.player.getPitch() * 0.017453292F).rotateY(-this.client.player.getYaw() * 0.017453292F);
            Vec3d vec3d4 = vec3d2.crossProduct(vec3d3);
            int i = 0;
            int j = 0;
            Iterator<SubtitlesHud.SubtitleEntry> iterator = this.entries.iterator();

            SubtitlesHud.SubtitleEntry subtitleEntry;
            while(iterator.hasNext()) {
                subtitleEntry = iterator.next();
                if (subtitleEntry.getTime() + 3000L <= Util.getMeasuringTimeMs()) {
                    iterator.remove();
                } else {
                    j = Math.max(j, this.client.textRenderer.getWidth(subtitleEntry.getText()));
                }
            }

            j += this.client.textRenderer.getWidth("<") + this.client.textRenderer.getWidth(" ") + this.client.textRenderer.getWidth(">") + this.client.textRenderer.getWidth(" ");

            for(iterator = this.entries.iterator(); iterator.hasNext(); ++i) {
                subtitleEntry = iterator.next();
                Text text = subtitleEntry.getText();
                Vec3d vec3d5 = subtitleEntry.getPosition().subtract(vec3d).normalize();
                double d = -vec3d4.dotProduct(vec3d5);
                double e = -vec3d2.dotProduct(vec3d5);
                boolean bl = e > 0.5;
                int l = j / 2;
                Objects.requireNonNull(this.client.textRenderer);
                int m = 9;
                int n = m / 2;
                int o = this.client.textRenderer.getWidth(text);
                int p = MathHelper.floor(MathHelper.clampedLerp(255.0F, 75.0F, (float)(Util.getMeasuringTimeMs() - subtitleEntry.getTime()) / 3000.0F));
                int q = p << 16 | p << 8 | p;
                matrices.push();
                matrices.translate((float)this.client.getWindow().getScaledWidth() - (float) l - 2.0F, (float)(this.client.getWindow().getScaledHeight() - 30) - (float) (i * (m + 1)), 0.0);
                matrices.scale(1.0F, 1.0F, 1.0F);
                fill(matrices, -l - 1, -n - 1, l + 1, n + 1, this.client.options.getTextBackgroundColor(0.8F));
                RenderSystem.enableBlend();

                double percent = o;
                if (d > 0.0) {
                    percent += this.client.textRenderer.getWidth(">");
                } else if (d < 0.0) {
                    percent += this.client.textRenderer.getWidth("<");
                }

                if (!loadedMap.containsKey(subtitleEntry)) {
                    loadedMap.put(subtitleEntry, CAnimationClient.animationConfig.enable_subtitle_animation ? 0D : (double) (frictions.length - 1));
                }

                double it2 = percent * frictions[loadedMap.get(subtitleEntry).intValue()];

                if (!bl) {
                    if (d > 0.0) {
                        this.client.textRenderer.draw(matrices, ">", (float)(l - this.client.textRenderer.getWidth(">") + it2), (float)(-n), q - 16777216);
                    } else if (d < 0.0) {
                        this.client.textRenderer.draw(matrices, "<", (float)(-l + it2), (float)(-n), q - 16777216);
                    }
                }

                this.client.textRenderer.draw(matrices, text, (float)(-o / 2 + it2), (float)(-n), q - 16777216);
                matrices.pop();

                if (loadedMap.get(subtitleEntry) < frictions.length - 1) {
                    loadedMap.put(subtitleEntry, loadedMap.get(subtitleEntry) + 1);
                }
            }

            RenderSystem.disableBlend();
        }
    }
}
