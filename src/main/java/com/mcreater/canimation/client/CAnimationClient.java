package com.mcreater.canimation.client;

import com.mcreater.canimation.config.CommonConfig;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.command.v1.ClientCommandManager;
import net.fabricmc.loader.FabricLoader;
import net.fabricmc.loader.impl.FabricLoaderImpl;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.BossBarHud;
import net.minecraft.client.gui.hud.ClientBossBar;
import net.minecraft.entity.boss.BossBar;
import net.minecraft.text.LiteralText;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.IOException;
import java.lang.reflect.Field;
import java.security.cert.X509Certificate;
import java.util.Map;
import java.util.UUID;

import static net.fabricmc.fabric.api.client.command.v1.ClientCommandManager.literal;

@Environment(EnvType.CLIENT)
public class CAnimationClient implements ClientModInitializer {
    public static final CommonConfig config = new CommonConfig();

    public void onInitializeClient() {
        trustAllHosts();

        if (FabricLoaderImpl.INSTANCE.isDevelopmentEnvironment()) {
            ClientCommandManager.DISPATCHER.register(
                    literal("//debugbar")
                            .executes(context -> {
                                UUID uuid = UUID.randomUUID();
                                BossBarHud hud = MinecraftClient.getInstance().inGameHud.getBossBarHud();

                                try {
                                    Field f = BossBarHud.class.getDeclaredField("bossBars");
                                    f.setAccessible(true);
                                    Map<UUID, ClientBossBar> map = (Map<UUID, ClientBossBar>) f.get(hud);

                                    map.put(uuid, new ClientBossBar(uuid, new LiteralText("debug"), 0, BossBar.Color.BLUE, BossBar.Style.NOTCHED_20, false, false, false));
                                } catch (Exception e) {
                                    throw new RuntimeException(e);
                                }

                                return 1;
                            })
            );

            ClientCommandManager.DISPATCHER.register(
                    literal("//cerbar")
                            .executes(context -> {
                                BossBarHud hud = MinecraftClient.getInstance().inGameHud.getBossBarHud();

                                try {
                                    Field f = BossBarHud.class.getDeclaredField("bossBars");
                                    f.setAccessible(true);
                                    Map<UUID, ClientBossBar> map = (Map<UUID, ClientBossBar>) f.get(hud);
                                    map.clear();
                                } catch (Exception e) {
                                    throw new RuntimeException(e);
                                }

                                return 1;
                            })
            );

            ClientCommandManager.DISPATCHER.register(
                    literal("//rmfbar")
                            .executes(context -> {
                                BossBarHud hud = MinecraftClient.getInstance().inGameHud.getBossBarHud();

                                try {
                                    Field f = BossBarHud.class.getDeclaredField("bossBars");
                                    f.setAccessible(true);
                                    Map<UUID, ClientBossBar> map = (Map<UUID, ClientBossBar>) f.get(hud);
                                    map.remove(map.keySet().iterator().next());
                                } catch (Exception e) {
                                    throw new RuntimeException(e);
                                }

                                return 1;
                            })
            );
        }
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
