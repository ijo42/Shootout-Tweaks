package ru.ijo42.shootout.packets;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import ru.ijo42.shootout.ShootoutTweaks;
import ru.ijo42.shootout.proxy.ClientProxy;

import java.util.Locale;

public class DamageDisplayMessage implements IMessage {
    float amount;
    int dealer;
    int dim;

    public DamageDisplayMessage() {
    }

    public DamageDisplayMessage(float amount, int dealer, String dim) {
        this.amount = amount;
        this.dealer = dealer;
        this.dim = dim.toLowerCase(Locale.ROOT).hashCode();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeFloat(amount);
        buf.writeInt(dealer);
        buf.writeInt(dim);
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        amount = buf.readFloat();
        dealer = buf.readInt();
        dim = buf.readInt();
    }

    @Override
    public String toString() {
        return "DamageDisplayMessage{" +
                "amount=" + amount +
                ", dealer=" + dealer +
                ", dim=" + dim +
                '}';
    }

    public static class DamageDisplayMessageHandler implements IMessageHandler<DamageDisplayMessage, IMessage> {

        @Override
        public IMessage onMessage(DamageDisplayMessage message, MessageContext ctx) {

            float amount = message.amount;
            int dealer = message.dealer;
            int dim = message.dim;

            if (ShootoutTweaks.INSTANCE.config.debug) {
                ShootoutTweaks.logger.info("Getting packet: {}", message);
                ShootoutTweaks.logger.info("ShootoutTweaks.proxy: {}", ShootoutTweaks.proxy);
                ShootoutTweaks.logger.info("((ClientProxy) ShootoutTweaks.proxy).damageRenderer: {}",
                        ((ClientProxy) ShootoutTweaks.proxy).damageRenderer);
            }

            ((ClientProxy) ShootoutTweaks.proxy).damageRenderer.updateDisplayDamage(dealer, amount, dim);
            return null;
        }
    }
}