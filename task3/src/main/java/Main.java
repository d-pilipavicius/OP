import DataStructures.DataHolder;
import Windows.*;
import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        DataHolder.resetGlobal();
        DisplaySingleton window = DisplaySingleton.getInstance();
        window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        window.setVisible(true);
        TitleWindowSetup.titleWindow();
    }
}
