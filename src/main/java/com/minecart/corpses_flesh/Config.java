package com.minecart.corpses_flesh;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;

@EventBusSubscriber(modid = Corpses_flesh.MODID, bus = EventBusSubscriber.Bus.MOD)
public class Config {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    private static final ModConfigSpec.BooleanValue CORPSE_DROP_VAL = BUILDER
            .comment("should corpse drop flesh and bones when harvested by player")
            .define("corpse_drop", true);

    private static final ModConfigSpec.DoubleValue PLAYER_HEAD_DROP_CHANCE_VAL = BUILDER
            .comment("the possibility of a corpse drop its owner's head after destroyed")
            .defineInRange("player_head_drop_chance", 1.0, 0.0, 1.0);

    static final ModConfigSpec SPEC = BUILDER.build();

    public static boolean CORPSE_DROP;
    public static float PLAYER_HEAD_DROP_CHANCE;

    @SubscribeEvent
    public static void onLoad(final ModConfigEvent event) {
        CORPSE_DROP = CORPSE_DROP_VAL.get();
        PLAYER_HEAD_DROP_CHANCE = PLAYER_HEAD_DROP_CHANCE_VAL.get().floatValue();
    }
}
