/*
 * jThread.java
 *
 * Created on February 24, 2007, 10:43 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package threading;

import main.Bool;
import processing.CodeProcessor;

/**
 *
 * @author lardmaster
 */
public class jThread extends Thread {
    volatile Bool keepRunning, pause;
    public jThread(CodeProcessor c) {
        super();
        this.keepRunning=c.keepRunning;
        this.pause=c.pause;
    }
    
    public void start() {
        super.start();
        keepRunning.setB(true);
        pause.setB(false);
    }
    public void terminate() {
        keepRunning.setB(false);
    }
    public void jResume() {
        pause.setB(false);
    }
    public void pause() {
        pause.setB(true);
    }
}
