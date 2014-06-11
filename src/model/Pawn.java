/*  Instituto Tecnológico de Costa Rica
 Escuela de Ingeniería en Computación
 Programación Orientada a Objetos
 Prof.: Mauricio Avilés
 Proyecto 3 - El Maharaja y los Cipayos
 Tomas Apeltauer
 Saúl Zamora 
 */
package model;

public class Pawn extends Figure {

    private final int[][] offsets = {
        {-1, 1},
        {0, 1},
        {1, 1},};

    @Override
    int[][] getAllMoves(int posX, int posY) {
        int[][] moves = new int[8][8];
        for (int[] offset : offsets) {
            int dstX = posX + offset[0];
            int dstY = posY + offset[1];
            if ((dstX >= 0 && dstX < 8) && (dstY >= 0 && dstY < 8)) {
                moves[dstY][dstX] = (dstX == posX) ? 1 : 2;
            }
        }
        return moves;
    }

    @Override
    boolean validateMove(int fromX, int fromY, int toX, int toY) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
