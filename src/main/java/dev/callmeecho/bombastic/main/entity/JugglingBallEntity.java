package dev.callmeecho.bombastic.main.entity;

import dev.callmeecho.bombastic.main.registry.BombasticEnchantmentComponentTypeRegistrar;
import dev.callmeecho.bombastic.main.registry.BombasticEntityTypeRegistrar;
import dev.callmeecho.bombastic.main.registry.BombasticItemRegistrar;
import net.minecraft.block.BlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.*;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;
import org.apache.commons.lang3.mutable.MutableFloat;

public class JugglingBallEntity extends PersistentProjectileEntity implements FlyingItemEntity {
    private static final TrackedData<Boolean> RETURNING = DataTracker.registerData(JugglingBallEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    private static final TrackedData<Byte> RETURN_SLOT = DataTracker.registerData(JugglingBallEntity.class, TrackedDataHandlerRegistry.BYTE);
    private static final TrackedData<Byte> BOUNCES = DataTracker.registerData(JugglingBallEntity.class, TrackedDataHandlerRegistry.BYTE);
    private static final TrackedData<Boolean> ENCHANTED = DataTracker.registerData(JugglingBallEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    private ItemStack stack = ItemStack.EMPTY;

    public JugglingBallEntity(EntityType<? extends PersistentProjectileEntity> entityType, World world) {
        super(BombasticEntityTypeRegistrar.JUGGLING_BALL, world);
    }

    @Override
    protected SoundEvent getHitSound() {
        return SoundEvents.BLOCK_SLIME_BLOCK_FALL;
    }

    public static JugglingBallEntity create(World world, PlayerEntity player, ItemStack stack, int returnSlot) {
        JugglingBallEntity ball = new JugglingBallEntity(BombasticEntityTypeRegistrar.JUGGLING_BALL, world);

        ball.setOwner(player);
        ball.setPos(player.getX(), player.getEyeY() - 0.1, player.getZ());
        ball.setStack(stack);
        ball.dataTracker.set(RETURN_SLOT, (byte) returnSlot);
        ball.dataTracker.set(ENCHANTED, stack.hasGlint());

        MutableFloat bounces = new MutableFloat(0.0F);
        EnchantmentHelper.forEachEnchantment(
                stack,
                (enchantment, level) -> enchantment.value().modifyValue(
                        BombasticEnchantmentComponentTypeRegistrar.JUGGLING_BALL_BOUNCE,
                        player.getRandom(),
                        level,
                        bounces
                ));

        ball.dataTracker.set(BOUNCES, (byte) (2 + bounces.intValue()));

        EnchantmentHelper.onProjectileSpawned((ServerWorld) world, stack, ball, item -> ball.stack = null);
        return ball;
    }

    @Override
    public boolean isImmuneToExplosion(Explosion explosion) {
        return true;
    }

    @Override
    public void onPlayerCollision(PlayerEntity player) {
        if (!this.dataTracker.get(RETURNING)) return;
        if ((!this.isOwner(player) && this.getOwner() != null) || !this.tryPickup(player)) return;

        player.sendPickup(this, 1);
        this.discard();
    }

    @Override
    protected void onEntityHit(EntityHitResult entityHitResult) {
        if (this.getWorld().isClient) return;
        if (entityHitResult.getEntity() == this.getOwner()) return;

        if (!(entityHitResult.getEntity() instanceof LivingEntity livingEntity)) return;
        if (this.dataTracker.get(RETURNING)) return;
        livingEntity.damage(
                getWorld().getDamageSources().thrown(this, this.getOwner()),
                (float) (this.getDamage() * this.getVelocity().length())
        );
        EnchantmentHelper.onTargetDamaged((ServerWorld) getWorld(), livingEntity, getWorld().getDamageSources().thrown(this, this.getOwner()), this.getStack());

        bounce(entityHitResult.getPos());
    }

    private void bounce(Vec3d pos) {
        this.dataTracker.set(BOUNCES, (byte) (this.dataTracker.get(BOUNCES) - 1));
        Vec3d normal = this.getPos().subtract(pos).multiply(2.0).normalize();
        this.setVelocity(normal.getX(), normal.getY(), normal.getZ());

        if (this.dataTracker.get(BOUNCES) <= 0)
            this.dataTracker.set(RETURNING, true);
    }

    @Override
    protected void initDataTracker(DataTracker.Builder builder) {
        super.initDataTracker(builder);
        builder.add(RETURNING, false);
        builder.add(RETURN_SLOT, (byte) 0);
        builder.add(BOUNCES, (byte) 0);
        builder.add(ENCHANTED, false);
    }

    public boolean getEnchanted() { return this.dataTracker.get(ENCHANTED); }

    @Override
    protected boolean canHit(Entity entity) {
        return super.canHit(entity) && entity != this.getOwner() && !(entity instanceof JugglingBallEntity);
    }

    @Override
    public void tick() {
        HitResult hitResult = ProjectileUtil.getCollision(this, this::canHit);
        if (hitResult.getType() != HitResult.Type.MISS) this.hitOrDeflect(hitResult);

        this.checkBlockCollision();
        Vec3d velocity = this.getVelocity();
        double x = this.getX() + velocity.x;
        double y = this.getY() + velocity.y;
        double z = this.getZ() + velocity.z;

        this.updateRotation();
        if (this.isTouchingWater())
            for (int i = 0; i < 4; ++i)
                this.getWorld().addParticle(ParticleTypes.BUBBLE, x - velocity.x * 0.25, y - velocity.y * 0.25, z - velocity.z * 0.25, velocity.x, velocity.y, velocity.z);

        this.setVelocity(velocity.multiply(0.99F));
        this.applyGravity();
        this.setPosition(x, y, z);
        
        if (age > 300 && !this.dataTracker.get(RETURNING)) this.dataTracker.set(RETURNING, true);
        if (dataTracker.get(RETURNING)) {
            if (this.getOwner() == null) {
                this.getWorld().spawnEntity(new ItemEntity(this.getWorld(), this.getX(), this.getY(), this.getZ(), this.getStack()));
                discard();
                return;
            }
            Vec3d location = this.getOwner().getEyePos().subtract(this.getPos());
            this.setVelocity(location.normalize());
            if (this.squaredDistanceTo(this.getOwner()) <= 0.0625F)
                this.setVelocity(this.getVelocity().multiply(0.5F));
        }
    }

    @Override
    public ItemStack getWeaponStack() {
        return this.getItemStack();
    }

    @Override
    protected void onBlockHit(BlockHitResult blockHitResult) {
        BlockState blockState = this.getWorld().getBlockState(blockHitResult.getBlockPos());
        blockState.onProjectileHit(this.getWorld(), blockState, blockHitResult, this);

        if (this.getWorld().isClient) return;
        if (this.getItemStack() != null) {
            EnchantmentHelper.onHitBlock(
                    (ServerWorld) this.getWorld(),
                    this.getItemStack(),
                    this.getOwner() instanceof LivingEntity livingEntity ? livingEntity : null,
                    this,
                    null,
                    blockHitResult.getBlockPos().clampToWithin(blockHitResult.getPos()),
                    this.getWorld().getBlockState(blockHitResult.getBlockPos()),
                    item -> {}
            );
        }

        if (this.dataTracker.get(RETURNING)) return;




        getWorld().playSound(
                null,
                blockHitResult.getBlockPos(),
                getHitSound(),
                SoundCategory.BLOCKS,
                1.0F,
                1.0F
        );

        ((ServerWorld)getWorld()).spawnParticles(
                ParticleTypes.ITEM_SLIME,
                blockHitResult.getPos().getX(),
                blockHitResult.getPos().getY(),
                blockHitResult.getPos().getZ(),
                10,
                0.1,
                0.1,
                0.1,
                1.0
        );

        bounce(blockHitResult.getPos());
    }

    @Override
    protected ItemStack getDefaultItemStack() {
        return new ItemStack(BombasticItemRegistrar.JUGGLING_BALL);
    }

    @Override
    protected double getGravity() { return 0.05F; }

    @Override
    public ItemStack getStack() { return !stack.isEmpty() ? stack.copy() : new ItemStack(BombasticItemRegistrar.JUGGLING_BALL); }
}
