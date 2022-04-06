import javax.swing.*;
class scorebard{
    public static void main(String args[]){
        JFrame frame = new JFrame("Bario Bros - Scoreboard");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600,500);
        JButton button = new JButton("Highscore");
        frame.getContentPane().add(button); // Adds Button to content pane of frame
        frame.setVisible(true);
    }
}
