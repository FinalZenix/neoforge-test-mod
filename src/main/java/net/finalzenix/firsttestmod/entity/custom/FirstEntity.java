package net.finalzenix.firsttestmod.entity.custom;

import net.finalzenix.firsttestmod.entity.ai.StalkPlayerGoal;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerPlayer;
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
import net.minecraft.world.entity.projectile.FishingHook;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class FirstEntity extends Monster {

    /* ==================================================================================================
       TODO: STEP 1 - DEFINE THE PHASES
       We need an Enum to track the current state of the entity.
       
       private enum Phase {
           STALK,      // Phase 1: The Observer (Passive stalking)
           PARANOIA,   // Phase 2: The Paranoia (Glitches, sounds, moving blocks)
           AGGRESSIVE  // Phase 3: The Broken (Weeping Angel style aggression)
       }

       // Helper fields
       private Phase currentPhase = Phase.STALK;
       private int persistentTimer = 0; // Track time to evolve phases
       private boolean isGlitching = false; // For renderer ID
    ================================================================================================== */
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
        /* ==================================================================================================
           TODO: STEP 3 - PHASE-AWARE AI GOALS
           Instead of just adding goals, we should conditionally check the phase 
           OR change the priority based on phase. 
           
           Better approach: Add ALL goals, but make the Goals themselves check the "currentPhase" 
           in their canUse() method.
           
           Required Goals:
           1. StalkPlayerGoal (Phase.STALK & PARANOIA)
              - Keep distance (15-30 blocks)
              - Vanish if seen
              
           2. ParanoiaInteractionGoal (Phase.PARANOIA)
              - Inherits Stalking behavior
              - Randomly messes with doors/torches (Probability check)
              
           3. FreezeWhenSeenGoal (Phase.AGGRESSIVE)
              - If (player.canSee(this)) -> Stop moving, Become Invulnerable
              
           4. RushAttackGoal      (Phase.AGGRESSIVE)
              - If (!player.canSee(this)) -> Sprint at player (Speed 1.5+)
        ================================================================================================== */
        
        // Existing Goals (Modify these later)
        this.goalSelector.addGoal(1, new StalkPlayerGoal<>(this, Player.class));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Player.class, true));
    }

    /* ==================================================================================================
       TODO: STEP 2 - MANAGE LOGIC IN TICK
       
       @Override
       public void tick() {
           super.tick();
           
           if (this.level().isClientSide) {
                // Client-side particle/sound effects
                if (random.nextFloat() < 0.05) {
                    // Play static sound
                    // Spawn "glitch" particle
                }
                return;
           }
           
           // Server-side Phase Progression
           persistentTimer++;
           
           // Example Progression:
           // If (persistentTimer > 24000 ticks (1 day)) -> currentPhase = PARANOIA;
           // If (persistentTimer > 72000 ticks (3 days)) -> currentPhase = AGGRESSIVE;
           
           // Phase 1 & 2 Logic: Vanish if seen
           if (currentPhase != Phase.AGGRESSIVE) {
               if (isLookingAtMe(nearestPlayer)) {
                   teleportAway(); // Move to a hidden spot from listHiddenPlaces()
               }
           }
       }
    ================================================================================================== */

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
    private static boolean isHidden(Player player, BlockPos pos) {
        Vec3 blockPos = new Vec3(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5);

        Vec3 toBlock = blockPos.subtract(player.getEyePosition()).normalize();

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

    public List<Vec3> listHiddenPlaces(Player player){

        // Loop in a radius around the player (e.g., -10 to +10)
        int radius = 20;

        ArrayList<Vec3> listHiddenPlaces = new ArrayList<>();

        for (int x = -radius; x <= radius; x++) {
            for (int z = -radius; z <= radius; z++) {
                // Check blocks on the ground level
                BlockPos targetPos = player.blockPosition().offset(x, 0, z);

                if(!this.level().getBlockState(targetPos).isAir()) continue;

                // SUGGESTION: Walkability Check
                // Before checking if it's hidden, check if the entity can actually stand there!
                // 1. Ground check: Is there a solid block BELOW?
                //    if (this.level().getBlockState(targetPos.below()).isAir()) continue; 
                //
                // 2. Space check: Is there air/space at the feet and head?
                //    if (!this.level().getBlockState(targetPos).isAir()) continue;       // Feet
                //    if (!this.level().getBlockState(targetPos.above()).isAir()) continue; // Head

                if (isHidden(player, targetPos)) {
                    Vec3 pos = new Vec3(targetPos.getX() + 0.5, targetPos.getY(), targetPos.getZ() + 0.5);
                    listHiddenPlaces.add(pos);
                }
            }
        }
        return listHiddenPlaces;
    }
}
