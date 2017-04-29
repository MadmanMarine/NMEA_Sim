/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mad.validation.builtin;

import javax.swing.JComponent;
import javax.swing.JTextField;
import mad.validation.Error;
import mad.validation.ErrorProvider;

/**
 *
 * @author assassin
 */
public class HexNumberValidator extends ErrorProvider {

    public HexNumberValidator(JComponent c) {
        super(c);
    }

    
    @Override
    protected Error ErrorDefinition(JComponent c) {
        try {
            Integer.parseInt(((JTextField) c).getText(),16);
        } catch (NumberFormatException e) {
            return new Error(Error.ERROR, "Please enter a valid string");
        }
        return new Error(Error.NO_ERROR, null);
    }


}
