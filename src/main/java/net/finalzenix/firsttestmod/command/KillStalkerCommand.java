package net.finalzenix.firsttestmod.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import net.finalzenix.firsttestmod.entity.custom.FirstEntity;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.phys.AABB;

import java.util.List;

public class KillStalkerCommand {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("killstalker")
                .then(Commands.literal("all")
                        .executes(KillStalkerCommand::killAll)) // Calls killAll()
                .then(Commands.literal("nearest")
                        .executes(KillStalkerCommand::killNearest)) // Calls killNearest()
        );
    }

    // 2. LOGIC: KILL ALL
    private static int killAll(CommandContext<CommandSourceStack> context) {
        try {
            ServerPlayer player = context.getSource().getPlayerOrException();
            AABB box = player.getBoundingBox().inflate(50);
            List<FirstEntity> listEntities = player.level().getEntitiesOfClass(FirstEntity.class, box);

            if(listEntities.isEmpty()){
                context.getSource().sendSuccess(() -> Component.literal("No stalker found in the active range."), false);
                return 1;
            }

            int count = 0;
            for (FirstEntity entity: listEntities){
                entity.discard();
                count++;
            }

            int finalCount = count;
            context.getSource().sendSuccess(() -> Component.literal("Killed " + finalCount + " Entites."), false);
            return 1;

        } catch (Exception e) {
            return 0;
        }
    }

    // 3. LOGIC: KILL NEAREST
    private static int killNearest(CommandContext<CommandSourceStack> context) {
        try {
            ServerPlayer player = context.getSource().getPlayerOrException();

            AABB box = player.getBoundingBox().inflate(50);
            List<FirstEntity> listEntities = player.level().getEntitiesOfClass(FirstEntity.class, box);

            if(listEntities.isEmpty()){
                context.getSource().sendSuccess(() -> Component.literal("No stalker found in the active range."), false);
                return 1;
            }

            FirstEntity closest = null;
            double closestDistance = -1;

            for(FirstEntity entity : listEntities){
                double distance = player.distanceToSqr(entity.position());
                if(closestDistance == -1){
                    closestDistance = distance;
                    closest = entity;
                }

                if(distance < closestDistance){
                    closestDistance = distance;
                    closest = entity;
                }
            }
            closest.discard();
            context.getSource().sendSuccess(() -> Component.literal("Deleted nearest stalker"), false);
            return 1;
        } catch (Exception e) {
            return 0;
        }
    }
}
