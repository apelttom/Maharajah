/*  Instituto Tecnológico de Costa Rica
 Escuela de Ingeniería en Computación
 Programación Orientada a Objetos
 Prof.: Mauricio Avilés
 Proyecto 3 - El Maharaja y los Cipayos
 Tomas Apeltauer
 Saúl Zamora 
 */
package model;

public class King extends Figure {

    @Override
    int[][] getAllMoves(int posX, int posY) {
        int[][] moves = new int[8][8];
        for (int y = posY - 1; y <= posY + 1; y++) {
            for (int x = posX - 1; x <= posX + 1; x++) {
                if ((x >= 0 && x < 8)
                        && (y >= 0 && y < 8)
                        && !(x == posX && y == posY)) {
                    moves[y][x] = 1;
                }
            }
        }
        return moves;
    }

    @Override
    boolean validateMove(int fromX, int fromY, int toX, int toY) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
