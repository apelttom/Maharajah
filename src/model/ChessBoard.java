/*  Instituto Tecnológico de Costa Rica
    Escuela de Ingeniería en Computación
    Programación Orientada a Objetos
    Prof.: Mauricio Avilés
    Proyecto 3 - El Maharaja y los Cipayos
    Tomas Apeltauer
    Saúl Zamora 
*/

package model;

public class ChessBoard {
    private Figure[][] grid = new Figure[8][8];
    
    //builder
    public ChessBoard()
    {
        grid = null;
    }
    
    //getter
    public Figure[][] getGrid() {
        return grid;
    }
    
}
