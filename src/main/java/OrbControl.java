import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.physics.PhysicsComponent;
import com.sun.javafx.util.Logging;
import javafx.geometry.Point2D;

import java.util.List;

public class OrbControl extends Component {
    private PhysicsComponent physics;
    private boolean hasBounced = false;

    public OrbControl() {}

    @Override
    public void onAdded() {
        entity.getTransformComponent().setScaleOrigin(new Point2D(16, 32));

        Entity player = FXGL.getGameWorld().getEntitiesByType(EntityType.PLAYER).get(0);
        physics.setOnPhysicsInitialized( () -> {
            physics.setVelocityX(180);
        });

        physics.onGroundProperty().addListener((obs, old, isOnGround) -> {
            if(hasBounced && isOnGround) {
                this.getEntity().removeFromWorld();
            }
            else {
                physics.setVelocityY(-160);
                hasBounced = true;
            }
        });
    }
}
