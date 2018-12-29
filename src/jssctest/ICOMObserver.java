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
public interface ICOMObserver {
    public void updateOnCOMEvent(ICOMObserver.COMObserverEvents event);

    public enum COMObserverEvents{ DATA_READY, ERROR_RAISED, MESSAGE_RAISED}; 
}
