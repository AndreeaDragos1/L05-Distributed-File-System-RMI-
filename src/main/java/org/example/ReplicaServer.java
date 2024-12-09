package org.example;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Map;

public class ReplicaServer extends UnicastRemoteObject implements ReplicaServerInterface {

    private final String name;
    private final Map<String, String> fileStorage = new HashMap<>();
    private ReplicaServerInterface nextServer;

    public ReplicaServer(String name, String nextServerUrl) throws RemoteException {
        super();
        this.name = name;

        if (nextServerUrl != null) {
            try {
                nextServer = (ReplicaServerInterface) Naming.lookup(nextServerUrl);
            } catch (Exception e) {
                System.err.println("Next server not available.");
            }
        }
    }

    @Override
    public void writeToReplication(String fileName, String content) throws RemoteException {
        fileStorage.put(fileName, content);
        System.out.println("[" + name + "] File written: " + fileName);

        if (nextServer != null) {
            nextServer.writeToReplication(fileName, content);
        }
    }

    @Override
    public String readFromReplication(String fileName) throws RemoteException {
        if (nextServer != null) {
            return nextServer.readFromReplication(fileName);
        }
        return fileStorage.getOrDefault(fileName, "File not found");
    }

    public static void main(String[] args) {
        if (args.length < 1) {
            System.err.println("Usage: java ReplicaServer <replica_name> [<next_server_url>]");
            return;
        }

        String replicaName = args[0];
        String nextServerUrl = args.length > 1 ? args[1] : null;

        try {
            ReplicaServer server = new ReplicaServer(replicaName, nextServerUrl);
            Naming.rebind("ReplicaServer" + replicaName, server);

            MasterServerInterface master = (MasterServerInterface) Naming.lookup("//localhost/MasterServer");
            ReplicaLoc location = new ReplicaLoc(replicaName, "localhost", true);
            master.registerReplicaServer(replicaName, location);

            System.out.println("Replica Server " + replicaName + " is running...");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
