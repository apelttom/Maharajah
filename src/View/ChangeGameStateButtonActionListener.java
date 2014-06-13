/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package View;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 *
 * @author ALEKS
 */
public class ChangeGameStateButtonActionListener implements ActionListener {
    
    private MainGUI mainGUI;

    public ChangeGameStateButtonActionListener(MainGUI mainGUI) {
        this.mainGUI = mainGUI;
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        this.mainGUI.changeGameState();
    }
    
}
