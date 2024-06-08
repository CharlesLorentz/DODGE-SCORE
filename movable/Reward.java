package movable;

import java.util.Random;

public class Reward extends Item{
    public static int rewardNum = 0;
    int value;
    protected Reward(int xPos, int yPos){
        super(xPos, yPos);
    }
    public static int[] getAvailablePos(){
//        检查路径的可用情况
        boolean[] availability = map.getPathInfo();
        int index = 0;
        for(int i = 0; i < availability.length; i++){if(availability[i]) ++index;}
//        随机获取一组可用的位置
        int rand = new Random().nextInt(index);
        int i = 0;
        do{
            if(!availability[i]){rand++;}
            i++;
        }while(i <= rand);
        map.setPathInfo(i - 1, false);
        return new int[]{map.getPathInfoPos(i - 1)[0], map.getPathInfoPos(i - 1)[1]};
    }

    public static void renew(Reward[] rewards, int index){
//        获取替换掉的路径信息 将其定义为可用 获取新奖励的位置 将其定义为不可用
        int pathInfoOrder = map.getPathInfoIndex(rewards[index].xPos,rewards[index].yPos);
        int[] pos = getAvailablePos();
        int newOrder = map.getPathInfoIndex(pos[0], pos[1]);
        map.setPathInfo(pathInfoOrder, true);
        map.setPathInfo(newOrder, false);
        rewards[index].setPos(pos[0], pos[1]);
    }
    public int getValue(){return value;}
}
