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

/**
 *
 * @author Thomas Apeltauer <apelttom@live.com>
 * Created on 10/06/2014
 * Last modified on 10/06/2014
 */
public class Game {
    private int gameState = GAME_STATE_WHITE;
    public static final int GAME_STATE_WHITE = 0;
    public static final int GAME_STATE_BLACK = 1;
    public static final int GAME_STATE_END = 2;
    
    private ArrayList<Figure> figures = new ArrayList<Figure>();
    private MoveValidator moveValidator;
    
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
     * @param sourceRow source row (Figure.ROW_..) of the figure to move
     * @param sourceColumn source column (Figure.COLUMN_..) of the figure to move
     * @param targetRow the target row (Figure.ROW_..)
     * @param targetColumn the target column (Figure.COLUMN_..)
     */
    public boolean movePiece(Move move) {
        if (!this.moveValidator.isMoveValid(move)) {
            System.out.println("Invalid move");
            return false;
        }
        
        Figure fig = getNonCapturedFigureAt(move.getSourceRow(), move.getSourceColumn());
        
        // check if the move is capturing an opponent figure
        int opponentColor = (fig.getColor() == Figure.COLOR_BLACK ? Figure.COLOR_WHITE : Figure.COLOR_BLACK);
        if (isNonCapturedFigureAt(opponentColor, move.getTargetRow(), move.getTargetColumn())) {
            Figure opponentFig = getNonCapturedFigureAt(move.getTargetRow(), move.getTargetColumn());
            opponentFig.isCaptured(true);
        }
        fig.setRow(move.getTargetRow());
        fig.setColumn(move.getTargetColumn());
        
        if (isGameEndConditionReached()) {
            this.gameState = GAME_STATE_END;
        } else {
            this.changeGameState();
        }
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
                System.out.println("Game over! Black Player WIN!");
            } else {
                System.out.println("Game over! White Player WIN!");
            }
            this.gameState = Game.GAME_STATE_END;
        }
        
        switch (this.gameState) {
            case GAME_STATE_BLACK:
                this.gameState = GAME_STATE_WHITE;
                break;
            case GAME_STATE_WHITE:
                this.gameState = GAME_STATE_BLACK;
                break;
            case GAME_STATE_END:
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
        for (Figure fig : this.figures) {
            if(fig.getType() == Figure.TYPE_KING && fig.isCaptured()
                    || fig.getType() == Figure.TYPE_MAHARAJA && fig.isCaptured()) {
                return true;
            }
        }
        return false;
    }
}
