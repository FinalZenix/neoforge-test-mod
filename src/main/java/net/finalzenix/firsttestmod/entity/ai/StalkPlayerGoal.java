package net.finalzenix.firsttestmod.entity.ai;

import net.minecraft.util.Mth;
import net.finalzenix.firsttestmod.entity.custom.FirstEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.player.Player;
import net.neoforged.fml.loading.targets.CommonServerLaunchHandler;

import java.util.function.Predicate;

public class StalkPlayerGoal<T extends LivingEntity> extends Goal {

    protected final Mob mob;
    protected final Class<T> targetType;
    private TargetingConditions lookAtContext;

    public StalkPlayerGoal(Mob mob, Class<T> targetType) {
        this.mob = mob;
        this.targetType = targetType;
    }

    @Override
    public boolean canUse() {
        // Goal: Only run if we have a valid living target
        LivingEntity target = this.mob.getTarget();
        return target != null && target.isAlive();
    }

    @Override
    public void start() {
        // When goal starts, maybe make a sound or set state
    }

    @Override
    public void stop() {
        // When goal ends, stop moving
        this.mob.getNavigation().stop();
    }

    @Override
    public void tick() {
        LivingEntity target = this.mob.getTarget();
        if (target != null) {
            // 1. Look at the target
            this.mob.getLookControl().setLookAt(target, 30.0F, 30.0F);

            if (this.mob instanceof FirstEntity stalker){

                if(target instanceof Player){
                    boolean isLooking = stalker.isLookingAtMe((Player) target);
                    if(isLooking){
                        this.mob.getNavigation().stop();
                    }else{
                        this.mob.getNavigation().moveTo(target, 1.2d);
                    }
                }
            }


        }
    }

    // Helper: Stop if target is dead or too far
    @Override
    public boolean canContinueToUse() {
        LivingEntity target = this.mob.getTarget();
        return target != null && target.isAlive() && this.mob.distanceToSqr(target) < 256.0D; // 16 blocks (16*16)
    }
}