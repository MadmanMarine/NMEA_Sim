/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mad.validation.builtin;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JTextField;
import mad.validation.Error;
import mad.validation.ErrorProvider;

/**
 *
 * @author User
 */
public class ValidateRegularExpression extends ErrorProvider {

    private final Pattern pattern;
    public ValidateRegularExpression(JFrame parent,JComponent c,String regex) {
        super(parent,c);
        this.pattern = Pattern.compile(regex,Pattern.CASE_INSENSITIVE);
    }

    
    @Override
    protected Error ErrorDefinition(JComponent c) {
         Matcher m = pattern.matcher(((JTextField)c).getText());
         if(m.matches())
         {
             return new Error(Error.NO_ERROR, "");
         }
         else
         {
             return new Error(Error.ERROR, "Error");
         }
    }




}
