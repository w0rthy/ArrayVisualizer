package templates;

import java.awt.HeadlessException;

import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.UIManager;

//Many thanks to Freek de Bruijn on StackOverflow for providing a custom JOptionPane.
//https://stackoverflow.com/questions/14407804/how-to-change-the-default-text-of-buttons-in-joptionpane-showinputdialog?noredirect=1&lq=1
final public class JEnhancedOptionPane extends JOptionPane {
 /**
  * 
  */
 private static final long serialVersionUID = 1L;

 public static String showInputDialog(final Object message, final Object[] options) throws HeadlessException {
     final JOptionPane pane = new JOptionPane(message, QUESTION_MESSAGE,
                                              OK_CANCEL_OPTION, null,
                                              options, null);
     pane.setWantsInput(true);
     pane.setComponentOrientation((getRootFrame()).getComponentOrientation());
     pane.setMessageType(QUESTION_MESSAGE);
     pane.selectInitialValue();
     final String title = UIManager.getString("OptionPane.inputDialogTitle", null);
     final JDialog dialog = pane.createDialog(null, title);
     dialog.setVisible(true);
     dialog.dispose();
     final Object value = pane.getInputValue();
     return (value == UNINITIALIZED_VALUE) ? null : (String) value;
 }
}