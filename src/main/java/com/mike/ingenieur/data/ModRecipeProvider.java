package com.mike.ingenieur.data;

import com.mike.ingenieur.block.BlockRegistry;
import com.mike.ingenieur.item.ItemRegistry;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.world.item.Items;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.crafting.conditions.IConditionBuilder;

import java.util.function.Consumer;

public class ModRecipeProvider extends RecipeProvider implements IConditionBuilder {
    public ModRecipeProvider(DataGenerator pGenerator) {
        super(pGenerator);
    }

    @Override
    protected void buildCraftingRecipes(Consumer<FinishedRecipe> pFinishedRecipeConsumer) {
        ShapedRecipeBuilder.shaped(ItemRegistry.MACHINE_CONTROLLER.get())
                .define('G', Tags.Items.DYES_GREEN).define('C', Tags.Items.INGOTS_COPPER)
                .define('R', Tags.Items.DUSTS_REDSTONE).define('T', Items.REDSTONE_TORCH)
                .pattern("GGG")
                .pattern("TRT")
                .pattern("CCC").unlockedBy("has_redstone",inventoryTrigger(ItemPredicate.Builder.item()
                        .of(Items.REDSTONE).build())).save(pFinishedRecipeConsumer);

        ShapedRecipeBuilder.shaped(BlockRegistry.MACHINE_CORE.get())
                .define('M', ItemRegistry.MACHINE_CONTROLLER.get())
                .define('I', Tags.Items.INGOTS_IRON)
                .pattern("III")
                .pattern("IMI")
                .pattern("III").unlockedBy("has_machine_controller", inventoryTrigger(ItemPredicate.Builder.item()
                        .of(ItemRegistry.MACHINE_CONTROLLER.get()).build())).save(pFinishedRecipeConsumer);

        ShapedRecipeBuilder.shaped(BlockRegistry.PICKAXE_SIMULATOR.get())
                .define('M', BlockRegistry.MACHINE_CORE.get())
                .define('R', Items.REDSTONE)
                .define('I', Tags.Items.INGOTS_IRON)
                .define('P', Tags.Items.TOOLS_PICKAXES)
                .pattern("IPI")
                .pattern("IMI")
                .pattern("RRR").unlockedBy("has_machine_core", inventoryTrigger(ItemPredicate.Builder.item()
                        .of(BlockRegistry.MACHINE_CORE.get()).build())).save(pFinishedRecipeConsumer);
    }
}
