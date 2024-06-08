package fixed;

import java.awt.Label;
import java.awt.Font;

class Head extends Label{
    protected String prompt;
    Head(String prompt, int width, int height){
        super(prompt);
        this.prompt = prompt;
        Font font = new Font("Arial", Font.BOLD, 30);
        setFont(font);
        setSize(width, height);
    }

    public String getPrompt() {return prompt;}
}