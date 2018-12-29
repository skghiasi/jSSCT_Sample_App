/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jssctest;

import jssc.SerialPortList;
import jssc.SerialPort;
import jssc.SerialPortException; 
import jssc.SerialPortTimeoutException;
import jsscController.*; 

/**
 *
 * @author eth
 */
public class JSSCTest {
    static {
    try {
//        System.loadLibrary("jSSC-2.8_x86.dll");
//        System.loadLibrary(System.getProperty("user.dir") + "\\jSSC-2.8_x86_64.dll");
          System.out.println("vversion: " + System.getProperty("java.version")); 
    } catch (UnsatisfiedLinkError exc) {
        System.out.println("kay error loading files");
//        System.out.println();
//        initLibStructure();
    }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
          COMHandler comhandler = COMHandler.getSingleInstance(); 
          JsscController controller = new JsscController(comhandler); 
//        String [] portNames = SerialPortList.getPortNames(); 
//        for(String name : portNames){
//            System.out.println(name);
//        }
//        SerialPort serialPort = new SerialPort("COM4");
//        try {
//        serialPort.openPort();//Open serial port
//        serialPort.setParams(SerialPort.BAUDRATE_9600, 
//                             SerialPort.DATABITS_8,
//                             SerialPort.STOPBITS_1,
//                             SerialPort.PARITY_NONE);//Set params. Also you can set params by this string: serialPort.setParams(9600, 8, 1, 0);
//        byte[] buffer = null;   
//        serialPort.writeBytes("This is a test string".getBytes());//Write data to port
//        try{
//        buffer = serialPort.readBytes(1,1000);
//        }catch (SerialPortTimeoutException e){
//            System.out.println("timeout in function");
//        }
//        System.out.println(buffer);
//        serialPort.closePort();//Close serial port
//    }
//    catch (SerialPortException ex) {
//        System.out.println(ex);
//    }
    }
    
    
    
}
