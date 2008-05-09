package main;

import java.awt.Dimension;
import java.awt.Robot;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;

public class Frame extends JFrame {
    Robot r;
    
    JTextArea code;
    
    private JPanel panel;
    
    private JButton runButton;
    
    Thread executor;
    
    private JButton stopButton;
    
    JPanel buttonPanel;
    
    private JButton pauseButton;
    
    public Frame(Robot r) {
        super();
        this.setBounds(0, 0, 375, 450);
        this.r = r;
        runButton = new JButton("Run") {
            
            /**
             *
             */
            private static final long serialVersionUID = 0L;
            
            
            protected void processMouseEvent(MouseEvent e) {
                if (e.getID() == MouseEvent.MOUSE_CLICKED) {
                    if (executor == null || !executor.isAlive()) {     
                        executor.run();
                    }
                }
                super.processMouseEvent(e);
            }
        };
        stopButton = new JButton("Stop") {
            
            /**
             *
             */
            private static final long serialVersionUID = 0L;
            
            
            protected void processMouseEvent(MouseEvent e) {
                if (e.getID() == MouseEvent.MOUSE_CLICKED) {
                    if (executor != null && executor.isAlive()) {
                        executor = null;
                    }
                }
                super.processMouseEvent(e);
            }
        };
        pauseButton = new JButton("Pause") {
            
            /**
             *
             */
            private static final long serialVersionUID = -7372694907863103293L;
            
            
            protected void processMouseEvent(MouseEvent e) {
                if (e.getID() == MouseEvent.MOUSE_CLICKED) {
                    if (executor != null && executor.isAlive()) {
                        executor.suspend();
                    }
                }
                super.processMouseEvent(e);
            }
        };
        panel = new JPanel();
        panel.setBorder(BorderFactory.createEtchedBorder());
        code = new JTextArea("Put code here");
        code.setBorder(BorderFactory.createEtchedBorder());
        code.setPreferredSize(new Dimension(300, 350));
        panel.add(code);
        panel.setPreferredSize(new Dimension(375, 375));
        buttonPanel = new JPanel();
        buttonPanel.add(runButton);
        buttonPanel.add(stopButton);
        panel.add(buttonPanel);
        this.add(panel);
        // this.setResizable(false);
        this.setVisible(true);
        
    }
    
    /**
     *
     */
    private static final long serialVersionUID = 4887525192006201710L;
    
}
