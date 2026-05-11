package com.minecart.corpses_flesh.mixin;

import com.minecart.corpses_flesh.AddonBlockItems;
import com.minecart.corpses_flesh.Config;
import com.minecart.corpses_flesh.mixin_interface.ICorpseEntityDamageSourceAccessor;
import de.maxhenkel.corpse.Main;
import de.maxhenkel.corpse.entities.CorpseBoundingBoxBase;
import de.maxhenkel.corpse.entities.CorpseEntity;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Containers;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.gameevent.GameEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Optional;
import java.util.UUID;

@Mixin(value = CorpseEntity.class, remap = false)
public abstract class CorpseEntityMixin extends CorpseBoundingBoxBase implements ICorpseEntityDamageSourceAccessor {
    private CorpseEntityMixin(EntityType<?> entityTypeIn, Level worldIn) {
        super(entityTypeIn, worldIn);
    }

    @Unique
    private static EntityDataAccessor<Float> DATA_ID_DAMAGE;

    @Unique
    private Holder<DamageType> deathDamage;

    @Shadow public abstract Optional<UUID> getCorpseUUID();
    @Shadow public abstract String getCorpseName();

    @Override
    public Holder<DamageType> getDamageType() {
        return deathDamage;
    }

    @Override
    public void setDamageType(Holder<DamageType> source) {
        this.deathDamage = source;
    }

    @Unique
    private void setDamage(float damage) {
        this.entityData.set(DATA_ID_DAMAGE, damage);
    }

    @Unique
    private float getDamage() {
        return (Float)this.entityData.get(DATA_ID_DAMAGE);
    }

    @Inject(method = "<clinit>", at = @At(value = "HEAD"))
    private static void staticstic(CallbackInfo info){
        DATA_ID_DAMAGE = SynchedEntityData.defineId(CorpseEntity.class, EntityDataSerializers.FLOAT);
    }

    @Inject(method = "addAdditionalSaveData", at = @At("TAIL"))
    protected void saveDamageHolder(CompoundTag compound, CallbackInfo ci) {
        if (this.deathDamage != null) {
            this.deathDamage.unwrapKey().ifPresent(key -> compound.putString("DeathDamageType", key.location().toString()));
        }
    }

    @Inject(method = "readAdditionalSaveData", at = @At("TAIL"))
    protected void loadDamageHolder(CompoundTag compound, CallbackInfo ci) {
        if (compound.contains("DeathDamageType")) {
            this.level().registryAccess().registryOrThrow(Registries.DAMAGE_TYPE)
                    .getHolder(ResourceKey.create(Registries.DAMAGE_TYPE, new ResourceLocation(compound.getString("DeathDamageType"))))
                    .ifPresent(holder -> this.deathDamage = holder);
        }
    }

    @Inject(method = "defineSynchedData", at = @At(value = "HEAD"))
    protected void defineSynchedData(CallbackInfo info) {
        this.entityData.define(DATA_ID_DAMAGE, 0.0F);
    }

    @Overwrite
    public boolean hurt(DamageSource source, float amount) {
        if (this.level().isClientSide)
            return true;
        if (this.isRemoved())
            return true;
        if (this.isInvulnerableTo(source))
            return false;
        if(source.is(DamageTypeTags.IS_FIRE) && !Main.SERVER_CONFIG.lavaDamage.get())
            return false;

        boolean flag = false;
        boolean isPlayer = false;
        boolean instabuild = false;
        if (source.getEntity() instanceof Player player) {
            isPlayer = true;
            instabuild = player.getAbilities().instabuild;
        }

        this.gameEvent(GameEvent.ENTITY_DAMAGE, source.getEntity());

        if (source != null && source.is(DamageTypeTags.IS_FIRE)) {
            this.setDamage(50f);
            flag = true;
        } else if(isPlayer) {
            this.setDamage(this.getDamage() + amount);
            this.dropFlesh(source, instabuild ? 0 : (int) amount);
            this.spawnBloodParticles(source, (int) amount);
            flag = true;
        }

        if (instabuild) {
            this.remove(RemovalReason.KILLED);
        }
        else if (this.getDamage() >= 50f) {
            this.dropPlayerHead();
            this.remove(RemovalReason.KILLED);
        }

        if(flag) {
            this.markHurt();
            return true;
        }
        return super.hurt(source, amount);
    }

    @Unique
    private void dropFlesh(DamageSource source, int amount){
        if(!Config.CORPSE_DROP)
            return;
        RandomSource random = level().getRandom();
        float fleshTarget = amount / 10.0f;
        int fleshCount = (int) fleshTarget;
        if (random.nextFloat() < (fleshTarget - fleshCount)) {
            fleshCount++;
        }
        float boneTarget = amount / 20.0f;
        int boneCount = (int) boneTarget;
        if (random.nextFloat() < (boneTarget - boneCount)) {
            boneCount++;
        }
        ItemStack fleshStack = new ItemStack(deathDamage.is(DamageTypeTags.IS_FIRE) ? AddonBlockItems.COOKED_FLESH.get() : AddonBlockItems.FLESH.get(), fleshCount);
        ItemStack boneStack = new ItemStack(Items.BONE, boneCount);
        if (fleshCount > 0) {
            Containers.dropItemStack(level(), getX(), getY(), getZ(), fleshStack);
        }
        if (boneCount > 0) {
            Containers.dropItemStack(level(), getX(), getY(), getZ(), boneStack);
        }
    }

    @Unique
    private void dropPlayerHead() {
        RandomSource source = level().getRandom();
        float chance = Config.PLAYER_HEAD_DROP_CHANCE;
        if (source.nextFloat() > chance) return;
        Optional<UUID> uuid = this.getCorpseUUID();
        String name = this.getCorpseName();
        if (uuid.isPresent() && name != null && !name.isEmpty()) {
            ItemStack headStack = new ItemStack(Items.PLAYER_HEAD, 1);
            CompoundTag nbt = headStack.getOrCreateTag();
            CompoundTag ownerTag = new CompoundTag();
            ownerTag.putUUID("Id", uuid.get());
            ownerTag.putString("Name", name);
            nbt.put("SkullOwner", ownerTag);
            Containers.dropItemStack(this.level(), this.getX(), this.getY(), this.getZ(), headStack);
        }
    }

    @Unique
    private void spawnBloodParticles(DamageSource source, int amount) {
        if (this.level() instanceof ServerLevel serverLevel) {
            BlockParticleOption bloodParticle = new BlockParticleOption(ParticleTypes.BLOCK, Blocks.REDSTONE_BLOCK.defaultBlockState());

            double bodyLength = 1.6;
            double bodyWidth = 0.5;

            float yawRad = (float) Math.toRadians(-this.getYRot());
            double cos = Math.cos(yawRad);
            double sin = Math.sin(yawRad);

            int particleCount = amount * 10;

            for (int i = 0; i < particleCount; i++) {
                double localZ = (this.level().random.nextDouble() - 0.5) * bodyLength;
                double localX = (this.level().random.nextDouble() - 0.5) * bodyWidth;

                double rotatedX = localX * cos - localZ * sin;
                double rotatedZ = localX * sin + localZ * cos;

                double finalX = this.getX() + rotatedX;
                double finalY = this.getY() + 0.1 + (this.level().random.nextDouble() * 0.2);
                double finalZ = this.getZ() + rotatedZ;

                serverLevel.sendParticles(bloodParticle,
                        finalX, finalY, finalZ,
                        1,
                        0, 0, 0,
                        0.15
                );
            }
        }
    }
}
