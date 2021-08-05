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

public class LeagueUtilsCore {
    private static Color ON_COLOR = new Color(127, 227, 104);
    private static Color OFF_COLOR = new Color(224,  67, 104);
    private static boolean running = false;
    private static Screen s = new Screen();
    private static JLabel mainText;
    private static Robot bot;
    public static void main(String[] args) {
        search();
    }

    /**
     * Use sikuli to search for the accept button on the screen. If found, click it.
     * @return location of the enemy champion, null if none found
     */
    private static Point search() {
        try {
            Match found = s.find("/images/5.png");
            if(found != null){ // found
                System.out.println(found.w + " " + found.y);
                return new Point(found.x + 50, found.y + 100);
            }
            else{
                System.out.println("cant find");
                return null;
            }
        } catch (FindFailed e) {
            System.out.println(e);
            return null;
        }
    }
}
