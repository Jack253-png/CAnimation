package com.mcreater.canimation.mixin;

import com.formdev.flatlaf.FlatDarculaLaf;
import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLaf;
import net.minecraft.server.dedicated.gui.PlayerStatsGui;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import javax.swing.*;
import java.awt.*;

@Mixin(value = PlayerStatsGui.class)
public abstract class PlayerStatsGuiMixin extends JComponent {
    @Final @Shadow private int[] memoryUsePercentage;
    @Shadow private int memoryUsePercentagePos;
    @Final @Shadow private String[] lines;
    private boolean getIsDark() {
        return UIManager.getLookAndFeel() instanceof FlatDarkLaf ||
                UIManager.getLookAndFeel() instanceof FlatDarculaLaf;
    }
    private boolean getIsFlatLaf() {
        return UIManager.getLookAndFeel() instanceof FlatLaf;
    }
    /**
     * @author Jack253-png
     * @reason overwrite for color control
     */
    @Overwrite
    public void paint(Graphics graphics) {
        int i;
        for(i = 0; i < 256; ++i) {
            int j = this.memoryUsePercentage[i + this.memoryUsePercentagePos & 255];
            int perc = (int) (j * 2.5);
            Color c = getIsDark() ? new Color(255, 255 - perc, 255 - perc) : new Color(perc, 0, 0);
            graphics.setColor(c);
            graphics.fillRect(i, 100 - j, 1, j);
        }

        graphics.setColor(getIsDark() ? getIsFlatLaf() ? new Color(242, 242, 242) : Color.WHITE : new Color(60, 63, 65));

        for(i = 0; i < this.lines.length; ++i) {
            String string = this.lines[i];
            if (string != null) {
                graphics.drawString(string, 32, 116 + i * 16);
            }
        }

    }
}
