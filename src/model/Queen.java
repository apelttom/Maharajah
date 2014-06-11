/*  Instituto Tecnológico de Costa Rica
 Escuela de Ingeniería en Computación
 Programación Orientada a Objetos
 Prof.: Mauricio Avilés
 Proyecto 3 - El Maharaja y los Cipayos
 Tomas Apeltauer
 Saúl Zamora 
 */
package model;

public class Queen extends Figure {

    @Override
    int[][] getAllMoves(int posX, int posY) {
        int[][] moves = new int[8][8];
        Figure rook = new Rook();
        Figure bishop = new Bishop();
        int[][] movesRook, movesBishop;
        movesRook = rook.getAllMoves(posX, posY);
        movesBishop = bishop.getAllMoves(posX, posY);
        
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                moves[i][j] = movesRook[i][j] + movesBishop[i][j];
            }
        }
        return moves;
    }

    @Override
    boolean validateMove(int fromX, int fromY, int toX, int toY) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
