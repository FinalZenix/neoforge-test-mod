package net.finalzenix.firsttestmod.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public class VisualizeHiddenSpotsCommand {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("visualizehidden")
                .executes(VisualizeHiddenSpotsCommand::run));
    }

    private static int run(CommandContext<CommandSourceStack> context) {
        try {
            ServerPlayer player = context.getSource().getPlayerOrException();
            Vec3 playerEye = player.getEyePosition();
            Vec3 playerLook = player.getViewVector(1.0F).normalize(); // Where you are looking
            
            // Loop in a radius around the player (e.g., -10 to +10)
            int radius = 20;
            int count = 0;

            for (int x = -radius; x <= radius; x++) {
                for (int z = -radius; z <= radius; z++) {
                    // Check blocks on the ground level
                    BlockPos targetPos = player.blockPosition().offset(x, 0, z);

                    if (isHidden(player, targetPos)) {
                        // If hidden, spawn a particle (Happy Villager = Green Sparkle)
                        // This sends a particle packet to the client
                        player.serverLevel().sendParticles(ParticleTypes.HAPPY_VILLAGER, 
                                targetPos.getX() + 0.5, targetPos.getY() + 1.5, targetPos.getZ() + 0.5, 
                                1, 0, 0, 0, 0);
                        count++;
                    }
                }
            }

            // Only send message if we actually did something
            int finalCount = count;
            context.getSource().sendSuccess(() -> Component.literal("Found " + finalCount + " hidden spots."), true);
            return 1;

        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    // ---------------------------------------------------------
    // EDUCATIONAL EXERCISE: Implement this logic!
    // ---------------------------------------------------------
    private static boolean isHidden(ServerPlayer player, BlockPos pos) {
        // HINT 1: Get the position of the block center as a Vec3
        // Vec3 blockPos = new Vec3(pos.getX() + 0.5, pos.getY() + 1.5, pos.getZ() + 0.5);
        Vec3 blockPos = new Vec3(pos.getX() + 0.5, pos.getY() + 1.5, pos.getZ() + 0.5);

        // HINT 2: Get the vector FROM the player TO the block
        Vec3 toBlock = blockPos.subtract(player.getEyePosition()).normalize();
        
        // HINT 3: Dot Product
        double dot = player.getViewVector(1.0F).normalize().dot(toBlock);

        if(dot < 0.5){
            return false;
        }else{
            ClipContext context = new ClipContext(
                    player.getEyePosition(),
                    blockPos,
                    net.minecraft.world.level.ClipContext.Block.VISUAL, // Check against visual shape of blocks
                    net.minecraft.world.level.ClipContext.Fluid.NONE,
                    player // User as the source to possibly ignore? Context uses it for collision context.
            );

            HitResult result = player.level().clip(context);

            // If we missed everything (Type.MISS), it means the path is clear to the target point.
            return !(result.getType() == HitResult.Type.MISS);
        }
    }
}
