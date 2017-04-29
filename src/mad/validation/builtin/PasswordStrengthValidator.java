/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mad.validation.builtin;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPasswordField;
import mad.validation.Error;
import mad.validation.ErrorProvider;

/**
 *http://justwild.us/examples/password/ 
 * @author assassin
 */
public class PasswordStrengthValidator extends ErrorProvider {

    public PasswordStrengthValidator(JFrame parent,JComponent c) {
        super(parent,c);
    }

    @Override
    protected Error ErrorDefinition(JComponent c) {
        String password = new String(((JPasswordField)c).getPassword());
        if (password.length() <= 4) {
            return new Error(Error.ERROR, "Error");
        } else if (password.length() < 8) {
            return new Error(Error.WARNING, "Password is very weak!");
        } else {
            return new Error(Error.NO_ERROR, "");
        }
    }
}
