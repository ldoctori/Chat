package client;

import java.io.BufferedReader;
import java.io.IOException;

public class ReadFromServer implements Runnable{

    private BufferedReader readFromServer;

    public ReadFromServer(BufferedReader readFromServer) {
            this.readFromServer = readFromServer;
    }

    @Override
    public void run() {
        while (true) {
            try {
                String line = readFromServer.readLine();
                System.out.println(line);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
