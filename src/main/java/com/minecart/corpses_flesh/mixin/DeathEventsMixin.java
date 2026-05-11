package com.minecart.corpses_flesh.mixin;

import com.minecart.corpses_flesh.mixin_interface.ICorpseEntityDamageSourceAccessor;
import de.maxhenkel.corpse.Main;
import de.maxhenkel.corpse.corelib.death.PlayerDeathEvent;
import de.maxhenkel.corpse.entities.CorpseEntity;
import de.maxhenkel.corpse.events.DeathEvents;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(value = DeathEvents.class, remap = false)
public class DeathEventsMixin {
    @SubscribeEvent
    @Overwrite
    public void playerDeath(PlayerDeathEvent event) {
        if ((Integer) Main.SERVER_CONFIG.maxDeathAge.get() != 0) {
            event.storeDeath();
        }
        event.removeDrops();
        ServerPlayer player = event.getPlayer();
        CorpseEntity corpse = CorpseEntity.createFromDeath(player, event.getDeath());
        ((ICorpseEntityDamageSourceAccessor) corpse).setDamageType(event.getSource().typeHolder());
        player.serverLevel().addFreshEntity(corpse);
        DeathEvents.deleteOldDeaths(player.serverLevel());
    }
}
