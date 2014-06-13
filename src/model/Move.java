/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package model;

/**
 *
 * @author ALEKS
 */
public class Move {
    private int sourceRow;
    private int sourceColumn;
    private int targetRow;
    private int targetColumn;
    
    // constructor
    public Move(int sourceRow, int sourceColumn, int targetRow, int targetColumn) {
        this.sourceRow = sourceRow;
        this.sourceColumn = sourceColumn;
        this.targetRow = targetRow;
        this.targetColumn = targetColumn;
    }

    //setters
    public void setSourceRow(int sourceRow) {
        this.sourceRow = sourceRow;
    }

    public void setSourceColumn(int sourceColumn) {
        this.sourceColumn = sourceColumn;
    }

    public void setTargetRow(int targetRow) {
        this.targetRow = targetRow;
    }

    public void setTargetColumn(int targetColumn) {
        this.targetColumn = targetColumn;
    }
    
    //getters
    public int getSourceRow() {
        return sourceRow;
    }

    public int getSourceColumn() {
        return sourceColumn;
    }

    public int getTargetRow() {
        return targetRow;
    }

    public int getTargetColumn() {
        return targetColumn;
    }
}
