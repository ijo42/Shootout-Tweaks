package ru.ijo42.shootout.damagedisplay;

import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import ru.ijo42.shootout.ShootOutNetworkWrapper;
import ru.ijo42.shootout.ShootoutTweaks;
import ru.ijo42.shootout.packets.DamageDisplayMessage;

public class DamageListener {

    @SubscribeEvent
    public void onDamage(LivingDamageEvent ev){
        if (ev.getEntity() instanceof EntityPlayerMP) {
            final EntityPlayerMP receiver = (EntityPlayerMP) ev.getEntity();
            String dim = ShootoutTweaks.INSTANCE.config.alterWorldGrab ?
                    receiver.world.getWorldInfo().getWorldName() :
                    receiver.world.provider.getSaveFolder();
            if (dim == null) {
                if (receiver.dimension != 0 || ShootoutTweaks.INSTANCE.config.debug) {
                    ShootoutTweaks.logger.error(String
                            .format("dim is null, forcing `%s`. dimension id: %d", receiver.world.getWorldInfo().getWorldName(),
                                    receiver.dimension));
                }
                dim = "world";
            }
            if (ev.getSource().getTrueSource() instanceof EntityPlayerMP) {
                final EntityPlayerMP dealer = (EntityPlayerMP) ev.getSource().getTrueSource();

                if (ShootoutTweaks.INSTANCE.config.debug) {
                    ShootoutTweaks.logger.info("{} damaged {} by {}HP in {}. Sending packets..",
                            dealer.toString(), receiver.toString(), ev.getAmount(), dim);
                }

                ShootOutNetworkWrapper.INSTANCE.sendTo(
                        new DamageDisplayMessage(Math.min(ev.getAmount(), receiver.getHealth()),
                                dealer.getGameProfile().hashCode(), dim),
                        receiver);

                ShootOutNetworkWrapper.INSTANCE.sendTo(
                        new DamageDisplayMessage(Math.min(ev.getAmount(), receiver.getHealth()), -1,
                                dim),
                        dealer);
            } else if (!(ev.getSource().getTrueSource() instanceof EntityCreature)) {

                if (ShootoutTweaks.INSTANCE.config.debug) {
                    ShootoutTweaks.logger.info("some another.. {}, {} damaged {} by {}HP in {}. Sending packet..",
                            ev.getSource().getTrueSource(), ev.getSource().damageType, receiver.toString(), ev.getAmount(), dim);
                }


                ShootOutNetworkWrapper.INSTANCE.sendTo(
                        new DamageDisplayMessage(Math.min(ev.getAmount(), receiver.getHealth()), receiver.getGameProfile().hashCode(),
                                dim),
                        receiver);
            }
        }
    }
}
