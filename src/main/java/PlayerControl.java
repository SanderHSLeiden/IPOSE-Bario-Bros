import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.physics.PhysicsComponent;

public class PlayerControl extends Component {
    private PhysicsComponent physics;
    private Boolean canJump = false;

    @Override
    public void onAdded() {
        physics.onGroundProperty().addListener((obs, old, isOnGround) -> {
            canJump = isOnGround;
        });
    }

    public void left() {
        physics.setVelocityX(-150);
    }

    public void right() {
        physics.setVelocityX(150);
    }

    public void jump() {
        if (!canJump) return;

        physics.setVelocityY(-300);
    }

    public void stop() {
        physics.setVelocityX(0);
    }
}