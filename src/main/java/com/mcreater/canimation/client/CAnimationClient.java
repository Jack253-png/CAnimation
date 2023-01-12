package com.mcreater.canimation.client;

import com.mcreater.canimation.config.CommonConfig;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.cert.X509Certificate;

@Environment(EnvType.CLIENT)
public class CAnimationClient implements ClientModInitializer {
    public static final CommonConfig config = new CommonConfig();
    public void onInitializeClient() {

    }
}
