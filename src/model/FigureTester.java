/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * THIS CLASS IS JUST FOR TESTING AND SHOULD BE DELETED BEFORE RELEASING.
 *
 * This class is in this package only because Figure class method getAllMoves is
 * without modifier (package protected) and so cannot be called outside of
 * package.
 *
 * @author Thomas Apeltauer <apelttom@live.com>
 * Created on 11/06/2014
 * Last modified on 11/06/2014
 */
public class FigureTester {

    public static void main(String args[]) throws IOException {
        BufferedReader reader
                = new BufferedReader(new InputStreamReader(System.in));
        String inputX, inputY;
        int x, y;
//        Figure figure = new King();
//        Figure figure = new Bishop();
//        Figure figure = new Knight();
//        Figure figure = new Rook();
//        Figure figure = new Queen();
//        Figure figure = new Pawn();
        Figure figure = new Maharaja();
        while (true) {
            System.out.println("Place the figure (Column):");
            inputX = reader.readLine();
            System.out.println("Place the figure (Row):");
            inputY = reader.readLine();
            x = Integer.parseInt(inputX) - 1;
            y = Integer.parseInt(inputY) - 1;
            int[][] moves = figure.getAllMoves(x, y);
            moveFigure(moves, x, y);
        }
    }

    static void moveFigure(int[][] figureMoves, int srcX, int srcY) {
        //Print moves of a figure
        for (int y = 0; y < 8; y++) {
            for (int x = 0; x < 8; x++) {
                String move = " ";
                if (figureMoves[y][x] == 1) {
                    move = "x";
                }
                if (figureMoves[y][x] == 2) {
                    move = "?";
                }
                if (x == srcX && y == srcY) {
                    move = "*";
                }
                System.out.print("[" + move + "] ");
            }
            System.out.println();
        }
        System.out.flush();
    }

}
