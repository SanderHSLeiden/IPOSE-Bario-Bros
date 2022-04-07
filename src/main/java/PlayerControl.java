import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.physics.PhysicsComponent;

public class PlayerControl extends Component {
    private PhysicsComponent physics;
    private boolean decreaseJumpTimer = false;
    private int timeToJumpLeft = 8;

    @Override
    public void onAdded() {
        physics.onGroundProperty().addListener((obs, old, isOnGround) -> {
            decreaseJumpTimer = !isOnGround;

            if (isOnGround) {
                timeToJumpLeft = 8;
            }
        });
    }

    @Override
    public void onUpdate(double tpf) {
        if (decreaseJumpTimer) {
            if (timeToJumpLeft > 0) {
                timeToJumpLeft--;
            }
        }
    }

    public void left() {
        physics.setVelocityX(-150);
    }

    public void right() {
        physics.setVelocityX(150);
    }

    public void jump() {
        if (timeToJumpLeft == 0) return;

        physics.setVelocityY(-300);
        timeToJumpLeft = 0;
    }

    public void stop() {
        physics.setVelocityX(0);
    }
}