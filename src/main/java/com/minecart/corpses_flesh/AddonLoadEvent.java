package com.minecart.corpses_flesh;

import net.minecraft.world.item.CreativeModeTabs;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class AddonLoadEvent {
    @SubscribeEvent
    public static void registerCreativeTabItems(BuildCreativeModeTabContentsEvent event){
        if(event.getTabKey().equals(CreativeModeTabs.INGREDIENTS)){
            event.accept(AddonBlockItems.FLESH.get());
            event.accept(AddonBlockItems.COOKED_FLESH.get());
        }
    }
}
