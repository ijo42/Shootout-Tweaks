package ru.ijo42.shootout.damagedisplay;

import com.google.common.collect.ImmutableSet;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.opengl.GL11;

import java.util.Arrays;
import java.util.Locale;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class DamageRenderer {
    private final FontRenderer fontRenderer;
    private final int receivedDamageColor;
    private final int takeDamageColor;
    private final int durationDisplay;
    private final Set<Integer> enableWorlds;
    public volatile long receivedUpdate = 0L;
    public volatile long takenUpdate = 0L;
    public int lastAttacked = -1;
    public volatile float currentDamageReceived = 0.0F;
    public volatile float currentDamageTaken = 0.0F;

    public DamageRenderer(int receivedDamageColor, int takeDamageColor, int durationDisplay, String[] enableWorlds) {
        this.fontRenderer = Minecraft.getMinecraft().fontRenderer;
        this.receivedDamageColor = receivedDamageColor;
        this.takeDamageColor = takeDamageColor;
        this.durationDisplay = durationDisplay;
        this.enableWorlds = ImmutableSet.copyOf(
                Arrays.stream(enableWorlds).map(s -> s.toLowerCase(Locale.ROOT))
                        .map(String::hashCode)
                        .collect(Collectors.toSet()));
    }

    public void updateDisplayDamage(int profile, float damage, int dim) {
        long currentTimeMillis = System.currentTimeMillis();

        if (enableWorlds.contains(dim)) {
            if (profile == -1) {
                float d = currentDamageReceived;
                currentDamageReceived = d + damage;
                receivedUpdate = currentTimeMillis;
            } else if (lastAttacked != -1 && lastAttacked == profile) {
                float d = currentDamageTaken;
                currentDamageTaken = d + damage;
                takenUpdate = currentTimeMillis;

            } else {
                currentDamageTaken = damage;
                lastAttacked = profile;
                takenUpdate = currentTimeMillis;
            }
        }
    }

    @SubscribeEvent
    public void onRender(RenderGameOverlayEvent.Text event) {

        long currentTimeMillis = System.currentTimeMillis();
        float height = event.getResolution().getScaledHeight(),
                width = event.getResolution().getScaledWidth();

        if (currentDamageReceived > 0) {
            if (receivedUpdate + TimeUnit.SECONDS.toMillis(durationDisplay) < currentTimeMillis) {
                currentDamageReceived = 0.0F;
            } else {
                GL11.glPushMatrix();
                fontRenderer
                        .drawStringWithShadow(String.format("%d", Math.round(currentDamageReceived)), width / 2, height / 2 - 15,
                                receivedDamageColor);
                GL11.glPopMatrix();
            }
        }

        if (currentDamageTaken > 0) {
            if (takenUpdate + TimeUnit.SECONDS.toMillis(durationDisplay) < currentTimeMillis) {
                currentDamageTaken = 0.0F;
                lastAttacked = -1;
            } else {
                GL11.glPushMatrix();
                fontRenderer
                        .drawStringWithShadow(String.format("%d", Math.round(currentDamageTaken)), width / 2 - 20, height / 2,
                                takeDamageColor);
                GL11.glPopMatrix();
            }
        }

    }
}
