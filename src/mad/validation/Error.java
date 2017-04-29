package mad.validation;

import java.awt.Color;
import javax.swing.ImageIcon;
import mad.utilities.ResourceManager;

/**
 *
 * @author Naveed Quadri
 */
public class Error {

    /**
     * No Error
     */
    public static final int NO_ERROR = 0;
    /**
     * Just an information
     */
    public static final int INFO = 1;
    /**
     * A warning
     */
    public static final int WARNING = 2;
    /**
     * A fatal error
     */
    public static final int ERROR = 3;
    
    private final ResourceManager resourceManager;
    private final int errorType;
    private final String message;

    /**
     * 
     * @param errorType Type of the error
     * @param message to be displayed in the tooltip
     */
    public Error(int errorType, String message) {
        this.errorType = errorType;
        this.message = message;
        resourceManager = new ResourceManager(Error.class.getResource("Settings.properties").getPath());
    }

    /**
     *
     * @return errorType
     */
    protected int getErrorType() {
        return errorType;
    }

    /**
     * 
     * @return message
     */
    protected String getMessage() {
        return message;
    }

    /**
     * Get a suitable color depending on the error type
     * @return A color
     */
    public Color getColor() {
        switch (errorType) {
            case ERROR:
                return new Color(resourceManager.readInteger("ErrorProvider.ErrorColor",16));
            case INFO:
                return new Color(resourceManager.readInteger("ErrorProvider.InfoColor",16));
            case NO_ERROR:
                return Color.WHITE; //any random color,as we'll be using the original color from the component
            case WARNING:
                return new Color(resourceManager.readInteger("ErrorProvider.WarningColor",16));
            default:
                throw new IllegalArgumentException("Not a valid error type");
        }
    }

    /**
     * Get a suitable icon depending on the icon
     * @return ImageIcon
     */
    public ImageIcon getImage() {
        switch (errorType) {
            case ERROR:
                return resourceManager.readImage("ErrorProvider.ErrorIcon");
            case INFO:
                return resourceManager.readImage("ErrorProvider.InfoIcon");
            case NO_ERROR:
                return null;
            case WARNING:
                return resourceManager.readImage("ErrorProvider.WarningIcon");
            default:
                throw new IllegalArgumentException("Not a valid error type");
        }
    }
}
