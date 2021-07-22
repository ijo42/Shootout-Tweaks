package ru.ijo42.shootout.proxy;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import ru.ijo42.shootout.Config;
import ru.ijo42.shootout.ShootoutTweaks;
import ru.ijo42.shootout.Utils;
import ru.ijo42.shootout.damagedisplay.DamageRenderer;

public class ClientProxy extends CommonProxy {
    public DamageRenderer damageRenderer;

    @Override
    public void init(FMLInitializationEvent event) {
        super.init(event);

        if (ShootoutTweaks.INSTANCE.config.debug) {
            ShootoutTweaks.logger.info("init Stage. loading colors...");
        }

        int giveDamageColor,
                takeDamageColor;
        try {
            final Config config = ShootoutTweaks.INSTANCE.config;
            giveDamageColor = Utils.toIntRGB(config.bulletDamage.giveDamageColor);
            takeDamageColor = Utils.toIntRGB(config.bulletDamage.takeDamageColor);
            this.damageRenderer = new DamageRenderer(giveDamageColor, takeDamageColor,
                    config.bulletDamage.delay, config.bulletDamage.enableWorlds);
            if (config.modules.bulletDamage) {
                ShootoutTweaks.logger.info("bulletDamage is true, registering...");
                MinecraftForge.EVENT_BUS.register(this.damageRenderer);
            } else {
                ShootoutTweaks.logger.info("bulletDamage is false, registration is not required");
            }
        } catch (Exception e) {
            ShootoutTweaks.logger.error(e);
        }
    }
}