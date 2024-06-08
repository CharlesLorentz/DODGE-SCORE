package movable;

import fixed.Map;

public class Role extends Item implements Move{

    protected Role( int xPos, int yPos){
        super(xPos, yPos);
    }

    @Override
    public void move(int xPos, int yPos) {
        this.xPos = xPos;
        this.yPos = yPos;
    }
}
