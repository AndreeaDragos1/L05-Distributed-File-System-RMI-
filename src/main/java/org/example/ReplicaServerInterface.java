package org.example;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ReplicaServerInterface extends Remote {
    void writeToReplication(String fileName, String content) throws RemoteException;
    String readFromReplication(String fileName) throws RemoteException;
}
