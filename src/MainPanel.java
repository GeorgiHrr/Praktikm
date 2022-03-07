import javax.swing.*;
import java.awt.*;

public class MainPanel extends JPanel {

    public MainPanel(int x, int y, int width, int height) {

        this.setBounds(x, y, width, height);
        this.setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));

    }
}


