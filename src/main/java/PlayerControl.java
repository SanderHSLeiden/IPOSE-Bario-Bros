import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.physics.PhysicsComponent;

public class PlayerControl extends Component {
    private PhysicsComponent physics;

    public void onUpdate(Entity entity, double tpf) {

    }

    public void left() {
        physics.setVelocityX(-150);
    }

    public void right() {
        physics.setVelocityX(150);
    }

    public void jump() {
        physics.setVelocityY(-400);
    }

    public void stop() {
        physics.setVelocityX(0);
    }
}