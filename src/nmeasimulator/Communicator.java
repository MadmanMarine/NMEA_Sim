/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package nmeasimulator;

import gnu.io.*;
import java.awt.Color;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.TooManyListenersException;

public class Communicator implements SerialPortEventListener
{
    //passed from main MainWindow
    MainWindow window = null;
    
    private int baud;

    //for containing the ports that will be found
    private Enumeration ports = null;
    //map the port names to CommPortIdentifiers
    private HashMap portMap = new HashMap();

    //this is the object that contains the opened port
    private CommPortIdentifier selectedPortIdentifier = null;
    private SerialPort serialPort = null;

    //input and output streams for sending and receiving data
    private InputStream input = null;
    private OutputStream output = null;

    //just a boolean flag that i use for enabling
    //and disabling buttons depending on whether the program
    //is connected to a serial port or not
    private boolean bConnected = false;

    //the timeout value for connecting with the port
    final static int TIMEOUT = 2000;

    //some ascii values for for certain things
    final static int SPACE_ASCII = 32;
    final static int DASH_ASCII = 45;
    final static int NEW_LINE_ASCII = 10;

    //a string for recording what goes on in the program
    //this string is written to the GUI
    String logText = "";
    String RxMessage = "";

    public Communicator(nmeasimulator.MainWindow window)
    {
        this.window = window;
    }

    //search for all the serial ports
    //pre: none
    //post: adds all the found ports to a combo box on the GUI
    public void searchForPorts()
    {
        ports = CommPortIdentifier.getPortIdentifiers();

        while (ports.hasMoreElements())
        {
            CommPortIdentifier curPort = (CommPortIdentifier)ports.nextElement();

            //get only serial ports
            if (curPort.getPortType() == CommPortIdentifier.PORT_SERIAL)
            {
                window.cboxPorts.addItem(curPort.getName());
                portMap.put(curPort.getName(), curPort);
            }
        }
    }

    //connect to the selected port in the combo box
    //pre: ports are already found by using the searchForPorts method
    //post: the connected comm port is stored in commPort, otherwise,
    //an exception is generated
    public void connect()
    {
        String selectedPort = (String)window.cboxPorts.getSelectedItem();
        selectedPortIdentifier = (CommPortIdentifier)portMap.get(selectedPort);
        
        CommPort commPort = null;
        baud = Integer.parseInt((String)window.cboxBaud.getSelectedItem());
        System.out.println("Baud selected = " + baud);
        
        try
        {
            //the method below returns an object of type CommPort
            commPort = selectedPortIdentifier.open("wiflyserialconfig", TIMEOUT);
            //the CommPort object can be casted to a SerialPort object
            serialPort = (SerialPort)commPort;
           
//            System.out.println("BaudRate: " + serialPort.getBaudRate());
//            System.out.println("DataBIts: " + serialPort.getDataBits());
//            System.out.println("StopBits: " + serialPort.getStopBits());
//            System.out.println("Parity: " + serialPort.getParity());
//            System.out.println("FlowControl: " + serialPort.getFlowControlMode());
            serialPort.setSerialPortParams(baud,SerialPort.DATABITS_8,SerialPort.STOPBITS_1,SerialPort.PARITY_NONE);
            System.out.println("BaudRate: " + serialPort.getBaudRate());
            System.out.println("DataBIts: " + serialPort.getDataBits());
            System.out.println("StopBits: " + serialPort.getStopBits());
            System.out.println("Parity: " + serialPort.getParity());
            System.out.println("FlowControl: " + serialPort.getFlowControlMode());

            //for controlling GUI elements
            setConnected(true);

            //logging
            logText = selectedPort + " opened successfully.";
            window.txtLog.setForeground(Color.black);
            window.txtLog.append(logText + "\n");

            //CODE ON SETTING BAUD RATE ETC OMITTED
            //XBEE PAIR ASSUMED TO HAVE SAME SETTINGS ALREADY

        }
        catch (PortInUseException e)
        {
            logText = selectedPort + " is in use. (" + e.toString() + ")";
            
            window.txtLog.setForeground(Color.RED);
            window.txtLog.append(logText + "\n");
        }
        catch (Exception e)
        {
            logText = "Failed to open " + selectedPort + "(" + e.toString() + ")";
            window.txtLog.append(logText + "\n");
            window.txtLog.setForeground(Color.RED);
        }
    }

    //open the input and output streams
    //pre: an open port
    //post: initialized intput and output streams for use to communicate data
    public boolean initIOStream()
    {
        //return value for whather opening the streams is successful or not
        boolean successful = false;

        try {
            //
            input = serialPort.getInputStream();
            output = serialPort.getOutputStream();
            writeData(0);
            
            successful = true;
            System.out.println("IO Stream initialised");
            return successful;
        }
        catch (IOException e) {
            logText = "I/O Streams failed to open. (" + e.toString() + ")";
            window.txtLog.setForeground(Color.red);
            window.txtLog.append(logText + "\n");
            return successful;
        }
    }

    //starts the event listener that knows whenever data is available to be read
    //pre: an open serial port
    //post: an event listener for the serial port that knows when data is recieved
    public void initListener()
    {
        try
        {
            serialPort.addEventListener(this);
            serialPort.notifyOnDataAvailable(true);
            System.out.println("Listener initialised");
        }
        catch (TooManyListenersException e)
        {
            logText = "Too many listeners. (" + e.toString() + ")";
            window.txtLog.setForeground(Color.red);
            window.txtLog.append(logText + "\n");
        }
    }

    //disconnect the serial port
    //pre: an open serial port
    //post: clsoed serial port
    public void disconnect()
    {
        //close the serial port
        try
        {
            writeData(0);

            serialPort.removeEventListener();
            serialPort.close();
            input.close();
            output.close();
            setConnected(false);

            logText = "Disconnected.";
            window.txtLog.setForeground(Color.red);
            window.txtLog.append(logText + "\n");
        }
        catch (Exception e)
        {
            logText = "Failed to close " + serialPort.getName() + "(" + e.toString() + ")";
            window.txtLog.setForeground(Color.red);
            window.txtLog.append(logText + "\n");
        }
    }

    final public boolean getConnected()
    {
        return bConnected;
    }

    public void setConnected(boolean bConnected)
    {
        this.bConnected = bConnected;
    }

    //what happens when data is received
    //pre: serial event is triggered
    //post: processing on the data it reads
    public void serialEvent(SerialPortEvent evt) {
        if (evt.getEventType() == SerialPortEvent.DATA_AVAILABLE)
        {
            //System.out.println("Serial event!");
            try
            {
                byte singleData = (byte)input.read();

                if (singleData != NEW_LINE_ASCII)
                {
                    logText = new String(new byte[] {singleData});
                    window.txtLog.append(logText);
                    parseResponse(logText);
                }
                else
                {
                    window.txtLog.append("\n");
                }
            }
            catch (Exception e)
            {
                logText = "Failed to read data. (" + e.toString() + ")";
                window.txtLog.setForeground(Color.red);
                window.txtLog.append(logText + "\n");
            }
        }
    }

    //method that can be called to send data
    //pre: open serial port
    //post: data sent to the other device
    public void writeData(int writeByte)
    {
        try
        {
           //System.out.println("Writing Data");
            output.write(writeByte);
            output.flush();

        }
        catch (Exception e)
        {
            logText = "Failed to write data. (" + e.toString() + ")";
            window.txtLog.setForeground(Color.red);
            window.txtLog.append(logText + "\n");
        }
    }
    
    private void parseResponse(String message){
            RxMessage = RxMessage + message;
            //System.out.println("parseData RxMessage: *" + RxMessage + "*");

            if (RxMessage.contains("CMD")) {
                window.WiFlyRxMessage = "CMD";
                System.out.println("parseData WiFlyRxMessage: *" + window.WiFlyRxMessage + "*");
                RxMessage = "";
            }
            if (RxMessage.contains("^^^") || RxMessage.contains("$$$")) {
                window.WiFlyRxMessage = "CMD";
                System.out.println("parseData WiFlyRxMessage: *" + window.WiFlyRxMessage + "*");
                RxMessage = "";
            }
            if (RxMessage.contains("EXIT")) {
                window.WiFlyRxMessage = "EXIT";
                System.out.println("parseData WiFlyRxMessage: *" + window.WiFlyRxMessage + "*");
                RxMessage = "";
            }
            if (RxMessage.contains("ERR:")) {
                window.WiFlyRxMessage = "ERR";
                System.out.println("parseData WiFlyRxMessage: *" + window.WiFlyRxMessage + "*");
                RxMessage = "";
            } 
            if (RxMessage.contains("Mac Addr=") && RxMessage.length() == 36) {
                window.WiFlyRxMessage = RxMessage.substring(RxMessage.length()-5);
                window.WiFlyRxMessage = window.WiFlyRxMessage.replace(":", "");
                window.WiFlyRxMessage = window.WiFlyRxMessage.toUpperCase();
                window.jTxtField_Heading.setText(window.WiFlyRxMessage);
                System.out.println("parseData WiFlyRxMessage: *" + window.WiFlyRxMessage);
                RxMessage = "";
            } 
            
            
    }
}
