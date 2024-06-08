package fixed;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.GridLayout;
import movable.Player;
import movable.Reward;
import movable.Robot;
import movable.Star;

public class Background extends JFrame {
    public Background(String title){
        super(title);
    }
    public void display(){
        Background bgFrame = new Background("DODGE! SCORE!");
//        bgFrame.setLayout(FlowLayout);

//        define the location of frame
        int frameWidth = 1000;
        int frameHeight = 600;
        int[] locationArgs = setCenter(frameWidth, frameHeight);
        bgFrame.setSize(frameWidth, frameHeight);
        bgFrame.setLocation(locationArgs[0], locationArgs[1]);

//        add head
        JPanel head = new JPanel();
        head.setSize(frameWidth, frameHeight / 10);
        head.setLayout(new GridLayout(1, 2));
        RestTime restTime = new RestTime("rest time:", frameWidth / 2, frameHeight / 10);
        CurrentScore scorer = new CurrentScore("current score:", frameWidth / 2, frameHeight / 10);
        head.add(restTime);
        head.add(scorer);
        restTime.setScorer(scorer);
        bgFrame.add(head, BorderLayout.NORTH);


        int gridRows = 15;
        int gridCols = 25;

//        add map
        Player player = new Player(1, 1);
        Robot robot = new Robot(gridRows - 2, gridCols - 2);
        Map map = new Map(gridRows, gridCols, frameWidth - 1, frameHeight - head.getHeight() - 16, scorer);
        robot.setMap(map);
        player.setMap(map);
        map.setRole(player, robot);
//        map.changeRolePathInfo(player, robot);
        int rewardNum = 7;
        Reward[] rewards = new Reward[rewardNum];
        for(int i = 0; i < rewardNum; i++){
            int[] pos = Star.getAvailablePos();
            rewards[i] = new Star(pos[0], pos[1]);
            Reward.rewardNum++;
        }
        map.setRewards(rewards);
        map.setTimer(restTime);
        bgFrame.add(map);

//        allow frame visible and to be closed correctly
        bgFrame.addKeyListener(player);
        bgFrame.setVisible(true);
        bgFrame.setDefaultCloseOperation(bgFrame.EXIT_ON_CLOSE);

    }

    private int[] setCenter(int frameWidth, int frameHeight){
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int screenWidth = (int)screenSize.getWidth();
        int screenHeight = (int)screenSize.getHeight();

        int[] locationArgs = new int[2];
        locationArgs[0] = (screenWidth - frameWidth) / 2;
        locationArgs[1] = (screenHeight - frameHeight) / 2;
        return locationArgs;
    }

}
