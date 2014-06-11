/*  Instituto Tecnológico de Costa Rica
 Escuela de Ingeniería en Computación
 Programación Orientada a Objetos
 Prof.: Mauricio Avilés
 Proyecto 3 - El Maharaja y los Cipayos
 Tomas Apeltauer
 Saúl Zamora 
 */
package model;

import java.awt.Image;

public abstract class Figure {

    private boolean active = false;
    private Image image = null;

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    abstract int[][] getAllMoves(int posX, int posY);
    
    abstract boolean validateMove(int fromX, int fromY, int toX, int toY);
}
