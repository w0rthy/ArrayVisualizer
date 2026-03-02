/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package prompts;

import javax.swing.JFrame;

import frames.UtilFrame;
import main.ArrayVisualizer;
import templates.Frame;
import threads.RunComparisonSort;
import threads.RunDistributionSort;
import threads.RunSelectionSorts;

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

final public class SortPrompt extends javax.swing.JFrame implements Frame {

    private static final long serialVersionUID = 1L;
    
    private int[] array;
    
    private ArrayVisualizer ArrayVisualizer;
    private JFrame Frame;
    private UtilFrame UtilFrame;
    
    @SuppressWarnings("unchecked")
    public SortPrompt(int[] array, ArrayVisualizer arrayVisualizer, JFrame frame, UtilFrame utilFrame) {
        this.array = array;
        this.ArrayVisualizer = arrayVisualizer;
        this.Frame = frame;
        this.UtilFrame = utilFrame;
        
        setAlwaysOnTop(true);
        setUndecorated(true);
        initComponents();
        jList2.setListData(ArrayVisualizer.getComparisonSorts()[1]);
        jList1.setListData(ArrayVisualizer.getDistributionSorts()[1]);
        reposition();
        setVisible(true);
    }

    @Override
    public void reposition() {
        setLocation(Frame.getX()+(Frame.getWidth()-getWidth())/2,Frame.getY()+(Frame.getHeight()-getHeight())/2);
    }


    @SuppressWarnings({ "unchecked", "rawtypes" })
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        this.jLabel1 = new javax.swing.JLabel();
        this.jLabel2 = new javax.swing.JLabel();
        this.jScrollPane1 = new javax.swing.JScrollPane();
        this.jList2 = new javax.swing.JList();
        this.jScrollPane2 = new javax.swing.JScrollPane();
        this.jList1 = new javax.swing.JList();
        this.jButton1 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setText("Comparative");

        jLabel2.setText("Distributive");

        jScrollPane1.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        jList2.setModel(new javax.swing.AbstractListModel() {
            
            private static final long serialVersionUID = 1L;

            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            @Override
            public int getSize() { return strings.length; }
            @Override
            public Object getElementAt(int i) { return strings[i]; }
        });

        jList2.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            @Override
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                jList2ValueChanged(evt);
            }
        });

        jScrollPane1.setViewportView(this.jList2);

        jScrollPane2.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        jList1.setModel(new javax.swing.AbstractListModel() {
            
            private static final long serialVersionUID = 1L;

            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            @Override
            public int getSize() { return strings.length; }
            @Override
            public Object getElementAt(int i) { return strings[i]; }
        });

        jList1.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            @Override
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                jList1ValueChanged(evt);
            }
        });

        jScrollPane2.setViewportView(this.jList1);

        //TODO: Better time estimate
        jButton1.setText("Run All (approx. " + (int) Math.max(Math.ceil(30 * (ArrayVisualizer.getCurrentLength() / 2048d)), 2) + " minutes)");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed();
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                        .addGap(40, 40, 40)
                        .addComponent(this.jLabel1)
                        .addGap(76, 76, 76)
                        .addComponent(this.jLabel2)
                        .addGap(35, 35, 35))
                .addGroup(layout.createSequentialGroup()
                        .addComponent(this.jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(this.jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(javax.swing.GroupLayout.Alignment.CENTER, layout.createSequentialGroup()
                        .addComponent(this.jButton1))
                );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(this.jLabel1)
                                .addComponent(this.jLabel2))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(this.jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 200, Short.MAX_VALUE)
                                .addComponent(this.jScrollPane1))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(this.jButton1)
                        .addGap(0, 0, 0))
                );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed() {//GEN-FIRST:event_jButton1ActionPerformed
        new Thread(){
            @Override
            public void run(){
                try {
                    /*
                    RunMergeSorts sortThread = new RunMergeSorts(ArrayVisualizer);
                    sortThread.ReportMergeSorts(array);
                    */
                    
                    RunSelectionSorts sortThread2 = new RunSelectionSorts(ArrayVisualizer);
                    sortThread2.ReportSelectionSorts(array);
                    
                    
                    /*
                    RunExchangeSorts sortThread1 = new RunExchangeSorts(ArrayVisualizer);
                    sortThread1.ReportExchangeSorts(array);
                    while(ArrayVisualizer.getSortingThread() != null) {
                        sleep(1000);
                    }
                    RunSelectionSorts sortThread2 = new RunSelectionSorts(ArrayVisualizer);
                    sortThread2.ReportSelectionSorts(array);
                    while(ArrayVisualizer.getSortingThread() != null) {
                        sleep(1000);
                    }
                    RunInsertionSorts sortThread3 = new RunInsertionSorts(ArrayVisualizer);
                    sortThread3.ReportInsertionSorts(array);
                    */
                    
                    //RunAllSorts.RunAllSorts();
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }.start();
        UtilFrame.jButton1ResetText();
        dispose();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jList1ValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_jList1ValueChanged
        // TODO add your handling code here:
        final int selection = evt.getFirstIndex();
        new Thread(){
            @Override
            public void run(){
                try {
                    RunDistributionSort sortThread = new RunDistributionSort(ArrayVisualizer);
                    sortThread.ReportDistributiveSort(array, selection);
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }.start();
        UtilFrame.jButton1ResetText();
        dispose();
    }//GEN-LAST:event_jList1ValueChanged

    private void jList2ValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_jList2ValueChanged
        // TODO add your handling code here:
        final int selection = evt.getFirstIndex();
        new Thread(){
            @Override
            public void run() {
                RunComparisonSort sortThread = new RunComparisonSort(ArrayVisualizer);
                sortThread.ReportComparativeSort(array, selection);
            }
        }.start();
        UtilFrame.jButton1ResetText();
        dispose();
    }//GEN-LAST:event_jList2ValueChanged

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    @SuppressWarnings("rawtypes")
    private javax.swing.JList jList1;
    @SuppressWarnings("rawtypes")
    private javax.swing.JList jList2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    // End of variables declaration//GEN-END:variables
}