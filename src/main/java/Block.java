import javafx.scene.paint.Color;

import javafx.scene.shape.Rectangle;

public class Block extends Rectangle {
    boolean is_indestructible;
    int score_worth;
    int x_coordinate;
    int y_coordinate;

    public Block(int i, int i1, Color color) {
        super(i,i1,color);
    }
}
