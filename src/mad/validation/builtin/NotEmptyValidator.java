/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mad.validation.builtin;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import mad.validation.Error;
import mad.validation.ErrorProvider;
//import mad.validation.demo.ErrorProviderTest;

/**
 *
 * @author Naveed Quadri
 */
public class NotEmptyValidator extends ErrorProvider {

    public NotEmptyValidator(JFrame parent,JComponent c) {
        super(parent,c);
    }

    @Override
    protected Error ErrorDefinition(JComponent c) {
        String str = "";
        if (c instanceof JTextField) {
            str = ((JTextField) c).getText();
        } else if (c instanceof JPasswordField) {
            str = new String(((JPasswordField) c).getPassword());
        }
        if (str.isEmpty()) {
            return new Error(Error.ERROR, "This field cannot be empty");
        } else {
            return new Error(Error.NO_ERROR, "");
        }
    }
}
