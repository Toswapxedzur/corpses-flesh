package com.minecart.corpses_flesh;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;

// An example config class. This is not required, but it's a good idea to have one to keep your config organized.
// Demonstrates how to use Forge's config APIs
@Mod.EventBusSubscriber(modid = Corpses_flesh.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class Config {
    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();

    private static final ForgeConfigSpec.BooleanValue CORPSE_DROP_VAL = BUILDER.comment("should corpse drop flesh and bones when harvested by player")
            .define("corpse_drop", true);

    private static final ForgeConfigSpec.DoubleValue PLAYER_HEAD_DROP_CHANCE_VAL = BUILDER.comment("the possibility of a corpse drop its owner's head after destroyed")
            .defineInRange("player_head_drop_chance", 1.0, 0.0, 1.0);

    static final ForgeConfigSpec SPEC = BUILDER.build();

    public static boolean CORPSE_DROP;
    public static float PLAYER_HEAD_DROP_CHANCE;

    @SubscribeEvent
    public static void onLoad(final ModConfigEvent event) {
        CORPSE_DROP = CORPSE_DROP_VAL.get();
        PLAYER_HEAD_DROP_CHANCE = PLAYER_HEAD_DROP_CHANCE_VAL.get().floatValue();
    }
}
