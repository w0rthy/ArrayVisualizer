package templates;

import java.io.PrintWriter;
import java.io.StringWriter;

import javax.swing.JOptionPane;
import javax.swing.JTextArea;

final public class JErrorPane extends JOptionPane {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    
    public volatile static boolean errorMessageActive = false;
    
    public static void invokeErrorMessage(Exception e) {
        errorMessageActive = true;
        
        StringWriter exceptionString = new StringWriter();
        e.printStackTrace(new PrintWriter(exceptionString));
        String printException = exceptionString.toString();
        
        JTextArea error = new JTextArea();
        error.setText(printException);
        error.setCaretPosition(0);
        error.setEditable(false);
        
        JOptionPane.showMessageDialog(null, error, "Error", JOptionPane.ERROR_MESSAGE);
        errorMessageActive = false;
    }
}