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
        int giveDamageColor,
                takeDamageColor;
        try {
            final Config config = ShootoutTweaks.INSTANCE.config;
            giveDamageColor = Utils.toIntRGB(config.bulletDamage.giveDamageColor);
            takeDamageColor = Utils.toIntRGB(config.bulletDamage.takeDamageColor);
            this.damageRenderer = new DamageRenderer(giveDamageColor, takeDamageColor, config.bulletDamage.delay);
            if (ShootoutTweaks.INSTANCE.config.modules.bulletDamage) {
                MinecraftForge.EVENT_BUS.register(this.damageRenderer);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}