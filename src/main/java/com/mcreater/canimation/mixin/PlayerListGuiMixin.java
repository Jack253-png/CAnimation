package com.mcreater.canimation.mixin;

import com.formdev.flatlaf.FlatDarculaLaf;
import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLaf;
import net.minecraft.server.dedicated.gui.PlayerListGui;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.swing.*;
import java.awt.*;

@Mixin(value = PlayerListGui.class, priority = 2147483647)
public abstract class PlayerListGuiMixin extends JComponent {
    private boolean getIsDark() {
        return UIManager.getLookAndFeel() instanceof FlatDarkLaf ||
                UIManager.getLookAndFeel() instanceof FlatDarculaLaf;
    }
    private boolean getIsFlatLaf() {
        return UIManager.getLookAndFeel() instanceof FlatLaf;
    }
    @Inject(at = @At("HEAD"), method = "tick")
    private void tick(CallbackInfo ci) {
        setBackground(getIsDark() ? new Color(60, 63, 65) : getIsFlatLaf() ? new Color(242, 242, 242) : Color.WHITE);
    }
}
