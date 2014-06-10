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

public class Figure {
    private boolean active;
    private Image image;
    
    //builder
    public Figure()
    {
        active = false;
        image = null;
    }
    
    //getters and setters
    public boolean isActive() {
        return active;
    }

    public Image getImage() {
        return image;
    }

    public void setActive(boolean nActive) {
        this.active = nActive;
    }

    public void setImage(Image nImage) {
        this.image = nImage;
    }
}
