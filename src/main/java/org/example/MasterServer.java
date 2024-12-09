package org.example;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MasterServer extends UnicastRemoteObject implements MasterServerInterface {

    private final Map<String, ReplicaLoc> replicaLocations = new HashMap<>();

    public MasterServer() throws RemoteException {
        super();
    }

    @Override
    public synchronized void registerReplicaServer(String name, ReplicaLoc location) throws RemoteException {
        replicaLocations.put(name, location);
        System.out.println("Registered replica: " + name + " at " + location.getHost());
    }

    @Override
    public synchronized List<ReplicaLoc> getReplicaLocations(String fileName) throws RemoteException {
        return new ArrayList<>(replicaLocations.values());
    }

    public static void main(String[] args) {
        try {
            LocateRegistry.createRegistry(1099);
            MasterServer master = new MasterServer();
            Naming.rebind("MasterServer", master);
            System.out.println("Master Server is running...");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
