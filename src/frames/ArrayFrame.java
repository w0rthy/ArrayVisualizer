/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package frames;

import java.awt.Toolkit;
import java.util.Hashtable;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import main.ArrayManager;
import main.ArrayVisualizer;
import templates.Frame;
import utils.Highlights;
import visuals.VisualStyles;

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

final public class ArrayFrame extends javax.swing.JFrame {
    final private static long serialVersionUID = 1L;

    private int[] array;
    
    private ArrayManager ArrayManager;
    private ArrayVisualizer ArrayVisualizer;
    private Frame abstractFrame;
    private Highlights Highlights;
    private JFrame Frame;
    private UtilFrame UtilFrame;

    public ArrayFrame(int[] array, ArrayVisualizer arrayVisualizer) {
        this.array = array;
        
        this.ArrayVisualizer = arrayVisualizer;
        this.ArrayManager = ArrayVisualizer.getArrayManager();
        
        this.Highlights = ArrayVisualizer.getHighlights();
        this.Frame = ArrayVisualizer.getMainWindow();
        this.UtilFrame = ArrayVisualizer.getUtilFrame();
        
        setUndecorated(true);
        initComponents();
        setLocation(Math.min((int) Toolkit.getDefaultToolkit().getScreenSize().getWidth() - getWidth(), Frame.getX() + Frame.getWidth()), Frame.getY() + 29);
        setAlwaysOnTop(false);
        setVisible(true);
    }

    public void reposition(){
        toFront();
        setLocation(Math.min((int) Toolkit.getDefaultToolkit().getScreenSize().getWidth() - getWidth() - UtilFrame.getWidth(), Frame.getX() + Frame.getWidth()), Frame.getY() + 29);
        if(this.abstractFrame != null && abstractFrame.isVisible())
            abstractFrame.reposition();
    }

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        this.jLabel1 = new javax.swing.JLabel();
        this.jSlider = new javax.swing.JSlider(SwingConstants.VERTICAL, 1, 12, 11);

        jLabel1.setText("Array Size");

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
                if(ArrayManager.isLengthMutable()) {
                    ArrayVisualizer.setCurrentLength((int) Math.pow(2, jSlider.getValue()));
                    ArrayManager.initializeArray(array);
                }
                else jSlider.setValue(ArrayVisualizer.getLogBaseTwoOfLength());
                if(ArrayVisualizer.getVisualStyles() == visuals.VisualStyles.CIRCULAR && jSlider.getValue() == 1) jSlider.setValue(2);
                
                Highlights.clearAllMarks();
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
                                        .addComponent(this.jSlider)
                                        .addGap(0, 10, Short.MAX_VALUE))))
                );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, true)
                .addGroup(layout.createSequentialGroup()
                        .addGap(5, 5, 5)
                        .addComponent(this.jLabel1)
                        .addGap(5, 5, 5)
                        .addComponent(this.jSlider, UtilFrame.getHeight() - 26, UtilFrame.getHeight() - 26, UtilFrame.getHeight() - 26))
                );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JSlider jSlider;
    // End of variables declaration//GEN-END:variables
}