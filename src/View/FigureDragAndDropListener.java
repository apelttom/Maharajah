/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package View;

import controller.Game;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import model.Figure;

/**
 *
 * @author ALEKS
 */
class FigureDragAndDropListener implements MouseListener, MouseMotionListener {
    private ArrayList<FigureGUI> guiFigures;
    private MainGUI mainGUI;
    
    private FigureGUI dragFigure;
    private int dragOffsetX;
    private int dragOffsetY;

    public FigureDragAndDropListener(ArrayList<FigureGUI> figures, MainGUI mainGUI) {
        this.guiFigures = figures;
        this.mainGUI = mainGUI;
    }

    @Override
    public void mousePressed(MouseEvent event) {
        int x = event.getPoint().x;
        int y = event.getPoint().y;
        
        for (int i = this.guiFigures.size()-1; i >= 0; i--) {
            FigureGUI figureGUI = this.guiFigures.get(i);
            if (figureGUI.isCaptured()) continue;
            
            if (mouseOverFigure(figureGUI, x, y)) {
                if ((this.mainGUI.getGameState() == Game.GAME_STATE_WHITE && figureGUI.getColor() == Figure.COLOR_WHITE)
                     || (this.mainGUI.getGameState() == Game.GAME_STATE_BLACK && figureGUI.getColor() == Figure.COLOR_BLACK)) {
                    
                    this.dragOffsetX = x - figureGUI.getX();
                    this.dragOffsetY = y - figureGUI.getY();
                    this.mainGUI.setDragFigure(figureGUI);
                    this.mainGUI.repaint();
                    break;
                }
            }
        }
        
        if (this.mainGUI.getDragFigure() != null) {
            this.guiFigures.remove(this.mainGUI.getDragFigure());
            this.guiFigures.add(this.mainGUI.getDragFigure());
        }
    }
    
    private boolean mouseOverFigure(FigureGUI fig, int x, int y) {
        return fig.getX() <= x
                && fig.getX() + fig.getWidth() >= x
                && fig.getY() <= y
                && fig.getY() + fig.getHeight() >= y;
                
    }
    
    @Override
    public void mouseReleased(MouseEvent event) {
        if (this.mainGUI.getDragFigure() != null) {
            int x = event.getPoint().x - this.dragOffsetX;
            int y = event.getPoint().y - this.dragOffsetY;
            
            mainGUI.setNewFigureLocation(this.mainGUI.getDragFigure(), x, y);
            this.mainGUI.repaint();
            this.mainGUI.setDragFigure(null);
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) { }

    @Override
    public void mouseDragged(MouseEvent e) {
        if (this.dragFigure != null) {
            int x = e.getPoint().x - this.dragOffsetX;
            int y = e.getPoint().y - this.dragOffsetY;
            
            System.out.println("Row: " + MainGUI.convertYToRow(y) + "Column: " + MainGUI.convertXToColumn(x));
            
            this.dragFigure.setX(x);
            this.dragFigure.setY(y);
            this.mainGUI.repaint();
        }        
    }

    @Override
    public void mouseEntered(MouseEvent e) { }

    @Override
    public void mouseExited(MouseEvent e) { }

    @Override
    public void mouseMoved(MouseEvent e) { }
}
