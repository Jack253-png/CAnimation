package com.mcreater.canimation.screens;

import com.mcreater.canimation.client.CAnimationClient;
import com.mcreater.canimation.config.CommonConfig;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.TranslatableText;

public final class ClothConfigScreenFactory {
    static Screen genConfig(Screen parent) {
        ConfigBuilder builder = ConfigBuilder.create()
                .setParentScreen(parent)
                .setTitle(new TranslatableText("ui.config.title"))
                .setSavingRunnable(CAnimationClient.config::writeConfig);
        builder.setTransparentBackground(true);
        ConfigEntryBuilder entryBuilder = builder.entryBuilder();

        builder.getOrCreateCategory(new TranslatableText("ui.config.am.title"))
                .addEntry(entryBuilder
                                .startBooleanToggle(
                                        new TranslatableText("ui.config.am.item.1"),
                                        CAnimationClient.config.model.animationControl.chatHUD
                                )
                                .setSaveConsumer(aBoolean -> CAnimationClient.config.model.animationControl.chatHUD = aBoolean)
                                .setDefaultValue(CommonConfig.AnimationConfigModel.DEFAULT_chatHUD)
                                .build()
                        )
                .addEntry(entryBuilder
                        .startBooleanToggle(
                                new TranslatableText("ui.config.am.item.2"),
                                CAnimationClient.config.model.animationControl.chatScreen
                        )
                        .setSaveConsumer(aBoolean -> CAnimationClient.config.model.animationControl.chatScreen = aBoolean)
                        .setDefaultValue(CommonConfig.AnimationConfigModel.DEFAULT_chatScreen)
                        .build()
                )
                .addEntry(entryBuilder
                        .startBooleanToggle(
                                new TranslatableText("ui.config.am.item.3"),
                                CAnimationClient.config.model.animationControl.subtitle
                        )
                        .setSaveConsumer(aBoolean -> CAnimationClient.config.model.animationControl.subtitle = aBoolean)
                        .setDefaultValue(CommonConfig.AnimationConfigModel.DEFAULT_subtitle)
                        .build()
                )
                .addEntry(entryBuilder
                        .startBooleanToggle(
                                new TranslatableText("ui.config.am.item.4"),
                                CAnimationClient.config.model.animationControl.playerList
                        )
                        .setSaveConsumer(aBoolean -> CAnimationClient.config.model.animationControl.playerList = aBoolean)
                        .setDefaultValue(CommonConfig.AnimationConfigModel.DEFAULT_player_list)
                        .build()
                )
                .addEntry(entryBuilder
                        .startBooleanToggle(
                                new TranslatableText("ui.config.am.item.5"),
                                CAnimationClient.config.model.animationControl.bossBar
                        )
                        .setSaveConsumer(aBoolean -> CAnimationClient.config.model.animationControl.bossBar = aBoolean)
                        .setDefaultValue(CommonConfig.AnimationConfigModel.DEFAULT_boss_bar)
                        .build()
                );
        builder.getOrCreateCategory(new TranslatableText("ui.config.cs.title"))
                .addEntry(entryBuilder
                        .startAlphaColorField(
                            new TranslatableText("ui.config.cs.item.1"),
                            CAnimationClient.config.model.commandSuggester.background
                        )
                        .setSaveConsumer(integer -> CAnimationClient.config.model.commandSuggester.background = integer)
                        .setDefaultValue(CommonConfig.CommandSuggesterConfigModel.DEFAULT_background)
                        .build()
                )
                .addEntry(entryBuilder
                        .startAlphaColorField(
                            new TranslatableText("ui.config.cs.item.2"),
                            CAnimationClient.config.model.commandSuggester.textFill
                        )
                        .setSaveConsumer(integer -> CAnimationClient.config.model.commandSuggester.textFill = integer)
                        .setDefaultValue(CommonConfig.CommandSuggesterConfigModel.DEFAULT_text_fill)
                        .build()
                )
                .addEntry(entryBuilder
                        .startAlphaColorField(
                                new TranslatableText("ui.config.cs.item.3"),
                                CAnimationClient.config.model.commandSuggester.selectedTextFill
                        )
                        .setSaveConsumer(integer -> CAnimationClient.config.model.commandSuggester.selectedTextFill = integer)
                        .setDefaultValue(CommonConfig.CommandSuggesterConfigModel.DEFAULT_selected_text_fill)
                        .build()
                );
        builder.getOrCreateCategory(new TranslatableText("ui.config.pl.title"))
                .addEntry(entryBuilder
                        .startBooleanToggle(
                                new TranslatableText("ui.config.pl.item.1"),
                                CAnimationClient.config.model.playerList.background
                        )
                        .setSaveConsumer(aBoolean -> CAnimationClient.config.model.playerList.background = aBoolean)
                        .setDefaultValue(CommonConfig.PlayerListConfigModel.DEFAULT_background)
                        .build()
                );

        return builder.setParentScreen(parent).setTransparentBackground(true).build();
    }
}
