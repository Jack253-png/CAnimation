package com.mcreater.canimation.server;

import com.mcreater.canimation.CAnimation;
import net.fabricmc.api.DedicatedServerModInitializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Objects;

public class CAnimationServer implements DedicatedServerModInitializer {
    Logger logger = LogManager.getLogger();
    public void onInitializeServer() {
        try {
            if (!Objects.equals(CAnimation.checkUpdate(), CAnimation.getCurrentModVer())) {
                logger.info("New version of CAnimation is available, you can download it on https://modrinth.com/mod/canimation");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
