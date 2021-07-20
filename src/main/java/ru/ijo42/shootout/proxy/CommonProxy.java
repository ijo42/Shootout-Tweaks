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
import ru.ijo42.shootout.Utils;
import ru.ijo42.shootout.damagedisplay.DamageListener;
import ru.ijo42.shootout.packets.DamageDisplayMessage;

import java.nio.file.Files;
import java.nio.file.Path;

public class CommonProxy {
    public void preInit(FMLPreInitializationEvent event) {
        Path configPath = event.getModConfigurationDirectory().toPath().resolve("plugins").resolve(ShootoutTweaks.CONFIG_FILENAME);
        configPath.getParent().toFile().mkdirs();

        if (Files.notExists(configPath)) {
            try {
                Files.copy(Utils.getResource(ShootoutTweaks.CONFIG_FILENAME), configPath);
            } catch (Exception e) {
                System.err.println("[ShootoutTweaks] Could not copy default config to " + configPath);
                return;
            }
        }

        Gson gson = new GsonBuilder().create();
        try {
            ShootoutTweaks.INSTANCE.config = gson.fromJson(Files.newBufferedReader(configPath), Config.class);
        } catch (Exception e) {
            System.err.println("[ShootoutTweaks] Could not load config");
        }

        ShootOutNetworkWrapper.INSTANCE.registerMessage(DamageDisplayMessage.DamageDisplayMessageHandler.class, DamageDisplayMessage.class, 0, Side.CLIENT);
        MinecraftForge.EVENT_BUS.register(new DamageListener());
    }

    public void init(FMLInitializationEvent event) {

    }

    public void postInit(FMLPostInitializationEvent event) {

    }

}