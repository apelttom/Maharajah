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

    public MoveValidator(Game chessGame) {
        this.game = chessGame;
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
            System.out.println("no source piece");
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
            System.out.println("it's not your turn");
            return false;
        }

        // check if target location within boundaries
        if (targetRow < Figure.ROW_1 || targetRow > Figure.ROW_8
                || targetColumn < Figure.COLUMN_A || targetColumn > Figure.COLUMN_H) {
            System.out.println("target row or column out of scope");
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
        // handle stalemate and checkmate
        return true;
    }

    private boolean isTargetLocationCaptureable() {
        if (targetFigure == null) {
            return false;
        } else if (targetFigure.getColor() != sourceFigure.getColor()) {
            return true;
        } else {
            return false;
        }
    }

    private boolean isTargetLocationFree() {
        return targetFigure == null;
    }

    private boolean isValidBishopMove(int sourceRow, int sourceColumn, int targetRow, int targetColumn) {
        // target location possible?
        if (isTargetLocationFree() || isTargetLocationCaptureable()) {
            //ok
        } else {
            System.out.println("Target location not free nor captureable.");
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
            System.out.println("Not moving diagonally.");
            isValid = false;
        }
        return isValid;
    }

    private boolean isValidQueenMove(int sourceRow, int sourceColumn, int targetRow, int targetColumn) {
        boolean result = isValidBishopMove(sourceRow, sourceColumn, targetRow, targetColumn);
        result |= isValidRookMove(sourceRow, sourceColumn, targetRow, targetColumn);
        return result;
    }
    
    private boolean isValidMaharajaMove(int sourceRow, int sourceColumn, int targetRow, int targetColumn) {
        boolean result = isValidQueenMove(sourceRow, sourceColumn, targetRow, targetColumn);
        result |= isValidKnightMove(sourceRow, sourceColumn, targetRow, targetColumn);
        return result;
    }

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
                        System.out.println("Not moving one down.");
                        isValid = false;
                    }
                }
            } else {
                // not the same column
                System.out.println("Not staying in same column.");
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
                        System.out.println("Not moving one down.");
                        isValid = false;
                    }
                }
            } else {
                // note one column to the left or right
                System.out.println("Not moving one column to left or right.");
                isValid = false;
            }
        }
        return isValid;
    }

    private boolean isValidKnightMove(int sourceRow, int sourceColumn, int targetRow, int targetColumn) {
        
        // target location possible?
        if (isTargetLocationFree() || isTargetLocationCaptureable()) {
            //ok
        } else {
            System.out.println("Target location not free nor captureable.");
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

    private boolean isValidKingMove(int sourceRow, int sourceColumn, int targetRow, int targetColumn) {

        // target location possible?
        if (isTargetLocationFree() || isTargetLocationCaptureable()) {
            //ok
        } else {
            System.out.println("Target location not free nor captureable.");
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
            System.out.println("Moving too far.");
            isValid = false;
        }
        return isValid;
    }

    private boolean isValidRookMove(int sourceRow, int sourceColumn, int targetRow, int targetColumn) {
        if (isTargetLocationFree() || isTargetLocationCaptureable()) {
            //ok
        } else {
            System.out.println("Target location not free nor captureable.");
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
            System.out.println("Not moving straight.");
            isValid = false;
        }

        return isValid;
    }

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
                System.out.println("Figures in between source and target.");
                return true;
            }

            currentRow += rowIncrementPerStep;
            currentColumn += columnIncrementPerStep;
        }
        return false;
    }

    public static void main(String[] args) {
        Game ch = new Game();
        MoveValidator mo = new MoveValidator(ch);
        Move move = null;
        boolean isValid;

        int sourceRow;
        int sourceColumn;
        int targetRow;
        int targetColumn;
        int testCounter = 1;

        // ok
        sourceRow = Figure.ROW_2;
        sourceColumn = Figure.COLUMN_D;
        targetRow = Figure.ROW_3;
        targetColumn = Figure.COLUMN_D;
        move = new Move(sourceRow, sourceColumn, targetRow, targetColumn);
        isValid = mo.isMoveValid(move);
        ch.movePiece(move);
        System.out.println(testCounter + ". test result: " + (true == isValid));
        testCounter++;

        // it's not white's turn
        sourceRow = Figure.ROW_2;
        sourceColumn = Figure.COLUMN_B;
        targetRow = Figure.ROW_3;
        targetColumn = Figure.COLUMN_B;
        move = new Move(sourceRow, sourceColumn, targetRow, targetColumn);
        isValid = mo.isMoveValid(move);
        System.out.println(testCounter + ". test result: " + (false == isValid));
        testCounter++;

        // ok
        sourceRow = Figure.ROW_7;
        sourceColumn = Figure.COLUMN_E;
        targetRow = Figure.ROW_6;
        targetColumn = Figure.COLUMN_E;
        move = new Move(sourceRow, sourceColumn, targetRow, targetColumn);
        isValid = mo.isMoveValid(move);
        ch.movePiece(move);
        System.out.println(testCounter + ". test result: " + (true == isValid));
        testCounter++;

        // pieces in the way
        sourceRow = Figure.ROW_1;
        sourceColumn = Figure.COLUMN_F;
        targetRow = Figure.ROW_4;
        targetColumn = Figure.COLUMN_C;
        move = new Move(sourceRow, sourceColumn, targetRow, targetColumn);
        isValid = mo.isMoveValid(move);
        System.out.println(testCounter + ". test result: " + (false == isValid));
        testCounter++;

        // ok
        sourceRow = Figure.ROW_1;
        sourceColumn = Figure.COLUMN_C;
        targetRow = Figure.ROW_4;
        targetColumn = Figure.COLUMN_F;
        move = new Move(sourceRow, sourceColumn, targetRow, targetColumn);
        isValid = mo.isMoveValid(move);
        ch.movePiece(move);
        System.out.println(testCounter + ". test result: " + (true == isValid));
        testCounter++;

        // ok
        sourceRow = Figure.ROW_8;
        sourceColumn = Figure.COLUMN_B;
        targetRow = Figure.ROW_6;
        targetColumn = Figure.COLUMN_C;
        move = new Move(sourceRow, sourceColumn, targetRow, targetColumn);
        isValid = mo.isMoveValid(move);
        ch.movePiece(move);
        System.out.println(testCounter + ". test result: " + (true == isValid));
        testCounter++;

        // invalid knight move
        sourceRow = Figure.ROW_1;
        sourceColumn = Figure.COLUMN_G;
        targetRow = Figure.ROW_3;
        targetColumn = Figure.COLUMN_G;
        move = new Move(sourceRow, sourceColumn, targetRow, targetColumn);
        isValid = mo.isMoveValid(move);
        System.out.println(testCounter + ". test result: " + (false == isValid));
        testCounter++;

        // invalid knight move
        sourceRow = Figure.ROW_1;
        sourceColumn = Figure.COLUMN_G;
        targetRow = Figure.ROW_2;
        targetColumn = Figure.COLUMN_E;
        move = new Move(sourceRow, sourceColumn, targetRow, targetColumn);
        isValid = mo.isMoveValid(move);
        System.out.println(testCounter + ". test result: " + (false == isValid));
        testCounter++;

        // ok
        sourceRow = Figure.ROW_1;
        sourceColumn = Figure.COLUMN_G;
        targetRow = Figure.ROW_3;
        targetColumn = Figure.COLUMN_H;
        move = new Move(sourceRow, sourceColumn, targetRow, targetColumn);
        isValid = mo.isMoveValid(move);
        ch.movePiece(move);
        System.out.println(testCounter + ". test result: " + (true == isValid));
        testCounter++;

        // pieces in between
        sourceRow = Figure.ROW_8;
        sourceColumn = Figure.COLUMN_A;
        targetRow = Figure.ROW_5;
        targetColumn = Figure.COLUMN_A;
        move = new Move(sourceRow, sourceColumn, targetRow, targetColumn);
        isValid = mo.isMoveValid(move);
        ch.movePiece(move);
        System.out.println(testCounter + ". test result: " + (false == isValid));
        testCounter++;
    }

}
