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
public interface ICOMConnectObservable {
    public void addConnectRegistrar(ICOMConnectObserver observer); 
    
    public void notifyConnectObservers(boolean isConnect); 
    
    
}
