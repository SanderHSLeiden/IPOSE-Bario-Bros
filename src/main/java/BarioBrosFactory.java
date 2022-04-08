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

    @Spawns("flag")
    public Entity newFinishFlag(SpawnData data) {
        return entityBuilder(data)
                .type(EntityType.FLAG)
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
