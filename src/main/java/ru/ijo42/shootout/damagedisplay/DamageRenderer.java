package ru.ijo42.shootout.damagedisplay;

import com.google.common.collect.ImmutableSet;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.opengl.GL11;
import ru.ijo42.shootout.ShootoutTweaks;

import java.util.Arrays;
import java.util.Locale;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class DamageRenderer {
    private final FontRenderer fontRenderer;
    private final int givenDamageColor;
    private final int receivedDamageColor;
    private final int durationDisplay;
    private final Set<Integer> enableWorlds;
    public volatile long receivedUpdate = 0L;
    public volatile long givenUpdate = 0L;
    public int lastAttacked = -1;
    public volatile float currentDamageReceived = 0.0F;
    public volatile float currentDamageGiven = 0.0F;

    public DamageRenderer(int givenDamageColor, int receivedDamageColor, int durationDisplay, String[] enableWorlds) {
        this.fontRenderer = Minecraft.getMinecraft().fontRenderer;
        this.givenDamageColor = givenDamageColor;
        this.receivedDamageColor = receivedDamageColor;
        this.durationDisplay = durationDisplay;
        this.enableWorlds = ImmutableSet.copyOf(
                Arrays.stream(enableWorlds).map(s -> s.toLowerCase(Locale.ROOT))
                        .map(String::hashCode)
                        .collect(Collectors.toSet()));
        if (ShootoutTweaks.INSTANCE.config.debug) {
            ShootoutTweaks.logger.info("Creating DamageRenderer w/: worlds=[{}]",
                    Arrays.stream(enableWorlds).map(s -> s.toLowerCase(Locale.ROOT))
                            .map(s -> s + ":" + s.hashCode()).collect(Collectors.joining(", "))
            );
        }
    }

    public void updateDisplayDamage(int profile, float damage, int dim) {
        long currentTimeMillis = System.currentTimeMillis();

        if (this.enableWorlds.contains(dim)) {
            if (profile == -1) {
                if (ShootoutTweaks.INSTANCE.config.debug) {
                    ShootoutTweaks.logger.info("Received packet with profile == -1. Displaying as given damage");
                }
                float d = profile == this.lastAttacked ? this.currentDamageGiven : 0.0F;
                this.currentDamageGiven = d + damage;
                this.givenUpdate = currentTimeMillis;
            } else {
                if (ShootoutTweaks.INSTANCE.config.debug) {
                    ShootoutTweaks.logger.info("Received packet with second cond. Displaying as received damage");
                }
                float d = this.currentDamageReceived;
                this.currentDamageReceived = d + damage;
                this.receivedUpdate = currentTimeMillis;
            }
        } else if (ShootoutTweaks.INSTANCE.config.debug) {
            ShootoutTweaks.logger.info("Received packet has disabled world. skipping...");
        }
    }

    @SubscribeEvent
    public void onRender(RenderGameOverlayEvent.Text event) {
        long currentTimeMillis = System.currentTimeMillis();
        float height = event.getResolution().getScaledHeight(),
                width = event.getResolution().getScaledWidth();

        if (this.currentDamageGiven > 0) {
            if (givenUpdate + TimeUnit.SECONDS.toMillis(durationDisplay) < currentTimeMillis) {
                this.currentDamageGiven = 0.0F;
                this.lastAttacked = -1;
            } else {
                GL11.glPushMatrix();
                this.fontRenderer
                        .drawStringWithShadow(String.format("%d", Math.round(this.currentDamageGiven)), width / 2, height / 2 - 15,
                                this.givenDamageColor);
                GL11.glPopMatrix();
            }
        }

        if (this.currentDamageReceived > 0) {
            if (this.receivedUpdate + TimeUnit.SECONDS.toMillis(this.durationDisplay) < currentTimeMillis) {
                this.currentDamageReceived = 0.0F;
            } else {
                GL11.glPushMatrix();
                this.fontRenderer
                        .drawStringWithShadow(String.format("%d", Math.round(this.currentDamageReceived)), width / 2 - 20, height / 2,
                                this.receivedDamageColor);
                GL11.glPopMatrix();
            }
        }

    }
}
