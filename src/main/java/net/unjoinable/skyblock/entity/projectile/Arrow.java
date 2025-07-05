package net.unjoinable.skyblock.entity.projectile;

import net.kyori.adventure.sound.Sound;
import net.minestom.server.MinecraftServer;
import net.minestom.server.ServerFlag;
import net.minestom.server.collision.*;
import net.minestom.server.coordinate.Point;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.coordinate.Vec;
import net.minestom.server.entity.Entity;
import net.minestom.server.entity.EntityType;
import net.minestom.server.entity.metadata.projectile.ProjectileMeta;
import net.minestom.server.instance.Chunk;
import net.minestom.server.instance.Instance;
import net.minestom.server.instance.block.Block;
import net.minestom.server.sound.SoundEvent;
import net.minestom.server.timer.TaskSchedule;
import net.minestom.server.utils.chunk.ChunkCache;
import net.minestom.server.utils.chunk.ChunkUtils;
import net.unjoinable.skyblock.entity.SkyblockEntity;
import net.unjoinable.skyblock.player.SkyblockPlayer;
import org.jspecify.annotations.Nullable;

import java.util.concurrent.ThreadLocalRandom;

import static net.minestom.server.collision.CollisionUtils.handlePhysics;

/**
 * A projectile arrow entity with realistic physics and collision detection.
 *
 * <p>This arrow handles trajectory calculation, spread mechanics, entity/block collisions,
 * and proper physics simulation including gravity, air resistance, and velocity damping.
 */
public final class Arrow extends Entity {
    
    // Physics constants
    private static final BoundingBox SMALL_BOUNDING_BOX = new BoundingBox(0.01, 0.01, 0.01);
    private static final BoundingBox COLLISION_BOUNDING_BOX = EntityType.ARROW.registry().boundingBox();
    private static final double SPREAD_MULTIPLIER = 0.0075D;
    private static final double TRAJECTORY_ADJUSTMENT = 0.2D;
    private static final double VELOCITY_DAMPING = 0.99;
    private static final double PITCH_ADJUSTMENT_OFFSET = 35f;
    private static final double PITCH_ADJUSTMENT_MULTIPLIER = 0.00214D;
    private static final double VERTICAL_VELOCITY_MULTIPLIER = 0.9;
    private static final int REMOVAL_DELAY_TICKS = 10;

    private final Entity shooter;
    private @Nullable PhysicsResult previousPhysicsResult;
    private @Nullable Pos lastKnownPosition;
    private boolean inBlock = false;
    private boolean firstTick = true;

    /**
     * Creates a new arrow projectile shot by the specified entity.
     *
     * @param shooter the entity that shot this arrow
     */
    public Arrow(Entity shooter) {
        super(EntityType.ARROW);
        this.shooter = shooter;
        this.collidesWithEntities = false;
        setBoundingBox(SMALL_BOUNDING_BOX);
        
        if (getEntityMeta() instanceof ProjectileMeta meta) {
            meta.setShooter(shooter);
        }
    }

    @Override
    public void tick(long time) {
        if (removed || inBlock) {
            return;
        }
        
        if (instance != null && ChunkUtils.isLoaded(currentChunk)) {
            previousPosition = position;
            movementTick();
            super.update(time);
        }
        
        updateOrientation();
        
        if (!handleEntityCollisions()) {
            handleBlockCollisions();
        }
    }

    /**
     * Updates the arrow's orientation based on its movement direction.
     */
    private void updateOrientation() {
        if (previousPosition == null) {
            return;
        }
        
        Vec movement = Vec.fromPoint(getPosition().sub(previousPosition));
        if (movement.isZero()) {
            return;
        }
        
        float yaw = (float) Math.toDegrees(Math.atan2(movement.x(), movement.z()));
        float pitch = (float) Math.toDegrees(Math.atan2(movement.y(), 
            Math.sqrt(movement.x() * movement.x() + movement.z() * movement.z())));
        
        position = getPosition().withView(yaw, pitch);
        
        if (firstTick) {
            firstTick = false;
            setView(yaw, pitch);
        }
    }

    @Override
    protected void movementTick() {
        gravityTickCount = onGround ? 0 : gravityTickCount + 1;
        
        if (vehicle != null) {
            return;
        }
        
        lastKnownPosition = position;
        PhysicsResult result = computePhysics();
        
        Chunk finalChunk = ChunkUtils.retrieve(instance, currentChunk, result.newPosition());
        if (!ChunkUtils.isLoaded(finalChunk)) {
            return;
        }
        
        onGround = result.isOnGround();
        
        if (!result.hasCollision()) {
            velocity = previousPhysicsResult.newVelocity()
                .mul(ServerFlag.SERVER_TICKS_PER_SECOND)
                .mul(VELOCITY_DAMPING);
        }
        
        refreshPosition(result.newPosition(), true, false);
        
        if (hasVelocity()) {
            sendPacketToViewers(getVelocityPacket());
        }
    }

    /**
     * Computes physics simulation for this tick.
     *
     * @return the physics result containing new position and collision data
     */
    private PhysicsResult computePhysics() {
        Block.Getter blockGetter = new ChunkCache(instance, currentChunk);
        Vec currentVelocity = velocity.div(ServerFlag.SERVER_TICKS_PER_SECOND);
        Vec newVelocity = updateVelocity(position, currentVelocity, blockGetter, getAerodynamics(), onGround);
        
        previousPhysicsResult = handlePhysics(blockGetter, boundingBox, position, 
            newVelocity, previousPhysicsResult, true);
        return previousPhysicsResult;
    }

    /**
     * Handles collisions with entities.
     *
     * @return true if a collision occurred and was processed
     */
    private boolean handleEntityCollisions() {
        if (previousPhysicsResult == null) {
            return false;
        }
        
        Vec movement = previousPhysicsResult.newPosition().sub(previousPosition).asVec();
        var collidedEntities = CollisionUtils.checkEntityCollisions(
            instance, COLLISION_BOUNDING_BOX, previousPosition, movement, movement.length(),
            entity -> entity != shooter && entity != this, previousPhysicsResult
        );
        
        return collidedEntities.stream().anyMatch(this::processEntityCollision);
    }

    /**
     * Processes a collision with an entity.
     *
     * @param collision the collision result
     * @return true if the collision was processed successfully
     */
    private boolean processEntityCollision(EntityCollisionResult collision) {
        if (shooter instanceof SkyblockPlayer player && collision.entity() instanceof SkyblockEntity entity) {
            player.playSound(Sound.sound(SoundEvent.ENTITY_ARROW_HIT_PLAYER, Sound.Source.PLAYER, 1f, 1f), player);
            entity.damage(player.getCombatSystem().attack(entity));
            remove();
            return true;
        }
        return false;
    }

    /**
     * Handles collisions with blocks by embedding the arrow.
     */
    private void handleBlockCollisions() {
        if (previousPhysicsResult == null || !previousPhysicsResult.hasCollision()) {
            return;
        }
        
        Point hitPoint = findCollisionPoint();
        if (hitPoint != null) {
            velocity = Vec.ZERO;
            setNoGravity(true);
            inBlock = true;
            
            position = new Pos(hitPoint.x(), hitPoint.y(), hitPoint.z(), 
                lastKnownPosition.yaw(), lastKnownPosition.pitch());
            
            MinecraftServer.getSchedulerManager().scheduleNextTick(this::synchronizePosition);
            MinecraftServer.getSchedulerManager()
                .buildTask(this::remove)
                .delay(TaskSchedule.tick(REMOVAL_DELAY_TICKS))
                .schedule();
        }
    }

    /**
     * Finds the collision point from physics results.
     *
     * @return the collision point, or null if none found
     */
    private @Nullable Point findCollisionPoint() {
        Point[] collisionPoints = previousPhysicsResult.collisionPoints();
        Shape[] collisionShapes = previousPhysicsResult.collisionShapes();
        
        for (int i = 0; i < collisionShapes.length; i++) {
            if (collisionShapes[i] instanceof ShapeImpl && collisionPoints[i] != null) {
                return collisionPoints[i];
            }
        }
        return null;
    }

    /**
     * Shoots the arrow from a point in the shooter's facing direction.
     *
     * @param from the starting position
     * @param power the shooting power multiplier
     * @param spread the accuracy spread (higher = less accurate)
     */
    public void shoot(Point from, double power, double spread) {
        shoot(from, from.add(shooter.getPosition().direction()), power, spread);
    }

    /**
     * Shoots the arrow from one point toward another.
     *
     * @param from the starting position
     * @param to the target position
     * @param power the shooting power multiplier
     * @param spread the accuracy spread (higher = less accurate)
     */
    public void shoot(Point from, Point to, double power, double spread) {
        Instance shooterInstance = shooter.getInstance();
        if (shooterInstance == null) {
            return;
        }

        if (shooter instanceof SkyblockPlayer player) {
            shooterInstance.playSound(Sound.sound(SoundEvent.ENTITY_ARROW_SHOOT, Sound.Source.PLAYER, 1f, 1f), player);
        }
        
        Vec trajectory = calculateTrajectory(from, to, power, spread);
        float yaw = -shooter.getPosition().yaw();
        float pitch = -shooter.getPosition().pitch();
        
        setInstance(shooterInstance, new Pos(from.x(), from.y(), from.z(), yaw, pitch))
            .whenComplete((_, throwable) -> {
                if (throwable != null) {
                    throwable.printStackTrace();
                } else {
                    synchronizePosition();
                    setVelocity(trajectory);
                }
            });
    }

    /**
     * Calculates the trajectory vector for the arrow.
     *
     * @param from starting position
     * @param to target position
     * @param power shooting power
     * @param spread accuracy spread
     * @return the calculated trajectory vector
     */
    private Vec calculateTrajectory(Point from, Point to, double power, double spread) {
        double[] direction = calculateDirection(from, to);
        applySpread(direction, spread);
        return new Vec(direction[0], direction[1], direction[2])
            .mul(ServerFlag.SERVER_TICKS_PER_SECOND * power);
    }

    /**
     * Calculates the normalized direction vector from source to target.
     *
     * @param from starting position
     * @param to target position
     * @return normalized direction array [x, y, z]
     */
    private double[] calculateDirection(Point from, Point to) {
        float adjustedPitch = -shooter.getPosition().pitch() - (float) PITCH_ADJUSTMENT_OFFSET;
        double pitchAdjustment = calculatePitchAdjustment(adjustedPitch);
        
        double dx = to.x() - from.x();
        double dy = to.y() - from.y() + pitchAdjustment;
        double dz = to.z() - from.z();
        
        if (!hasNoGravity()) {
            dy += Math.sqrt(dx * dx + dz * dz) * TRAJECTORY_ADJUSTMENT;
        }
        
        double length = Math.sqrt(dx * dx + dy * dy + dz * dz);
        return new double[]{
            dx / length, 
            (dy / length) * VERTICAL_VELOCITY_MULTIPLIER, 
            dz / length
        };
    }

    /**
     * Calculates pitch adjustment for trajectory compensation.
     *
     * @param pitch the adjusted pitch angle
     * @return the pitch adjustment value
     */
    private double calculatePitchAdjustment(float pitch) {
        double pitchDiff = pitch - 45;
        return (pitchDiff == 0 ? 0.0001 : pitchDiff) * PITCH_ADJUSTMENT_MULTIPLIER;
    }

    /**
     * Applies random spread to the direction vector for accuracy simulation.
     *
     * @param direction the direction array to modify
     * @param spread the spread amount
     */
    private void applySpread(double[] direction, double spread) {
        var random = ThreadLocalRandom.current();
        double spreadFactor = spread * SPREAD_MULTIPLIER;
        
        direction[0] += random.nextGaussian() * spreadFactor;
        direction[1] += random.nextGaussian() * spreadFactor;
        direction[2] += random.nextGaussian() * spreadFactor;
    }

    /**
     * Updates velocity based on physics simulation including drag and gravity.
     *
     * @param entityPosition current position
     * @param currentVelocity current velocity vector
     * @param blockGetter block getter for friction calculation
     * @param aerodynamics aerodynamics properties
     * @param entityOnGround whether the entity is on ground
     * @return updated velocity vector
     */
    private Vec updateVelocity(Pos entityPosition, Vec currentVelocity, Block.Getter blockGetter, 
                              Aerodynamics aerodynamics, boolean entityOnGround) {
        double drag = entityOnGround 
            ? blockGetter.getBlock(entityPosition.sub(0.0, 0.5000001, 0.0)).registry().friction() 
                * aerodynamics.horizontalAirResistance()
            : aerodynamics.horizontalAirResistance();
        
        double x = currentVelocity.x() * drag;
        double y = (currentVelocity.y() - aerodynamics.gravity()) * aerodynamics.verticalAirResistance();
        double z = currentVelocity.z() * drag;
        
        return new Vec(
            Math.abs(x) < 1.0E-6 ? 0.0 : x,
            Math.abs(y) < 1.0E-6 ? 0.0 : y,
            Math.abs(z) < 1.0E-6 ? 0.0 : z
        );
    }
}