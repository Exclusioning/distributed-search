package com.mycompany.pegsolitaire;


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.io.IOException;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

public class GameSwing {
    
    public JFrame frame;
    public JButton[][] buttons;
    public JLabel scoreLabel;
    
    private GameGui gui;
    
    public JSpinner depthSpin;
    public JSpinner threadSpin;
    
    public GameSwing(GameGui gui){
        this.gui = gui;
        gui.setGui(this);
        
        frame = new JFrame("Peg Solitaire");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        buttons = new JButton[7][7];
        
        buildUI();
        
        frame.pack();
        frame.setVisible(true);
    }
    
    public void buildUI(){
        frame.setLayout(new BorderLayout());
        
        JPanel boardPanel = new JPanel();
        boardPanel.setLayout(new GridLayout(7, 7));
        
        for(int x = 0; x < 7; x++){
            for(int y = 0; y < 7; y++){
                
                JButton button = new JButton();
                button.setPreferredSize(new Dimension(60,60));

                button.setOpaque(true);
                button.setContentAreaFilled(true);
                button.setBorderPainted(false);
                buttons[x][y] = button;
                
                final int fx = x;
                final int fy = y;
                
                button.addActionListener(e -> gui.cellClick(fx, fy));
                
                boardPanel.add(button);
            }
            
        }
        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new GridLayout(3,1));
        
        depthSpin = new JSpinner(new SpinnerNumberModel(2, 1, 8, 1));
        controlPanel.add(new JLabel("Search depth:"));
        controlPanel.add(depthSpin);
        
        threadSpin = new JSpinner(new SpinnerNumberModel(2, 1, 8, 1));
        controlPanel.add(new JLabel("Number of Threads:"));
        controlPanel.add(threadSpin);
        
        frame.add(controlPanel, BorderLayout.WEST);
        JButton suggestion = new JButton("Suggested Move");
            suggestion.addActionListener(e ->{
                new Thread(() -> {
                    try {
                        Move move = gui.suggestedMove();
                        if(move != null){
                                SwingUtilities.invokeLater(() -> {
                                highlightCell(move.fromX, move.fromY);
                                highlightCell(move.toX, move.toY);
                                });
                            }
                        }   catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }).start();
            });
            frame.add(suggestion, BorderLayout.NORTH);
            
            JCheckBox distributed = new JCheckBox("To use Server Evaluation");
            distributed.addActionListener(e -> gui.useDistribution = distributed.isSelected());
            frame.add(distributed, BorderLayout.EAST);
        
        scoreLabel = new JLabel("Score: 0");
        scoreLabel.setHorizontalAlignment(SwingConstants.CENTER);
        scoreLabel.setFont(new Font("Arial", Font.BOLD, 16));
        
        frame.add(boardPanel, BorderLayout.CENTER);
        frame.add(scoreLabel, BorderLayout.SOUTH);   
    }
    
    public void boardUpdater(){
        int[][] grid = gui.getBoard().getGrid();
        
        for(int x = 0; x < 7; x++){
            for(int y = 0; y < 7; y++){
                
                JButton button = buttons[x][y];
                
                if(grid[x][y] == -1){
                    button.setBackground(Color.DARK_GRAY);
                    button.setEnabled(false);
                } else if (grid[x][y] == 1){
                    button.setBackground(Color.RED);
                    button.setEnabled(true);
                } else if (grid[x][y] == 0){
                    button.setBackground(Color.WHITE);
                    button.setEnabled(true);
                }
            }
        }
    }
    
    public int getDepth(){
        return (int) depthSpin.getValue();
    }
    
    public int getThreads(){
        return (int) threadSpin.getValue();
    }
    
    public void highlightCell(int x, int y){
        buttons[x][y].setBackground(Color.YELLOW);
    }
    
    public void clearHighlight(){
        boardUpdater();
    }
    
    public void scoreUpdater(int score){
        scoreLabel.setText("Score: " + score);
    }
    
    public void gameOverMSG(String msg){
        JOptionPane.showMessageDialog(frame, msg);
    }
}
