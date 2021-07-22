package ru.ijo42.shootout.proxy;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;
import ru.ijo42.shootout.Config;
import ru.ijo42.shootout.ShootOutNetworkWrapper;
import ru.ijo42.shootout.ShootoutTweaks;
import ru.ijo42.shootout.damagedisplay.DamageListener;
import ru.ijo42.shootout.packets.DamageDisplayMessage;

import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;

public class CommonProxy {
    public void preInit(FMLPreInitializationEvent event) {
        ShootoutTweaks.logger = event.getModLog();
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        Path configPath = event.getModConfigurationDirectory().toPath().resolve("plugins").resolve(ShootoutTweaks.CONFIG_FILENAME);

        if (configPath.getParent().toFile().mkdirs()) {
            ShootoutTweaks.logger.debug("config dir created");
        }

        if (Files.notExists(configPath)) {
            ShootoutTweaks.logger.debug("config not found... write default");
            if (writeDefaultConfig(gson, configPath)) {
                return;
            }
        }

        ShootoutTweaks.logger.debug("loading config from {}", configPath.toString());

        try {
            ShootoutTweaks.INSTANCE.config = gson.fromJson(Files.newBufferedReader(configPath), Config.class);
        } catch (Exception e) {
            ShootoutTweaks.logger.error("Could not load config. Forcing defaults");
            if (writeDefaultConfig(gson, configPath)) {
                ShootoutTweaks.INSTANCE.config = new Config();
            }
        }

        if (ShootoutTweaks.INSTANCE.config.debug) {
            ShootoutTweaks.logger.info("registering DamageDisplay Packet");
        }

        ShootOutNetworkWrapper.INSTANCE
                .registerMessage(DamageDisplayMessage.DamageDisplayMessageHandler.class, DamageDisplayMessage.class, 0, Side.CLIENT);
        if (event.getSide() == Side.SERVER) {
            ShootoutTweaks.logger.info("Detected server-side... registering DamageListener");
            MinecraftForge.EVENT_BUS.register(new DamageListener());
        }
    }

    private boolean writeDefaultConfig(Gson gson, Path configPath) {
        try {
            gson.newJsonWriter(new PrintWriter(configPath.toFile()))
                    .jsonValue(gson.toJson(new Config(), Config.class)).close();
        } catch (Exception e) {
            ShootoutTweaks.logger.error("Could not copy default config to " + configPath);
            return true;
        }
        ShootoutTweaks.logger.debug("default config created");
        return false;
    }

    public void init(FMLInitializationEvent event) {

    }

    public void postInit(FMLPostInitializationEvent event) {

    }

}