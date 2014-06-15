/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Controller;

import View.MainGUI;
import model.Figure;
import controller.Game;

/**
 *
 * @author ALEKS
 */
public class Main {
    
    public static void main(String[] args) {
        Game newGame = new Game();
        
        MainGUI whiteGUI = new MainGUI(newGame);
        MainGUI blackGUI = new MainGUI(newGame);
        whiteGUI.setFrameTittle("The Maharaja and the cepoys: WHITE PLAYER SCREEN");
        blackGUI.setFrameTittle("The Maharaja and the cepoys: BLACK PLAYER SCREEN");
        newGame.setPlayer(Figure.COLOR_WHITE, whiteGUI);
        newGame.setPlayer(Figure.COLOR_BLACK, blackGUI);
        
        new Thread(newGame).start();
    }
    
}
