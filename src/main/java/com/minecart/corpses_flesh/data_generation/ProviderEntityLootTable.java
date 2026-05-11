package com.minecart.corpses_flesh.data_generation;

import com.minecart.corpses_flesh.AddonBlockItems;
import net.minecraft.advancements.critereon.EntityFlagsPredicate;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.data.loot.EntityLootSubProvider;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.LootingEnchantFunction;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.functions.SmeltItemFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemEntityPropertyCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;

import java.util.stream.Stream;

public class ProviderEntityLootTable extends EntityLootSubProvider {
    protected ProviderEntityLootTable() {
        super(FeatureFlags.REGISTRY.allFlags());
    }

    @Override
    public void generate() {
        this.add(EntityType.VILLAGER, LootTable.lootTable()
                .withPool(LootPool.lootPool()
                        .setRolls(ConstantValue.exactly(1.0F))
                        .add(LootItem.lootTableItem(AddonBlockItems.FLESH.get())
                                .apply(SetItemCountFunction.setCount(UniformGenerator.between(3.0f, 8.0f)))
                                .apply(SmeltItemFunction.smelted().when(
                                        LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.THIS, ENTITY_ON_FIRE)
                                ))
                                .apply(LootingEnchantFunction.lootingMultiplier(
                                        UniformGenerator.between(0.0f, 1.0f)
                                ))
                        )
                )
                .withPool(LootPool.lootPool()
                        .setRolls(ConstantValue.exactly(1.0F))
                        .add(LootItem.lootTableItem(Items.BONE)
                                .apply(SetItemCountFunction.setCount(UniformGenerator.between(0.0f, 2.0f)))
                                .apply(LootingEnchantFunction.lootingMultiplier(
                                        UniformGenerator.between(0.0f, 1.0f)
                                ))
                        )
                )
        );
    }

    @Override
    protected Stream<EntityType<?>> getKnownEntityTypes() {
        return Stream.of(EntityType.VILLAGER);
    }
}
