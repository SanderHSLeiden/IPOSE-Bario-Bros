import com.almasb.fxgl.core.math.FXGLMath;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.GameWorld;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.physics.PhysicsComponent;
import com.almasb.fxgl.texture.AnimatedTexture;
import com.almasb.fxgl.texture.AnimationChannel;
import javafx.geometry.Point2D;
import javafx.util.Duration;

public class PlayerControl extends Component {
    private PhysicsComponent physics;
    private boolean isOnGround = false;
    private boolean canJump = false;
    private boolean isWalking = false;
    public boolean hasPower = false;

    private AnimatedTexture texture;
    private AnimationChannel animIdle, animWalk, animJump;

    public PlayerControl() {
        animIdle = new AnimationChannel(FXGL.image("mario_idle.png"), 1, 16, 32, Duration.seconds(1), 0, 0);
        animWalk = new AnimationChannel(FXGL.image("mario_run.png"), 1, 16, 32, Duration.millis(250), 0, 2);
        animJump = new AnimationChannel(FXGL.image("mario_jump.png"), 1, 16, 32, Duration.seconds(1), 0, 0);

        texture = new AnimatedTexture(animIdle);
    }

    @Override
    public void onAdded() {
        entity.getTransformComponent().setScaleOrigin(new Point2D(16, 32));
        entity.getViewComponent().addChild(texture);

        physics.onGroundProperty().addListener((obs, old, isOnGround) -> {
            this.isOnGround = isOnGround;
            canJump = isOnGround;
        });
    }

    @Override
    public void onUpdate(double tpf) {
        if (!isOnGround) {
            texture.loopAnimationChannel(animJump);

            if (physics.getVelocityY() > -10) {
                texture.setRotate(texture.getRotate() + (texture.getRotate() / 36 + 1));
            }

            return;
        }

        if (texture.getRotate() > 0) {
            texture.setRotate(texture.getRotate() - (texture.getRotate() / 2));
        }

        if (isWalking) {
            if (texture.getAnimationChannel() == animIdle || texture.getAnimationChannel() == animJump) {
                texture.loopAnimationChannel(animWalk);
            }

            return;
        }

        texture.loopAnimationChannel(animIdle);
    }

    public void usePower() {
        if(hasPower) {
            FXGL.getGameWorld().spawn("flameOrb", entity.getX() + entity.getWidth(), entity.getY() - 8);
        }
    }

    public void left() {
        isWalking = true;
        physics.setVelocityX(-150);
    }

    public void right() {
        isWalking = true;
        physics.setVelocityX(150);
    }

    public void jump() {
        if (!canJump) return;

        texture.setRotate(-10);
        physics.setVelocityY(-300);
    }

    public void stop() {
        isWalking = false;
        texture.loopAnimationChannel(animIdle);
        physics.setVelocityX(0);
    }
}