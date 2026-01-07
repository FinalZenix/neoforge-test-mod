package net.finalzenix.firsttestmod.entity.ai;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.player.Player;

import java.util.EnumSet;

public class LongTargetGoal<T extends LivingEntity> extends Goal {
    private final Class<T> targetClass;
    private final Mob mob;
    private final TargetingConditions targetConditions;
    private Player target;

    public LongTargetGoal(Mob mob, Class<T> targetClass) {
        this.mob = mob;
        this.targetClass = targetClass;
        this.setFlags(EnumSet.of(Flag.TARGET)); // We are modifying the target
        this.targetConditions = TargetingConditions.forCombat().range(this.getFollowDistance()).selector(null);
    }

    private double getFollowDistance() {
        return this.mob.getAttributeValue(Attributes.FOLLOW_RANGE);
    }

    @Override
    public boolean canUse(){

        if (this.mob.getTarget() == null){ return false; }

        this.target = this.mob.level().getNearestPlayer(this.targetConditions, this.mob, this.mob.getX(), this.mob.getEyeY(), this.mob.getZ());

        if(target != null){
            this.mob.setTarget(target);
            return true;
        }
        return false;

    }

    @Override
    public void start(){
        this.mob.setTarget(this.target);
        super.start();
    }
}
