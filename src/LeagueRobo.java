import java.awt.AWTException;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.LayoutManager;
import java.awt.Robot;
import java.awt.event.InputEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.plaf.basic.BasicToggleButtonUI;

import com.google.gson.Gson;
import com.tulskiy.keymaster.common.HotKey;
import com.tulskiy.keymaster.common.HotKeyListener;
import com.tulskiy.keymaster.common.Provider;

import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.opencv.core.Point;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.commons.io.IOUtils;
import org.apache.hc.core5.ssl.SSLContextBuilder;
import org.sikuli.script.FindFailed;
import org.sikuli.script.ImagePath;
import org.sikuli.script.Match;
import org.sikuli.script.Screen;

import Data.Data.AllPlayer;
import Data.Data.Root;

public class LeagueRobo {
    private static Color ON_COLOR = new Color(127, 227, 104);
    private static Color OFF_COLOR = new Color(224, 67, 104);
    private static boolean running = false;
    private static Screen s = new Screen();
    private static JLabel mainText;
    private static Robot bot;
    private static double last = 0;
    private static int lastLevel = 0;
    private static int ap;
    private static CloseableHttpClient client;
    private static Gson gs = new Gson();
    private static Root d;
    private static int spellLevel = 0;
    private static HttpUriRequest request;
    private static CloseableHttpResponse response;
    // key codes for ability levels
    private static final int Q = 81;
    private static final int W = 87;
    private static final int E = 69;
    private static final int R = 82;
    private static final int D = 70;
    private static final int F = 68;
    private static final int CTRL = 17;
    private static final int FIVE = 53;
    // ability level order
    private static int[] oDer = { Q, W, E, Q, Q, R, Q, W, Q, W, R, W, W, E, E, R, E, E };
    private static boolean firing = false;
    private static final Point ENEMY_SPAWN = new Point(2240, 841);
    private static final Point BOT_TOWER = new Point(2250, 990);
    private static Point nextMove = ENEMY_SPAWN;
    private static Boolean following = false;
    private static JToggleButton toggle;

    public static void main(String[] args) throws IOException, InterruptedException {

        Provider provider = Provider.getCurrentProvider(true);
        HotKeyListener startstop = new HotKeyListener() {
            @Override
            public void onHotKey(HotKey hotKey) {
                if (running) {
                    System.out.println("STOPPING");
                    running = false;
                    toggle.doClick();
                } else {
                    System.out.println("STARTING");
                    running = true;
                    toggle.doClick();
                }
            }
        };
        provider.register(KeyStroke.getKeyStroke("0"), startstop);
        HotKeyListener pathtoggle = new HotKeyListener() {
            @Override
            public void onHotKey(HotKey hotKey) {
                if(nextMove == ENEMY_SPAWN){
                    nextMove = BOT_TOWER;
                    System.out.println("changing bot route");
                }
                else{
                    nextMove = ENEMY_SPAWN;
                    System.out.println("changing default route");
                }
            }
        };
        provider.register(KeyStroke.getKeyStroke("9"), pathtoggle);
        HotKeyListener followset = new HotKeyListener() {
            @Override
            public void onHotKey(HotKey hotKey) {
                following = !following;
                System.out.println("toggle following");
            }
        };
        provider.register(KeyStroke.getKeyStroke("8"), followset);

        request = new HttpGet("https://127.0.0.1:2999/liveclientdata/allgamedata");
        try {
            client = HttpClients.custom().setSSLHostnameVerifier(new NoopHostnameVerifier())
                    .setSslcontext(
                            new SSLContextBuilder().loadTrustMaterial(null, (x509Certificates, s) -> true).build())
                    .build();
        } catch (KeyManagementException | NoSuchAlgorithmException | KeyStoreException e1) {
            e1.printStackTrace();
        }

        do {
            getData();
            waitFor(1000);
        } while (response == null);

        System.out.println("Connection made!");

        for (AllPlayer p : d.allPlayers) {
            if (p.championName == "Morgana") {
                ap = d.allPlayers.indexOf(p);
            }
        }

        setupUI();
        try {
            bot = new Robot();
        } catch (AWTException e) {
        }
    }

    /**
     * Query the client for game data.
     * @return
     */
    private static Boolean getData() {
        try {
            response = client.execute(request);
            d = gs.fromJson(IOUtils.toString(response.getEntity().getContent(), "UTF-8"), Root.class);
            response.close();
            return true;//(d.allPlayers == null);

        } catch (IOException e) {
            System.out.println(e);
            return null;
        }
    }

    /**
     * Wait for a given number of ms.
     * @param time
     */
    private static void waitFor(int time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Use sikuli to search for the accept button on the screen. If found, click it.
     */
    private static void searchEnemy() {
        try {
            Match found = s.find("/images/7.png");
            if (found != null) { // found
                firing = true;
                
                if(found.x > 1160 && found.x < 1200 && found.y > 350 && found.y < 390){
                    //System.out.println("ignoring, probably me");
                    return;
                }

                bot.mouseMove(found.x + 50, found.y + 100);

                bot.keyPress(Q);
                waitFor(100);
                bot.keyRelease(Q);
                waitFor(100);

                bot.keyPress(W);
                waitFor(100);
                bot.keyRelease(W);
                waitFor(100);

                if (spellLevel > 5) {
                    bot.keyPress(R);
                    waitFor(100);
                    bot.keyRelease(R);
                    waitFor(100);
                }
                firing = false;
                // System.out.println(found.w + " " + found.y);
                waitFor(1000);
            }
        } catch (FindFailed e) {
        }
    }

    /**
     * Shield allies on shield cool down.
     */
    private static void searchFriendly() {
        try {
            Match found = s.find("/images/f.png");
            if (found != null) { // found
                firing = true;

                bot.mouseMove(found.x + 50, found.y + 100);

                bot.keyPress(E);
                waitFor(100);
                bot.keyRelease(E);

                firing = false;

                waitFor(5000);
            }
        } catch (FindFailed e) {
        }
    }

    /**
     * Search for a friendly health bar to follow around.
     */
    private static Point searchFollow() {
        try {
            Match found = s.findText("/images/f2.png");
            if (found != null) { // found-
                return new Point(found.x + 10, found.y + 10);

            }
            return null;
        } catch (FindFailed e) {
            return null;
        }
    }

    /**
     * Perform a move by clicking the minimap.
     */
    private static void move() {
        if (nextMove == null) {
            System.out.println("default route");
            nextMove = ENEMY_SPAWN;
        }
        if (following) {
            Point found = searchFollow();
            if(found != null){
                nextMove = found;
            }
        }
        bot.mouseMove((int) nextMove.x, (int) nextMove.y);
        waitFor(100);
        bot.mousePress(InputEvent.BUTTON3_DOWN_MASK);
        waitFor(100);
        bot.mouseRelease(InputEvent.BUTTON3_DOWN_MASK);
        waitFor(100);
    }

    /**
     * Runaway and use summoner spells.
     */
    private static void retreat() {
        if (((last - d.activePlayer.championStats.currentHealth) / d.activePlayer.championStats.maxHealth > 0.15)
                && ((last - d.activePlayer.championStats.currentHealth)
                        / d.activePlayer.championStats.maxHealth < 0.50)) { // took damage
            System.out.println("retreating");
            bot.keyPress(D); // use summoners
            waitFor(100);
            bot.keyPress(D);
            waitFor(500);
            bot.keyPress(F);
            waitFor(100);
            bot.keyPress(F);
            runaway();
        } else if (((last - d.activePlayer.championStats.currentHealth)
                / d.activePlayer.championStats.maxHealth < 0.50)) { // took a lot of damage
            bot.keyPress(FIVE); // hour glass
            waitFor(100);
            bot.keyPress(FIVE);
        }
        last = d.activePlayer.championStats.currentHealth;
    }

    /**
     * Recall when we take a lot of damage.
     */
    private static void healthLow() {
        if (d.activePlayer.championStats.currentHealth < (d.activePlayer.championStats.maxHealth / 5.0)) {
            System.out.println("low health!");
            runaway();
            bot.keyPress(66);
            waitFor(100);
            bot.keyRelease(66);
            System.out.println("waiting For 12");
            waitFor(12000);
            buy();
        }
    }

    /**
     * Run away towards our nexus.
     */
    private static void runaway() {
        System.out.println("running away");
        bot.mouseMove(2028, 1052);
        bot.mousePress(InputEvent.BUTTON2_DOWN_MASK);
        waitFor(100);
        bot.mouseRelease(InputEvent.BUTTON2_DOWN_MASK);
        waitFor(4000);
    }

    /**
     * Buy the recommended items from the shop.
     */
    private static void buy() {
        System.out.println("buying");
        bot.keyPress(80);
        waitFor(100);
        bot.keyRelease(80);
        bot.mouseMove(740, 520);
        bot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
        waitFor(100);
        bot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
        waitFor(100);
        bot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
        waitFor(100);
        bot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
        bot.keyPress(80);
        bot.keyRelease(80);
    }

    /**
     * Level our skills according to the skill order.
     */
    private static void level() {
        System.out.println("leveling");
        bot.keyPress(CTRL);
        waitFor(200);
        bot.keyPress(oDer[spellLevel]);
        waitFor(200);
        bot.keyRelease(oDer[spellLevel]);
        waitFor(200);
        bot.keyRelease(CTRL);
        spellLevel++;
    }

    /**
     * setup the main GUI.
     */
    private static void setupUI() {
        ImagePath.add(System.getProperty("user.dir"));

        // frame setup
        JFrame frame = new JFrame();
        JPanel panel = new JPanel(new GridLayout(2, 1));
        frame.setSize(300, 200);
        LayoutManager layout = new FlowLayout();
        frame.setLayout(layout);
        frame.setTitle("QAccept");

        mainText = new JLabel("Disabled.", SwingConstants.CENTER);
        // toggle button setup
        toggle = new JToggleButton("OFF");
        toggle.setPreferredSize(new Dimension(150, 50));
        toggle.setUI(new BasicToggleButtonUI()); // lets us set the background colour on event
        toggle.setBackground(OFF_COLOR);
        toggle.setFont(new Font("Mono", Font.BOLD, 20));
        toggle.setFocusPainted(false);

        ItemListener itemListener = new ItemListener() {
            public void itemStateChanged(ItemEvent itemEvent) {
                if (toggle.isSelected()) { // turning on
                    running = true;
                    toggle.setText("ON");
                    toggle.setBackground(ON_COLOR);
                    waitFor(3000);
                    new Thread(new Runnable() { // enemy search
                        public void run() {
                            while (running) {
                                searchEnemy();
                            }
                        }
                    }).start();
                    new Thread(new Runnable() { // friendly search
                        public void run() {
                            while (running) {
                                searchFriendly();
                            }
                        }
                    }).start();
                    new Thread(new Runnable() { // main loop
                        public void run() {
                            buy();
                            while (running) { // run until the button is clicked off
                                if (!getData()) {
                                    System.out.println("stopping");
                                    running = false;
                                    break;
                                }
                                if (d.gameData.gameTime > 70.0 && d.activePlayer.championStats.currentHealth >= 1) { // alive
                                    if (d.allPlayers.get(ap).level != lastLevel) {
                                        level();
                                    }

                                    healthLow();
                                    retreat();

                                    if (!firing) {
                                        move();
                                    }
                                    lastLevel = d.allPlayers.get(ap).level;
                                }
                                waitFor(200);
                            }
                        }
                    }).start();
                } else { // turning off
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
