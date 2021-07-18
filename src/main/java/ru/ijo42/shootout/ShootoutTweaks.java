package ru.ijo42.shootout;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import java.awt.*;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

@Mod(
        modid = ShootoutTweaks.MOD_ID,
        name = ShootoutTweaks.MOD_NAME,
        version = ShootoutTweaks.VERSION
)
public class ShootoutTweaks {

    public static final String MOD_ID = "shootout";
    public static final String MOD_NAME = "Shootout Tweaks";
    public static final String VERSION = "1.0-SNAPSHOT";
    public static final String CONFIG_FILENAME = "shootouttweaks.json";

    @Mod.Instance(MOD_ID)
    public static ShootoutTweaks INSTANCE;
    public static Config config;
    public static Color giveDamageColor;
    public static Color takeDamageColor;

    @Mod.EventHandler
    public void preinit(FMLPreInitializationEvent event) {
        Path configPath = event.getModConfigurationDirectory().toPath().resolve("plugins").resolve(CONFIG_FILENAME);
        if (Files.notExists(configPath)) {
            try {
                Files.copy(getResource(CONFIG_FILENAME), configPath);
            } catch (Exception e) {
                System.err.println("[ShootoutTweaks] Could not copy default config to " + configPath);
                return;
            }
        }

        Gson gson = new GsonBuilder().create();
        try {
            config = gson.fromJson(Files.newBufferedReader(configPath), Config.class);
            toIntRGB(config.bulletDamage.giveDamageColor);
            toIntRGB(config.bulletDamage.takeDamageColor);
        } catch (Exception e) {
            System.err.println("[ShootoutTweaks] Could not load config");
        }
    }

    public static InputStream getResource(String name) {
        return ShootoutTweaks.class.getResourceAsStream(name);
    }

    public static int toIntRGB(String color) {
        return Integer.parseInt(color.replace("0x", "").replace("#", ""),16);
    }
}
