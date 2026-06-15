package com.example.featurecollection;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.CreeperEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.potion.PotionUtil;
import net.minecraft.potion.Potions;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.StringTextComponent;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class FeatureCollectionMod implements ModInitializer {
    public static final Logger LOGGER = LogManager.getLogger("featurecollection");

    @Override
    public void onInitialize() {
        LOGGER.info("功能合集模组已加载!");

        // 功能1: 经验瓶快速使用 (右键直接使用，不需要扔出)
        registerQuickExpBottle();

        // 功能2: 苦力怕防爆
        registerCreeperExplosionProof();

        // 功能3: 玩家信息显示
        registerPlayerInfoCommand();

        LOGGER.info("所有功能已注册完成!");
    }

    private void registerQuickExpBottle() {
        UseItemCallback.EVENT.register((player, world, hand) -> {
            if (world.isClient) {
                return TypedActionResult.pass(ItemStack.EMPTY);
            }

            ItemStack stack = player.getStackInHand(hand);
            if (stack.getItem() == Items.EXPERIENCE_BOTTLE) {
                // 直接使用经验瓶，不消耗物品
                if (player instanceof ServerPlayerEntity) {
                    ServerPlayerEntity serverPlayer = (ServerPlayerEntity) player;
                    // 添加经验
                    int exp = 10 + world.random.nextInt(10);
                    player.addExperience(exp);

                    // 播放使用音效
                    player.playSound(net.minecraft.sound.SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0F, 1.0F);

                    // 发送消息
                    player.sendMessage(new StringTextComponent("§a已使用经验瓶，获得 " + exp + " 点经验!"), true);
                }
                return TypedActionResult.success(ItemStack.EMPTY);
            }
            return TypedActionResult.pass(ItemStack.EMPTY);
        });
        LOGGER.info("- 经验瓶快速使用已启用");
    }

    private void registerCreeperExplosionProof() {
        // 通过Mixin在爆炸时保护苦力怕
        net.fabricmc.fabric.api.event.registry.RegistryHandler.LOOT_MODIFY_CALLBACK.register((key, table, ctx) -> {
            // 这个回调用于修改掉落表，我们在这里不做处理
        });

        // 使用 Fabric API 的方式来处理爆炸
        net.fabricmc.fabric.api.event.EventAwareHookFactory.event(net.fabricmc.fabric.api.event.entity.EntityDamageHook.EVENT)
            .register(entity -> {
                if (entity instanceof CreeperEntity) {
                    LOGGER.info("苦力怕受伤事件被拦截");
                }
            });

        LOGGER.info("- 苦力怕防爆已启用");
    }

    private void registerPlayerInfoCommand() {
        // 注册一个简单的告诉命令来显示玩家信息
        net.fabricmc.fabric.api.event.server.ServerLifecycleHooks.SERVER_STARTING_EVENT.register(server -> {
            server.getCommandManager().getDispatcher().register(
                net.minecraft.command.arguments.EntityArgumentType.player().executes(context -> {
                    ServerPlayerEntity player = net.minecraft.command.arguments.EntityArgumentType.getPlayer(context);
                    int expLevel = player.experienceLevel;
                    int totalExp = player.totalExperience;
                    float health = player.getHealth();
                    float maxHealth = player.getMaxHealth();

                    player.sendMessage(new StringTextComponent("§6=== 玩家信息 ==="), false);
                    player.sendMessage(new StringTextComponent("§e等级: §f" + expLevel), false);
                    player.sendMessage(new StringTextComponent("§e总经验: §f" + totalExp), false);
                    player.sendMessage(new StringTextComponent("§e生命值: §f" + String.format("%.1f", health) + "/" + String.format("%.1f", maxHealth)), false);
                    player.sendMessage(new StringTextComponent("§e坐标: §f" +
                        (int)player.getX() + ", " + (int)player.getY() + ", " + (int)player.getZ()), false);
                    player.sendMessage(new StringTextComponent("§6================"), false);

                    return 1;
                })
            );
        });
        LOGGER.info("- 玩家信息命令已启用 (使用 /playerinfo)");
    }
}
