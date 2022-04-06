
import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.app.scene.GameScene;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.level.Level;
import com.almasb.fxgl.input.UserAction;
import com.almasb.fxgl.input.virtual.VirtualButton;
import javafx.scene.input.KeyCode;

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
