/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package View;

import java.awt.Image;
import model.Figure;

/**
 *
 * @author ALEKS
 */
public class FigureGUI {
    
    private Image img;
    private int x;
    private int y;
    private Figure figure;
    
    // constructor
    public FigureGUI(Image img, Figure figure) {
        this.img = img;
        this.figure = figure;
        this.resetToUnderlyingFigurePosition();
    }
    
    // setters
    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }
    
    // getters
    public Image getImg() {
        return img;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
    
    public int getWidth() {
        return img.getWidth(null);
    }
    
    public int getHeight() {
        return img.getHeight(null);
    }
    
    public int getColor() {
        return this.figure.getColor();
    }
    
    public Figure getFigure() {
        return this.figure;
    }
    
    public boolean isCaptured() {
        return this.figure.isCaptured();
    }
    
    @Override
    public String toString() {
        return this.figure + " " + x + " " + y;
    }
    
    public void resetToUnderlyingFigurePosition() {
        this.x = MainGUI.convertColumnToX(figure.getColumn());
        this.y = MainGUI.convertRowToY(figure.getRow());
    }
}
