/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsscController;

import jsscGuiTest.JUsartReadWrite;
import jssctest.*; 

/**
 *
 * @author eth
 */
public class JsscController {
    private COMHandler comport_model; 
    private JUsartReadWrite gui_view; 
    
    public JsscController(COMHandler input_model){
        this.comport_model = input_model; 
        gui_view = new JUsartReadWrite(input_model, this); 
        gui_view.setVisible(true);
    }
    
    public void connectToCom(){
        try{
            comport_model.connect("COM4", 0, 0, 0, 0);
        }catch(Exception e){
            System.out.println("controller says: unable to connect");
        }        
    }
    
    public void disconnectFromCom(){
        try{
            comport_model.disconnect();
        }catch(Exception e){
            System.out.println("controller says: unable to disconnect"); 
        }
    }
    
    public void setCOMWriteString(String str){
        comport_model.setCOMWriteString(str);
    }
    
    
}
