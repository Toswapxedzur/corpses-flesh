package com.minecart.corpses_flesh.data_generation;

import com.minecart.corpses_flesh.AddonBlockItems;
import com.minecart.corpses_flesh.Corpses_flesh;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.SimpleCookingRecipeBuilder;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.function.Consumer;

public class GeneratorRecipe extends RecipeProvider {
    public GeneratorRecipe(PackOutput output) {
        super(output);
    }

    @Override
    protected void buildRecipes(Consumer<FinishedRecipe> writer) {

        SimpleCookingRecipeBuilder.smoking(Ingredient.of(AddonBlockItems.FLESH.get()), RecipeCategory.FOOD, AddonBlockItems.COOKED_FLESH.get(), 0.35F, 200)
                .unlockedBy("has_flesh", has(AddonBlockItems.FLESH.get()))
                .save(writer, Corpses_flesh.modLoc("cooked_flesh_from_smoking"));

        SimpleCookingRecipeBuilder.smelting(Ingredient.of(AddonBlockItems.FLESH.get()), RecipeCategory.FOOD, AddonBlockItems.COOKED_FLESH.get(), 0.35F, 200)
                .unlockedBy("has_flesh", has(AddonBlockItems.FLESH.get()))
                .save(writer, Corpses_flesh.modLoc("cooked_flesh_from_smelting"));

        SimpleCookingRecipeBuilder.blasting(Ingredient.of(AddonBlockItems.FLESH.get()), RecipeCategory.FOOD, Items.ROTTEN_FLESH, 0.35F, 200)
                .unlockedBy("has_flesh", has(AddonBlockItems.FLESH.get()))
                .save(writer, Corpses_flesh.modLoc("cooked_flesh_from_blasting"));

        SimpleCookingRecipeBuilder.campfireCooking(Ingredient.of(AddonBlockItems.FLESH.get()), RecipeCategory.FOOD, AddonBlockItems.COOKED_FLESH.get(), 0.35F, 200)
                .unlockedBy("has_flesh", has(AddonBlockItems.FLESH.get()))
                .save(writer, Corpses_flesh.modLoc("cooked_flesh_from_campfire_cooking"));
    }
}
