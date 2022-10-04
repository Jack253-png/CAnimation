package com.mcreater.canimation.screens;

import com.mcreater.canimation.client.CAnimationClient;
import com.mcreater.canimation.config.CommandSuggesterConfig;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.TranslatableText;

public final class ClothConfigScreenFactory {
    private static void saveConfig(){

    }
    static Screen genConfig(Screen parent) {
        ConfigBuilder builder = ConfigBuilder.create()
                .setParentScreen(parent)
                .setTitle(new TranslatableText("ui.config.title"))
                .setSavingRunnable(ClothConfigScreenFactory::saveConfig);
        ConfigEntryBuilder entryBuilder = builder.entryBuilder();

        // general
        builder.getOrCreateCategory(new TranslatableText("ui.config.title"))
                .addEntry(entryBuilder
                        .startAlphaColorField(
                            new TranslatableText("ui.config.item.1"),
                            CAnimationClient.commandSuggesterConfig.suggestion_background
                        )
                        .setSaveConsumer(integer -> {
                            CAnimationClient.commandSuggesterConfig.suggestion_background = integer;
                            CAnimationClient.commandSuggesterConfig.writeConfig();
                        })
                        .setDefaultValue(CommandSuggesterConfig.DEFAULT_SUGGESTION_BACKGROUND)
                        .build()
                )
                .addEntry(entryBuilder
                        .startAlphaColorField(
                            new TranslatableText("ui.config.item.2"),
                            CAnimationClient.commandSuggesterConfig.suggestion_text_fill
                        )
                        .setSaveConsumer(integer -> {
                            CAnimationClient.commandSuggesterConfig.suggestion_text_fill = integer;
                            CAnimationClient.commandSuggesterConfig.writeConfig();
                        })
                        .setDefaultValue(CommandSuggesterConfig.DEFAULT_SUGGESTION_TEXT_FILL)
                        .build()
                )
                .addEntry(entryBuilder
                        .startAlphaColorField(
                                new TranslatableText("ui.config.item.3"),
                                CAnimationClient.commandSuggesterConfig.suggestion_selected_text_fill
                        )
                        .setSaveConsumer(integer -> {
                            CAnimationClient.commandSuggesterConfig.suggestion_selected_text_fill = integer;
                            CAnimationClient.commandSuggesterConfig.writeConfig();
                        })
                        .setDefaultValue(CommandSuggesterConfig.DEFAULT_SUGGESTION_SELECTED_TEXT_FILL)
                        .build()
                );

        return builder.build();
    }
}
