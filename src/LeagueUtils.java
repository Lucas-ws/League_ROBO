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
import java.net.http.HttpClient;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.SwingConstants;
import javax.swing.plaf.basic.BasicToggleButtonUI;

import com.google.gson.Gson;

import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
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

public class LeagueUtils {
    private static Color ON_COLOR = new Color(127, 227, 104);
    private static Color OFF_COLOR = new Color(224, 67, 104);
    private static boolean running = false;
    private static Screen s = new Screen();
    private static JLabel mainText;
    private static Robot bot;
    private static boolean retreating;
    private static double last = 0;
    private static int lastLevel = 0;
    private static int ap;
    private static CloseableHttpClient client;
    private static Gson gs = new Gson();
    private static Root d;
    private static Boolean valid = false;
    private static int spellLevel = 0;
    private static HttpUriRequest request;
    private static CloseableHttpResponse response;
    private static int[] order = { 81, 87, 69, 81, 81, 82, 81, 87, 81, 87, 82, 87, 87, 69, 69, 82, 69, 69 };
    private static boolean firing = false;

    public static void main(String[] args) throws IOException, InterruptedException {
        // client = HttpClients
        // .custom()
        // .setSSLHostnameVerifier(NoopHostnameVerifier.INSTANCE)
        // .setHostnameVerifier(new AllowAllHostnameVerifier())
        // .build();
        request = new HttpGet("https://127.0.0.1:2999/liveclientdata/allgamedata");
        try {
            client = HttpClients.custom().setSSLHostnameVerifier(new NoopHostnameVerifier())
                    .setSslcontext(
                            new SSLContextBuilder().loadTrustMaterial(null, (x509Certificates, s) -> true).build())
                    .build();
        } catch (KeyManagementException | NoSuchAlgorithmException | KeyStoreException e1) {
            e1.printStackTrace();
        }

        response = client.execute(request);
        d = gs.fromJson(IOUtils.toString(response.getEntity().getContent(), "UTF-8"), Root.class);
        response.close();
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
     * Use sikuli to search for the accept button on the screen. If found, click it.
     */
    private static void search() {
        try {
            Match found = s.find("/images/5.png");
            if (found != null) { // found
                firing = true;
                bot.mouseMove(found.x + 50, found.y + 100);

                bot.keyPress(81);
                Thread.sleep(100);
                bot.keyRelease(81);
                Thread.sleep(100);

                bot.keyPress(87);
                Thread.sleep(100);
                bot.keyRelease(87);
                Thread.sleep(100);
                if (spellLevel > 5) {
                    bot.keyPress(82);
                    Thread.sleep(100);
                    bot.keyRelease(82);
                    Thread.sleep(100);
                }
                firing = false;
                // System.out.println(found.w + " " + found.y);
                Thread.sleep(1000);
            } else {
                // System.out.println("cant find");
            }
        } catch (FindFailed | InterruptedException e) {
            // System.out.println(e);
        }
    }

    private static void searchfriendly() {
        try {
            Match found = s.find("/images/f2.png");
            if (found != null) { // found
                bot.mouseMove(found.x + 50, found.y + 100);

                bot.keyPress(69);
                Thread.sleep(100);
                bot.keyRelease(69);

                Thread.sleep(5000);
            } else {
                // System.out.println("cant find");
            }
        } catch (FindFailed | InterruptedException e) {
            // System.out.println(e);
        }
    }

    private static void move() {
        bot.mouseMove(2240, 841);
        // bot.mouseMove(2250, 990);
        try {
            Thread.sleep(100);
            bot.mousePress(InputEvent.BUTTON3_DOWN_MASK);
            Thread.sleep(100);
            bot.mouseRelease(InputEvent.BUTTON3_DOWN_MASK);
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static Boolean retreat() {
        Boolean b = (last - d.activePlayer.championStats.currentHealth) / d.activePlayer.championStats.maxHealth > 0.15;
        last = d.activePlayer.championStats.currentHealth;
        return b;
    }

    private static boolean healthLow() {
        return d.activePlayer.championStats.currentHealth < (d.activePlayer.championStats.maxHealth / 5.0);
    }

    private static void runaway() {
        System.out.println("running away");
        bot.mouseMove(2028, 1052);
        bot.mousePress(InputEvent.BUTTON2_DOWN_MASK);
        try {
            Thread.sleep(100);
            bot.mouseRelease(InputEvent.BUTTON2_DOWN_MASK);
            Thread.sleep(4000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static void buy() {
        try {
            System.out.println("buying");
            bot.keyPress(80);
            Thread.sleep(100);
            bot.keyRelease(80);
            bot.mouseMove(740, 520);
            bot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
            Thread.sleep(100);
            bot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
            Thread.sleep(100);
            bot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
            Thread.sleep(100);
            bot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
            bot.keyPress(80);
            bot.keyRelease(80);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * setup the main GUI.a
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
        JToggleButton toggle = new JToggleButton("OFF");
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
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e1) {
                        e1.printStackTrace();
                    }
                    new Thread(new Runnable() { // we need to run this in a new thread so UI events can continue
                        public void run() {
                            while (running) { // run until the button is clicked off
                                search();
                            }
                        }
                    }).start();
                    new Thread(new Runnable() { // we need to run this in a new thread so UI events can continue
                        public void run() {
                            while (running) { // run until the button is clicked off
                                searchfriendly();
                            }
                        }
                    }).start();
                    new Thread(new Runnable() { // we need to run this in a new thread so UI events can continue
                        public void run() {
                            buy();
                            while (running) { // run until the button is clicked off
                                try {
                                    response = client.execute(request);
                                    d = gs.fromJson(IOUtils.toString(response.getEntity().getContent(), "UTF-8"),
                                            Root.class);
                                    response.close();
                                    if (d == null) {
                                        continue;
                                    }

                                } catch (IOException e) {
                                    e.printStackTrace();
                                    System.out.println("stopping");
                                    running = false;
                                }
                                // d.gameData.gameTime > 70.0 &&
                                if (d.gameData.gameTime > 70.0 && d.activePlayer.championStats.currentHealth >= 1) {
                                    if (d.allPlayers.get(ap).level != lastLevel) {
                                        try {
                                            System.out.println("leveling");
                                            bot.keyPress(17);
                                            Thread.sleep(200);
                                            bot.keyPress(order[spellLevel]);
                                            Thread.sleep(200);
                                            bot.keyRelease(order[spellLevel]);
                                            Thread.sleep(200);
                                            bot.keyRelease(17);
                                            spellLevel++;
                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                        }

                                    }
                                    if (healthLow()) {
                                        System.out.println("low health!");
                                        try {
                                            runaway();
                                            bot.keyPress(66);
                                            Thread.sleep(100);
                                            bot.keyRelease(66);
                                            System.out.println("waiting 10");
                                            Thread.sleep(12000);
                                        } catch (InterruptedException e) {
                                        }
                                        buy();
                                    } else if (retreat()) {
                                        System.out.println("retreating");
                                        bot.keyPress(70);
                                        try {
                                            Thread.sleep(100);
                                            bot.keyPress(70);
                                            Thread.sleep(500);
                                            bot.keyPress(68);
                                            Thread.sleep(100);
                                            bot.keyPress(68);
                                        } catch (InterruptedException e) {
                                        }
                                        runaway();
                                    } else {
                                        if (!firing) {
                                            move();
                                        }
                                    }
                                    lastLevel = d.allPlayers.get(ap).level;
                                }
                                try {
                                    Thread.sleep(200);
                                } catch (InterruptedException e) {
                                }
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
