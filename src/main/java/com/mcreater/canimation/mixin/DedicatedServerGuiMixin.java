package com.mcreater.canimation.mixin;

import com.formdev.flatlaf.FlatDarculaLaf;
import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatIntelliJLaf;
import com.formdev.flatlaf.FlatLightLaf;
import com.formdev.flatlaf.FlatPropertiesLaf;
import com.formdev.flatlaf.util.SystemInfo;
import net.minecraft.server.dedicated.MinecraftDedicatedServer;
import net.minecraft.server.dedicated.gui.DedicatedServerGui;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import javax.swing.*;
import javax.swing.plaf.FontUIResource;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Objects;
import java.util.function.Consumer;

@Mixin(value = DedicatedServerGui.class, priority = 2147483647)
public abstract class DedicatedServerGuiMixin {
    @Shadow @Final @Mutable private static Font FONT_MONOSPACE;
    @Inject(at = @At("RETURN"), method = "<clinit>")
    private static void __clinit__(CallbackInfo ci) throws Exception {
        FONT_MONOSPACE = Font.createFont(Font.TRUETYPE_FONT, Objects.requireNonNull(DedicatedServerGuiMixin.class.getClassLoader().getResourceAsStream("assets/canimation/GNU Unifont.ttf"))).deriveFont(16F);
        FontUIResource resource = new FontUIResource(FONT_MONOSPACE);
        UIManager.getDefaults().keys().asIterator().forEachRemaining(o -> {
            Object value = UIManager.get(o);
            if (value instanceof FontUIResource) {
                UIManager.put(o, resource);
            }
        });
        System.setProperty("os.version", "10.0");
    }
    @Inject(at = @At("RETURN"), method = "create")
    private static void create(MinecraftDedicatedServer server, CallbackInfoReturnable<DedicatedServerGui> cir) throws Exception {
        UIManager.setLookAndFeel(new FlatIntelliJLaf());
        FlatIntelliJLaf.installLafInfo();
        FlatLightLaf.installLafInfo();
        FlatDarculaLaf.installLafInfo();
        FlatDarkLaf.installLafInfo();

        new Thread(() -> {
            do {
                Arrays.asList(JFrame.getFrames()).forEach(frame -> {
                    JMenuBar bar = new JMenuBar();

                    JMenu menu = new JMenu("Theme");
                    JMenuItem item;
                    for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                        if (!info.getName().equals("CDE/Motif")) {
                            item = new JMenuItem("");
                            item.setAction(new AbstractAction(info.getName()) {
                                public void actionPerformed(ActionEvent e) {
                                    try {
                                        UIManager.setLookAndFeel(info.getClassName());
                                        SwingUtilities.updateComponentTreeUI(frame);
                                    } catch (Exception ex) {
                                        ex.printStackTrace();
                                    }
                                }
                            });
                            menu.add(item);
                        }
                    }
                    bar.add(menu);
                    try {
                        ((JFrame) frame).setJMenuBar(bar);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    SwingUtilities.updateComponentTreeUI(frame);
                });
            } while (JFrame.getFrames().length <= 0);
        }).start();
    }
}
