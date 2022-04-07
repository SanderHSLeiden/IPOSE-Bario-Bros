
import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.app.scene.GameScene;
import com.almasb.fxgl.app.scene.Viewport;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.components.CollidableComponent;
import com.almasb.fxgl.entity.level.Level;
import com.almasb.fxgl.input.UserAction;
import com.almasb.fxgl.input.virtual.VirtualButton;

import com.almasb.fxgl.physics.CollisionHandler;
import javafx.scene.input.KeyCode;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import com.almasb.fxgl.app.services.FXGLDialogService;

import javax.swing.*;
import java.awt.*;

import static com.almasb.fxgl.dsl.FXGLForKtKt.*;


public class BarioBrosApp extends GameApplication {

    Entity player;
    int currentLevelInt;
    Level currentLevel;

    String player1_name = null;
    String player2_name = null;
    boolean Answer1;


    @Override
    protected void initSettings(GameSettings settings) {
        settings.setWidth(1280);
        settings.setHeight(720);
        settings.setTitle("Bario Bros");
        settings.setVersion("1.0");

      currentLevelInt = 1;
    }

    @Override
    protected void initGame() {
        FXGL.getGameWorld().addEntityFactory(new BarioBrosFactory());

        setLevel(currentLevelInt);
        respawnPlayer();
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
    protected void onUpdate(double tpf) {
        if (player1_name == null || player2_name == null) {
            loginUser();

            return;
        }
        if(Answer1 == false){
            loginUser();

        }



        if (player.getY() > currentLevel.getHeight()) {
            FXGL.getGameScene().getViewport().shake(6, .2);

            respawnPlayer();
        }
    }

    @Override
    protected void initPhysics() {
        FXGL.getPhysicsWorld().addCollisionHandler(new CollisionHandler(EntityType.PLAYER, EntityType.FLAG) {
            @Override
            protected void onCollision(Entity player, Entity flag) {
                currentLevelInt += 1;
                getGameController().startNewGame();
            }
        });
    }

    private void loginUser() {

        FXGL.getDialogService().showConfirmationBox(  "Start game", answer1 -> {
            System.out.println("You pressed yes? " + answer1);
            Answer1 = answer1;



        });
        FXGL.getDialogService().showInputBox("speler 2 vul je naam in:", answer -> {
            System.out.println("You typed: "+ answer);
            player2_name = answer;
        });

        FXGL.getDialogService().showInputBox("speler 1 vul je naam in:", answer -> {
            System.out.println("You typed: "+ answer);
            player1_name = answer;
        });

    }

    private void respawnPlayer() {
        if (player != null) {
            player.removeFromWorld();
        }

        player = FXGL.getGameWorld().spawn("player", 50, 50);


        Viewport viewport = FXGL.getGameScene().getViewport();
        viewport.bindToEntity(player, getAppWidth() / 2.0, getAppHeight() / 2.0);
        viewport.setLazy(true);
    }

    private void setLevel(int level) {
        GameScene gameScene = FXGL.getGameScene();
        String levelPath = String.format("tmx/level_%s.tmx", level);

        currentLevel = FXGL.setLevelFromMap(levelPath);
        gameScene.getViewport().setBounds(0, 0, currentLevel.getWidth(), currentLevel.getHeight());
        gameScene.getViewport().setZoom(gameScene.getViewport().getHeight() / currentLevel.getHeight());
    }

    public static void main(String[] args) {
        launch(args);
    }
}
