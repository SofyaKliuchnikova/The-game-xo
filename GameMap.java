import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Random;

public class GameMap extends JPanel {

    public static final int GAME_HH = 0;
    public static final int GAME_HAi = 1;

    public static int human_dot = 1;
    public static int ai_dot = 2;
    public static int empty_dot = 0;


    public static Random random = new Random();

    private int gameMode;
    private int fieldSizeX;
    private int fieldSizeY;
    private int winLen;

    private int cellWidth;
    private int cellHeight;

    private int[][] field;

    private boolean isGameOver;
    private final int STATE_DRAW = 0;
    private final int HUMAN_WIN = 1;
    private final int AI_WIN = 2;
    private int stateGameOver;

    private boolean isInitMap;


    GameMap() {

        setBackground(Color.gray);
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                super.mouseReleased(e);
                update(e);
            }
        });
        isInitMap = false;
    }

    void startNewGame(int fieldSizeX, int fieldSizeY, int winLen) {
        this.fieldSizeX = fieldSizeX;
        this.fieldSizeY = fieldSizeY;
        this.winLen = winLen;
        this.field = new int[fieldSizeY][fieldSizeX];
        this.isGameOver = false;
        isInitMap = true;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        render(g);
    }

    private void update(MouseEvent e) {

        if (isGameOver) return;
        if (!isInitMap) return;

        int cellX = e.getX() / cellWidth;
        int cellY = e.getY() / cellHeight;

        if (!isValidCell(cellX, cellY) || !isEmptyCell(cellX, cellY)) {
            return;
        }
        field[cellY][cellX] = human_dot;

        if (win(human_dot)) {
            setGameOver(HUMAN_WIN);
            return;
        }
        if (fullMap()) {
            setGameOver(STATE_DRAW);
            return;
        }

        aiTurn();
        repaint();

        if (win(ai_dot)) {
            setGameOver(AI_WIN);
            return;
        }
        if (fullMap()) {
            setGameOver(STATE_DRAW);
            return;
        }
    }

    private void setGameOver(int gameOverState) {
        isGameOver = true;
        stateGameOver = gameOverState;
        repaint();
    }


    private void render(Graphics g) {

        if (!isInitMap) return;

        int width = getWidth();
        int height = getHeight();

        cellWidth = width / fieldSizeX;
        cellHeight = height / fieldSizeY;

        g.setColor(Color.black);

        for (int i = 0; i < fieldSizeY; i++) {
            int y = i * cellHeight;
            g.drawLine(0, y, width, y);
        }

        for (int i = 0; i < fieldSizeX; i++) {
            int x = i * cellWidth;
            g.drawLine(x, 0, x, height);
        }

        for (int y = 0; y < fieldSizeY; y++) {
            for (int x = 0; x < fieldSizeX; x++) {
                if (isEmptyCell(x, y)) continue;
                if (field[y][x] == human_dot) {
                    g.setColor(Color.red);
                    g.fillOval(x * cellWidth, y * cellHeight, cellWidth, cellHeight);
                } else if (field[y][x] == ai_dot) {
                    g.setColor(Color.blue);
                    g.fillOval(x * cellWidth, y * cellHeight, cellWidth, cellHeight);
                } else {
                    throw new RuntimeException("Не получилось отрисовать у=" + y + " и х=" + x);
                }
            }
        }
        if (isGameOver) {
            showMessageGameOver(g);
        }
    }

    private void showMessageGameOver(Graphics g) {
        g.setColor(Color.darkGray);
        g.fillRect(0, 170, getWidth(), 80);
        g.setColor(Color.cyan);
        g.setFont(new Font("Calibri", Font.BOLD, 50));
        switch (stateGameOver) {
            case STATE_DRAW:
                g.drawString("Ничья", 160, getHeight() / 2);
                break;
            case HUMAN_WIN:
                g.drawString("Ты победил", 110, getHeight() / 2);
                break;
            case AI_WIN:
                g.drawString("Ты проиграл", 100, getHeight() / 2);
                break;
            default:
                throw new RuntimeException("Не получилось вывести сообщение");
        }
    }


    public boolean isEmptyCell(int x, int y) {
        return field[y][x] == empty_dot;
    }

    public boolean isValidCell(int x, int y) {
        return x >= 0 && x < fieldSizeX && y >= 0 && y < fieldSizeY;
    }

    public boolean fullMap() {
        for (int y = 0; y < fieldSizeY; y++) {
            for (int x = 0; x < fieldSizeX; x++) {
                if (field[y][x] == empty_dot) {
                    return false;
                }
            }
        }
        return true;
    }

    public void aiTurn() {
        if (turnAIWinCell()) {
            return;
        }
        if (turnHumanWinCel()) {
            return;
        }

        int x;
        int y;
        do {
            x = random.nextInt(fieldSizeX);
            y = random.nextInt(fieldSizeY);
        } while (!isEmptyCell(x, y));
        field[y][x] = ai_dot;
    }

    private boolean turnAIWinCell() {
        for (int i = 0; i < fieldSizeY; i++) {
            for (int j = 0; j < fieldSizeX; j++) {
                if (isEmptyCell(j, i)) {
                    field[i][j] = ai_dot;
                    if (win(ai_dot)) {
                        return true;
                    }
                    field[i][j] = empty_dot;
                }
            }
//
        }
        return false;
    }

    private boolean turnHumanWinCel() {
        for (int i = 0; i < fieldSizeY; i++) {
            for (int j = 0; j < fieldSizeX; j++) {
                if (isEmptyCell(j, i)) {
                    field[i][j] = human_dot;
                    if (win(human_dot)) {
                        field[i][j] = ai_dot;
                        return true;
                    }
                    field[i][j] = empty_dot;
                }
            }

        }
        return false;
    }

    public boolean win(int currentPlayer) {
        for (int y = 0; y < fieldSizeY; y++) {
            for (int x = 0; x < fieldSizeX; x++) {
                if (vector(y, x, 1, 0, currentPlayer, winLen)) return true;  //горизонталь
                if (vector(y, x, 1, 1, currentPlayer, winLen)) return true;  //диагональ правая
                if (vector(y, x, 0, 1, currentPlayer, winLen)) return true;  //вертикаль
                if (vector(y, x, 1, -1, currentPlayer, winLen)) return true; //диагональ левая
            }
        }
        return false;
    }

    public boolean vector(int y, int x, int vy, int vx, int currentPlayer, int vectorLength) {
        int end_y = y + (vectorLength - 1) * vy;
        int end_x = x + (vectorLength - 1) * vx;
        if (!isValidCell(end_y, end_x)) return false;
        for (int i = 0; i < vectorLength; i++) {
            if (field[y + i * vy][x + i * vx] != currentPlayer) return false;
        }
        return true;
    }


}
