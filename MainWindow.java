import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainWindow extends JFrame {
    private static final int WINDOW_WIDTH = 500;
    private static final int WINDOW_HEIGHT = 500;
    private static final int WINDOW_POS_X = 500;
    private static final int WINDOW_POS_Y = 300;


    private SettingsWindow settingsWindow;
    private GameMap gameMap;

    MainWindow() {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        setLocation(WINDOW_POS_X, WINDOW_POS_Y);
        setTitle("Крестики-нолики");
        setResizable(false);

        settingsWindow = new SettingsWindow(this);
        gameMap = new GameMap();

        JButton btnStartGame = new JButton("Начать игру");
        btnStartGame.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                settingsWindow.setVisible(true);
            }
        });

        JButton btnExitGame = new JButton("Выйти из игры");
        btnExitGame.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });


        JPanel panelButtons = new JPanel();
        panelButtons.setLayout(new GridLayout(1, 2));
        panelButtons.add(btnStartGame);
        panelButtons.add(btnExitGame);

        add(panelButtons, BorderLayout.SOUTH);
        add(gameMap);

        setVisible(true);
    }

    void startNewGame(int fieldSizeX, int fieldSizeY, int winLength) {
        gameMap.startNewGame(fieldSizeX, fieldSizeY, winLength);
    }
}
