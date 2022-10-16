package com.mcreater.canimation.screens;

import com.mcreater.canimation.client.CAnimationClient;
import com.mcreater.canimation.config.AnimationConfig;
import com.mcreater.canimation.config.CommandSuggesterConfig;
import com.mcreater.canimation.config.PlayerListConfig;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.LiteralText;
import net.minecraft.text.TranslatableText;

import java.util.function.Consumer;

public final class ClothConfigScreenFactory {
    static Screen genConfig(Screen parent) {
        ConfigBuilder builder = ConfigBuilder.create()
                .setParentScreen(parent)
                .setTitle(new TranslatableText("ui.config.title"))
                .setSavingRunnable(() -> {});
        builder.setTransparentBackground(true);
        ConfigEntryBuilder entryBuilder = builder.entryBuilder();

        builder.getOrCreateCategory(new TranslatableText("ui.config.am.title"))
                .addEntry(entryBuilder
                                .startBooleanToggle(
                                        new TranslatableText("ui.config.am.item.1"),
                                        CAnimationClient.animationConfig.enable_chatHUD_animation
                                )
                                .setSaveConsumer(aBoolean -> {
                                    CAnimationClient.animationConfig.enable_chatHUD_animation = aBoolean;
                                    CAnimationClient.animationConfig.writeConfig();
                                })
                                .setDefaultValue(AnimationConfig.DEFAULT_chatHUD_animation)
                                .build()
                        )
                .addEntry(entryBuilder
                        .startBooleanToggle(
                                new TranslatableText("ui.config.am.item.2"),
                                CAnimationClient.animationConfig.enable_chatScreen_animation
                        )
                        .setSaveConsumer(aBoolean -> {
                            CAnimationClient.animationConfig.enable_chatScreen_animation = aBoolean;
                            CAnimationClient.animationConfig.writeConfig();
                        })
                        .setDefaultValue(AnimationConfig.DEFAULT_chatScreen_animation)
                        .build()
                )
                .addEntry(entryBuilder
                        .startBooleanToggle(
                                new TranslatableText("ui.config.am.item.3"),
                                CAnimationClient.animationConfig.enable_subtitle_animation
                        )
                        .setSaveConsumer(aBoolean -> {
                            CAnimationClient.animationConfig.enable_subtitle_animation = aBoolean;
                            CAnimationClient.animationConfig.writeConfig();
                        })
                        .setDefaultValue(AnimationConfig.DEFAULT_subtitle_animation)
                        .build()
                )
                .addEntry(entryBuilder
                        .startBooleanToggle(
                                new TranslatableText("ui.config.am.item.4"),
                                CAnimationClient.animationConfig.enable_player_list_animation
                        )
                        .setSaveConsumer(aBoolean -> {
                            CAnimationClient.animationConfig.enable_player_list_animation = aBoolean;
                            CAnimationClient.animationConfig.writeConfig();
                        })
                        .setDefaultValue(AnimationConfig.DEFAULT_player_list_animation)
                        .build()
                )
        ;
        builder.getOrCreateCategory(new TranslatableText("ui.config.cs.title"))
                .addEntry(entryBuilder
                        .startAlphaColorField(
                            new TranslatableText("ui.config.cs.item.1"),
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
                            new TranslatableText("ui.config.cs.item.2"),
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
                                new TranslatableText("ui.config.cs.item.3"),
                                CAnimationClient.commandSuggesterConfig.suggestion_selected_text_fill
                        )
                        .setSaveConsumer(integer -> {
                            CAnimationClient.commandSuggesterConfig.suggestion_selected_text_fill = integer;
                            CAnimationClient.commandSuggesterConfig.writeConfig();
                        })
                        .setDefaultValue(CommandSuggesterConfig.DEFAULT_SUGGESTION_SELECTED_TEXT_FILL)
                        .build()
                );
        builder.getOrCreateCategory(new TranslatableText("ui.config.pl.title"))
                .addEntry(entryBuilder
                        .startBooleanToggle(
                                new TranslatableText("ui.config.pl.item.1"),
                                CAnimationClient.playerListConfig.enable_player_list_background
                        )
                        .setSaveConsumer(aBoolean -> {
                            CAnimationClient.playerListConfig.enable_player_list_background = aBoolean;
                            CAnimationClient.playerListConfig.writeConfig();
                        })
                        .setDefaultValue(PlayerListConfig.DEFAULT_player_list_background)
                        .build()
                );

        return builder.build();
    }
}
