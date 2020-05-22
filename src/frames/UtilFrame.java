/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package frames;

import java.awt.Toolkit;
import java.util.Hashtable;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import main.ArrayManager;
import main.ArrayVisualizer;
import prompts.ShufflePrompt;
import prompts.SortPrompt;
import prompts.ViewPrompt;
import templates.Frame;
import utils.Delays;
import utils.Highlights;
import utils.Sounds;
import utils.Timer;
 
/*
 * 
MIT License

Copyright (c) 2019 w0rthy

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
 *
 */

/**
 *
 * @author S630690
 */
final public class UtilFrame extends javax.swing.JFrame {
    final private static long serialVersionUID = 1L;

    private int[] array;
    
    private ArrayManager ArrayManager;
    private ArrayVisualizer ArrayVisualizer;
    private Delays Delays;
    private Frame abstractFrame;
    private Highlights Highlights;
    private JFrame Frame;
    private Timer RealTimer;
    private Sounds Sounds;

    public UtilFrame(int[] array, ArrayVisualizer arrayVisualizer) {
        this.array = array;
        
        this.ArrayVisualizer = arrayVisualizer;
        this.ArrayManager = ArrayVisualizer.getArrayManager();
        
        this.Delays = ArrayVisualizer.getDelays();
        this.Frame = ArrayVisualizer.getMainWindow();
        this.Highlights = ArrayVisualizer.getHighlights();
        this.RealTimer = ArrayVisualizer.getTimer();
        this.Sounds = ArrayVisualizer.getSounds();
        
        setUndecorated(true);
        initComponents();
        setLocation(Math.min((int) Toolkit.getDefaultToolkit().getScreenSize().getWidth() - getWidth(), Frame.getX() + Frame.getWidth()), Frame.getY() + 29);
        setAlwaysOnTop(false);
        setVisible(true);
    }

    public void reposition(ArrayFrame af){
        toFront();
        setLocation(Math.min((int) Toolkit.getDefaultToolkit().getScreenSize().getWidth() - getWidth(), Frame.getX() + Frame.getWidth() + af.getWidth()), Frame.getY() + 29);
        if(this.abstractFrame != null && abstractFrame.isVisible())
            abstractFrame.reposition();
    }

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        this.jLabel1 = new javax.swing.JLabel();
        this.jButton1 = new javax.swing.JButton();
        this.jButton2 = new javax.swing.JButton();
        this.jButton3 = new javax.swing.JButton();
        this.jCheckBox1 = new javax.swing.JCheckBox();
        this.jCheckBox2 = new javax.swing.JCheckBox();
        this.jButton4 = new javax.swing.JButton();
        this.jCheckBox3 = new javax.swing.JCheckBox();
        this.jCheckBox4 = new javax.swing.JCheckBox();
        this.jButton5 = new javax.swing.JButton();
        this.jCheckBox5 = new javax.swing.JCheckBox();
        this.jButton6 = new javax.swing.JButton();
        this.jCheckBox6 = new javax.swing.JCheckBox();
        this.jCheckBox7 = new javax.swing.JCheckBox();
        this.jCheckBox8 = new javax.swing.JCheckBox();
        this.jSlider = new javax.swing.JSlider(SwingConstants.VERTICAL, 1, 12, 11);

        jLabel1.setText("Settings");

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);

        Hashtable<Integer, JLabel> labels = new Hashtable<>();
        labels.put(1, new JLabel("2"));
        labels.put(2, new JLabel("4"));
        labels.put(3, new JLabel("8"));
        labels.put(4, new JLabel("16"));
        labels.put(5, new JLabel("32"));
        labels.put(6, new JLabel("64"));
        labels.put(7, new JLabel("128"));
        labels.put(8, new JLabel("256"));
        labels.put(9, new JLabel("512"));
        labels.put(10, new JLabel("1024"));
        labels.put(11, new JLabel("2048"));
        labels.put(12, new JLabel("4096"));

        jSlider.setMajorTickSpacing(1);
        jSlider.setLabelTable(labels);
        jSlider.setPaintLabels(true);
        jSlider.setPaintTicks(true);
        jSlider.setSnapToTicks(true);
        jSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent event) {
                ArrayVisualizer.setCurrentLength((int) Math.pow(2, jSlider.getValue()));
            }
        });

        jButton1ResetText();
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed();
            }
        });

        jButton2ResetText();
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed();
            }
        });

        jButton3.setText("Change Speed");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed();
            }
        });

        jButton4.setText("Skip Sort");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed();
            }
        });

        jCheckBox1.setSelected(true);
        jCheckBox1.setText("Show Shuffle");
        jCheckBox1.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox1ActionPerformed();
            }
        });

        jCheckBox2.setSelected(false);
        jCheckBox2.setText("Linked Dots");
        jCheckBox2.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox2ActionPerformed();
            }
        });

        jCheckBox3.setSelected(true);
        jCheckBox3.setText("End Sweep Anim");
        jCheckBox3.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox3ActionPerformed();
            }
        });

        jCheckBox4.setSelected(true);
        jCheckBox4.setText("Calc Real Time");
        jCheckBox4.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox4ActionPerformed();
            }
        });

        jButton5.setText("Clear Stats");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed();
            }
        });

        jCheckBox5.setSelected(false);
        jCheckBox5.setText("Softer Sounds");
        jCheckBox5.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox5ActionPerformed();
            }
        });

        jButton6ResetText();
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed();
            }
        });

        jCheckBox6.setSelected(true);
        jCheckBox6.setText("Display Stats");
        jCheckBox6.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox6ActionPerformed();
            }
        });

        jCheckBox7.setSelected(true);
        jCheckBox7.setText("Enable Sounds");
        jCheckBox7.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox7ActionPerformed();
            }
        });

        jCheckBox8.setSelected(false);
        jCheckBox8.setText("Enable Color");
        jCheckBox8.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox8ActionPerformed();
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER, true)
                .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER, true)
                                .addComponent(this.jLabel1)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, true)
                                        .addComponent(this.jCheckBox1)
                                        .addComponent(this.jCheckBox2)
                                        .addComponent(this.jCheckBox3)
                                        .addComponent(this.jCheckBox4)
                                        .addComponent(this.jCheckBox6)
                                        .addComponent(this.jCheckBox7)
                                        .addComponent(this.jCheckBox8)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, true)
                                                .addComponent(this.jCheckBox5)
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                                                        .addComponent(this.jButton6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                        .addComponent(this.jButton5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                        .addComponent(this.jButton4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                        .addComponent(this.jButton3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                        .addComponent(this.jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                        .addComponent(this.jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))))
                        .addGap(0, 10, Short.MAX_VALUE))
                );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, true)
                .addGroup(layout.createSequentialGroup()
                        .addGap(5, 5, 5)
                        .addComponent(this.jLabel1)
                        .addGap(7, 7, 7)
                        .addComponent(this.jButton2)
                        .addGap(5, 5, 5)
                        .addComponent(this.jCheckBox2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(this.jCheckBox8)
                        .addGap(7, 7, 7)
                        .addComponent(this.jButton3)
                        .addGap(12, 12, 12)
                        .addComponent(this.jButton1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(this.jButton4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(this.jButton6)
                        .addGap(7, 7, 7)
                        .addComponent(this.jCheckBox1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(this.jCheckBox7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(this.jCheckBox5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(this.jCheckBox3)
                        .addGap(8, 8, 8)
                        .addComponent(this.jButton5)
                        .addGap(5, 5, 5)
                        .addComponent(this.jCheckBox6)
                        .addComponent(this.jCheckBox4))
                );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed() {//GEN-FIRST:event_jButton1ActionPerformed
        //CHANGE SORT
        if(this.abstractFrame != null && abstractFrame.isVisible()){
            boolean tmp = this.abstractFrame instanceof SortPrompt;
            abstractFrame.dispose();
            jButton1ResetText();
            if(tmp)
                return;
        }
        this.abstractFrame = new SortPrompt(this.array, this.ArrayVisualizer, this.Frame, this);
        jButton1.setText("Close");
        jButton2ResetText();
        jButton6ResetText();
    }//GEN-LAST:event_jButton1ActionPerformed

    public void jButton1ResetText() {
        jButton1.setText("Choose Sort");
    }

    private void jButton2ActionPerformed() {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        //CHANGE VIEW
        if(this.abstractFrame != null && abstractFrame.isVisible()){
            boolean tmp = this.abstractFrame instanceof ViewPrompt;
            jButton2ResetText();
            abstractFrame.dispose();
            if(tmp)
                return;
        }
        this.abstractFrame = new ViewPrompt(this.ArrayVisualizer, this.Frame, this);
        jButton2.setText("Close");
        jButton1ResetText();
        jButton6ResetText();
    }//GEN-LAST:event_jButton2ActionPerformed

    public void jButton2ResetText() {
        jButton2.setText("Visual Style");
    }

    private void jButton3ActionPerformed() {//GEN-FIRST:event_jButton3ActionPerformed
        boolean speedPromptAllowed;
        
        if(this.abstractFrame == null) {
            speedPromptAllowed = true;
        }
        else if(!this.abstractFrame.isVisible()) {
            speedPromptAllowed = true;
        }
        else {
            speedPromptAllowed = false;
        }
        
        if(speedPromptAllowed) {
            try{
                double oldRatio = Delays.getSleepRatio();
                double newRatio = Double.parseDouble(JOptionPane.showInputDialog(null, "Modify the visual's speed below (Ex. 10 = Ten times faster)", oldRatio));
                if(newRatio == 0) throw new Exception("Divide by zero!");
                Delays.setSleepRatio(newRatio);
                Delays.updateCurrentDelay(oldRatio, Delays.getSleepRatio());
            }
            catch(Exception e) { //TODO: Display not a number instead of println
                System.out.println("Not a number! (" + e.getMessage() + ")");
            }
        }
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jCheckBox1ActionPerformed() {//GEN-FIRST:event_jCheckBox2ActionPerformed
        ArrayVisualizer.toggleShuffleAnimation(jCheckBox1.isSelected());
    }//GEN-LAST:event_jCheckBox1ActionPerformed

    private void jCheckBox2ActionPerformed() {//GEN-FIRST:event_jCheckBox3ActionPerformed
        ArrayVisualizer.toggleLinkedDots(jCheckBox2.isSelected());
    }//GEN-LAST:event_jCheckBox2ActionPerformed

    private void jCheckBox3ActionPerformed() {//GEN-FIRST:event_jCheckBox3ActionPerformed
        Highlights.toggleFancyFinishes(jCheckBox3.isSelected());
    }//GEN-LAST:event_jCheckBox3ActionPerformed

    private void jButton4ActionPerformed() {//GEN-FIRST:event_jButton4ActionPerformed
        Delays.changeSkipped(true);
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jCheckBox4ActionPerformed() {//GEN-FIRST:event_jButton4ActionPerformed
        RealTimer.toggleRealTimer(jCheckBox4.isSelected());
    }//GEN-LAST:event_jCheckBox4ActionPerformed

    private void jButton5ActionPerformed() {//GEN-FIRST:event_jButton4ActionPerformed
        ArrayVisualizer.resetAllStatistics();
    }//GEN-LAST:event_jButton6ActionPerformed

    private void jCheckBox5ActionPerformed() {//GEN-FIRST:event_jButton4ActionPerformed
        if(jCheckBox5.isSelected()) {
            Sounds.toggleSofterSounds(true);
        }
        else {
            Sounds.toggleSofterSounds(false);
        }
    }//GEN-LAST:event_jCheckBox5ActionPerformed

    private void jButton6ActionPerformed() {//GEN-FIRST:event_jButton2ActionPerformed
        //CHANGE SIZE
        if(this.abstractFrame != null && abstractFrame.isVisible()){
            boolean tmp = this.abstractFrame instanceof ShufflePrompt;
            abstractFrame.dispose();
            jButton6ResetText();
            if(tmp)
                return;
        }
        this.abstractFrame = new ShufflePrompt(this.ArrayManager, this.Frame, this);
        jButton6.setText("Close");
        jButton1ResetText();
        jButton2ResetText();
    }//GEN-LAST:event_jButton7ActionPerformed

    public void jButton6ResetText() {
        jButton6.setText("Choose Shuffle");
    }

    private void jCheckBox6ActionPerformed() {//GEN-FIRST:event_jButton4ActionPerformed
        ArrayVisualizer.toggleStatistics(jCheckBox6.isSelected());
    }//GEN-LAST:event_jCheckBox6ActionPerformed

    private void jCheckBox7ActionPerformed() {//GEN-FIRST:event_jButton4ActionPerformed
        Sounds.toggleSounds(jCheckBox7.isSelected());
    }//GEN-LAST:event_jCheckBox7ActionPerformed

    private void jCheckBox8ActionPerformed() {//GEN-FIRST:event_jButton4ActionPerformed
        ArrayVisualizer.toggleColor(jCheckBox8.isSelected());
    }//GEN-LAST:event_jCheckBox8ActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JCheckBox jCheckBox2;
    private javax.swing.JCheckBox jCheckBox3;
    private javax.swing.JCheckBox jCheckBox4;
    private javax.swing.JCheckBox jCheckBox5;
    private javax.swing.JCheckBox jCheckBox6;
    private javax.swing.JCheckBox jCheckBox7;
    private javax.swing.JCheckBox jCheckBox8;
    private javax.swing.JSlider jSlider;
    // End of variables declaration//GEN-END:variables
}