/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package model;

import controller.Game;

/**
 *
 * @author ALEKS
 */
public class MoveValidator {
    private Game game;
    private Figure sourceFigure;
    private Figure targetFigure;

    public MoveValidator(Game maharajaGame) {
        this.game = maharajaGame;
    }

    /**
     * Checks if the specified move is valid
     *
     * @param move to validate
     * @return true if move is valid, false if move is invalid
     */
    public boolean isMoveValid(Move move) {
        int sourceRow = move.getSourceRow();
        int sourceColumn = move.getSourceColumn();
        int targetRow = move.getTargetRow();
        int targetColumn = move.getTargetColumn();

        sourceFigure = game.getNonCapturedFigureAt(sourceRow, sourceColumn);
        targetFigure = this.game.getNonCapturedFigureAt(targetRow, targetColumn);

        // source piece does not exist
        if (sourceFigure == null) {
            System.out.println("No source figure.");
            return false;
        }

        // source piece has right color?
        if (sourceFigure.getColor() == Figure.COLOR_WHITE
                && this.game.getGameState() == Game.GAME_STATE_WHITE) {
            // ok
        } else if (sourceFigure.getColor() == Figure.COLOR_BLACK
                && this.game.getGameState() == Game.GAME_STATE_BLACK) {
            // ok
        } else {
            System.out.println("Not your turn!");
            return false;
        }

        // check if target location within boundaries
        if (targetRow < Figure.ROW_1 || targetRow > Figure.ROW_8
                || targetColumn < Figure.COLUMN_A || targetColumn > Figure.COLUMN_H) {
            System.out.println("Warning #1: Target location out of boundaries.");
            return false;
        }

        // validate piece movement rules
        boolean validFigureMove = false;
        switch (sourceFigure.getType()) {
            case Figure.TYPE_BISHOP:
                validFigureMove = isValidBishopMove(sourceRow, sourceColumn, targetRow, targetColumn);
                break;
            case Figure.TYPE_KING:
                validFigureMove = isValidKingMove(sourceRow, sourceColumn, targetRow, targetColumn);
                break;
            case Figure.TYPE_KNIGHT:
                validFigureMove = isValidKnightMove(sourceRow, sourceColumn, targetRow, targetColumn);
                break;
            case Figure.TYPE_PAWN:
                validFigureMove = isValidPawnMove(sourceRow, sourceColumn, targetRow, targetColumn);
                break;
            case Figure.TYPE_QUEEN:
                validFigureMove = isValidQueenMove(sourceRow, sourceColumn, targetRow, targetColumn);
                break;
            case Figure.TYPE_ROOK:
                validFigureMove = isValidRookMove(sourceRow, sourceColumn, targetRow, targetColumn);
                break;
            case Figure.TYPE_MAHARAJA:
                validFigureMove = isValidMaharajaMove(sourceRow, sourceColumn, targetRow, targetColumn);
            default:
                break;
        }
        if (!validFigureMove) {
            return false;
        } else {
            // ok
        }
        /**
         * handler for stalemate and checkmate
         */
        return true;
    }

    private boolean isTargetLocationCaptureable() {
        if (targetFigure.getColor() != sourceFigure.getColor()) {
            return true;
        } else {
            return false;
        }
    }

    private boolean isTargetLocationFree() {
        return targetFigure == null;
    }
    
    /**
     * Validation for the BHISOP move
     * @param sourceRow
     * @param sourceColumn
     * @param targetRow
     * @param targetColumn
     * @return true if valid move
     */
    private boolean isValidBishopMove(int sourceRow, int sourceColumn, int targetRow, int targetColumn) {
        // target location possible?
        if (isTargetLocationFree() || isTargetLocationCaptureable()) {
            //ok
        } else {
            System.out.println("Warning #2: Target location not free nor captureable.");
            return false;
        }

        boolean isValid = false;
        // first lets check if the path to the target is diagonally at all
        int diffRow = targetRow - sourceRow;
        int diffColumn = targetColumn - sourceColumn;

        if (diffRow == diffColumn && diffColumn > 0) {
            // moving diagonally up-right
            isValid = !arePiecesBetweenSourceAndTarget(sourceRow, sourceColumn, targetRow, targetColumn, +1, +1);

        } else if (diffRow == -diffColumn && diffColumn > 0) {
            // moving diagnoally down-right
            isValid = !arePiecesBetweenSourceAndTarget(sourceRow, sourceColumn, targetRow, targetColumn, -1, +1);

        } else if (diffRow == diffColumn && diffColumn < 0) {
            // moving diagnoally down-left
            isValid = !arePiecesBetweenSourceAndTarget(sourceRow, sourceColumn, targetRow, targetColumn, -1, -1);

        } else if (diffRow == -diffColumn && diffColumn < 0) {
            // moving diagnoally up-left
            isValid = !arePiecesBetweenSourceAndTarget(sourceRow, sourceColumn, targetRow, targetColumn, +1, -1);

        } else {
            // not moving diagonally
            System.out.println(diffRow);
            System.out.println(diffColumn);
            System.out.println("Warning #3: Not moving diagonally.");
            isValid = false;
        }
        return isValid;
    }
    
    /**
     * Validation for the QUEEN move
     * @param sourceRow
     * @param sourceColumn
     * @param targetRow
     * @param targetColumn
     * @return true if valid move
     */
    private boolean isValidQueenMove(int sourceRow, int sourceColumn, int targetRow, int targetColumn) {
        boolean result = isValidBishopMove(sourceRow, sourceColumn, targetRow, targetColumn);
        result |= isValidRookMove(sourceRow, sourceColumn, targetRow, targetColumn);
        return result;
    }
    
    /**
     * Validation for the MAHARAJA move
     * @param sourceRow
     * @param sourceColumn
     * @param targetRow
     * @param targetColumn
     * @return true if valid move
     */
    private boolean isValidMaharajaMove(int sourceRow, int sourceColumn, int targetRow, int targetColumn) {
        boolean result = isValidQueenMove(sourceRow, sourceColumn, targetRow, targetColumn);
        result |= isValidKnightMove(sourceRow, sourceColumn, targetRow, targetColumn);
        return result;
    }
    
    /**
     * Validation for the PAWN move
     * @param sourceRow
     * @param sourceColumn
     * @param targetRow
     * @param targetColumn
     * @return true if valid move
     */
    private boolean isValidPawnMove(int sourceRow, int sourceColumn, int targetRow, int targetColumn) {

        boolean isValid = false;
        if (isTargetLocationFree()) {
            if (sourceColumn == targetColumn) {
                // same column
                if (sourceFigure.getColor() == Figure.COLOR_BLACK) {
                    // black
                    if (sourceRow - 1 == targetRow) {
                        // move one down
                        isValid = true;
                    } else {
                        System.out.println("Warning #4: Not moving one down.");
                        isValid = false;
                    }
                }
            } else {
                // not the same column
                System.out.println("Warning #5: Not staying in same column.");
                isValid = false;
            }
        } else if (isTargetLocationCaptureable()) {

            if (sourceColumn + 1 == targetColumn || sourceColumn - 1 == targetColumn) {
                // one column to the right or left
                if (sourceFigure.getColor() == Figure.COLOR_BLACK) {
                    if (sourceRow - 1 == targetRow) {
                        // move one down
                        isValid = true;
                    } else {
                        System.out.println("Warning #6: Not moving one down.");
                        isValid = false;
                    }
                }
            } else {
                // note one column to the left or right
                System.out.println("Warning #7: Not moving one column to left or right.");
                isValid = false;
            }
        }
        return isValid;
    }
    
    /**
     * Validation for the KNIGHT move
     * @param sourceRow
     * @param sourceColumn
     * @param targetRow
     * @param targetColumn
     * @return true if valid move
     */
    private boolean isValidKnightMove(int sourceRow, int sourceColumn, int targetRow, int targetColumn) {
        
        // target location possible?
        if (isTargetLocationFree() || isTargetLocationCaptureable()) {
            //ok
        } else {
            System.out.println("Warning #8: Target location not free nor captureable.");
            return false;
        }

        if (sourceRow + 2 == targetRow && sourceColumn + 1 == targetColumn) {
            // move up up right
            return true;
        } else if (sourceRow + 1 == targetRow && sourceColumn + 2 == targetColumn) {
            // move up right right
            return true;
        } else if (sourceRow - 1 == targetRow && sourceColumn + 2 == targetColumn) {
            // move down right right
            return true;
        } else if (sourceRow - 2 == targetRow && sourceColumn + 1 == targetColumn) {
            // move down down right
            return true;
        } else if (sourceRow - 2 == targetRow && sourceColumn - 1 == targetColumn) {
            // move down down left
            return true;
        } else if (sourceRow - 1 == targetRow && sourceColumn - 2 == targetColumn) {
            // move down left left
            return true;
        } else if (sourceRow + 1 == targetRow && sourceColumn - 2 == targetColumn) {
            // move up left left
            return true;
        } else if (sourceRow + 2 == targetRow && sourceColumn - 1 == targetColumn) {
            // move up up left
            return true;
        } else {
            return false;
        }
    }
    
    /**
     * Validation for the KING move
     * @param sourceRow
     * @param sourceColumn
     * @param targetRow
     * @param targetColumn
     * @return true if valid move
     */
    private boolean isValidKingMove(int sourceRow, int sourceColumn, int targetRow, int targetColumn) {

        // target location possible?
        if (isTargetLocationFree() || isTargetLocationCaptureable()) {
            //ok
        } else {
            System.out.println("Warning #9: Target location not free nor captureable.");
            return false;
        }
        
        boolean isValid = true;
        if (sourceRow + 1 == targetRow && sourceColumn == targetColumn) {
            //up
            isValid = true;
        } else if (sourceRow + 1 == targetRow && sourceColumn + 1 == targetColumn) {
            //up right
            isValid = true;
        } else if (sourceRow == targetRow && sourceColumn + 1 == targetColumn) {
            //right
            isValid = true;
        } else if (sourceRow - 1 == targetRow && sourceColumn + 1 == targetColumn) {
            //down right
            isValid = true;
        } else if (sourceRow - 1 == targetRow && sourceColumn == targetColumn) {
            //down
            isValid = true;
        } else if (sourceRow - 1 == targetRow && sourceColumn - 1 == targetColumn) {
            //down left
            isValid = true;
        } else if (sourceRow == targetRow && sourceColumn - 1 == targetColumn) {
            //left
            isValid = true;
        } else if (sourceRow + 1 == targetRow && sourceColumn - 1 == targetColumn) {
            //up left
            isValid = true;
        } else {
            System.out.println("Warning #10: Moving too far.");
            isValid = false;
        }
        
        /**
         * Validation for stalemate and checkmate
         */
        
        return isValid;
    }
    
    /**
     * Validation for the ROOK move
     * @param sourceRow
     * @param sourceColumn
     * @param targetRow
     * @param targetColumn
     * @return true if valid move
     */
    private boolean isValidRookMove(int sourceRow, int sourceColumn, int targetRow, int targetColumn) {
        if (isTargetLocationFree() || isTargetLocationCaptureable()) {
            //ok
        } else {
            System.out.println("Warning #11: Target location not free nor captureable.");
            return false;
        }

        boolean isValid = false;

        // check if the path to the target is straight at all
        int diffRow = targetRow - sourceRow;
        int diffColumn = targetColumn - sourceColumn;

        if (diffRow == 0 && diffColumn > 0) {
            // right
            isValid = !arePiecesBetweenSourceAndTarget(sourceRow, sourceColumn, targetRow, targetColumn, 0, +1);

        } else if (diffRow == 0 && diffColumn < 0) {
            // left
            isValid = !arePiecesBetweenSourceAndTarget(sourceRow, sourceColumn, targetRow, targetColumn, 0, -1);

        } else if (diffRow > 0 && diffColumn == 0) {
            // up
            isValid = !arePiecesBetweenSourceAndTarget(sourceRow, sourceColumn, targetRow, targetColumn, +1, 0);

        } else if (diffRow < 0 && diffColumn == 0) {
            // down
            isValid = !arePiecesBetweenSourceAndTarget(sourceRow, sourceColumn, targetRow, targetColumn, -1, 0);

        } else {
            // not moving diagonally
            System.out.println("Warning #12: Not moving straight.");
            isValid = false;
        }

        return isValid;
    }
    
    /**
     * Checks for figure between source and target locations. Bishops and Rooks
     * can take several "steps" at a time, so it's necessary to check in all
     * these steps (rowIncrementPerStep and columnIncrementPerStep)
     * @param sourceRow
     * @param sourceColumn
     * @param targetRow
     * @param targetColumn
     * @param rowIncrementPerStep 
     * @param columnIncrementPerStep
     * @return 
     */
    private boolean arePiecesBetweenSourceAndTarget(int sourceRow, int sourceColumn,
            int targetRow, int targetColumn, int rowIncrementPerStep, int columnIncrementPerStep) {

        int currentRow = sourceRow + rowIncrementPerStep;
        int currentColumn = sourceColumn + columnIncrementPerStep;
        while (true) {
            if (currentRow == targetRow && currentColumn == targetColumn) {
                break;
            }
            if (currentRow < Figure.ROW_1 || currentRow > Figure.ROW_8
                    || currentColumn < Figure.COLUMN_A || currentColumn > Figure.COLUMN_H) {
                break;
            }

            if (this.game.isNonCapturedFigureAt(currentRow, currentColumn)) {
                System.out.println("Warning #13: Figures in between source and target.");
                return true;
            }

            currentRow += rowIncrementPerStep;
            currentColumn += columnIncrementPerStep;
        }
        return false;
    }
}
