package ru.ijo42.shootout.packets;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import ru.ijo42.shootout.ShootoutTweaks;
import ru.ijo42.shootout.proxy.ClientProxy;

public class DamageDisplayMessage implements IMessage {
    float amount;
    int dealer;

    public DamageDisplayMessage() {
    }

    public DamageDisplayMessage(float amount, int dealer) {
        this.amount = amount;
        this.dealer = dealer;
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeFloat(amount);
        buf.writeInt(dealer);
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        amount = buf.readFloat();
        dealer = buf.readInt();
    }

    public static class DamageDisplayMessageHandler implements IMessageHandler<DamageDisplayMessage, IMessage> {

        @Override
        public IMessage onMessage(DamageDisplayMessage message, MessageContext ctx) {

            float amount = message.amount;
            int dealer = message.dealer;

            ((ClientProxy)ShootoutTweaks.proxy).damageRenderer.updateDisplayDamage(dealer, amount);
            return null;
        }
    }
}