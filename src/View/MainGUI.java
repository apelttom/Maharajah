/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package View;

import controller.Game;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.net.URL;
import java.util.ArrayList;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import model.Figure;
import model.Move;
import model.MoveValidator;


/**
 * (x,y) coordinates point to the upper left position of the component
 * all lists are treated as 0 being the bottom and size-1 being the top figure
 * 
 */
public class MainGUI extends JPanel {
    
    private static final int BOARD_START_X = 301;
    private static final int BOARD_START_Y = 51;
    
    private static final int SQUARE_WIDTH = 50;
    private static final int SQUARE_HEIGHT = 50;
    
    private static final int FIGURE_WIDTH = 48;
    private static final int FIGURE_HEIGHT = 48;
    
    private static final int FIGURES_START_AT_X = BOARD_START_X + (int) (SQUARE_WIDTH/2.0 - FIGURE_WIDTH/2.0);
    private static final int FIGURES_START_AT_Y = BOARD_START_Y + (int) (SQUARE_HEIGHT/2.0 - FIGURE_HEIGHT/2.0);
    
    private static final int DRAG_TARGET_SQUARE_START_X = BOARD_START_X - (int) (FIGURE_WIDTH/2.0);
    private static final int DRAG_TARGET_SQUARE_START_Y = BOARD_START_Y - (int) (FIGURE_HEIGHT/2.0);
    
    private Image imgBackground;
    private JLabel lblGameState;
    private JLabel lblGameMessages;
    
    private Game game;
    private ArrayList<FigureGUI> guiFigures = new ArrayList<FigureGUI>();
    
    private FigureGUI dragFigure;
    private Move lastMove;
    
    public MainGUI() {
        this.setLayout(null);
        // load and set background image
        try {
            URL urlBackgroundImg = getClass().getResource("img/board.png");
            this.imgBackground = new ImageIcon(urlBackgroundImg).getImage();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,e.getMessage(),"Error",JOptionPane.ERROR_MESSAGE);
        }
        
        // create game
        this.game = new Game();
        
        // wrap figures into their graphical representation
        for (Figure figure : this.game.getFigures()) {
            createAndLocateGUIFigure(figure);
        }
        
        // mouse listeners to enable drag and drop
        FigureDragAndDropListener listener = new FigureDragAndDropListener(this.guiFigures, this);
        this.addMouseListener(listener);
        this.addMouseMotionListener(listener);
        
        // button to change state
        JButton btnChangeGameState = new JButton("Change player");
        btnChangeGameState.addActionListener( new ChangeGameStateButtonActionListener(this) );
        btnChangeGameState.setBounds(0, 0, 120, 30);
        this.add(btnChangeGameState);
        
        // label to display game state
        String labelText = "Current player: " + this.getGameStateAsText();
        this.lblGameState = new JLabel(labelText);
        lblGameState.setBounds(0, 30, 150, 30);
        lblGameState.setForeground(Color.WHITE);
        this.add(lblGameState);
        
        // create application frame and set visible
        JFrame f = new JFrame();
        f.setVisible(true);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.add(this);
        f.setSize(this.imgBackground.getWidth(null), this.imgBackground.getHeight(null)); 
    }
        
    /**
     * @return textual description of current game state
     */
    private String getGameStateAsText() {
        return (this.game.getGameState() == Game.GAME_STATE_BLACK ? "Black" : "White");
    }
    
    /**
     * create game figure
     * @param fig figure to representate 
     */
    private void createAndLocateGUIFigure(Figure fig) {
        Image img = this.getImageForFigure(fig.getColor(), fig.getType());
        FigureGUI guiFigure = new FigureGUI(img, fig);
        this.guiFigures.add(guiFigure);
    }
    
    /**
     * load image for given color and type. Translates the color
     * and type information into a filename and loads that particular file.
     * 
     * @param color color constant
     * @param type type constant
     * @return image
     */
    private Image getImageForFigure(int color, int type) {
        String filename = "";
        
        filename += (color == Figure.COLOR_WHITE ? "w" : "b");
        
        switch (type) {
            case Figure.TYPE_BISHOP:
                filename += "b";
                break;
            case Figure.TYPE_KING:
                filename += "k";
                break;
            case Figure.TYPE_KNIGHT:
                filename += "n";
                break;
            case Figure.TYPE_PAWN:
                filename += "p";
                break;
            case Figure.TYPE_QUEEN:
                filename += "q";
                break;
            case Figure.TYPE_ROOK:
                filename += "r";
                break;
            case Figure.TYPE_MAHARAJA:
                filename += "k";
                break;
        }
        filename += ".png";
        URL urlFigureImg = getClass().getResource("img/" + filename);
        return new ImageIcon(urlFigureImg).getImage();
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        // draw background
        g.drawImage(this.imgBackground, 0, 0, null);

	// draw pieces
	for (FigureGUI guiFigure : this.guiFigures) {
            if(!guiFigure.isCaptured()) {
                g.drawImage(guiFigure.getImg(), guiFigure.getX(), guiFigure.getY(), null);
            }
        }
        // draw last move, if user is not dragging game piece
        if( !isUserDraggingFigure() && this.lastMove != null ) {
            int highlightSourceX = convertColumnToX(this.lastMove.getSourceColumn());
            int highlightSourceY = convertRowToY(this.lastMove.getSourceRow());
            int highlightTargetX = convertColumnToX(this.lastMove.getTargetColumn());
            int highlightTargetY = convertRowToY(this.lastMove.getTargetRow());
            
            g.setColor(Color.YELLOW);
            g.drawRoundRect( highlightSourceX+4, highlightSourceY+4, SQUARE_WIDTH-8, SQUARE_HEIGHT-8,10,10);
            g.drawRoundRect( highlightTargetX+4, highlightTargetY+4, SQUARE_WIDTH-8, SQUARE_HEIGHT-8,10,10);
        }
        // draw valid target locations, if user is dragging a game piece
        if( isUserDraggingFigure() ){
            MoveValidator moveValidator = this.game.getMoveValidator();
            
            // iterate the complete board to check if target locations are valid
            for (int column = Figure.COLUMN_A; column <= Figure.COLUMN_H; column++) {
                for (int row = Figure.ROW_1; row <= Figure.ROW_8; row++) {
                    int sourceRow = this.dragFigure.getFigure().getRow();
                    int sourceColumn = this.dragFigure.getFigure().getColumn();

                    // check if target location is valid
                    if( moveValidator.isMoveValid( new Move(sourceRow, sourceColumn, row, column)) ){
                        
                        int highlightX = convertColumnToX(column);
                        int highlightY = convertRowToY(row);

                        // draw a black drop shadow by drawing a black rectangle with an offset of 1 pixel
                        g.setColor(Color.BLACK);
                        g.drawRoundRect( highlightX+5, highlightY+5, SQUARE_WIDTH-8, SQUARE_HEIGHT-8,10,10);
                        // draw the highlight
                        g.setColor(Color.GREEN);
                        g.drawRoundRect( highlightX+4, highlightY+4, SQUARE_WIDTH-8, SQUARE_HEIGHT-8,10,10);
                    }
                }
            }
        }
        // draw game state label
        this.lblGameState.setText(this.getGameStateAsText());
    }
    
    /**
     * @return true if the user is currently dragging a figure
     */
    private boolean isUserDraggingFigure() {
        return this.dragFigure != null;
    }
    
    /**
     * switches between the different game states
     */
    public void changeGameState() {
        this.game.changeGameState();
        this.lblGameState.setText("Current player: " + this.getGameStateAsText());
    }

    public FigureGUI getDragFigure() {
        return dragFigure;
    }

    public void setDragFigure(FigureGUI dragFigure) {
        this.dragFigure = dragFigure;
    }
    
    /**
     * @return current game state
     */
    public int getGameState() {
        return this.game.getGameState();
    }
    
    /**
     * convet logical column into x coordinate
     * @param column
     * @return x coordinate for column
     */
    public static int convertColumnToX(int column) {
        return FIGURES_START_AT_X + SQUARE_WIDTH * column;
    }
    
    /**
     * convert logical row into y coordinate
     * @param row
     * @return y coordinate for row
     */
    public static int convertRowToY(int row) {
        return FIGURES_START_AT_Y + SQUARE_HEIGHT * (Figure.ROW_8 - row);
    }
    
    /**
     * convert x coordinate into logical column
     * @param x
     * @return logical column for x coordinate
     */
    public static int convertXToColumn(int x) {
        return (x - DRAG_TARGET_SQUARE_START_X)/SQUARE_WIDTH;
    }
    
    /**
     * convert y coordinate into logical row
     * @param y
     * @return logical row for y coordinate
     */ 
    public static int convertYToRow(int y) {
        return Figure.ROW_8 - (y - DRAG_TARGET_SQUARE_START_Y)/SQUARE_HEIGHT;
    }
    
    /**
     * if location valid, change location of given figure.
     * if location invalid, move figure to its original position.
     * @param dragFigure
     * @param x
     * @param y 
     */
    public void setNewFigureLocation(FigureGUI dragFigure, int x, int y) {
        int targetRow = MainGUI.convertYToRow(y);
        int targetColumn = MainGUI.convertXToColumn(x);
        
        if (targetRow < Figure.ROW_1 || targetRow > Figure.ROW_8 || targetColumn < Figure.COLUMN_A || targetColumn > Figure.COLUMN_H) {
            // reset to original position if move is not valid
            dragFigure.resetToUnderlyingFigurePosition();
        }else{
            System.out.println("Moving piece to " + targetRow + "/" + targetColumn);
            Move move = new Move(dragFigure.getFigure().getRow(),
                                dragFigure.getFigure().getColumn(),
                                targetRow, targetColumn);
            boolean wasMoveSuccesfull = this.game.movePiece(move);
            
            if (wasMoveSuccesfull) {
                this.lastMove = move;
            }
            
            dragFigure.resetToUnderlyingFigurePosition();
        }
    }
    
    public static void main(String[] args) {
        new MainGUI();
    }
}
