/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.pegsolitaire;
public class PegSolitaire {

    public static void main(String[] args) {
        GameGui gui = new GameGui();
        GameSwing swing = new GameSwing(gui);
        
        gui.startGame(swing);        
    }
}
