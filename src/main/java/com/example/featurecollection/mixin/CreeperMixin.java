package com.example.featurecollection.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.CreeperEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.World;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.Locator;

@Mixin(CreeperEntity.class)
public abstract class CreeperMixin extends LivingEntity {

    private static final Logger LOGGER = LogManager.getLogger("featurecollection");

    protected CreeperMixin(EntityType<? extends LivingEntity> type, World world) {
        super(type, world);
    }

    @Inject(at = @At("HEAD"), method = "explode", cancellable = true)
    private void onExplode(CallbackInfoReturnable<Boolean> cir) {
        // 苦力怕防爆功能：当苦力怕爆炸时，阻止其造成伤害
        LOGGER.info("检测到苦力怕爆炸，已阻止!");
        cir.setReturnValue(false);
    }
}
