import javax.swing.*;
import javax.swing.plaf.basic.BasicToggleButtonUI;

import org.sikuli.script.*;

import java.awt.Font;
import java.awt.event.ItemListener;
import java.awt.LayoutManager;
import java.awt.AWTException;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ItemEvent;
import java.awt.GridLayout;
import java.awt.Robot;
import java.awt.event.InputEvent;
import java.awt.MouseInfo;
import java.awt.Point;

public class LeagueUtils {
    private static Color ON_COLOR = new Color(127, 227, 104);
    private static Color OFF_COLOR = new Color(224,  67, 104);
    private static boolean running = false;
    private static Screen s = new Screen();
    private static JLabel mainText;
    private static Robot bot;
    public static void main(String[] args) {
        setupUI();
        try {
            bot = new Robot();
        } catch (AWTException e) {
        }
    }

    /**
     * Use sikuli to search for the accept button on the screen. If found, click it.
     */
    private static void search() {
        try {
            Match found = s.find("/images/5.png");
            if(found != null){ // found
                Point old = MouseInfo.getPointerInfo().getLocation();
                bot.mouseMove(found.x + 50, found.y + 100);
                bot.keyPress(81);
                bot.keyRelease(81);
                Thread.sleep(4000);
                //mainText.setText("Queue Accepted");
                System.out.println(found.w + " " + found.y);
                bot.mouseMove((int)old.getX(), (int)old.getY());
            }
            else{
                System.out.println("cant find");
            }
        } catch (FindFailed | InterruptedException e) {
            System.out.println(e);
        }
    }

    /**
     * setup the main GUI.
     */
    private static void setupUI() {
        ImagePath.add(System.getProperty("user.dir"));

        // frame setup
        JFrame frame = new JFrame();
        JPanel panel = new JPanel(new GridLayout(2,1));
        frame.setSize(300,200); 
        LayoutManager layout = new FlowLayout();  
        frame.setLayout(layout);
        frame.setTitle("QAccept");

        mainText = new JLabel("Disabled.", SwingConstants.CENTER);
        // toggle button setup
        JToggleButton toggle = new JToggleButton("OFF");
        toggle.setPreferredSize(new Dimension(150,50));
        toggle.setUI(new BasicToggleButtonUI()); // lets us set the background colour on event
        toggle.setBackground(OFF_COLOR);
        toggle.setFont(new Font("Mono", Font.BOLD, 20));
        toggle.setFocusPainted(false);

        ItemListener itemListener = new ItemListener() {
            public void itemStateChanged(ItemEvent itemEvent)
            {
                if (toggle.isSelected()) { // turning on
                    running = true;
                    //mainText.setText("Waiting for Queue...");
                    new Thread(new Runnable() { // we need to run this in a new thread so UI events can continue
                        public void run() {
                            while(running){ // run until the button is clicked off
                                search();
                            }
                        }
                    }).start();
                    toggle.setText("ON");
                    toggle.setBackground(ON_COLOR);
                }
                else { // turning off
                    running = false;
                    mainText.setText("Disabled.");
                    toggle.setText("OFF");
                    toggle.setBackground(OFF_COLOR);
                }
            }
        };
        toggle.addItemListener(itemListener);
        panel.add(mainText);
        panel.add(toggle);
        frame.add(panel);
        frame.setVisible(true);
    }
}
