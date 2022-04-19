import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.physics.PhysicsComponent;
import com.sun.javafx.util.Logging;
import javafx.geometry.Point2D;

public class EnemyControl extends Component {
    private PhysicsComponent physics;

    private boolean goingLeft = true;
    private double rotationAngle = 0;
    private int fromX;
    private int toX;

    public EnemyControl() {}

    @Override
    public void onAdded() {
        entity.getTransformComponent().setScaleOrigin(new Point2D(32, 32));

        if (!entity.getProperties().exists("fromX") || !entity.getProperties().exists("toX")) {
            Logging.getJavaFXLogger().log(System.Logger.Level.WARNING, "A enemy entity is missing the fromX or toX property, this enemy won't move");

            fromX = (int)entity.getX();
            toX = (int)entity.getX();

            return;
        }

        fromX = entity.getProperties().getInt("fromX");
        toX = entity.getProperties().getInt("toX");
    }

    @Override
    public void onUpdate(double tpf) {
        if (fromX != toX) {
            if (goingLeft) {
                rotationAngle -= 5;

                entity.setRotation(rotationAngle);
                physics.setVelocityX(-50);
            } else {
                rotationAngle += 5;

                entity.setRotation(rotationAngle);
                physics.setVelocityX(50);
            }

            if (entity.getX() < fromX) {
                goingLeft = false;
            }

            if (entity.getX() > toX) {
                goingLeft = true;
            }
        }
    }
}