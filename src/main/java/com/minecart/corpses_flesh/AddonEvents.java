package com.minecart.corpses_flesh;

import net.minecraft.world.item.CreativeModeTabs;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;

@EventBusSubscriber
public class AddonEvents {
    @SubscribeEvent
    private static void registerCreativeTabItems(BuildCreativeModeTabContentsEvent event){
        if(event.getTabKey().equals(CreativeModeTabs.INGREDIENTS)){
            event.accept(AddonBlockItems.FLESH.get());
            event.accept(AddonBlockItems.COOKED_FLESH.get());
        }
    }
}
