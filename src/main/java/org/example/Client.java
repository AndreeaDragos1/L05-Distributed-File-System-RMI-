package org.example;

import java.rmi.Naming;
import java.util.List;

public class Client {

    public static void main(String[] args) {
        try {
            MasterServerInterface master = (MasterServerInterface) Naming.lookup("//localhost/MasterServer");

            System.out.println("Fetching replica locations...");
            List<ReplicaLoc> replicas = master.getReplicaLocations("anyFile");

            if (replicas.isEmpty()) {
                System.out.println("No replicas available.");
                return;
            }

            // Use the first replica for simplicity
            ReplicaLoc replicaLoc = replicas.get(0);
            ReplicaServerInterface replica = (ReplicaServerInterface)
                    Naming.lookup("//" + replicaLoc.getHost() + "/ReplicaServer" + replicaLoc.getId());

            // Write data
            String fileName = "testFile.txt";
            String fileContent = "Hello, Chained Replication!";
            replica.writeToReplication(fileName, fileContent);
            System.out.println("File written: " + fileName);

            // Read data
            String retrievedData = replica.readFromReplication(fileName);
            System.out.println("File content: " + retrievedData);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
