
import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.app.scene.GameScene;
import com.almasb.fxgl.app.scene.SceneFactory;
import com.almasb.fxgl.app.scene.Viewport;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.level.Level;
import com.almasb.fxgl.input.UserAction;
import com.almasb.fxgl.input.virtual.VirtualButton;

import com.almasb.fxgl.physics.CollisionHandler;
import com.almasb.fxgl.physics.box2d.collision.Collision;
import com.almasb.fxgl.texture.AnimatedTexture;
import com.almasb.fxgl.texture.AnimationChannel;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;

import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import static com.almasb.fxgl.dsl.FXGLForKtKt.*;


public class BarioBrosApp extends GameApplication {

    Entity player;
    String player_name = "";
    int player_current_score = 0;
    int player_total_score = 0;

    int currentLevelNumber;
    Level currentLevelData;

    Timer levelTimer;
    boolean outOfTime = false;

    @Override
    protected void initSettings(GameSettings settings) {
        settings.setWidth(1280);
        settings.setSceneFactory(new SceneFactory());
        settings.setMainMenuEnabled(true);
        settings.setCloseConfirmation(true);
        settings.setHeight(720);
        settings.setTitle("Bario Bros");
        settings.setVersion("177013");

        currentLevelNumber = 1;
    }


    @Override
    protected void initGame() {
        outOfTime = false;

        FXGL.getGameWorld().addEntityFactory(new BarioBrosFactory());
        setLevel(currentLevelNumber);
        respawnPlayer();

        startTimer();
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
    protected void initUI() {
        Label playerText = new Label("Player name:");
        playerText.setStyle("-fx-text-fill: black");
        playerText.setTranslateX(20);
        playerText.setTranslateY(0);
        playerText.textProperty().bind(FXGL.getWorldProperties().stringProperty("Player name"));
        FXGL.getGameScene().addUINode(playerText);

        Label scoreText = new Label("Score:");
        scoreText.setStyle("-fx-text-fill: black");
        scoreText.setTranslateX(20);
        scoreText.setTranslateY(20);
        FXGL.getGameScene().addUINode(scoreText);

        Label score = new Label("High score");
        score.setStyle("-fx-text-fill: black");
        score.setTranslateX(60);
        score.setTranslateY(20);
        score.textProperty().bind(FXGL.getWorldProperties().intProperty("High score").asString());
        FXGL.getGameScene().addUINode(score);
    }

    @Override
    protected void initGameVars(Map<String, Object> vars) {
        vars.put("High score", player_current_score);
        vars.put("Player name", player_name);
    }

    @Override
    protected void onUpdate(double tpf) {
        if (player_name.isEmpty()) {
            loginUser();

            return;
        }

        if (player.getY() > currentLevelData.getHeight()) {
            FXGL.getGameScene().getViewport().shake(6, .2);

            respawnPlayer();
        }

        if (outOfTime) {
            levelTimer.cancel();

            FXGL.getDialogService().showMessageBox("Tijd is op!", new Runnable() {
                @Override
                public void run() {
                    getGameController().startNewGame();
                }
            });
        }
    }

    @Override
    protected void initPhysics() {
        FXGL.getPhysicsWorld().addCollisionHandler(new CollisionHandler(EntityType.PLAYER, EntityType.FLAG) {
            @Override
            protected void onCollision(Entity player, Entity flag) {
                player_total_score += player_current_score;
                player_current_score = 0;

                if(currentLevelNumber == 4){
                    FXGL.getDialogService().showMessageBox(String.format("Gefeliciteerd %s, je hebt Bario Bros uitgespeeld!\nJe eindscore was %d punten", player_name, player_total_score), new Runnable() {
                        @Override
                        public void run() {
                            currentLevelNumber = 1;
                            getGameController().gotoMainMenu();
                        }
                    });
                }
                else {
                    currentLevelNumber += 1;

                    getGameController().startNewGame();
                }
            }
        });

        FXGL.getPhysicsWorld().addCollisionHandler(new CollisionHandler(EntityType.PLAYER, EntityType.UNUSEDQUESTIONMARK) {
            @Override
            protected void onCollision(Entity player, Entity unusedQuestionMark) {
                if(player.getY() > unusedQuestionMark.getY()
                        && (player.getX() >= unusedQuestionMark.getX() || player.getX() + player.getWidth() >= unusedQuestionMark.getX())
                        && (
                                player.getX() <= unusedQuestionMark.getX() + unusedQuestionMark.getWidth()
                                        || player.getX() + player.getWidth() <= unusedQuestionMark.getX() + unusedQuestionMark.getWidth()
                            )
                ) {
                    player_current_score += 10;
                    FXGL.inc("High score", 10);
                    unusedQuestionMark.removeFromWorld();
                }
            }
        });

        FXGL.getPhysicsWorld().addCollisionHandler(new CollisionHandler(EntityType.PLAYER, EntityType.COIN) {
            @Override
            protected void onCollision(Entity player, Entity coin) {
                player_current_score += 100;
                FXGL.inc("High score", 100);
                coin.removeFromWorld();
            }
        });
    }

    private void startTimer() {
        if(levelTimer != null){
            levelTimer.cancel();
        }
        levelTimer = new Timer();
        levelTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                outOfTime = true;
            }
        }, 1000*60, 1000);
    }

    private void loginUser() {
        FXGL.getDialogService().showInputBox("Vul je speler naam in:", answer -> {
            player_name = answer;
            FXGL.set("Player name", player_name);
        });
    }

    private void respawnPlayer() {
        if (player != null) {
            player.removeFromWorld();
            startTimer();
        }

        player = FXGL.getGameWorld().spawn("player", 50, 50);


        Viewport viewport = FXGL.getGameScene().getViewport();
        viewport.bindToEntity(player, getAppWidth() / 2.0, getAppHeight() / 2.0);
        viewport.setLazy(true);
    }

    private void setLevel(int level) {
        GameScene gameScene = FXGL.getGameScene();
        String levelPath = String.format("tmx/level_%s.tmx", level);

        currentLevelData = FXGL.setLevelFromMap(levelPath);
        gameScene.getViewport().setBounds(0, 0, currentLevelData.getWidth(), currentLevelData.getHeight());
        gameScene.getViewport().setZoom(gameScene.getViewport().getHeight() / currentLevelData.getHeight());

        switch (level) {
            case 1:
            case 3:
                gameScene.setBackgroundColor(Color.rgb(161,173,255));

                break;
            case 2:
            case 4:
                gameScene.setBackgroundColor(Color.rgb(0, 0, 0));

                break;
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
