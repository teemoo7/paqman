package ch.teemoo.paqman;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.Timer;

import ch.teemoo.paqman.models.Bonus;
import ch.teemoo.paqman.models.Character;
import ch.teemoo.paqman.models.Direction;
import ch.teemoo.paqman.models.Game;
import ch.teemoo.paqman.models.Ghost;
import ch.teemoo.paqman.models.Pacman;

public class Board extends JPanel implements ActionListener {
    private static final int TIMER_DELAY_MILLISECONDS = 40;

    private static final Font FONT = new Font("Helvetica", Font.BOLD, 14);
    private static final Color TEXT_COLOR = new Color(245, 158, 3);
    private static final Color DOTS_COLOR = new Color(245, 158, 3);
    private static final Color MAZE_COLOR = new Color(5, 73, 100);
    private static final Color BACKGROUND_COLOR = Color.black;

    public static final int BLOCK_SIZE = 48;
    private static final int BLOCK_HALF_SIZE = BLOCK_SIZE / 2;
    public static final int HORIZONTAL_SCREEN_BLOCKS_AMOUNT = 25;
    public static final int VERTICAL_SCREEN_BLOCKS_AMOUNT = 20;
    private static final int HORIZONTAL_SCREEN_SIZE = HORIZONTAL_SCREEN_BLOCKS_AMOUNT * BLOCK_SIZE;
    private static final int VERTICAL_SCREEN_SIZE = VERTICAL_SCREEN_BLOCKS_AMOUNT * BLOCK_SIZE;
    private static final int DOT_SIZE = 4;
    private static final int DOT_POS = (BLOCK_SIZE - DOT_SIZE) / 2;
    private static final int MAZE_STROKE = 2;
    private static final int LEGEND_MARGIN = 10;

    private static final int CYCLES_BETWEEN_PACMAN_ANIMATION = 2;
    private static final int PACMAN_ANIMATION_IMAGES_COUNT = 3;

    private static final int INITIAL_GHOSTS_COUNT = 4;
    private static final int MAX_GHOSTS_COUNT = 7;
    private static final int GHOSTS_INITIAL_BLOCK_POS_X = 12;
    private static final int GHOSTS_INITIAL_BLOCK_POS_Y = 7;
    private static final int[] GHOSTS_VALID_SPEEDS = {2, 3, 4, 6, 8, 12, 16};

    private static final int PACMAN_INITIAL_SPEED = 8;
    private static final int PACMAN_INITIAL_BLOCK_POS_X = 12;
    private static final int PACMAN_INITIAL_BLOCK_POS_Y = 13;
    private static final int INITIAL_PACMAN_LIVES = 3;

    private static final int BONUS_BLOCK_POS_X = GHOSTS_INITIAL_BLOCK_POS_X;
    private static final int BONUS_BLOCK_POS_Y = GHOSTS_INITIAL_BLOCK_POS_Y;
    private static final int BONUS_VALUE = 50;
    private static final int BONUS_INITIAL_TIME_TO_LIVE = 1000;
    private static final int LEVEL_COMPLETE_BONUS_VALUE = 100;
    public static final double BONUS_PROBABILITY = 0.002;

    private int pacmanAnimationCount = CYCLES_BETWEEN_PACMAN_ANIMATION;
    private int pacmanAnimationImageDirection = 1;
    private int pacmanAnimationImageNumber = 0;

    private Image ghostImgUp, ghostImgLeft, ghostImgRight, ghostImgDown;
    private Image pacmanUpImg1, pacmanLeftImg1, pacmanRightImg1, pacmanDownImg1;
    private Image pacmanUpImg2, pacmanLeftImg2, pacmanRightImg2, pacmanDownImg2;
    private Image pacmanUpImg3, pacmanLeftImg3, pacmanRightImg3, pacmanDownImg3;
    private Image bonusImg;

    private Direction requestedDirection;

    private final Game game = new Game();

    /**
     *          0   00000000
     * Left     1   00000001
     * Top      2   00000010
     * Right    4   00000100
     * Bottom   8   00001000
     * Dot      16  00010000
     * ??       32  00100000
     * ??       48  00110000
     */

    private static final short[] LEVEL_DATA = {
        19, 26, 26, 26, 26, 18, 26, 26, 26, 26, 26, 22, 7, 19, 26, 26, 26, 26, 26, 18, 26, 26, 26, 26, 22, 21, 3, 2, 2, 6, 21, 3, 2, 2, 2, 6, 21, 5, 21, 3, 2, 2, 2, 6, 21, 3, 2, 2, 6, 21, 21, 9, 8, 8, 12, 21, 9, 8, 8, 8, 12, 21, 13, 21, 9, 8, 8, 8, 12, 21, 9, 8, 8, 12, 21, 17, 26, 26, 26, 26, 16, 26, 26, 18, 26, 26, 24, 26, 24, 26, 26, 18, 26, 26, 16, 26, 26, 26, 26, 20, 21, 3, 2, 2, 6, 21, 3, 6, 21, 3, 2, 2, 2, 2, 2, 6, 21, 3, 6, 21, 3, 2, 2, 6, 21, 21, 9, 8, 8, 12, 21, 1, 4, 21, 9, 8, 8, 0, 8, 8, 12, 21, 1, 4, 21, 9, 8, 8, 12, 21, 25, 26, 26, 26, 26, 20, 1, 4, 25, 26, 26, 22, 13, 19, 26, 26, 28, 1, 4, 17, 26, 26, 26, 26, 28, 3, 2, 2, 2, 6, 21, 1, 0, 10, 10, 14, 17, 18, 20, 11, 10, 10, 0, 4, 21, 3, 2, 2, 2, 6, 1, 0, 0, 0, 4, 21, 1, 4, 19, 26, 26, 24, 24, 24, 26, 26, 22, 1, 4, 21, 1, 0, 0, 0, 4, 9, 8, 8, 8, 12, 21, 9, 12, 21, 11, 10, 10, 2, 10, 10, 14, 21, 9, 12, 21, 9, 8, 8, 8, 12, 19, 26, 26, 26, 26, 16, 26, 26, 24, 26, 26, 22, 5, 19, 26, 26, 24, 26, 26, 16, 26, 26, 26, 26, 22, 21, 3, 2, 2, 6, 21, 3, 2, 2, 2, 6, 21, 5, 21, 3, 2, 2, 2, 6, 21, 3, 2, 2, 6, 21, 21, 9, 8, 0, 4, 21, 9, 8, 8, 8, 12, 21, 13, 21, 9, 8, 8, 8, 12, 21, 1, 0, 8, 12, 21, 25, 26, 22, 1, 4, 17, 26, 26, 18, 26, 26, 24, 26, 24, 26, 26, 18, 26, 26, 20, 1, 4, 19, 26, 28, 3, 6, 21, 1, 4, 21, 3, 6, 21, 3, 2, 2, 2, 2, 2, 6, 21, 3, 6, 21, 1, 4, 21, 3, 6, 9, 12, 21, 9, 12, 21, 1, 4, 21, 9, 8, 8, 0, 8, 8, 12, 21, 1, 4, 21, 9, 12, 21, 9, 12, 19, 26, 24, 26, 26, 28, 1, 4, 25, 26, 26, 22, 5, 19, 26, 26, 28, 1, 4, 25, 26, 26, 24, 26, 22, 21, 3, 2, 2, 2, 2, 0, 0, 2, 2, 6, 21, 5, 21, 3, 2, 2, 0, 0, 2, 2, 2, 2, 6, 21, 21, 9, 8, 8, 8, 8, 8, 8, 8, 8, 12, 21, 13, 21, 9, 8, 8, 8, 8, 8, 8, 8, 8, 12, 21, 25, 26, 26, 26, 26, 26, 26, 26, 26, 26, 26, 24, 26, 24, 26, 26, 26, 26, 26, 26, 26, 26, 26, 26, 28
    };

    private final short[] screenData = new short[HORIZONTAL_SCREEN_BLOCKS_AMOUNT * VERTICAL_SCREEN_BLOCKS_AMOUNT];
    private Timer timer;

    public Board() {
        loadImages();
        initTimer();
        initBoard();
    }

    private void initBoard() {
        addKeyListener(new PacmanKeysAdapter());
        setFocusable(true);
        setBackground(Color.black);
    }

    private void initTimer() {
        timer = new Timer(TIMER_DELAY_MILLISECONDS, this);
        timer.start();
    }

    @Override
    public void addNotify() {
        super.addNotify();
        initGame();
    }

    private void playGame(Game game, Graphics2D g2d) {
        if (game.isPacmanCaught()) {
            //todo: move this in check collisions
            killPacman(game, g2d);
        } else {
            computeBonus(game);
            if (game.getBonus() != null) {
                drawBonus(game.getBonus(), g2d);
            }

            movePacman(game);
            drawPacman(game.getPacman(), g2d);

            moveGhosts(game);
            drawGhosts(game.getGhosts(), g2d);

            checkCollisions(game);
            checkLevelComplete(game);
        }
    }

    private void checkCollisions(Game game) {
        var pacman = game.getPacman();
        for (var ghost: game.getGhosts()) {
            if (pacman.getPosX() > (ghost.getPosX() - BLOCK_HALF_SIZE) && pacman.getPosX() < (ghost.getPosX() + BLOCK_HALF_SIZE)
                && pacman.getPosY() > (ghost.getPosY() - BLOCK_HALF_SIZE) && pacman.getPosY() < (ghost.getPosY() + BLOCK_HALF_SIZE)
                && game.isGameStarted()) {
                game.setPacmanCaught(true);
                break;
            }
        }
        final var bonus = game.getBonus();
        if (bonus != null) {
            if (pacman.getPosX() > (bonus.getPosX() - BLOCK_HALF_SIZE) && pacman.getPosX() < (bonus.getPosX() + BLOCK_HALF_SIZE)
                && pacman.getPosY() > (bonus.getPosY() - BLOCK_HALF_SIZE) && pacman.getPosY() < (bonus.getPosY() + BLOCK_HALF_SIZE)
                && game.isGameStarted()) {
                game.incrementScore(bonus.getValue());
                game.setBonus(null);
            }
        }
    }

    private void drawBonus(Bonus bonus, Graphics2D g2d) {
        int margin = MAZE_STROKE / 2;
        var marginX = margin + (BLOCK_SIZE - bonusImg.getWidth(this)) / 2;
        var marginY = margin + (BLOCK_SIZE - bonusImg.getHeight(this)) / 2;
        g2d.drawImage(bonusImg, bonus.getPosX() + marginX, bonus.getPosY() + marginY, this);
    }

    private void computeBonus(Game game) {
        final var bonus = game.getBonus();
        if (bonus != null) {
            bonus.decrementTimeToLive();
            if (bonus.getTimeToLive() <= 0) {
                game.setBonus(null);
            }
        } else {
            if (Math.random() < BONUS_PROBABILITY) {
                game.setBonus(
                    new Bonus(BONUS_BLOCK_POS_X * BLOCK_SIZE, BONUS_BLOCK_POS_Y * BLOCK_SIZE, BONUS_VALUE,
                        BONUS_INITIAL_TIME_TO_LIVE / game.getGhostsCount()));
            }
        }
    }

    private void displayStartMessage(Graphics2D g2d) {
        displayMessage("Press <ENTER> to start", g2d);
    }

    private void displayGameOverMessage(Graphics2D g2d) {
        displayMessage("Game over", g2d);
    }

    private void displayMessage(String message, Graphics2D g2d) {
        g2d.setColor(BACKGROUND_COLOR);
        g2d.fillRect(50, VERTICAL_SCREEN_SIZE / 2 - 30, HORIZONTAL_SCREEN_SIZE - 100, 50);
        g2d.setColor(MAZE_COLOR);
        g2d.drawRect(50, VERTICAL_SCREEN_SIZE / 2 - 30, HORIZONTAL_SCREEN_SIZE - 100, 50);

        final FontMetrics fontMetrics = this.getFontMetrics(FONT);

        g2d.setColor(TEXT_COLOR);
        g2d.setFont(FONT);
        g2d.drawString(message, (HORIZONTAL_SCREEN_SIZE - fontMetrics.stringWidth(message)) / 2, VERTICAL_SCREEN_SIZE / 2);
    }

    private void drawScoreAndLives(Game game, Graphics2D g) {
        g.setFont(FONT);
        g.setColor(TEXT_COLOR);
        g.drawString("Score: " + game.getScore(), HORIZONTAL_SCREEN_SIZE / 2 + 100, VERTICAL_SCREEN_SIZE + 15);

        for (int i = 0; i < game.getPacman().getLives(); i++) {
            g.drawImage(pacmanLeftImg3, i * pacmanLeftImg3.getWidth(this) + LEGEND_MARGIN, VERTICAL_SCREEN_SIZE + LEGEND_MARGIN, this);
        }
    }

    private void checkLevelComplete(Game game) {
        boolean isLevelComplete = true;

        for (int data: screenData) {
            if ((data & 48) != 0) {
                isLevelComplete = false;
                break;
            }
        }

        if (isLevelComplete) {
            game.incrementScore(LEVEL_COMPLETE_BONUS_VALUE);
            if (game.getGhostsCount() < MAX_GHOSTS_COUNT) {
                game.incrementGhostsCount();
            }
            if (game.getGhostsTopSpeedIndex() < GHOSTS_VALID_SPEEDS.length - 1) {
                game.incrementCurrentTopSpeed();
            }
            initScreenDataMaze(game);
        }
    }

    private void killPacman(Game game, Graphics2D g2d) {
        var pacman = game.getPacman();
        pacman.decrementLives();

        if (pacman.getLives() == 0) {
            displayGameOverMessage(g2d);
            game.setGameStarted(false);
        } else {
            setInitialPositionsForLevel(game);
        }
    }

    private void moveGhosts(Game game) {
        for (var ghost: game.getGhosts()) {
            if (isPositionCompleteBlock(ghost)) {
                int screenDataPos = ghost.getPosX() / BLOCK_SIZE + HORIZONTAL_SCREEN_BLOCKS_AMOUNT * (ghost.getPosY() / BLOCK_SIZE);

                List<Direction> eligibleDirections = new ArrayList<>();

                final short screenDataValue = screenData[screenDataPos];
                final Direction direction = ghost.getMovingDirection();

                if (!isLeftWall(screenDataValue) && direction != Direction.RIGHT) {
                    eligibleDirections.add(Direction.LEFT);
                }
                if (!(isTopWall(screenDataValue)) && direction != Direction.DOWN) {
                    eligibleDirections.add(Direction.UP);
                }
                if (!(isRightWall(screenDataValue)) && direction != Direction.LEFT) {
                    eligibleDirections.add(Direction.RIGHT);
                }
                if (!(isBottomWall(screenDataValue)) && direction != Direction.UP) {
                    eligibleDirections.add(Direction.DOWN);
                }

                if (eligibleDirections.isEmpty()) {
                    if (isSurroundedByWalls(screenDataValue)) {
                        ghost.setMovingDirection(Direction.NONE);
                    } else {
                        ghost.setMovingDirection(getOppositeDirection(direction));
                    }
                } else {
                    int index = (int) (Math.random() * eligibleDirections.size());
                    ghost.setMovingDirection(eligibleDirections.get(index));
                }

            }

            ghost.move();
        }
    }

    private boolean isPositionCompleteBlock(Character character) {
        return character.getPosX() % BLOCK_SIZE == 0 && character.getPosY() % BLOCK_SIZE == 0;
    }

    private void drawGhosts(List<Ghost> ghosts, Graphics2D g2d) {
        int margin = MAZE_STROKE / 2;
        var marginX = margin + (BLOCK_SIZE - ghostImgLeft.getWidth(this)) / 2;
        var marginY = margin + (BLOCK_SIZE - ghostImgLeft.getHeight(this)) / 2;
        for (var ghost: ghosts) {
            g2d.drawImage(getGhostImage(ghost), ghost.getPosX() + marginX, ghost.getPosY() + marginY, this);
        }
    }

    private Image getGhostImage(Ghost ghost) {
        Image ghostImage;
        switch (ghost.getMovingDirection()) {
        case UP:
            ghostImage = ghostImgUp;
            break;
        case DOWN:
            ghostImage = ghostImgDown;
            break;
        case RIGHT:
            ghostImage = ghostImgRight;
            break;
        case LEFT:
        default:
            ghostImage = ghostImgLeft;
        }
        return ghostImage;
    }

    private void movePacman(Game game) {
        var pacman = game.getPacman();

        // Compute whether pacman direction must be changed
        if (requestedDirection != Direction.NONE) {
            if ((requestedDirection == Direction.LEFT && pacman.getMovingDirection() == Direction.RIGHT)
                || (requestedDirection == Direction.RIGHT && pacman.getMovingDirection() == Direction.LEFT)
                || (requestedDirection == Direction.UP && pacman.getMovingDirection() == Direction.DOWN)
                || (requestedDirection == Direction.DOWN && pacman.getMovingDirection() == Direction.UP)) {

                pacman.setMovingDirection(requestedDirection);
                pacman.setLookingDirection(requestedDirection);
            }
        }

        if (isPositionCompleteBlock(pacman)) {
            int screenDataIndex = pacman.getPosX() / BLOCK_SIZE + HORIZONTAL_SCREEN_BLOCKS_AMOUNT * (pacman.getPosY() / BLOCK_SIZE);
            short currentScreenData = screenData[screenDataIndex];

            if (isDot(currentScreenData)) {
                screenData[screenDataIndex] = eatDot(currentScreenData);
                game.incrementScore(1);
            }

            if (requestedDirection != Direction.NONE) {
                if (!isFacingAWall(requestedDirection, currentScreenData)) {
                    pacman.setMovingDirection(requestedDirection);
                    pacman.setLookingDirection(requestedDirection);
                }
            }

            if (isFacingAWall(pacman.getMovingDirection(), currentScreenData)) {
                pacman.setMovingDirection(Direction.NONE);
            }
        }

        pacman.move();
    }

    private short eatDot(short currentScreenData) {
        return (short) (currentScreenData & 15);
    }

    private boolean isFacingAWall(Direction direction, short currentScreenData) {
        return (direction == Direction.LEFT && isLeftWall(currentScreenData))
            || (direction == Direction.RIGHT && isRightWall(currentScreenData))
            || (direction == Direction.UP && isTopWall(currentScreenData))
            || (direction == Direction.DOWN && isBottomWall(currentScreenData));
    }

    private void drawPacman(Pacman pacman, Graphics2D g2d) {
        switch (pacman.getLookingDirection()) {
        case RIGHT:
            drawPacmanLookingRight(pacman, g2d);
            break;
        case UP:
            drawPacmanLookingUp(pacman, g2d);
            break;
        case DOWN:
            drawPacmanLookingDown(pacman, g2d);
            break;
        case LEFT:
        default:
            drawPacmanLookingLeft(pacman, g2d);
        }
    }

    private void drawPacmanLookingUp(Pacman pacman, Graphics2D g2d) {
        drawPacmanLooking(pacman, g2d, pacmanUpImg1, pacmanUpImg2, pacmanUpImg3);
    }

    private void drawPacmanLookingDown(Pacman pacman, Graphics2D g2d) {
        drawPacmanLooking(pacman, g2d, pacmanDownImg1, pacmanDownImg2, pacmanDownImg3);
    }

    private void drawPacmanLookingLeft(Pacman pacman, Graphics2D g2d) {
        drawPacmanLooking(pacman, g2d, pacmanLeftImg1, pacmanLeftImg2, pacmanLeftImg3);
    }

    private void drawPacmanLookingRight(Pacman pacman, Graphics2D g2d) {
        drawPacmanLooking(pacman, g2d, pacmanRightImg1, pacmanRightImg2, pacmanRightImg3);
    }

    private void drawPacmanLooking(Pacman pacman, Graphics2D g2d, Image img1, Image img2, Image img3) {
        var pacmanPosX = pacman.getPosX();
        var pacmanPosY = pacman.getPosY();
        int margin = MAZE_STROKE / 2;
        var marginX = margin + (BLOCK_SIZE - img1.getWidth(this)) / 2;
        var marginY = margin + (BLOCK_SIZE - img1.getHeight(this)) / 2;
        switch (pacmanAnimationImageNumber) {
            case 1:
                g2d.drawImage(img2, pacmanPosX + marginX, pacmanPosY + marginY, this);
                break;
            case 2:
                g2d.drawImage(img3, pacmanPosX + marginX, pacmanPosY + marginY, this);
                break;
            default:
                g2d.drawImage(img1, pacmanPosX + marginX, pacmanPosY + marginY, this);
                break;
        }
    }

    private void drawLevel(Graphics2D g2d) {
        int margin = MAZE_STROKE / 2;
        int screenDataIndex = 0;
        for (int y = 0; y < VERTICAL_SCREEN_SIZE; y += BLOCK_SIZE) {
            for (int x = 0; x < HORIZONTAL_SCREEN_SIZE; x += BLOCK_SIZE) {
                short screenDataValue = screenData[screenDataIndex];

                g2d.setColor(MAZE_COLOR);
                g2d.setStroke(new BasicStroke(MAZE_STROKE));
                if (isLeftWall(screenDataValue)) {
                    g2d.drawLine(x + margin, y + margin, x + margin, y + margin + BLOCK_SIZE - MAZE_STROKE);
                }
                if (isTopWall(screenDataValue)) {
                    g2d.drawLine(x + margin, y + margin , x + margin + BLOCK_SIZE - MAZE_STROKE, y + margin);
                }
                if (isRightWall(screenDataValue)) {
                    g2d.drawLine(x + margin + BLOCK_SIZE - MAZE_STROKE, y + margin, x + margin + BLOCK_SIZE - MAZE_STROKE, y + margin + BLOCK_SIZE - MAZE_STROKE);
                }
                if (isBottomWall(screenDataValue)) {
                    g2d.drawLine(x + margin, y + margin + BLOCK_SIZE - MAZE_STROKE, x + margin + BLOCK_SIZE - MAZE_STROKE, y + margin + BLOCK_SIZE - MAZE_STROKE);
                }

                if (isDot(screenDataValue)) {
                    g2d.setColor(DOTS_COLOR);
                    g2d.fillRect(x + DOT_POS, y + DOT_POS, DOT_SIZE, DOT_SIZE);
                }
                screenDataIndex++;
            }
        }
    }

    private boolean isLeftWall(short screenDataValue) {
        return (screenDataValue & 1) != 0;
    }

    private boolean isTopWall(short screenDataValue) {
        return (screenDataValue & 2) != 0;
    }

    private boolean isRightWall(short screenDataValue) {
        return (screenDataValue & 4) != 0;
    }

    private boolean isBottomWall(short screenDataValue) {
        return (screenDataValue & 8) != 0;
    }

    private boolean isSurroundedByWalls(short screenDataValue) {
        return (screenDataValue & 15) == 15;
    }

    private boolean isDot(short screenDataValue) {
        return (screenDataValue & 16) != 0;
    }

    private void initGame() {
        game.getPacman().setLives(INITIAL_PACMAN_LIVES);
        game.resetScore();
        game.setGhostsCount(INITIAL_GHOSTS_COUNT);
        game.setGhostsTopSpeedIndex(0);
        initScreenDataMaze(game);
    }

    private void initScreenDataMaze(Game game) {
        System.arraycopy(LEVEL_DATA, 0, screenData, 0, HORIZONTAL_SCREEN_BLOCKS_AMOUNT * VERTICAL_SCREEN_BLOCKS_AMOUNT);
        setInitialPositionsForLevel(game);
    }

    private void setInitialPositionsForLevel(Game game) {
        game.getGhosts().clear();

        // Set ghosts initial position
        Direction direction = Direction.RIGHT;
        for (int i = 0; i < game.getGhostsCount(); i++) {
            var ghostsPosX = GHOSTS_INITIAL_BLOCK_POS_X * BLOCK_SIZE;
            var ghostsPosY = GHOSTS_INITIAL_BLOCK_POS_Y * BLOCK_SIZE;

            // Randomly selects speed (at most top speed)
            int randomSpeedIndex = (int) (Math.random() * GHOSTS_VALID_SPEEDS.length);
            var ghostTopSpeed = GHOSTS_VALID_SPEEDS[game.getGhostsTopSpeedIndex()];
            var ghostsSpeed = Math.min(GHOSTS_VALID_SPEEDS[randomSpeedIndex], ghostTopSpeed);
            game.addGhost(new Ghost(ghostsPosX, ghostsPosY, direction, ghostsSpeed));

            // Alternate directions for next ghost
            direction = getOppositeDirection(direction);
        }

        // Set pacman initial position
        var pacman = game.getPacman();
        pacman.setPosX(PACMAN_INITIAL_BLOCK_POS_X * BLOCK_SIZE);
        pacman.setPosY(PACMAN_INITIAL_BLOCK_POS_Y * BLOCK_SIZE);
        pacman.setMovingDirection(Direction.NONE);
        pacman.setLookingDirection(Direction.LEFT);
        pacman.setSpeed(PACMAN_INITIAL_SPEED);

        game.setPacmanCaught(false);

        requestedDirection = Direction.NONE;
    }

    private Direction getOppositeDirection(Direction direction) {
        switch (direction) {
        case RIGHT:
            return Direction.LEFT;
        case LEFT:
            return Direction.RIGHT;
        case UP:
            return Direction.DOWN;
        case DOWN:
            return Direction.UP;
        default:
            return Direction.NONE;
        }
    }

    private void loadImages() {
        ghostImgUp = new ImageIcon("src/main/resources/images/ghost_up.png").getImage();
        ghostImgDown = new ImageIcon("src/main/resources/images/ghost_down.png").getImage();
        ghostImgLeft = new ImageIcon("src/main/resources/images/ghost_left.png").getImage();
        ghostImgRight = new ImageIcon("src/main/resources/images/ghost_right.png").getImage();

        pacmanUpImg1 = new ImageIcon("src/main/resources/images/pacman_up_0.png").getImage();
        pacmanUpImg2 = new ImageIcon("src/main/resources/images/pacman_up_1.png").getImage();
        pacmanUpImg3 = new ImageIcon("src/main/resources/images/pacman_up_2.png").getImage();
        pacmanDownImg1 = new ImageIcon("src/main/resources/images/pacman_down_0.png").getImage();
        pacmanDownImg2 = new ImageIcon("src/main/resources/images/pacman_down_1.png").getImage();
        pacmanDownImg3 = new ImageIcon("src/main/resources/images/pacman_down_2.png").getImage();
        pacmanLeftImg1 = new ImageIcon("src/main/resources/images/pacman_left_0.png").getImage();
        pacmanLeftImg2 = new ImageIcon("src/main/resources/images/pacman_left_1.png").getImage();
        pacmanLeftImg3 = new ImageIcon("src/main/resources/images/pacman_left_2.png").getImage();
        pacmanRightImg1 = new ImageIcon("src/main/resources/images/pacman_right_0.png").getImage();
        pacmanRightImg2 = new ImageIcon("src/main/resources/images/pacman_right_1.png").getImage();
        pacmanRightImg3 = new ImageIcon("src/main/resources/images/pacman_right_2.png").getImage();

        bonusImg = new ImageIcon("src/main/resources/images/pizza.png").getImage();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        doDrawing(game, g);
    }

    private void doDrawing(Game game, Graphics g) {
        Graphics2D g2d = (Graphics2D) g;

        // Background
        g2d.setColor(BACKGROUND_COLOR);
        g2d.fillRect(0, 0, HORIZONTAL_SCREEN_SIZE, VERTICAL_SCREEN_SIZE + 40);

        drawLevel(g2d);
        drawScoreAndLives(game, g2d);
        computePacmanAnimationImage();

        if (game.isGameStarted()) {
            playGame(game, g2d);
        } else {
            displayStartMessage(g2d);
        }

        Toolkit.getDefaultToolkit().sync();
        g2d.dispose();
    }

    private void computePacmanAnimationImage() {
        // Animation is not about the pacman direction, it is the move from an image to another
        pacmanAnimationCount--;
        if (pacmanAnimationCount <= 0) {
            pacmanAnimationCount = CYCLES_BETWEEN_PACMAN_ANIMATION;
            pacmanAnimationImageNumber = pacmanAnimationImageNumber + pacmanAnimationImageDirection;

            // Animation back and forth
            if (pacmanAnimationImageNumber == (PACMAN_ANIMATION_IMAGES_COUNT - 1) || pacmanAnimationImageNumber == 0) {
                // Reverse animation direction
                pacmanAnimationImageDirection = -pacmanAnimationImageDirection;
            }
        }
    }

    class PacmanKeysAdapter extends KeyAdapter {

        @Override
        public void keyPressed(KeyEvent e) {
            int key = e.getKeyCode();
            if (game.isGameStarted()) {
                if (key == KeyEvent.VK_LEFT) {
                    requestedDirection = Direction.LEFT;
                } else if (key == KeyEvent.VK_RIGHT) {
                    requestedDirection = Direction.RIGHT;
                } else if (key == KeyEvent.VK_UP) {
                    requestedDirection = Direction.UP;
                } else if (key == KeyEvent.VK_DOWN) {
                    requestedDirection = Direction.DOWN;
                } else if (key == KeyEvent.VK_Q && timer.isRunning()) {
                    game.setGameStarted(false);
                } else if (key == KeyEvent.VK_ESCAPE) {
                    if (timer.isRunning()) {
                        timer.stop();
                    } else {
                        timer.start();
                    }
                }
            } else {
                if (key == KeyEvent.VK_ENTER) {
                    game.setGameStarted(true);
                    initGame();
                }
            }
        }

        @Override
        public void keyReleased(KeyEvent e) {
            super.keyReleased(e);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        repaint();
    }
}
