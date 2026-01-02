package net.finalzenix.firsttestmod.recipe;

import net.finalzenix.firsttestmod.item.ModItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.SimpleCookingRecipeBuilder;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.concurrent.CompletableFuture;

public class ModRecipeProvider extends RecipeProvider {
    public ModRecipeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries);
    }

    @Override
    protected void buildRecipes(RecipeOutput output){
        SimpleCookingRecipeBuilder.smelting(
                Ingredient.of(ModItems.RAW_BISMUTH.get()),
                RecipeCategory.MISC,
                ModItems.BISMUTH.get(),
                0.7f,
                200
        ).unlockedBy("has_raw_bismuth", has(ModItems.RAW_BISMUTH.get()))
            .save(output,"bismuth_from_smelting");

        SimpleCookingRecipeBuilder.blasting(
                Ingredient.of(ModItems.RAW_BISMUTH.get()),
                RecipeCategory.MISC,
                ModItems.BISMUTH.get(),
                0.7f,
                100
        ).unlockedBy("has_raw_bismuth", has(ModItems.RAW_BISMUTH.get()))
                .save(output,"bismuth_from_blasting");
    }
}
