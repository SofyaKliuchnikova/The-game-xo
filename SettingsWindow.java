import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SettingsWindow extends JFrame {

    private static final int WINDOW_WIDTH = 400;
    private static final int WINDOW_HEIGHT = 400;

    private static final int MIN_FIELD_SIZE = 3;
    private static final int MAX_FIELD_SIZE = 10;
    private static final int MIN_WIN_LENGTH = 3;

    private JSlider sliderFieldSize;
    private JSlider winLength;

    private MainWindow mainWindow;

    SettingsWindow(MainWindow mainWindow) {
        this.mainWindow = mainWindow;
        setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        setTitle("Настройки новой игры");

        Rectangle rectangle = mainWindow.getBounds();
        int posX = (int) rectangle.getCenterX() - WINDOW_WIDTH / 2;
        int posY = (int) rectangle.getCenterY() - WINDOW_HEIGHT / 2;
        setLocation(posX, posY);
        setResizable(false);
        getRootPane().setBorder(BorderFactory.createEmptyBorder(0, 20, 10, 20));

        setLayout(new GridLayout(8, 1));

        addFieldControl();

        JButton btnStart = new JButton("Применить и начать игру!");
        btnStart.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                buttonStartClick();
            }
        });

        add(btnStart);
        add(btnStart, BorderLayout.SOUTH);
    }


    private void addFieldControl() {
        add(new JLabel("Вы будете играть против компьютера"));
        JLabel lbFieldSize = new JLabel("Поле " + MIN_FIELD_SIZE + "x" + MIN_FIELD_SIZE);
        JLabel lbWinLength = new JLabel("Собрать " + MIN_WIN_LENGTH + " подряд");

        sliderFieldSize = new JSlider(MIN_FIELD_SIZE, MAX_FIELD_SIZE, MIN_FIELD_SIZE);
        sliderFieldSize.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                int currentValue = sliderFieldSize.getValue();
                lbFieldSize.setText("Поле " + currentValue + "x" + currentValue);
                winLength.setMaximum(currentValue);
            }
        });


        winLength = new JSlider(MIN_WIN_LENGTH, MIN_FIELD_SIZE, MIN_FIELD_SIZE);
        winLength.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                lbWinLength.setText("Собрать " + winLength.getValue() + " подряд");
            }
        });

        add(new JLabel("Выберите размер поля"));
        add(lbFieldSize);
        add(sliderFieldSize);
        add(new JLabel("Выберите длину выигрышной комбинации"));
        add(lbWinLength);
        add(winLength);

    }

    private void buttonStartClick() {

        int fieldSize = sliderFieldSize.getValue();
        int winLen = winLength.getValue();

        mainWindow.startNewGame(fieldSize, fieldSize, winLen);

        setVisible(false);
    }

}
