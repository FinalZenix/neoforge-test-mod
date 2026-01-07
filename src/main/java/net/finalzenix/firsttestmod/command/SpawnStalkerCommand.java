package net.finalzenix.firsttestmod.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import net.finalzenix.firsttestmod.entity.ModEntities;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class SpawnStalkerCommand {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("spawnstalker")
                .executes(SpawnStalkerCommand::run));
    }

    private static int run(CommandContext<CommandSourceStack> context) {
        try {
            ServerPlayer player = context.getSource().getPlayerOrException();
            Level level = player.level();
            BlockPos playerPos = player.blockPosition();

            // 1. Scan downwards to find ground
            BlockPos.MutableBlockPos mutablePos = new BlockPos.MutableBlockPos(playerPos.getX(), playerPos.getY(), playerPos.getZ());
            
            // Limit search to 100 blocks down to prevent infinite loops or lag
            for (int i = 0; i < 100; i++) {
                // If we are below the world, stop
                if (mutablePos.getY() < level.getMinBuildHeight()) break;

                BlockState state = level.getBlockState(mutablePos);
                if (!state.isAir()) {
                    // FOUND GROUND!
                    // Spawn ON TOP of this block (y + 1)
                    Entity entity = ModEntities.FIRST_ENTITY.get().create(level);
                    if (entity != null) {
                        entity.setPos(mutablePos.getX() + 0.5, mutablePos.getY() + 1.0, mutablePos.getZ() + 0.5);
                        level.addFreshEntity(entity);
                        context.getSource().sendSuccess(() -> Component.literal("Spawned Stalker at " + mutablePos.toShortString()), true);
                        return 1;
                    }
                }
                mutablePos.move(0, -1, 0);
            }

            context.getSource().sendFailure(Component.literal("Could not find ground below you!"));
            return 0;

        } catch (Exception e) {
            context.getSource().sendFailure(Component.literal("Error: " + e.getMessage()));
            return 0;
        }
    }
}
