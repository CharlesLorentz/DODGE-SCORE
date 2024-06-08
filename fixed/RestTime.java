package fixed;

import javax.swing.JOptionPane;

public final class RestTime extends Head {
    private int rest = 60;
    private CurrentScore scorer;
    private Thread timer = new Thread(() -> {
        while(true){
            try{
//                每过一秒剩余时间的值减少1 调用setText重置显示的文本
                setText(prompt + rest);
                Thread.sleep(1000);
                rest--;
//                直到剩余时间小于0时游戏结束 显示结算界面
                if(rest <= 0) {
                    JOptionPane.showMessageDialog(null, "您的得分是" + scorer.getScore() + "分");
//                    new LastPage("结算", "您的得分是" + scorer.getScore() + "分");
                    System.exit(0);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    });
//    private Thread timer = new Thread(){
//        public void run() {
//            while(true){
//                try{
//                    setText(prompt + rest);
//                    Thread.sleep(1000);
//                    rest--;
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//    };
    RestTime(String prompt, int width, int height){
        super(prompt, width, height);
        timer.start();
    }
    public void setScorer(CurrentScore scorer){this.scorer = scorer; }
    public void punish(){
        rest = rest - 10;
    }

}
