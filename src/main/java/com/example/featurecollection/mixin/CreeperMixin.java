package com.example.featurecollection.mixin;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.hostile.CreeperEntity;
import net.minecraft.world.World;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CreeperEntity.class)
public abstract class CreeperMixin extends LivingEntity {

    private static final Logger LOGGER = LoggerFactory.getLogger("featurecollection");

    protected CreeperMixin(EntityType<? extends LivingEntity> type, World world) {
        super(type, world);
    }

    @Inject(method = "explode", at = @At("HEAD"), cancellable = true)
    private void onExplode(CallbackInfo ci) {
        LOGGER.info("[苦力怕防爆] 拦截苦力怕爆炸于位置 ({}, {}, {})",
            (int) this.getX(), (int) this.getY(), (int) this.getZ());
        this.discard();
        ci.cancel();
    }
}
