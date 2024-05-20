package swing_changes;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

public class ImprovedTextField extends JTextField {
    private String hintText = "";
    public ImprovedTextField(int x, int y, int width, int height) {
        super();
        setSize(width, height);
        setLocation(x, y);
    }
    public ImprovedTextField(int x, int y, int width, int height, String hintText) {
        super();
        this.hintText = hintText;
        setSize(width, height);
        setLocation(x, y);
        setForeground(Color.gray);
        setText(hintText);
        addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if(getText().equals(hintText)) {
                    setForeground(Color.BLACK);
                    setText("");
                }
            }
            @Override
            public void focusLost(FocusEvent e) {
                if(getText().isEmpty()){
                    setForeground(Color.gray);
                    setText(hintText);
                }
            }
        });
    }
    public String getHintText() {
        return hintText;
    }

    public boolean isEmpty() {
        if(getText().equals(hintText)) {
            return true;
        }
        return false;
    }
}
