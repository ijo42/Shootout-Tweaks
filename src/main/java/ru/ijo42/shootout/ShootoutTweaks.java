package ru.ijo42.shootout;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import ru.ijo42.shootout.proxy.CommonProxy;

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
    @SidedProxy(clientSide = "ru.ijo42.shootout.proxy.ClientProxy", serverSide = "ru.ijo42.shootout.proxy.CommonProxy")
    public static CommonProxy proxy;
    public Config config;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        proxy.preInit(event);
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        proxy.init(event);
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        proxy.postInit(event);
    }
}
