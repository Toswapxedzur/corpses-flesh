package com.minecart.corpses_flesh.data_generation;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@EventBusSubscriber
public class DataGeneration {
    @SubscribeEvent
    private static void gatherData(GatherDataEvent event){
        DataGenerator generator = event.getGenerator();
        PackOutput output = generator.getPackOutput();
        ExistingFileHelper fileHelper = event.getExistingFileHelper();
        CompletableFuture<HolderLookup.Provider> loopUp = event.getLookupProvider();

        generator.addProvider(event.includeServer(), new LootTableProvider(output, Collections.EMPTY_SET, List.of(
                new LootTableProvider.SubProviderEntry(ProviderEntityLootTable::new, LootContextParamSets.ENTITY)
        ), loopUp));
        generator.addProvider(event.includeServer(), new GeneratorRecipe(output, loopUp));

        generator.addProvider(event.includeClient(), new GeneratorItemModel(output, fileHelper));
    }
}
