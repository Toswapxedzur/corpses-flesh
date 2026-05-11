package com.minecart.corpses_flesh.mixin_interface;

import net.minecraft.core.Holder;
import net.minecraft.world.damagesource.DamageType;

public interface ICorpseEntityDamageSourceAccessor {
    void setDamageType(Holder<DamageType> source);

    Holder<DamageType> getDamageType();
}
