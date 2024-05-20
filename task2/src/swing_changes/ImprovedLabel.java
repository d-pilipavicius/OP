package swing_changes;

import javax.swing.*;
import java.awt.*;

public class ImprovedLabel extends JLabel {
    public ImprovedLabel(int x, int y, String text, String fontName, int fontFormat, int textSize){
        super(text);
        setBounds(x, y, text.length()*textSize, textSize+textSize/5);
        setFont(new Font(fontName, fontFormat, textSize));
    }
    public ImprovedLabel(int x, int y, String text){
        super(text);
        setBounds(x, y, text.length()*10, 12);
        setFont(new Font("Product Sans", Font.PLAIN, 10));
    }
    public ImprovedLabel(int x, int y, String text, int textSize){
        super(text);
        setBounds(x, y, text.length()*textSize, textSize+textSize/5);
        setFont(new Font("Product Sans", Font.PLAIN, textSize));
    }
}
