package fixed;

import javax.swing.JPanel;
import java.awt.Graphics;
import java.util.Random;
import java.awt.Image;
import javax.swing.ImageIcon;
import movable.Player;
import movable.Reward;
import movable.Robot;
import movable.Role;

final public class Map extends JPanel{
    private final int rows;
    private final int cols;
    private final int width;
    private final int height;
    public final int WALL = 0;
    public final int PATH = 1;
    private int[][] binMaze;
    private Player player;
    private Robot robot;
    public Reward[] rewards;
    private RestTime timer;
    private CurrentScore scorer;
    private final Image floorImage;
    private final Image wallImage;
    private final Image playerImage;
    private final Image robotImage;
    private final Image starImage;
    private final PathInfo[] pathInfo;
    private Thread robotMove = new Thread(() -> {
        while(true){
            try{
                Thread.sleep(200);
                robot.moveTo();
                repaint();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    });
    private Thread checkCrash = new Thread(() -> {
        while(true){
            try{
                boolean result = Role.crash(player, robot);
                if(result){
                    timer.punish();
//                    System.out.println(true);
//                    System.exit(0);
                    Thread.sleep(3000);
                }
                else{
                    Thread.sleep(1);
//                    System.out.println(1);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    });
    private Thread scorerThread = new Thread(() -> {
        while(true) {
            for (int i = 0; i < rewards.length; i++){
                if (Reward.crash(player, rewards[i])) {
                    scorer.setScore(scorer.getScore() + 10);
                    scorer.setText(scorer.getPrompt() + scorer.getScore());
                    Reward.renew(rewards, i);
                    repaint();
                    break;
                }
                else{
                    try {
                        Thread.sleep(1);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
//                    System.out.println(2);
                }
            }
        }
    });
//    private Thread robotMove = new Thread(){
//        public void run(){
//            while(true){
//                try{
//                    Thread.sleep(1000);
//                    robot.moveTo();
//                    repaint();
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//    };

    Map(int rows, int cols, int width, int height, CurrentScore scorer) {
        super();
        this.rows = rows;
        this.cols = cols;
        this.width = width;
        this.height = height;
        pathInfo = new PathInfo[rows * cols];

        floorImage = new ImageIcon(getClass().getResource("../image/path.png")).getImage().getScaledInstance(width / cols, height / rows, Image.SCALE_DEFAULT);
        wallImage = new ImageIcon(getClass().getResource("../image/wall.png")).getImage().getScaledInstance(width / cols, height / rows, Image.SCALE_DEFAULT);
        playerImage = new ImageIcon(getClass().getResource("../image/player.png")).getImage().getScaledInstance(width / cols, height / rows, Image.SCALE_DEFAULT);
        robotImage = new ImageIcon(getClass().getResource("../image/robot.png")).getImage().getScaledInstance(width / cols, height / rows, Image.SCALE_DEFAULT);
        starImage = new ImageIcon(getClass().getResource("../image/star.png")).getImage().getScaledInstance(width / cols, height / rows, Image.SCALE_DEFAULT);

        binMaze = generateMaze(rows, cols);
//        for(int i = 0; i < rows; i++) {
//            for (int j = 0; j < cols; j++)
//                System.out.print(binMaze[i][j] + " ");
//            System.out.println();
//        }
        for(int i = 0; i < rows; i++)
            for(int j = 0; j < cols; j++){
                if(binMaze[i][j] == WALL) continue;
                pathInfo[PathInfo.pathNum] = new PathInfo(i, j, true, PathInfo.pathNum);
                PathInfo.pathNum++;
            }
        PathInfo.pathNum--;
        this.scorer = scorer;
        robotMove.start();
    }
    public void setTimer(RestTime timer){
        this.timer = timer;
        checkCrash.start();
    }
    public int getCols(){return cols;}
    public int getRows(){return rows;}
    public void setRole(Player player, Robot robot){
        this.player = player;
        this.robot = robot;
    }
//    public void changeRolePathInfo(Player player, Robot robot){
//        pathInfo[player.getXPos() * cols + player.getYPos()].available = false;
//        pathInfo[robot.getXPos() * cols + robot.getYPos()].available = false;
//    }
    public void refreshPathInfo(){
        for(int i = 0; i < PathInfo.pathNum; i++){
//            在向地图中添加奖励物品时需要更新路径信息 可能会存在空指针异常 所以采用异常处理
//            当创建前N-1个奖励时Reward[N-2]之后的元素可能为空指针 忽略该问题即可
            try{
                for(int j = 0; j < Reward.rewardNum; j++){
                    if(rewards[j].getXPos() == pathInfo[i].getXPos() && rewards[j].getYPos() == pathInfo[i].getYPos()){
                        pathInfo[i].setUnavailable();
                        break;
                    }
                }
            } catch (NullPointerException e){

            }
            if(player.getXPos() == pathInfo[i].getXPos() && player.getYPos() == pathInfo[i].getYPos())
                pathInfo[i].setUnavailable();
            else if(robot.getXPos() == pathInfo[i].getXPos() && robot.getYPos() == pathInfo[i].getYPos())
                pathInfo[i].setUnavailable();
            else pathInfo[i].setAvailable();
        }
    }
    public void setRewards(Reward[] rewards){
        this.rewards = rewards;
        scorerThread.start();
    }
    public class PathInfo{
        public static int pathNum = 0;
        private int xPos;
        private int yPos;
        private boolean available;
        private int order;
        PathInfo(int xPos,int yPos, boolean available, int order){
            this.xPos = xPos;
            this.yPos = yPos;
            this.available = available;
            this.order = order;
        }

        public void setAvailable() {this.available = true;}
        public void setUnavailable() {this.available = false;}
        public boolean getAvailable() {return available;}
        public int getXPos(){return xPos;}
        public int getYPos(){return yPos;}
    }
    public void setPathInfo(int index, boolean result){pathInfo[index].available = result;}
    public int[] getPathInfoPos(int index){return new int[]{pathInfo[index].getXPos(), pathInfo[index].getYPos()};}
    public int getPathInfoIndex(int xPos, int yPos){
        for(int i = 0; i < PathInfo.pathNum; i++){
            if(xPos == pathInfo[i].getXPos() && yPos == pathInfo[i].getYPos()) return pathInfo[i].order;
        }
        return -1;
    }
    public boolean[] getPathInfo(){
        refreshPathInfo();
        boolean[] result = new boolean[PathInfo.pathNum];
        for(int i = 0; i < PathInfo.pathNum; i++) result[i] = pathInfo[i].getAvailable();
        return result;
    }
    public int[][] getBinMaze(){return binMaze;}

    private int[][] generateMaze(int rows, int cols){
        binMaze = new int[rows][cols];
        for (int y = 0; y < rows; y++) {
            for (int x = 0; x < cols; x++) {
                if (x == 0 || x == cols - 1 || y == 0 || y == rows - 1)
                    binMaze[y][x] = WALL;
                else
                    binMaze[y][x] = PATH;
            }
        }
        division(1, 1, cols - 2, rows - 2);

        return binMaze;
    }

    private void division(int startX, int startY, int endX, int endY) {

        Random random = new Random();

        // 如果迷宫的宽度或者高度小于2了就不能再分割了
        if (endX - startX < 2 || endY - startY < 2)
            return;

        // x,y只能是偶数
        int posX = startX + 1 + random.nextInt((endX - startX) / 2) * 2;// 纵向分割分割线的横坐标
        int posY = startY + 1 + random.nextInt((endY - startY) / 2) * 2;// 横向分割线的纵坐标
        for (int i = startX; i <= endX; i++) // 横向分割
            binMaze[posY][i] = WALL;
        for (int i = startY; i <= endY; i++) // 纵向分割
            binMaze[i][posX] = WALL;

        division(startX, startY, posX - 1, posY - 1);// 左下区域
        division(startX, posY + 1, posX - 1, endY);// 左上区域
        division(posX + 1, posY + 1, endX, endY);// 右上区域
        division(posX + 1, startY, endX, posY - 1);// 右下区域

        // 随机打开三扇门
        switch (random.nextInt(4)) {
            case 0:
                openDoor(startX, posY, posX - 1, posY); // 开左边的墙
                openDoor(posX, posY + 1, posX, endY); // 开上方的墙
                openDoor(posX + 1, posY, endX, posY); // 开右边的墙
                break;
            case 1:
                openDoor(posX, posY + 1, posX, endY); // 开上方的墙
                openDoor(posX + 1, posY, endX, posY); // 开右边的墙
                openDoor(posX, startY, posX, posY - 1);// 开下面的墙
                break;
            case 2:
                openDoor(posX + 1, posY, endX, posY); // 开右边的墙
                openDoor(posX, startY, posX, posY - 1);// 开下面的墙
                openDoor(startX, posY, posX - 1, posY); // 开左边的墙
                break;
            case 3:
                openDoor(posX, startY, posX, posY - 1);// 开下面的墙
                openDoor(startX, posY, posX - 1, posY); // 开左边的墙
                openDoor(posX, posY + 1, posX, endY); // 开上方的墙
                break;
        }
    }

    private void openDoor(int startX, int startY, int endX, int endY) {
        Random random = new Random();
        int x;// 开门的横坐标
        int y;// 开门的纵坐标

        // 墙是横着的
        if (startY == endY) {
            x = startX + random.nextInt((endX - startX) / 2 + 1) * 2;
            binMaze[startY][x] = PATH;
        }
        // 墙是竖着的
        if (startX == endX) {
            y = startY + random.nextInt((endY - startY) / 2 + 1) * 2;// 在奇数墙上开门
            binMaze[y][startX] = PATH;
        }
    }

    public void paint(Graphics g){
        super.paint(g);
        int count = 0;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if(i == player.getXPos() && j == player.getYPos()){
                    g.drawImage(playerImage, j * (width / cols), i * (height / rows), this);
                    continue;
                }
                if(i == robot.getXPos() && j == robot.getYPos()){
                    g.drawImage(robotImage, j * (width / cols), i * (height / rows), this);
                    continue;
                }
                for(int k = 0; k < rewards.length; k++){
                    if(i == rewards[k].getXPos() && j == rewards[k].getYPos()){
                        g.drawImage(starImage, j * (width / cols), i * (height / rows), this);
                        count = 1;
                        break;
                    }
                }
                if(count == 1){
                    count = 0;
                    continue;
                }
                switch (binMaze[i][j]) {
                    case 0 :
                        g.drawImage(wallImage, j * (width / cols), i * (height / rows), this);
                        break;
                    case 1 :
                        g.drawImage(floorImage, j * (width / cols), i * (height / rows), this);
                        break;
                }
            }
        }
    }
}