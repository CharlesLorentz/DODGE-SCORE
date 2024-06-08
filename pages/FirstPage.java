package pages;
import fixed.Background;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class FirstPage extends JFrame implements ActionListener{
    String instruction = "<html>游戏规则：<br>玩家通过WASD控制方向，在躲避机器人的同时抓取星星。<br>" +
            "游戏限时60秒，如果撞到机器人会罚时10秒,同时获得3秒免疫效果。<br>" +
            "每个星星价值10分，努力在规定时间内获得更多分数吧！</html>";
    public FirstPage(String title){
        super(title);
        int[] locationArgs = setCenter(500, 300);
        setLocation(locationArgs[0], locationArgs[1]);

        JLabel tips = new JLabel(instruction);
        add(tips, BorderLayout.NORTH);
        JButton btn = new JButton("开始游戏");
        add(btn, BorderLayout.SOUTH);

        setSize(500, 300);
        setVisible(true);
        btn.addActionListener(this);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        Background bgFrame = new Background("DODGE! SCORE!");
        bgFrame.display();
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
