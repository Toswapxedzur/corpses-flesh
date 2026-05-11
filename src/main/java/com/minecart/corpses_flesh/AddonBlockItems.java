package com.minecart.corpses_flesh;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class AddonBlockItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Corpses_flesh.MODID);

    public static final RegistryObject<Item> FLESH = ITEMS.register("flesh", () -> new Item(
            new Item.Properties().food(
                    new FoodProperties.Builder()
                            .nutrition(2)
                            .saturationMod(0.2f)
                            .effect(() -> new MobEffectInstance(MobEffects.HUNGER, 300, 0), 0.6f)
                            .build()
            )
    ));

    public static final RegistryObject<Item> COOKED_FLESH = ITEMS.register("cooked_flesh", () -> new Item(
            new Item.Properties().food(
                    new FoodProperties.Builder()
                            .nutrition(4)
                            .saturationMod(0.3f)
                            .build()
            )
    ));
}
