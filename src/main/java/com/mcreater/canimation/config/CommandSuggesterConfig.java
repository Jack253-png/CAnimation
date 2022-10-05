package com.mcreater.canimation.config;

import com.mcreater.canimation.utils.FileHelper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

public class CommandSuggesterConfig extends AbstractConfig {
    private static final Logger logger = LogManager.getLogger(CommandSuggesterConfig.class);
    public int suggestion_background = DEFAULT_SUGGESTION_BACKGROUND;
    public int suggestion_text_fill = DEFAULT_SUGGESTION_TEXT_FILL;
    public int suggestion_selected_text_fill = DEFAULT_SUGGESTION_SELECTED_TEXT_FILL;

    public static final int DEFAULT_MAX_SUGGESTION_SIZE = 10;
    public static final int DEFAULT_SUGGESTION_BACKGROUND = -805306368;
    public static final int DEFAULT_SUGGESTION_TEXT_FILL = -5592406;
    public static final int DEFAULT_SUGGESTION_SELECTED_TEXT_FILL = -256;
    public CommandSuggesterConfig(){
        super(FileHelper.getConfigPath("canimation/canimation-command-suggester.json"));
        readConfig();
        checkConfig();
    }

    public void checkConfig() {
        writeConfig();
    }

    public void writeConfig() {
        try {
            CommandSuggesterConfigModel model = new CommandSuggesterConfigModel();
            model.suggestion_background = suggestion_background;
            model.suggestion_text_fill = suggestion_text_fill;
            model.suggestion_selected_text_fill = suggestion_selected_text_fill;
            if (!createFile() && !getConfigFile().exists()) throw new IOException("Cannot create file");
            FileHelper.write(getConfigFile(), AbstractConfig.GSON.toJson(model));
        }
        catch (Exception e){
            logger.error("failed to apply config!", e);
        }
    }

    public void readConfig() {
        try {
            CommandSuggesterConfigModel model = AbstractConfig.GSON.fromJson(FileHelper.read(getConfigFile()), CommandSuggesterConfigModel.class);
            suggestion_background = model.suggestion_background;
            suggestion_text_fill = model.suggestion_text_fill;
            suggestion_selected_text_fill = model.suggestion_selected_text_fill;
        }
        catch (Exception e){
            logger.error("failed to read config!", e);
        }
    }

    public static class CommandSuggesterConfigModel {
        public int suggestion_background;
        public int suggestion_text_fill;
        public int suggestion_selected_text_fill;
    }
}
