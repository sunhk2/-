package com.example.featurecollection;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.CreeperEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FeatureCollectionMod implements ModInitializer {
    public static final Logger LOGGER = LoggerFactory.getLogger("featurecollection");
    public static final String MOD_ID = "featurecollection";

    @Override
    public void onInitialize() {
        LOGGER.info("功能合集模组 v26.1.2 已加载!");

        registerQuickExpBottle();
        registerCreeperExplosionProof();
        registerPlayerInfoCommand();

        LOGGER.info("所有功能已注册完成!");
    }

    private void registerQuickExpBottle() {
        UseItemCallback.EVENT.register((player, world, hand) -> {
            if (world.isClient) {
                return TypedActionResult.pass(ItemStack.EMPTY);
            }

            ItemStack stack = player.getStackInHand(hand);
            if (stack.isOf(Items.EXPERIENCE_BOTTLE)) {
                int exp = 10 + world.random.nextInt(10);
                player.addExperience(exp);
                player.playSound(SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0F, 1.0F);
                player.sendMessage(Text.literal("§a已使用经验瓶，获得 " + exp + " 点经验!"), true);
                return TypedActionResult.success(ItemStack.EMPTY);
            }
            return TypedActionResult.pass(ItemStack.EMPTY);
        });
        LOGGER.info("[功能1] 经验瓶快速使用已启用");
    }

    private void registerCreeperExplosionProof() {
        LOGGER.info("[功能2] 苦力怕防爆已启用 (由 CreeperMixin 拦截 explode 方法)");
    }

    private void registerPlayerInfoCommand() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            dispatcher.register(CommandManager.literal("playerinfo")
                .requires(source -> source.hasPermissionLevel(0))
                .executes(this::executePlayerInfo));
        });
        LOGGER.info("[功能3] 玩家信息命令已启用 (使用 /playerinfo)");
    }

    private int executePlayerInfo(CommandContext<ServerCommandSource> context) {
        try {
            ServerPlayerEntity player = context.getSource().getPlayer();
            if (player == null) {
                context.getSource().sendError(Text.literal("该命令只能由玩家执行!"));
                return 0;
            }

            int expLevel = player.experienceLevel;
            int totalExp = player.totalExperience;
            float health = player.getHealth();
            float maxHealth = player.getMaxHealth();

            player.sendMessage(Text.literal("§6============"), false);
            player.sendMessage(Text.literal("§6== 玩家信息 =="), false);
            player.sendMessage(Text.literal("§6============"), false);
            player.sendMessage(Text.literal("§e 玩家: §f" + player.getGameProfile().getName()), false);
            player.sendMessage(Text.literal("§e 等级: §f" + expLevel), false);
            player.sendMessage(Text.literal("§e 总经验: §f" + totalExp), false);
            player.sendMessage(Text.literal("§e 生命值: §f" + String.format("%.1f", health) + "/" + String.format("%.1f", maxHealth)), false);
            player.sendMessage(Text.literal("§e 坐标: §f" +
                (int)player.getX() + ", " + (int)player.getY() + ", " + (int)player.getZ()), false);
            player.sendMessage(Text.literal("§6============"), false);

            return 1;
        } catch (Exception e) {
            LOGGER.error("执行 playerinfo 命令时出错", e);
            context.getSource().sendError(Text.literal("命令执行失败，请查看日志。"));
            return 0;
        }
    }
}
