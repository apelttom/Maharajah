/*  Instituto Tecnológico de Costa Rica
    Escuela de Ingeniería en Computación
    Programación Orientada a Objetos
    Prof.: Mauricio Avilés
    Proyecto 3 - El Maharaja y los Cipayos
    Tomas Apeltauer
    Saúl Zamora 
*/

package Model;

public class Player {
    private String name;
    private boolean forfeit;
    
    //builder
    public Player(String pName)
    {
        name = pName;
        forfeit = false;
    }
    
    // getters and setters
    public String getName() {
        return name;
    }

    public boolean isForfeit() {
        return forfeit;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setForfeit(boolean forfeit) {
        this.forfeit = forfeit;
    }
}
