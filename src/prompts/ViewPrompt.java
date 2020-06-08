/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package prompts;

import javax.swing.JFrame;

import frames.UtilFrame;
import main.ArrayVisualizer;
import templates.Frame;
import visuals.VisualStyles;
import javax.swing.JButton;
import javax.swing.GroupLayout.Alignment;
import javax.swing.GroupLayout;
import javax.swing.LayoutStyle.ComponentPlacement;

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

final public class ViewPrompt extends javax.swing.JFrame implements Frame {

    private static final long serialVersionUID = 1L;

    private ArrayVisualizer ArrayVisualizer;
    private JFrame Frame;
    private UtilFrame UtilFrame;
    
    public ViewPrompt(ArrayVisualizer arrayVisualizer, JFrame frame, UtilFrame utilFrame) {
        this.ArrayVisualizer = arrayVisualizer;
        this.Frame = frame;
        this.UtilFrame = utilFrame;
        
        setAlwaysOnTop(true);
        setUndecorated(true);
        initComponents();
        reposition();
        setVisible(true);
    }

    @Override
    public void reposition(){
        setLocation(Frame.getX() + ((Frame.getWidth() - getWidth()) / 2), Frame.getY() + ((Frame.getHeight() - getHeight()) / 2));
    }

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        this.jLabel1 = new javax.swing.JLabel();
        this.barGraph = new javax.swing.JButton();
        this.dotGraph = new javax.swing.JButton();
        this.colorCircle = new javax.swing.JButton();
        this.triangleMesh = new javax.swing.JButton();
        this.spiral = new javax.swing.JButton();
        this.disparity = new javax.swing.JButton();
        this.disparityDots = new javax.swing.JButton();
        this.spiralDots= new javax.swing.JButton();
        this.rainbow = new javax.swing.JButton();
        this.hoops = new javax.swing.JButton();
        this.sineWave = new javax.swing.JButton();
        this.waveDots = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);

        jLabel1.setText("Select Visual Style");

        barGraph.setText("Bar Graph");
        barGraph.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                barGraphActionPerformed(evt);
            }
        });

        dotGraph.setText("Dot Graph");
        dotGraph.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                dotGraphActionPerformed(evt);
            }
        });

        colorCircle.setText("Color Circle");
        colorCircle.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                colorCircleActionPerformed(evt);
            }
        });

        triangleMesh.setText("Triangle Mesh");
        triangleMesh.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                triangleMeshActionPerformed(evt);
            }
        });

        spiral.setText("Spiral");
        spiral.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                spiralActionPerformed(evt);
            }
        });

        rainbow.setText("Rainbow");
        rainbow.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rainbowActionPerformed(evt);
            }
        });

        disparity.setText("Disparity Circle");
        disparity.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                disparityActionPerformed(evt);
            }
        });

        hoops.setText("Hoops");
        hoops.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                hoopsActionPerformed(evt);
            }
        });

        disparityDots.setText("Disparity Dots");
        disparityDots.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                disparityDotsActionPerformed(evt);
            }
        });

        spiralDots.setText("Spiral Dots");
        spiralDots.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                spiralDotsActionPerformed(evt);
            }
        });
        
        sineWave.setText("Sine Wave");
        sineWave.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sineWaveActionPerformed(evt);
            }
        });
        waveDots.setText("Wave Dots");
        waveDots.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                waveDotsActionPerformed(evt);
            }
        });
        
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        layout.setHorizontalGroup(
            layout.createParallelGroup(Alignment.CENTER)
                .addComponent(jLabel1)
                .addGroup(Alignment.LEADING, layout.createSequentialGroup()
                    .addGap(18)
                    .addGroup(layout.createParallelGroup(Alignment.LEADING)
                        .addComponent(barGraph, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 105, Short.MAX_VALUE)
                        .addComponent(rainbow, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 105, Short.MAX_VALUE)
                        .addComponent(colorCircle, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 105, Short.MAX_VALUE)
                        .addComponent(disparity, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(disparityDots, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 105, Short.MAX_VALUE)
                        .addComponent(sineWave, GroupLayout.DEFAULT_SIZE, 105, Short.MAX_VALUE))
                    .addPreferredGap(ComponentPlacement.RELATED)
                    .addGroup(layout.createParallelGroup(Alignment.LEADING)
                        .addComponent(waveDots, GroupLayout.DEFAULT_SIZE, 101, Short.MAX_VALUE)
                        .addComponent(dotGraph, GroupLayout.DEFAULT_SIZE, 101, Short.MAX_VALUE)
                        .addComponent(triangleMesh, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(spiral, GroupLayout.DEFAULT_SIZE, 101, Short.MAX_VALUE)
                        .addComponent(hoops, GroupLayout.DEFAULT_SIZE, 101, Short.MAX_VALUE)
                        .addComponent(spiralDots, GroupLayout.DEFAULT_SIZE, 101, Short.MAX_VALUE))
                    .addGap(18))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(Alignment.CENTER)
                .addGroup(layout.createSequentialGroup()
                    .addGap(7)
                    .addComponent(jLabel1)
                    .addPreferredGap(ComponentPlacement.UNRELATED)
                    .addGroup(layout.createParallelGroup(Alignment.BASELINE)
                        .addComponent(barGraph)
                        .addComponent(dotGraph))
                    .addPreferredGap(ComponentPlacement.UNRELATED)
                    .addGroup(layout.createParallelGroup(Alignment.BASELINE)
                        .addComponent(rainbow)
                        .addComponent(triangleMesh))
                    .addPreferredGap(ComponentPlacement.UNRELATED)
                    .addGroup(layout.createParallelGroup(Alignment.BASELINE)
                        .addComponent(colorCircle)
                        .addComponent(hoops))
                    .addPreferredGap(ComponentPlacement.UNRELATED)
                    .addGroup(layout.createParallelGroup(Alignment.BASELINE)
                        .addComponent(disparity)
                        .addComponent(spiral))
                    .addPreferredGap(ComponentPlacement.UNRELATED)
                    .addGroup(layout.createParallelGroup(Alignment.BASELINE)
                        .addComponent(disparityDots)
                        .addComponent(spiralDots))
                    .addPreferredGap(ComponentPlacement.UNRELATED)
                    .addGroup(layout.createParallelGroup(Alignment.LEADING)
                        .addComponent(sineWave)
                        .addComponent(waveDots))
                    .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        getContentPane().setLayout(layout);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void setAllFieldsFalse(){
        ArrayVisualizer.togglePointer(false);
        ArrayVisualizer.toggleDistance(false);
        ArrayVisualizer.togglePixels(false);
        ArrayVisualizer.toggleRainbow(false);
        ArrayVisualizer.toggleSpiral(false);
        ArrayVisualizer.toggleWave(false);
    }

    private void barGraphActionPerformed(java.awt.event.ActionEvent evt) {
        setAllFieldsFalse();
        ArrayVisualizer.setVisual(VisualStyles.BARS);
        UtilFrame.jButton2ResetText();
        dispose();
    }
    private void dotGraphActionPerformed(java.awt.event.ActionEvent evt) {
        setAllFieldsFalse();
        ArrayVisualizer.setVisual(VisualStyles.PIXELS);
        UtilFrame.jButton2ResetText();
        dispose();
    }
    private void rainbowActionPerformed(java.awt.event.ActionEvent evt) {
        setAllFieldsFalse();
        ArrayVisualizer.setVisual(VisualStyles.BARS);
        ArrayVisualizer.toggleRainbow(true);
        UtilFrame.jButton2ResetText();
        dispose();
    }
    private void triangleMeshActionPerformed(java.awt.event.ActionEvent evt) {
        setAllFieldsFalse();
        ArrayVisualizer.setVisual(VisualStyles.MESH);
        UtilFrame.jButton2ResetText();
        dispose();
    }
    private void colorCircleActionPerformed(java.awt.event.ActionEvent evt) {
        //TODO: Pointer as separate option
        setAllFieldsFalse();
        ArrayVisualizer.setVisual(VisualStyles.CIRCULAR);
        //ArrayVisualizer.togglePointer(true);
        ArrayVisualizer.toggleRainbow(true);
        if(ArrayVisualizer.getCurrentLength() == 2) ArrayVisualizer.setCurrentLength(4);
        UtilFrame.jButton2ResetText();
        dispose();
    }
    private void spiralActionPerformed(java.awt.event.ActionEvent evt) {
        setAllFieldsFalse();
        ArrayVisualizer.setVisual(VisualStyles.CIRCULAR);
        ArrayVisualizer.toggleSpiral(true);
        if(ArrayVisualizer.getCurrentLength() == 2) ArrayVisualizer.setCurrentLength(4);
        UtilFrame.jButton2ResetText();
        dispose();
    }
    private void disparityActionPerformed(java.awt.event.ActionEvent evt) {
        setAllFieldsFalse();
        ArrayVisualizer.setVisual(VisualStyles.CIRCULAR);
        ArrayVisualizer.toggleDistance(true);
        //ArrayVisualizer.togglePointer(true);
        if(ArrayVisualizer.getCurrentLength() == 2) ArrayVisualizer.setCurrentLength(4);
        UtilFrame.jButton2ResetText();
        dispose();
    }
    private void hoopsActionPerformed(java.awt.event.ActionEvent evt) {
        setAllFieldsFalse();
        ArrayVisualizer.setVisual(VisualStyles.HOOPS);
        UtilFrame.jButton2ResetText();
        dispose();
    }
    private void disparityDotsActionPerformed(java.awt.event.ActionEvent evt) {
        setAllFieldsFalse();
        ArrayVisualizer.setVisual(VisualStyles.CIRCULAR);
        ArrayVisualizer.toggleDistance(true);
        ArrayVisualizer.togglePixels(true);
        if(ArrayVisualizer.getCurrentLength() == 2) ArrayVisualizer.setCurrentLength(4);
        UtilFrame.jButton2ResetText();
        dispose();
    }
    private void spiralDotsActionPerformed(java.awt.event.ActionEvent evt) {
        setAllFieldsFalse();
        ArrayVisualizer.setVisual(VisualStyles.CIRCULAR);
        ArrayVisualizer.togglePixels(true);
        ArrayVisualizer.toggleSpiral(true);
        if(ArrayVisualizer.getCurrentLength() == 2) ArrayVisualizer.setCurrentLength(4);
        UtilFrame.jButton2ResetText();
        dispose();
    }
    private void sineWaveActionPerformed(java.awt.event.ActionEvent evt) {
        setAllFieldsFalse();
        ArrayVisualizer.setVisual(VisualStyles.BARS);
        ArrayVisualizer.toggleWave(true);
        UtilFrame.jButton2ResetText();
        dispose();
    }
    private void waveDotsActionPerformed(java.awt.event.ActionEvent evt) {
        setAllFieldsFalse();
        ArrayVisualizer.setVisual(VisualStyles.PIXELS);
        ArrayVisualizer.togglePixels(true);
        ArrayVisualizer.toggleWave(true);
        UtilFrame.jButton2ResetText();
        dispose();
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton barGraph;
    private javax.swing.JButton dotGraph;
    private javax.swing.JButton colorCircle;
    private javax.swing.JButton triangleMesh;
    private javax.swing.JButton spiral;
    private javax.swing.JButton spiralDots;
    private javax.swing.JButton disparity;
    private javax.swing.JButton disparityDots;
    private javax.swing.JButton rainbow;
    private javax.swing.JButton hoops;
    private javax.swing.JButton sineWave;
    private javax.swing.JButton waveDots;
    private javax.swing.JLabel jLabel1;
}