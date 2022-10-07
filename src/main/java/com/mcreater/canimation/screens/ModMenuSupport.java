package com.mcreater.canimation.screens;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import net.minecraft.client.gui.screen.Screen;

public class ModMenuSupport implements ModMenuApi {
    public ConfigScreenFactory<Screen> getModConfigScreenFactory() {
        return ClothConfigScreenFactory::genConfig;
    }
}
