/*  Instituto Tecnológico de Costa Rica
 Escuela de Ingeniería en Computación
 Programación Orientada a Objetos
 Prof.: Mauricio Avilés
 Proyecto 3 - El Maharaja y los Cipayos
 Tomas Apeltauer
 Saúl Zamora 
 */
package model;

public class Maharaja extends Figure {

    @Override
    int[][] getAllMoves(int posX, int posY) {
        int[][] moves = new int[8][8];
        Figure queen = new Queen();
        Figure knight = new Knight();
        int[][] movesQueen, movesKnight;
        movesQueen = queen.getAllMoves(posX, posY);
        movesKnight = knight.getAllMoves(posX, posY);

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                moves[i][j] = movesQueen[i][j] + movesKnight[i][j];
            }
        }
        return moves;
    }

    @Override
    boolean validateMove(int fromX, int fromY, int toX, int toY) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
