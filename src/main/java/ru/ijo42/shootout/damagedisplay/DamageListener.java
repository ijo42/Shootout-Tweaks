package ru.ijo42.shootout.damagedisplay;

import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import ru.ijo42.shootout.ShootOutNetworkWrapper;
import ru.ijo42.shootout.packets.DamageDisplayMessage;

public class DamageListener {

    @SubscribeEvent
    public void onDamage(LivingDamageEvent ev){
        if (ev.getEntity() instanceof EntityPlayerMP) {
            final EntityPlayerMP receiver = (EntityPlayerMP) ev.getEntity();

            if (ev.getSource().getTrueSource() instanceof EntityPlayerMP) {
                final EntityPlayerMP dealer = (EntityPlayerMP) ev.getSource().getTrueSource();

                ShootOutNetworkWrapper.INSTANCE.sendTo(
                        new DamageDisplayMessage(Math.min(ev.getAmount(), receiver.getHealth()),
                                dealer.getGameProfile().hashCode()),
                        receiver);

                ShootOutNetworkWrapper.INSTANCE.sendTo(
                        new DamageDisplayMessage(Math.min(ev.getAmount(), receiver.getHealth()), -1),
                        dealer);
            } else if (!(ev.getSource().getTrueSource() instanceof EntityCreature)) {
                ShootOutNetworkWrapper.INSTANCE.sendTo(
                        new DamageDisplayMessage(Math.min(ev.getAmount(), receiver.getHealth()), receiver.getGameProfile().hashCode()),
                        receiver);
            }
        }
    }
}
