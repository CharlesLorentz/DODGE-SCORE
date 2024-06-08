package fixed;

public final class CurrentScore extends Head{
    private int score = 0;
    CurrentScore(String prompt, int width, int height){
        super(prompt, width, height);
        setText(prompt + score);
    }
    public void setScore(int score) {this.score = score;}
    public int getScore() {return score;}
}
