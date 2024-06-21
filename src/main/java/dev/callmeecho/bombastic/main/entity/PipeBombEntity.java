package dev.callmeecho.bombastic.main.entity;

import dev.callmeecho.bombastic.main.registry.BombasticDataComponentTypeRegistrar;
import dev.callmeecho.bombastic.main.registry.BombasticEntityTypeRegistrar;
import dev.callmeecho.bombastic.main.registry.BombasticItemRegistrar;
import dev.callmeecho.bombastic.main.registry.BombasticParticleRegistrar;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.FlyingItemEntity;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.FireworkRocketEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.entity.projectile.thrown.PotionEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.minecraft.world.WorldEvents;

import java.util.List;

public class PipeBombEntity extends PersistentProjectileEntity implements FlyingItemEntity {
    private static final TrackedData<Integer> FUSE = DataTracker.registerData(PipeBombEntity.class, TrackedDataHandlerRegistry.INTEGER);

    private List<ItemStack> triggers;

    public PipeBombEntity(EntityType<PipeBombEntity> entityType, World world) {
        super(entityType, world);
    }

    public static PipeBombEntity create(World world, ItemStack itemStack, PlayerEntity player) {
        PipeBombEntity pipeBomb = new PipeBombEntity(BombasticEntityTypeRegistrar.PIPE_BOMB, world);

        pipeBomb.setOwner(player);
        pipeBomb.setPos(player.getX(), player.getEyeY() - 0.1, player.getZ());

        pipeBomb.triggers = itemStack.get(BombasticDataComponentTypeRegistrar.TRIGGERS);

        return pipeBomb;
    }

    @Override
    protected void initDataTracker(DataTracker.Builder builder) {
        super.initDataTracker(builder);

        builder.add(FUSE, 50);
    }

    @Override
    public void tick() {
        super.tick();

        this.setFuse(this.getFuse() - 1);

        if (getFuse() % 5 == 0 && !this.getWorld().isClient) {
            ((ServerWorld) this.getWorld()).spawnParticles(
                    ParticleTypes.SMOKE,
                    this.getX(),
                    this.getY(),
                    this.getZ(),
                    5,
                    0.1,
                    0.1,
                    0.1,
                    0.0
            );

            this.playSound(
                    SoundEvents.BLOCK_METAL_PRESSURE_PLATE_CLICK_ON,
                    1.0F,
                    1.0F + (Math.abs(((float) (this.getFuse() - 50) / 50)))
            );
        }

        if (this.getFuse() <= 0) {
            if (!this.getWorld().isClient) {
                for (int i = 0; i < 20; i++) {
                    ((ServerWorld)getWorld()).spawnParticles(
                            ParticleTypes.FLAME,
                            this.getX(),
                            this.getY(),
                            this.getZ(),
                            20,
                            random.nextGaussian() / 7.5F,
                            Math.abs(random.nextGaussian() / 7.5F),
                            random.nextGaussian() / 7.5F,
                            random.nextGaussian() / 5F
                    );
                }
            }

            this.explode();
            this.discard();
        }
    }

    @Override
    protected SoundEvent getHitSound() { return SoundEvents.INTENTIONALLY_EMPTY; }

    public void explode() {
        if (this.getWorld().isClient) return;

        getWorld().syncWorldEvent(WorldEvents.SMASH_ATTACK, this.getSteppingPos(), 750);
        if (this.triggers == null) return;
        for (ItemStack trigger : triggers) {
            if (trigger.isOf(Items.TNT)) {
                this.getWorld().createExplosion(
                        this,
                        this.getX(),
                        this.getBodyY(0.0625),
                        this.getZ(),
                        4.0F,
                        World.ExplosionSourceType.TRIGGER
                );
            }
            else if (trigger.isOf(BombasticItemRegistrar.PARTY_POPPER)) {
                for (int i = 0; i < 50; i++) {
                    ((ServerWorld)getWorld()).spawnParticles(
                            BombasticParticleRegistrar.CONFETTI,
                            this.getX(),
                            this.getY(),
                            this.getZ(),
                            20,
                            random.nextGaussian() / 7.5F,
                            Math.abs(random.nextGaussian() / 7.5F),
                            random.nextGaussian() / 7.5F,
                            random.nextFloat() / 5F
                    );
                }
            } else if (trigger.isOf(Items.LINGERING_POTION)) {
                PotionEntity potionEntity = new PotionEntity(EntityType.POTION, getWorld());
                potionEntity.setItem(trigger);
                potionEntity.setPos(this.getX(), this.getY(), this.getZ());
                getWorld().spawnEntity(potionEntity);
                potionEntity.onCollision(new BlockHitResult(this.getPos(), Direction.UP, this.getBlockPos(), false));
            } else if (trigger.isOf(Items.FIREWORK_ROCKET)) {
                FireworkRocketEntity fireworkRocket = new FireworkRocketEntity(
                        this.getWorld(),
                        this.getX(),
                        this.getY(),
                        this.getZ(),
                        trigger
                );

                fireworkRocket.setOwner(this.getOwner());

                this.getWorld().spawnEntity(fireworkRocket);
                fireworkRocket.explodeAndRemove();
            }
        }
    }

    public void setFuse(int fuse) { this.dataTracker.set(FUSE, fuse); }
    public int getFuse() { return this.dataTracker.get(FUSE); }

    @Override
    protected ItemStack getDefaultItemStack() {
        ItemStack item = new ItemStack(BombasticItemRegistrar.PIPE_BOMB);
        item.set(BombasticDataComponentTypeRegistrar.PINNED, false);
        return item;
    }
    @Override
    public ItemStack getStack() { return getDefaultItemStack(); }

    @Override
    public void onPlayerCollision(PlayerEntity playerEntity) { }
}
