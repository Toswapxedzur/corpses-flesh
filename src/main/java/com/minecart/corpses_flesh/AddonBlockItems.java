package com.minecart.corpses_flesh;

import de.maxhenkel.corpse.Main;
import de.maxhenkel.corpse.entities.CorpseEntity;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class AddonBlockItems {
    public static final DeferredRegister.Items ITEMS;
    public static final Supplier<Item> FLESH;
    public static final Supplier<Item> COOKED_FLESH;

    static {
        ITEMS = DeferredRegister.createItems(Corpses_flesh.MODID);

        FLESH = ITEMS.registerSimpleItem("flesh", new Item.Properties().food(
                new FoodProperties.Builder().nutrition(2).saturationModifier(0.2f).effect(new MobEffectInstance(MobEffects.HUNGER, 300, 0), 0.6f).build()
        ));
        COOKED_FLESH = ITEMS.registerSimpleItem("cooked_flesh", new Item.Properties().food(
                new FoodProperties.Builder().nutrition(4).saturationModifier(0.3f).build()
        ));
    }
}
