package client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;

public class WriteToServer implements Runnable{

    private BufferedWriter writeToServer;
    private BufferedReader consoleReader;

    public WriteToServer(BufferedWriter writeToServer, BufferedReader consoleReader) {
        this.writeToServer = writeToServer;
        this.consoleReader = consoleReader;
    }

    @Override
    public void run() {

        String toServer;
        boolean msg = true;
          while (msg == true) {
            try {
                toServer = consoleReader.readLine();
            if (toServer.equals("Exit") == true) {
                msg = false;
            }
                writeToServer.write(toServer + "\n");
                writeToServer.flush();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
