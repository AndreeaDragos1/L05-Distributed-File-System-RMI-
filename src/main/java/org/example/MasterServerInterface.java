package org.example;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface MasterServerInterface extends Remote {
    void registerReplicaServer(String name, ReplicaLoc location) throws RemoteException;
    List<ReplicaLoc> getReplicaLocations(String fileName) throws RemoteException;
}
