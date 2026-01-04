package net.finalzenix.firsttestmod.entity.custom;

import net.finalzenix.firsttestmod.entity.ai.StalkPlayerGoal;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class FirstEntity extends Monster {
    public FirstEntity(EntityType<? extends Monster> type, Level level) {
        super(type, level);
    }

    public static AttributeSupplier.@NotNull Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 50.0)      // Hard to kill
                .add(Attributes.MOVEMENT_SPEED, 0.6)  // Fast
                .add(Attributes.ATTACK_DAMAGE, 1.0);   // Hurts a lot
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new StalkPlayerGoal<>(this, Player.class));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Player.class, true));
    }

    /**
     * Checks if the player is looking at this entity within a wider FOV (approx 120 deg total)
     * and has a clear line of sight (no blocks in between).
     */
    public boolean isLookingAtMe(net.minecraft.world.entity.player.Player player) {
        // 1. FOV Check
        net.minecraft.world.phys.Vec3 playerLook = player.getViewVector(1.0F).normalize();
        net.minecraft.world.phys.Vec3 toEntity = this.getBoundingBox().getCenter().subtract(player.getEyePosition()).normalize();
        
        // Dot product threshold adjusted to 0.5 (cos 60)
        // This gives a 120-degree total cone, which comfortably covers a standard 90-degree FOV setting.
        if (playerLook.dot(toEntity) < 0.5) {
            return false;
        }

        // 2. Line of Sight (RayTrace)
        // Check if there are blocks between player eyes and entity center.
        net.minecraft.world.level.ClipContext context = new net.minecraft.world.level.ClipContext(
            player.getEyePosition(),
            this.getBoundingBox().getCenter(),
            net.minecraft.world.level.ClipContext.Block.VISUAL, // Check against visual shape of blocks
            net.minecraft.world.level.ClipContext.Fluid.NONE,
            player // User as the source to possibly ignore? Context uses it for collision context.
        );

        net.minecraft.world.phys.HitResult result = this.level().clip(context);

        // If we missed everything (Type.MISS), it means the path is clear to the target point.
        return result.getType() == net.minecraft.world.phys.HitResult.Type.MISS;
    }
}
