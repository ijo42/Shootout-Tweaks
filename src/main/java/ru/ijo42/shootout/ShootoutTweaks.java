package ru.ijo42.shootout;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(
        modid = ShootoutTweaks.MOD_ID,
        name = ShootoutTweaks.MOD_NAME,
        version = ShootoutTweaks.VERSION
)
public class ShootoutTweaks {

    public static final String MOD_ID = "shootout";
    public static final String MOD_NAME = "Shootout Tweaks";
    public static final String VERSION = "1.0-SNAPSHOT";

    @Mod.Instance(MOD_ID)
    public static ShootoutTweaks INSTANCE;

    @Mod.EventHandler
    public void preinit(FMLPreInitializationEvent event) {

    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {

    }

    @Mod.EventHandler
    public void postinit(FMLPostInitializationEvent event) {

    }
}
