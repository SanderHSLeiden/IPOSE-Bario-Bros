import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.*;
import com.almasb.fxgl.entity.components.CollidableComponent;
import com.almasb.fxgl.physics.BoundingShape;
import com.almasb.fxgl.physics.HitBox;
import com.almasb.fxgl.physics.PhysicsComponent;
import com.almasb.fxgl.physics.SensorCollisionHandler;
import com.almasb.fxgl.physics.box2d.dynamics.BodyType;
import com.almasb.fxgl.physics.box2d.dynamics.FixtureDef;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

import java.awt.*;

import static com.almasb.fxgl.dsl.FXGLForKtKt.entityBuilder;

public class BarioBrosFactory implements EntityFactory {

    @Spawns("platform")
    public Entity newPlatform(SpawnData data) {
        return entityBuilder(data)
                .bbox(new HitBox(BoundingShape.box(data.<Integer>get("width"), data.<Integer>get("height"))))
                .with(new PhysicsComponent())
                .type(EntityType.PLATFORM)
                .build();
    }

    @Spawns("wall")
    public Entity newWall(SpawnData data) {
        return entityBuilder(data)
                .bbox(new HitBox(BoundingShape.box(data.<Integer>get("width"), data.<Integer>get("height"))))
                .with(new PhysicsComponent())
                .type(EntityType.WALL)
                .build();
    }

    @Spawns("questionmark")
    public Entity newQuestionBlock(SpawnData data) {
        return entityBuilder(data)
                .type(EntityType.QUESTIONMARK)
                .bbox(new HitBox(BoundingShape.box(data.<Integer>get("width"), data.<Integer>get("height"))))
                .with(new PhysicsComponent())
                .type(EntityType.QUESTIONMARK)
                .build();
    }

    @Spawns("unusedquestionmark")
    public Entity newUnusedQuestionBlock(SpawnData data) {
        return entityBuilder(data)
                .bbox(new HitBox(BoundingShape.box(data.<Integer>get("width"), data.<Integer>get("height"))))
                .with(new PhysicsComponent())
                .with(new CollidableComponent(true))
                .type(EntityType.UNUSEDQUESTIONMARK)
                .build();
    }

    @Spawns("unusedpowerquestionmark")
    public Entity newUnusedPowerQuestionBlock(SpawnData data) {
        return entityBuilder(data)
                .bbox(new HitBox(BoundingShape.box(data.<Integer>get("width"), data.<Integer>get("height"))))
                .with(new PhysicsComponent())
                .with(new CollidableComponent(true))
                .type(EntityType.UNUSEDPOWERQUESTIONMARK)
                .build();
    }

    @Spawns("star")
    public Entity newStar(SpawnData data) {
        return entityBuilder(data)
                //.bbox(new HitBox(BoundingShape.box(data.<Integer>get("width"), data.<Integer>get("height"))))
                .viewWithBBox("star.png")
                .with(new CollidableComponent(true))
                .type(EntityType.STAR)
                .build();
    }

    @Spawns("flower")
    public Entity newFlower(SpawnData data) {
        return entityBuilder(data)
                //.bbox(new HitBox(BoundingShape.box(data.<Integer>get("width"), data.<Integer>get("height"))))
                .viewWithBBox("flower.png")
                .with(new CollidableComponent(true))
                .type(EntityType.FLOWER)
                .build();
    }

    @Spawns("flameOrb")
    public Entity newFlameOrb(SpawnData data) {
        PhysicsComponent physicsComponent = new PhysicsComponent();
        physicsComponent.setBodyType(BodyType.DYNAMIC);
        physicsComponent.addGroundSensor(new HitBox("GROUND_SENSOR", new Point2D(0, 1), BoundingShape.circle(2)));

        return entityBuilder(data)
                .viewWithBBox(new Circle(2, Color.RED))
                .with(physicsComponent)
                .with(new OrbControl())
                .with(new CollidableComponent(true))
                .type(EntityType.FLAMEORB)
                .build();
    }

    @Spawns("flag")
    public Entity newFinishFlag(SpawnData data) {
        return entityBuilder(data)
                .bbox(new HitBox(BoundingShape.box(data.<Integer>get("width"), data.<Integer>get("height"))))
                .with(new CollidableComponent(true))
                .type(EntityType.FLAG)
                .build();
    }

    @Spawns("coin")
    public Entity newCoin(SpawnData data) {
        return entityBuilder(data)
                .bbox(new HitBox(BoundingShape.box(data.<Integer>get("width"), data.<Integer>get("height"))))
                .with(new CollidableComponent(true))
                .type(EntityType.COIN)
                .build();
    }

    @Spawns("enemy")
    public Entity newEnemy(SpawnData data) {
        PhysicsComponent physicsComponent = new PhysicsComponent();
        physicsComponent.setBodyType(BodyType.DYNAMIC);
        physicsComponent.addGroundSensor(new HitBox("GROUND_SENSOR", new Point2D(1, 32), BoundingShape.box(30, 1)));

        return entityBuilder(data)
                .bbox(new HitBox(BoundingShape.box(32, 32)))
                .with(physicsComponent)
                .with(new EnemyControl())
                .with(new CollidableComponent(true))
                .type(EntityType.ENEMY)
                .build();
    }

    @Spawns("player")
    public Entity newPlayer(SpawnData data) {
        PhysicsComponent physicsComponent = new PhysicsComponent();
        physicsComponent.setBodyType(BodyType.DYNAMIC);
        physicsComponent.addGroundSensor(new HitBox("GROUND_SENSOR", new Point2D(1, 32), BoundingShape.box(14, 1)));

        // this avoids player sticking to walls
        physicsComponent.setFixtureDef(new FixtureDef().friction(0.0f));

        return entityBuilder(data)
                .bbox(new HitBox(BoundingShape.box(16, 32)))
                .with(physicsComponent)
                .with(new PlayerControl())
                .with(new CollidableComponent(true))
                .type(EntityType.PLAYER)
                .build();
    }

}
