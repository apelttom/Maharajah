/*  Instituto Tecnológico de Costa Rica
 Escuela de Ingeniería en Computación
 Programación Orientada a Objetos
 Prof.: Mauricio Avilés
 Proyecto 3 - El Maharaja y los Cipayos
 Tomas Apeltauer
 Saúl Zamora 
 */
package model;

public class Rook extends Figure {

    @Override
    int[][] getAllMoves(int posX, int posY) {
        int[][] moves = new int[8][8];
        for (int y = 0; y < 8; y++) {
            for (int x = 0; x < 8; x++) {
                if (((x == posX) || (y == posY)) && !(x == posX && y == posY)) {
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
