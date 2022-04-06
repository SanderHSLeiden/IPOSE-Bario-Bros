
import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.app.scene.GameScene;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.GameWorld;
import com.almasb.fxgl.entity.level.Level;
import com.almasb.fxgl.entity.level.tiled.TMXLevelLoader;

import java.io.File;
import java.net.MalformedURLException;

public class BarioBrosApp extends GameApplication {

    @Override
    protected void initSettings(GameSettings settings) {
        settings.setWidth(1280);
        settings.setHeight(720);
        settings.setTitle("Bario Bros");
        settings.setVersion("1.0");
    }

    @Override
    protected void initGame() {
        setLevel(3);
    }

    @Override
    protected void initPhysics() {}

    @Override
    protected void initUI() {}

    @Override
    protected void onUpdate(double tpf) {}

    private void setLevel(int level) {
        try {
            GameWorld gameWorld = FXGL.getGameWorld();
            GameScene gameScene = FXGL.getGameScene();
            String levelPath = String.format("src/main/resources/levels/level_%s.tmx", level);
            File levelFile = new File(levelPath);
            Level levelLoaded = new TMXLevelLoader().load(levelFile.toURI().toURL(), gameWorld);

            gameWorld.setLevel(levelLoaded);
            gameScene.getViewport().setBounds(0, 0, levelLoaded.getWidth(), levelLoaded.getHeight());
            gameScene.getViewport().setZoom(gameScene.getViewport().getHeight() / levelLoaded.getHeight());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
