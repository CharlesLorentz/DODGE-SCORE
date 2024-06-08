package movable;

import java.util.Random;

public class Robot extends Role{
    public Robot(int xPos, int yPos){super(xPos, yPos);}

    public void moveTo(){
        int available = 0;
        int x = getXPos();
        int y = getYPos();
        Boolean[] count = new Boolean[]{false, false, false, false};
        if (map.getBinMaze()[x - 1][y] == map.PATH){
            available++;
            count[0] = true;
        }
        if (map.getBinMaze()[x + 1][y] == map.PATH){
            available++;
            count[1] = true;
        }
        if (map.getBinMaze()[x][y - 1] == map.PATH){
            available++;
            count[2] = true;
        }
        if (map.getBinMaze()[x][y + 1] == map.PATH){
            available++;
            count[3] = true;
        }
        int rand = new Random().nextInt(available);
        int i = 0;
        do{
            if(count[i] == false){rand++;}
            i++;
        }while(i <= rand);
        switch(i - 1){
            case 0: move(x - 1, y); break;
            case 1: move(x + 1, y); break;
            case 2: move(x, y - 1); break;
            case 3: move(x, y + 1); break;
        }
    }
}
