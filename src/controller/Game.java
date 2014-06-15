/*  Instituto Tecnológico de Costa Rica
 Escuela de Ingeniería en Computación
 Programación Orientada a Objetos
 Prof.: Mauricio Avilés
 Proyecto 3 - El Maharaja y los Cipayos
 Tomas Apeltauer
 Saúl Zamora 
 */
package controller;

import java.util.ArrayList;
import model.Figure;
import model.Move;
import model.MoveValidator;
import model.Player;

/**
 *
 * @author Thomas Apeltauer <apelttom@live.com>
 * Created on 10/06/2014
 * Last modified on 10/06/2014
 */
public class Game implements Runnable{
    private int gameState = GAME_STATE_WHITE;
    public static final int GAME_STATE_WHITE = 0;
    public static final int GAME_STATE_BLACK = 1;
    public static final int GAME_STATE_END_BLACK_WON = 2;
    public static final int GAME_STATE_END_WHITE_WON = 3;
    
    private ArrayList<Figure> figures = new ArrayList<>();
    private ArrayList<Figure> capturedFigures = new ArrayList<>();
    private MoveValidator moveValidator;
    
    // players
    private Player blackPlayer;
    private Player whitePlayer;
    private Player activePlayer;
    
    /**
     * Initialize game
     */
    public Game() {
        
        this.moveValidator = new MoveValidator(this);
        
        // create and locate black figures
        createAndLocateFigure(Figure.COLOR_BLACK, Figure.TYPE_ROOK, Figure.ROW_8, Figure.COLUMN_A);
        createAndLocateFigure(Figure.COLOR_BLACK, Figure.TYPE_KNIGHT, Figure.ROW_8, Figure.COLUMN_B);
        createAndLocateFigure(Figure.COLOR_BLACK, Figure.TYPE_BISHOP, Figure.ROW_8, Figure.COLUMN_C);
        createAndLocateFigure(Figure.COLOR_BLACK, Figure.TYPE_QUEEN, Figure.ROW_8, Figure.COLUMN_D);
        createAndLocateFigure(Figure.COLOR_BLACK, Figure.TYPE_KING, Figure.ROW_8, Figure.COLUMN_E);
        createAndLocateFigure(Figure.COLOR_BLACK, Figure.TYPE_BISHOP, Figure.ROW_8, Figure.COLUMN_F);
        createAndLocateFigure(Figure.COLOR_BLACK, Figure.TYPE_KNIGHT, Figure.ROW_8, Figure.COLUMN_G);
        createAndLocateFigure(Figure.COLOR_BLACK, Figure.TYPE_ROOK, Figure.ROW_8, Figure.COLUMN_H);
        
        // black pawns
        int currentColumn = Figure.COLUMN_A;
        for (int i = 0; i < 8; i++) {
            createAndLocateFigure(Figure.COLOR_BLACK, Figure.TYPE_PAWN, Figure.ROW_7, currentColumn);
            currentColumn++;
        }
        
        // create and locate white Maharaja
        createAndLocateFigure(Figure.COLOR_WHITE, Figure.TYPE_MAHARAJA, Figure.ROW_1, Figure.COLUMN_D);
    }
    
    /**
     * set the player for the specified color
     * @param figureColor
     * @param player 
     */
    public void setPlayer(int figureColor, Player player) {
        switch (figureColor) {
            case Figure.COLOR_BLACK:
                this.blackPlayer = player; break;
            case Figure.COLOR_WHITE:
                this.whitePlayer = player; break;
        }
    }
    
    /**
     * start game
     */
    public void startGame() {
        System.out.println("The Maharaja and the cepoys");
        while (this.blackPlayer == null || this.whitePlayer == null) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
        
        this.activePlayer = this.whitePlayer;
        
        while ( !isGameEndConditionReached() ) {
            waitForMove();
            switchActivePlayer();
        }
        System.out.println("Game Over!");
        if(this.activePlayer == whitePlayer) {
            System.out.println("White player won!");
        } else {
            System.out.println("Black player won!");
        }
    }
    
    private void switchActivePlayer() {
        if (this.activePlayer == this.whitePlayer) {
            this.activePlayer = this.blackPlayer;
        } else {
            this.activePlayer = this.blackPlayer;
        }
        this.changeGameState();
    }
    
    private void waitForMove() {
        Move move = null;
        do {
            move = this.activePlayer.getMove();
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                System.out.println("Error: " + e.getMessage());
            }
        } while (move == null || !this.moveValidator.isMoveValid(move));
        
        boolean success = this.movePiece(move);
        if (success) {
            this.blackPlayer.moveSuccesfullyExecuted(move);
            this.whitePlayer.moveSuccesfullyExecuted(move);
        } else {
            throw new IllegalStateException("Failed to execute move");
        }
    }

    public MoveValidator getMoveValidator() {
        return moveValidator;
    }
    
    /**
     * create figure instance and add it to the internal list of figures
     * @param color of Figure.COLOR_..
     * @param type of Figure.TYPE_..
     * @param row of Figure.ROW_..
     * @param column of Figure.COLUMN_..
     */
    private void createAndLocateFigure(int color, int type, int row, int column) {
        Figure figure = new Figure(color, type, row, column);
        this.figures.add(figure);
    }
    /**
     * Move figure to specified location. If location is occupied, the
     * opponent figure is marked as 'captured'
     * @param move
     * @return true if valid move
     */
    public boolean movePiece(Move move) {
        Figure figure = getNonCapturedFigureAt(move.getSourceRow(), move.getSourceColumn());
        
        // is move capturing an opponent figure?
        int opponentColor = (figure.getColor() == Figure.COLOR_BLACK ? 
                Figure.COLOR_WHITE : Figure.COLOR_BLACK);
        
        if (isNonCapturedFigureAt(opponentColor, 
                move.getTargetRow(), move.getTargetColumn())) {
            Figure opponentFigure = getNonCapturedFigureAt(move.getTargetRow(), move.getTargetColumn());
            this.figures.remove(opponentFigure);
            this.capturedFigures.add(opponentFigure);
            opponentFigure.isCaptured(true);
        }
        figure.setRow(move.getTargetRow());
        figure.setColumn(move.getTargetColumn());
        return true;
    }
    
    /**
     * returns the first figure at the specified location that is not
     * marked as 'captured'.
     * @param row of Figure.ROW_..
     * @param column of Figure.COLUMN_..
     * @return the first not captured figure at specified location
     */
    public Figure getNonCapturedFigureAt(int row, int column) {
        for (Figure fig : this.figures) {
            if (fig.getRow() == row
                    && fig.getColumn() == column
                    && fig.isCaptured() == false) {
                return fig;
            }
        }
        return null;
    }
    
    /**
     * Checks if there is a figure at the specified location that is
     * not marked as 'captured'
     * @param color of Figure.COLOR_..
     * @param row of Figure.ROW_..
     * @param column of Figure.COLUMN_..
     * @return  true if the location contains a not-captures figure
     *          of the specified color
     */
    private boolean isNonCapturedFigureAt(int color, int row, int column) {
        for (Figure fig : this.figures) {
            if (fig.getRow() == row
                    && fig.getColumn() == column
                    && fig.isCaptured() == false
                    && fig.getColor() == color) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * Checks whether there is a non-captured piece at the specified location
     * 
     * @param row one of Piece.ROW_..
     * @param column on of Piece.COLUMN_..
     * @return true, if the location contains a piece
     */
    public boolean isNonCapturedFigureAt(int row, int column) {
        for (Figure piece : this.figures) {
            if (piece.getRow() == row && piece.getColumn() == column
                    && piece.isCaptured() == false) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * @return current game state (Game.GAME_STATE_..)
     */
    public int getGameState() {
        return this.gameState;
    }
    
    /**
     * @return internal list of figures
     */
    public ArrayList<Figure> getFigures() {
        return this.figures;
    }
    
    /**
     * switches game state depending on the current board situation.
     */
    public void changeGameState() {
        if (this.isGameEndConditionReached()) {
            if (this.gameState == Game.GAME_STATE_BLACK) {
                this.gameState = Game.GAME_STATE_END_BLACK_WON;
            } else if (this.gameState == Game.GAME_STATE_WHITE) {
                this.gameState = Game.GAME_STATE_END_WHITE_WON;
            }
            return;
        }
        
        switch (this.gameState) {
            case GAME_STATE_BLACK:
                this.gameState = GAME_STATE_WHITE;
                break;
            case GAME_STATE_WHITE:
                this.gameState = GAME_STATE_BLACK;
                break;
            case GAME_STATE_END_WHITE_WON:
            case GAME_STATE_END_BLACK_WON:
                break;
            default:
                throw new IllegalStateException("Unknown game state: " + this.gameState);
        }
    }
    
    /**
     * Check if the game condition is met: Black King is captured or White Maharaja is captured
     * @return true is the game end condition is met
     */
    private boolean isGameEndConditionReached() {
        for (Figure fig : this.capturedFigures) {
            if(fig.getType() == Figure.TYPE_KING || 
               fig.getType() == Figure.TYPE_MAHARAJA) {
                return true;
            }
        }
        return false;
    }
    
    @Override
    public void run() {
        this.startGame();
    }
}
