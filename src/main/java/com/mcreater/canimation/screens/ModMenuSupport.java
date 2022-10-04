package com.mcreater.canimation.screens;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;

public class ModMenuSupport implements ModMenuApi {
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return ClothConfigScreenFactory::genConfig;
    }
}
