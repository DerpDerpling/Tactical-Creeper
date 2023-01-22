package derp.tacticalcreeper.entity.custom;

import derp.tacticalcreeper.TacticalCreeper;
import net.minecraft.block.BlockState;
import net.minecraft.entity.AreaEffectCloudEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LightningEntity;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.mob.CreeperEntity;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.passive.CatEntity;
import net.minecraft.entity.passive.OcelotEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import net.minecraft.world.explosion.Explosion;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import java.util.Collection;
import java.util.EnumSet;
import java.util.Iterator;


public class TacticalCreeperEntity extends CreeperEntity implements IAnimatable {
    private AnimationFactory factory = new AnimationFactory(this);
    private static final TrackedData<Integer> FUSE_SPEED = DataTracker.registerData(TacticalCreeperEntity.class, TrackedDataHandlerRegistry.INTEGER);
    private static final TrackedData<Boolean> CHARGED = DataTracker.registerData(TacticalCreeperEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    private static final TrackedData<Boolean> IGNITED = DataTracker.registerData(TacticalCreeperEntity.class, TrackedDataHandlerRegistry.BOOLEAN);


    private int lastFuseTime;
    private int currentFuseTime;
    private int fuseTime = 30;
    private int explosionRadius = 13;

    public TacticalCreeperEntity(EntityType<? extends HostileEntity> entityType, World world) {
        super((EntityType<? extends CreeperEntity>) entityType, world);
    }



    public static DefaultAttributeContainer.Builder setAttributes() {
        return HostileEntity.createMobAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 20.0D)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 2.0f)
                .add(EntityAttributes.GENERIC_ATTACK_SPEED, 2.0f)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.5f);

    }

    private <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
        if (event.isMoving()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.babycreeper.walk", true));
            return PlayState.CONTINUE;
        }

        event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.babycreeper.idle", true));
        return PlayState.CONTINUE;
    }

    @Override
    public void registerControllers(AnimationData animationData) {
        animationData.addAnimationController(new AnimationController(this, "controller",
                0, this::predicate));
    }

    @Override
    public AnimationFactory getFactory() {
        return factory;
    }


    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return SoundEvents.ENTITY_CREEPER_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.ENTITY_CREEPER_DEATH;
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState state) {
        this.playSound(SoundEvents.ENTITY_PIG_STEP, 0.15f, 1.0f);
    }
    //goals

    protected void initGoals() {
        this.goalSelector.add(1, new SwimGoal(this));
        this.goalSelector.add(2, new CreeperIgniteGoal(this));
        this.goalSelector.add(3, new FleeEntityGoal(this, OcelotEntity.class, 6.0F, 1.0, 1.2));
        this.goalSelector.add(3, new FleeEntityGoal(this, CatEntity.class, 6.0F, 1.0, 1.2));
        this.goalSelector.add(4, new MeleeAttackGoal(this, 1.0, false));
        this.goalSelector.add(5, new WanderAroundFarGoal(this, 0.8));
        this.goalSelector.add(6, new LookAtEntityGoal(this, PlayerEntity.class, 8.0F));
        this.goalSelector.add(6, new LookAroundGoal(this));
        this.targetSelector.add(1, new ActiveTargetGoal(this, PlayerEntity.class, true));
        this.targetSelector.add(2, new RevengeGoal(this, new Class[0]));
    }


    //actual creeper code

    protected void initDataTracker() {
        super.initDataTracker();
        this.dataTracker.startTracking(FUSE_SPEED, -1);
        this.dataTracker.startTracking(CHARGED, false);
        this.dataTracker.startTracking(IGNITED, false);
    }

    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);

        nbt.putShort("Fuse", (short)this.fuseTime);
        nbt.putByte("ExplosionRadius", (byte)this.explosionRadius);
        nbt.putBoolean("ignited", this.isIgnited());
    }
    public boolean isIgnited() {
        return (Boolean)this.dataTracker.get(IGNITED);
    }

    public void ignite() {
        this.dataTracker.set(IGNITED, true);
    }
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        if (nbt.contains("Fuse", 99)) {
            this.fuseTime = nbt.getShort("Fuse");
        }

        if (nbt.contains("ExplosionRadius", 99)) {
            this.explosionRadius = nbt.getByte("ExplosionRadius");
        }

        if (nbt.getBoolean("ignited")) {
            this.ignite();
        }

    }
    public boolean tryAttack(Entity target) {
        return true;
    }

    public float getClientFuseTime(float timeDelta) {
        return MathHelper.lerp(timeDelta, (float)this.lastFuseTime, (float)this.currentFuseTime) / (float)(this.fuseTime - 2);
    }

    public int getFuseSpeed() {
        return (Integer)this.dataTracker.get(FUSE_SPEED);
    }

    public void setFuseSpeed(int fuseSpeed) {
        this.dataTracker.set(FUSE_SPEED, fuseSpeed);
    }

    public void onStruckByLightning(ServerWorld world, LightningEntity lightning) {
        super.onStruckByLightning(world, lightning);
        this.dataTracker.set(CHARGED, true);
    }

    protected ActionResult interactMob(PlayerEntity player, Hand hand) {
        ItemStack itemStack = player.getStackInHand(hand);
        if (itemStack.isOf(Items.FLINT_AND_STEEL)) {
            this.world.playSound(player, this.getX(), this.getY(), this.getZ(), SoundEvents.ITEM_FLINTANDSTEEL_USE, this.getSoundCategory(), 1.0F, this.random.nextFloat() * 0.4F + 0.8F);
            if (!this.world.isClient) {
                this.ignite();
                itemStack.damage(1, player, (playerx) -> {
                    playerx.sendToolBreakStatus(hand);
                });
            }

            return ActionResult.success(this.world.isClient);
        } else {
            return super.interactMob(player, hand);
        }
    }

    public void tick() {
        if (this.isAlive()) {
            this.lastFuseTime = this.currentFuseTime;
            if (this.isIgnited()) {
                this.setFuseSpeed(1);
            }

            int i = this.getFuseSpeed();
            if (i > 0 && this.currentFuseTime == 0) {
                this.playSound(SoundEvents.ENTITY_ILLUSIONER_DEATH, 1.0F, 1.0F);
                this.emitGameEvent(GameEvent.PRIME_FUSE);
            }

            this.currentFuseTime += i;
            if (this.currentFuseTime < 0) {
                this.currentFuseTime = 0;
            }

            if (this.currentFuseTime >= this.fuseTime) {
                this.currentFuseTime = this.fuseTime;
                this.jump();
                this.explode();

            }
        }

        super.tick();
    }
//jump then land on player
public void jump() {
    this.setVelocity(this.getVelocity().add(0.0D, 0.7D, 0.0D));
}
    public void playSound(SoundEvent sound) {
        this.world.playSound(null, this.getX(), this.getY(), this.getZ(), sound, this.getSoundCategory(), 1.0F, 1.0F);
    }
    public void landOnPlayer(PlayerEntity player) {
        this.setPosition(player.getX(), player.getY(), player.getZ());
    }



    private void explode() {
        if (!this.world.isClient) {
            Explosion.DestructionType destructionType = this.world.getGameRules().getBoolean(GameRules.DO_MOB_GRIEFING) ? Explosion.DestructionType.DESTROY : Explosion.DestructionType.NONE;
            this.dead = true;
            this.world.createExplosion(this, this.getX(), this.getY(), this.getZ(), 13, false, destructionType);
            this.discard();
            this.spawnEffectsCloud();
        }

    }

    private void spawnEffectsCloud() {
        Collection<StatusEffectInstance> collection = this.getStatusEffects();
        if (!collection.isEmpty()) {
            AreaEffectCloudEntity areaEffectCloudEntity = new AreaEffectCloudEntity(this.world, this.getX(), this.getY(), this.getZ());
            areaEffectCloudEntity.setRadius(0.0F);
            areaEffectCloudEntity.setRadiusOnUse(-0.5F);
            areaEffectCloudEntity.setWaitTime(1);
            areaEffectCloudEntity.setDuration(areaEffectCloudEntity.getDuration() / 2);
            areaEffectCloudEntity.setRadiusGrowth(-areaEffectCloudEntity.getRadius() / (float) areaEffectCloudEntity.getDuration());
            Iterator var3 = collection.iterator();

            while (var3.hasNext()) {
                StatusEffectInstance statusEffectInstance = (StatusEffectInstance) var3.next();
                areaEffectCloudEntity.addEffect(new StatusEffectInstance(statusEffectInstance));
            }

            this.world.spawnEntity(areaEffectCloudEntity);
        }

    }
}
