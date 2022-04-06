
import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.app.scene.GameScene;
import com.almasb.fxgl.app.scene.Viewport;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.level.Level;
import com.almasb.fxgl.input.UserAction;
import com.almasb.fxgl.input.virtual.VirtualButton;
import com.almasb.fxgl.physics.CollisionHandler;
import com.almasb.fxgl.physics.HitBox;
import com.almasb.fxgl.physics.box2d.collision.Collision;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.io.File;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.stream.Collectors;

import static com.almasb.fxgl.dsl.FXGLForKtKt.*;

public class BarioBrosApp extends GameApplication {

    Entity player;

    @Override
    protected void initSettings(GameSettings settings) {
        settings.setWidth(1280);
        settings.setHeight(720);
        settings.setTitle("Bario Bros");
        settings.setVersion("1.0");
    }

    @Override
    protected void initGame() {
        FXGL.getGameWorld().addEntityFactory(new BarioBrosFactory());

        setLevel(3);

        player = FXGL.getGameWorld().spawn("player", 50, 50);

        Viewport viewport = FXGL.getGameScene().getViewport();
        viewport.bindToEntity(player, getAppWidth() / 2, getAppHeight() / 2);
        viewport.setLazy(true);

        System.out.println(FXGL.getGameWorld().getEntities());
    }

    @Override
    protected void initInput() {
        getInput().addAction(new UserAction("Left") {
            @Override
            protected void onAction() {
                player.getComponent(PlayerControl.class).left();
            }

            @Override
            protected void onActionEnd() {
                player.getComponent(PlayerControl.class).stop();
            }
        }, KeyCode.A, VirtualButton.LEFT);

        getInput().addAction(new UserAction("Right") {
            @Override
            protected void onAction() {
                player.getComponent(PlayerControl.class).right();
            }

            @Override
            protected void onActionEnd() {
                player.getComponent(PlayerControl.class).stop();
            }
        }, KeyCode.D, VirtualButton.RIGHT);

        getInput().addAction(new UserAction("Jump") {
            @Override
            protected void onActionBegin() {
                player.getComponent(PlayerControl.class).jump();
            }
        }, KeyCode.W, VirtualButton.A);
    }

    @Override
    protected void initUI() {}

    @Override
    protected void onUpdate(double tpf) {}

    @Override
    protected void initPhysics() {
/*        FXGL.getPhysicsWorld().addCollisionHandler(new CollisionHandler(EntityType.PLAYER, EntityType.PLATFORM) {
            @Override
            protected void onCollision(Entity player, Entity platform) {
                System.out.println("yooo");
                player.setY(platform.getY() - 16);
            }
        });*/
    }

    private void setLevel(int level) {
        GameScene gameScene = FXGL.getGameScene();
        String levelPath = String.format("tmx/level_%s.tmx", level);

        Level levelLoaded = FXGL.setLevelFromMap(levelPath);
        gameScene.getViewport().setBounds(0, 0, levelLoaded.getWidth(), levelLoaded.getHeight());
        gameScene.getViewport().setZoom(gameScene.getViewport().getHeight() / levelLoaded.getHeight());
    }

    public static void main(String[] args) {
        launch(args);
    }
}
