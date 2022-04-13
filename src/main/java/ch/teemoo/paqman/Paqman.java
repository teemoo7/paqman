package ch.teemoo.paqman;

import java.awt.EventQueue;
import javax.swing.JFrame;

public class Paqman extends JFrame {

    public Paqman() {
        init();
    }

    private void init() {
        var board = new Board();
        add(board);
        setTitle("Paqman");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        var width = Board.HORIZONTAL_SCREEN_BLOCKS_AMOUNT * Board.BLOCK_SIZE;
        var height = Board.VERTICAL_SCREEN_BLOCKS_AMOUNT * Board.BLOCK_SIZE + 100;
        setSize(width, height);
        setLocationRelativeTo(null);
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            var paqman = new Paqman();
            paqman.setVisible(true);
        });
    }
}
