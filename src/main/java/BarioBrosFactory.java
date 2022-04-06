import com.almasb.fxgl.entity.*;
import com.almasb.fxgl.entity.components.CollidableComponent;
import com.almasb.fxgl.physics.BoundingShape;
import com.almasb.fxgl.physics.HitBox;
import com.almasb.fxgl.physics.PhysicsComponent;
import com.almasb.fxgl.physics.box2d.dynamics.BodyType;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

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

    @Spawns("questionmark")
    public Entity newQuestionBlock(SpawnData data) {
        return entityBuilder(data)
                .bbox(new HitBox(BoundingShape.box(data.<Integer>get("width"), data.<Integer>get("height"))))
                .with(new PhysicsComponent())
                .build();
    }

    @Spawns("flag")
    public Entity newFinishFlag(SpawnData data) {
        return entityBuilder(data)
                .bbox(new HitBox(BoundingShape.box(data.<Integer>get("width"), data.<Integer>get("height"))))
                .with(new PhysicsComponent())
                .build();
    }

    @Spawns("player")
    public Entity newPlayer(SpawnData data) {
        PhysicsComponent physicsComponent = new PhysicsComponent();
        physicsComponent.setBodyType(BodyType.DYNAMIC);

        return entityBuilder(data)
                .viewWithBBox(new Rectangle(16,32, Color.RED))
                .with(physicsComponent)
                .with(new PlayerControl())
                .type(EntityType.PLAYER)
                .build();
    }

}
