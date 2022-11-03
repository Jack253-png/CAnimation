package com.mcreater.canimation.mixin;

import com.formdev.flatlaf.FlatDarculaLaf;
import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatIntelliJLaf;
import com.formdev.flatlaf.FlatLaf;
import com.formdev.flatlaf.FlatLightLaf;
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
import java.util.Arrays;
import java.util.Objects;
import java.util.Vector;

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
    }
    private static boolean getIsDark() {
        return UIManager.getLookAndFeel() instanceof FlatDarkLaf ||
                UIManager.getLookAndFeel() instanceof FlatDarculaLaf;
    }
    private static boolean getIsFlatLaf() {
        return UIManager.getLookAndFeel() instanceof FlatLaf;
    }
    private static Vector<Component> getComponents(Component rootPane) {
        Vector<Component> com = new Vector<>();
        if (rootPane instanceof Container) {
            Arrays.asList(((Container) rootPane).getComponents()).forEach(component -> {
                com.add(component);
                if (component instanceof Container) {
                    com.addAll(getComponents(component));
                }
            });
        }
        else {
            com.add(rootPane);
        }
        return com;
    }
    private static void setTheme(Frame frame, UIManager.LookAndFeelInfo info) {
        try {
            UIManager.setLookAndFeel(info.getClassName());
            SwingUtilities.updateComponentTreeUI(frame);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        getComponents(frame).forEach(c -> c.setBackground(getIsDark() ? new Color(60, 63, 65) : getIsFlatLaf() ? new Color(242, 242, 242) : Color.WHITE));
    }
    private static void setTheme(Frame frame, LookAndFeel info) {
        try {
            UIManager.setLookAndFeel(info);
            SwingUtilities.updateComponentTreeUI(frame);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        getComponents(frame).forEach(c -> c.setBackground(getIsDark() ? new Color(60, 63, 65) : getIsFlatLaf() ? new Color(242, 242, 242) : Color.WHITE));
    }
    @Inject(at = @At("RETURN"), method = "create")
    private static void create(MinecraftDedicatedServer server, CallbackInfoReturnable<DedicatedServerGui> cir) {
        FlatIntelliJLaf.installLafInfo();
        FlatLightLaf.installLafInfo();
        FlatDarculaLaf.installLafInfo();
        FlatDarkLaf.installLafInfo();

        new Thread(() -> {
            do {
                Arrays.asList(JFrame.getFrames()).forEach(frame -> {
                    setTheme(frame, new FlatIntelliJLaf());

                    JMenuBar bar = new JMenuBar();

                    JMenu menu = new JMenu("Theme");
                    JMenuItem item;
                    for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                        if (!info.getName().equals("CDE/Motif")) {
                            item = new JMenuItem("");
                            item.setAction(new AbstractAction(info.getName()) {
                                public void actionPerformed(ActionEvent e) {
                                    setTheme(frame, info);
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
