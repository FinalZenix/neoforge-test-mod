package net.finalzenix.firsttestmod.entity.custom;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class FirstEntity extends Monster {
    public FirstEntity(EntityType<? extends Monster> type, Level level) {
        super(type, level);
    }

    public static AttributeSupplier.@NotNull Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 50.0)      // Hard to kill
                .add(Attributes.MOVEMENT_SPEED, 0.35)  // Fast
                .add(Attributes.ATTACK_DAMAGE, 1.0);   // Hurts a lot
    }
}
