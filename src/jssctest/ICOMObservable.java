/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jssctest;

/**
 *
 * @author eth
 */
public interface ICOMObservable {
    public void addCOMObserver(ICOMObserver readObserver);  
    public void removeCOMObserver(ICOMObserver readObserver); 
    public void notifyCOMObservers(ICOMObserver.COMObserverEvents event);
    
    public void addCOMConnectObserver(ICOMConnectObserver com_connect_observer); 
    
    public String getCOMData();
    public String getStringMessage(); 
    
    
}
