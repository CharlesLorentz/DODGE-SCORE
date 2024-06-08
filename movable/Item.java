package movable;

import fixed.Map;

public class Item {
    int xPos;
    int yPos;
    static Map map;
    protected Item( int xPos, int yPos){
        this.xPos = xPos;
        this.yPos = yPos;
    }

    public void setMap(Map map){this.map = map;}
    public int getXPos(){return xPos;}
    public int getYPos(){return yPos;}
    public void setPos(int xPos, int yPos){
        this.xPos = xPos;
        this.yPos = yPos;
    }

    public static boolean crash(Item item1, Item item2){
        if(item1.getXPos() == item2.getXPos() && item1.getYPos() == item2.getYPos())
            return true;
        else return false;
    }
}
