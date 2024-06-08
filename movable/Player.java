package movable;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Player extends Role implements KeyListener {
    public Player(int xPos, int yPos){
        super(xPos, yPos);
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        char key = e.getKeyChar();
        int x = getXPos();
        int y = getYPos();
        if (key == 'w' && map.getBinMaze()[x - 1][y] == map.PATH) {move(x - 1, y);}
        if (key == 's' && map.getBinMaze()[x + 1][y] == map.PATH) {move(x + 1, y);}
        if (key == 'a' && map.getBinMaze()[x][y - 1] == map.PATH) {move(x, y - 1);}
        if (key == 'd' && map.getBinMaze()[x][y + 1] == map.PATH) {move(x, y + 1);}
        map.repaint();
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
