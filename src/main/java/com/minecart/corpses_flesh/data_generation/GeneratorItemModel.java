package com.minecart.corpses_flesh.data_generation;

import com.minecart.corpses_flesh.AddonBlockItems;
import com.minecart.corpses_flesh.Corpses_flesh;
import net.minecraft.client.gui.components.SpriteIconButton;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class GeneratorItemModel extends ItemModelProvider {
    public GeneratorItemModel(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, Corpses_flesh.MODID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        basicItem(AddonBlockItems.FLESH.get());
        basicItem(AddonBlockItems.COOKED_FLESH.get());
    }
}
