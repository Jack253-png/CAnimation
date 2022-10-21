package com.mcreater.canimation.client;

import com.mcreater.canimation.config.AnimationConfig;
import com.mcreater.canimation.config.CommandSuggesterConfig;
import com.mcreater.canimation.config.PlayerListConfig;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.screen.Screen;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.cert.X509Certificate;

@Environment(EnvType.CLIENT)
public class CAnimationClient implements ClientModInitializer {
    public static final CommandSuggesterConfig commandSuggesterConfig = new CommandSuggesterConfig();
    public static final AnimationConfig animationConfig = new AnimationConfig();
    public static final PlayerListConfig playerListConfig = new PlayerListConfig();

    public void onInitializeClient() {
        trustAllHosts();
    }
    public static void trustAllHosts() {
        TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
            public X509Certificate[] getAcceptedIssuers() {
                return new X509Certificate[]{};
            }
            public void checkClientTrusted(X509Certificate[] chain, String authType) {}
            public void checkServerTrusted(X509Certificate[] chain, String authType) {}
        }
        };
        try {
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
