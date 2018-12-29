/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jssctest;

import java.util.ArrayList;
import java.util.Vector;
import jssc.SerialPortEventListener;
import jssc.SerialPortList;
import jssc.SerialPortEvent; 
import jssc.SerialPortException; 
import jssc.SerialPort; 
import jssc.SerialPortTimeoutException;

/**
 *
 * @author eth
 */
public class COMHandler implements ICOMObservable, ICOMWriteObservable, ICOMConnectObservable {
    private static COMHandler singleton_instance; 
    
    private ArrayList<ICOMObserver> com_observer_list; 
    private Vector<ICOMWriteObserver> com_write_observer_list; 
    private ArrayList<ICOMConnectObserver> com_connect_observer_list; 
    
    private volatile String com_read_result;
    private volatile String message = "";
    
    private volatile byte[] com_write_message; 
     
//    private volatile static Thread readerThread; 
    private volatile static Thread writerThread; 
    
    static SerialPort serialPort;
    
    private COMHandler(){
        com_observer_list = new ArrayList(); 
        com_write_observer_list = new Vector(); 
        com_connect_observer_list = new ArrayList(); 
    }
    
    public static COMHandler getSingleInstance(){
        if(singleton_instance == null){
            singleton_instance = new COMHandler(); 
        }
        return singleton_instance; 
    }
    
     public void connect ( String portName, int baud_rate, int data_bits , int parity_bits, int stop_bits ) throws Exception
    {
            System.out.println("Com port connection called with"); 
            System.out.println("PortName: " + portName); 
            System.out.println("baudrate: " + baud_rate); 
            System.out.println("parity: " + parity_bits); 
            System.out.println("stop_bits: " + stop_bits);  

            serialPort = new SerialPort("COM3"); 
    try {
            serialPort.openPort();//Open port
            serialPort.setParams(9600, 8, 1, 0);//Set params
            int mask = SerialPort.MASK_RXCHAR ;//+ SerialPort.MASK_CTS + SerialPort.MASK_DSR;//Prepare mask
            serialPort.setEventsMask(mask);//Set mask
            serialPort.addEventListener(new SerialReader(this));//Add SerialPortEventListener
            
            startThreads(new SerialWriter(this));
    }
    catch (SerialPortException ex) {
//            System.out.println(ex);
              System.out.println("problem in connecting to the device"); 
        }
    }
     
     
    public void disconnect () throws Exception{
          stopThreads();
          if(serialPort.isOpened())
            serialPort.closePort(); 
          

          message = "Disconnected from COM port"; 
          notifyCOMObservers(ICOMObserver.COMObserverEvents.MESSAGE_RAISED);
          notifyConnectObservers(false);      
    }
    
    /** */
    public static class SerialReader implements SerialPortEventListener{
        COMHandler instance_of_comhandler; 
        
        public SerialReader(COMHandler comport_handler){
            this.instance_of_comhandler = comport_handler; 
        }
        
        public void serialEvent(SerialPortEvent event) {
        if(event.isRXCHAR()){//If data is available
//            if(event.getEventValue() == 10){//Check bytes count in the input buffer
                //Read data, if 10 bytes available 
                               
                try {
                    int bytes_in_buffer = serialPort.getInputBufferBytesCount();         
                    byte buffer[] = serialPort.readBytes(bytes_in_buffer, 100);
                    //System.out.println(buffer.toString());
                    instance_of_comhandler.com_read_result = new String(buffer, 0 , bytes_in_buffer); 
                    instance_of_comhandler.notifyCOMObservers(ICOMObserver.COMObserverEvents.DATA_READY);
                }
                catch (SerialPortException ex) {
                    System.out.println(ex);
                }
                catch (SerialPortTimeoutException ex){
                    System.out.println("time out happened"); 
                }
//            }
        }else{
            System.out.println("comport event listener says: no char was in buffer");
        }
//        else if(event.isCTS()){//If CTS line has changed state
//            if(event.getEventValue() == 1){//If line is ON
//                System.out.println("CTS - ON");
//            }
//            else {
//                System.out.println("CTS - OFF");
//            }
//        }
//        else if(event.isDSR()){///If DSR line has changed state
//            if(event.getEventValue() == 1){//If line is ON
//                System.out.println("DSR - ON");
//            }
//            else {
//                System.out.println("DSR - OFF");
//            }
//        }
    }          
 }
    

    /** */
    public static class SerialWriter implements ICOMWriteObserver, Runnable
    {
        
        COMHandler instance_of_comhandler;
        byte [] com_write_message; 
//        
        public SerialWriter ( /* OutputStream out , */ COMHandler instance_of_comhandler )
        {
//            this.out = out;
            this.instance_of_comhandler = instance_of_comhandler; 
            this.instance_of_comhandler.addComWriteObserver((ICOMWriteObserver)this);
            
        }
        
        @Override
        public void run ()
        {
            Thread thisThread = Thread.currentThread();
            try{
                while(com_write_message != null && writerThread != thisThread){
                      serialPort.writeBytes(com_write_message); 
                      com_write_message = null; 
                }
            }catch(Exception e){
                
            }
        }
        
        @Override 
        public void updateOnComWriteEvent(){
            com_write_message = instance_of_comhandler.com_write_message; 
            run(); 
            
        }
    }
    
    private void startThreads(SerialWriter serial_writer /*, SerialReader serial_reader*/){
////        readerThread = new Thread(serial_reader); 
        writerThread = new Thread(serial_writer); 
//        
////        readerThread.start();
        writerThread.start();
    }
    
    private void stopThreads(){
//        readerThread = null; 
        writerThread = null; 
    }
    
   
    
    public ArrayList<String> getAvailablePortNames(){
        ArrayList<String> portNames = new ArrayList(); 
        
        String [] string_portNames = SerialPortList.getPortNames(); 
        for(String name : string_portNames){
            portNames.add(name); 
        }        
        return portNames; 
    }
    
     public void setCOMWriteString(String outgoing_message){
        if(outgoing_message == null)
            return; 
        com_write_message = outgoing_message.getBytes(); 
        notifyComWriteObservers();
    }
    
    
    @Override
    public void addCOMObserver(ICOMObserver readObserver){
        if(readObserver == null)
            return; 
        
        com_observer_list.add(readObserver); 
    } 
    
    
    
    @Override 
    public void removeCOMObserver(ICOMObserver readObserver){
        if(readObserver == null)
            return;
        if(com_observer_list.contains(readObserver))
            com_observer_list.remove(readObserver); 
                  
    } 
    
    
    @Override
    public void notifyCOMObservers(ICOMObserver.COMObserverEvents event){
        for(int i = 0 ; i< com_observer_list.size() ; i++){
            com_observer_list.get(i).updateOnCOMEvent(event);
        }
    }
    
    @Override
    public void addCOMConnectObserver(ICOMConnectObserver com_connect_observer){
        if(com_connect_observer == null)
            return; 
        com_connect_observer_list.add(com_connect_observer); 
    }
    
    @Override
    public String getCOMData(){
        return com_read_result;
    }
    
    @Override
    public String getStringMessage(){
        return this.message; 
    }
    
    @Override 
    public void addComWriteObserver (ICOMWriteObserver observer){
        if(observer == null)
            return; 
        com_write_observer_list.add(observer); 
    }
    
    @Override
    public void removeComWriteObserver(ICOMWriteObserver observer){
        if(observer == null)
            return; 
        if(com_write_observer_list.contains(observer))
            com_write_observer_list.remove(observer); 
    }
    
    @Override
    public void notifyComWriteObservers(){ 
        for(int i = 0 ; i < com_write_observer_list.size() ; i++){    
            com_write_observer_list.get(i).updateOnComWriteEvent();
        }
    }
    
    @Override
    public void addConnectRegistrar(ICOMConnectObserver observer){
        if(observer == null)
            return; 
        com_connect_observer_list.add(observer); 
    } 
    
    @Override
    public void notifyConnectObservers(boolean isConnect){
        for(int i = 0 ; i < com_connect_observer_list.size() ; i++){
            com_connect_observer_list.get(i).updateOnConnectEvent(isConnect);
        }
    }
    
    
    
}
