package client;

import java.io.*;
import java.net.Socket;

public class Client {

    private int port;
    private Socket clientSocket;
    private BufferedReader consoleReader;
    private BufferedReader readFromServer;
    private BufferedWriter writeToServer;

    public Client(int port) throws IOException, InterruptedException {

        this.port = port;
        clientSocket = new Socket("localhost", port);
        readFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        writeToServer = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
        consoleReader = new BufferedReader(new InputStreamReader(System.in));
        String line = readFromServer.readLine();
        System.out.println(line);
        writeToServer.write(consoleReader.readLine() + "\n");
        writeToServer.flush();
        line = readFromServer.readLine();
        while (!line.equals("ok")) {
            System.out.println(line);
            writeToServer.write(consoleReader.readLine() + "\n");
            writeToServer.flush();
            line = readFromServer.readLine();
        }
        System.out.println(readFromServer.readLine());
        writeToServer.write(consoleReader.readLine() + "\n");
        writeToServer.flush();
        System.out.println(readFromServer.readLine());
        writeToServer.write(consoleReader.readLine() + "\n");
        writeToServer.flush();
        System.out.println(readFromServer.readLine());
        messaging();
        readFromServer.close();
        writeToServer.close();
        consoleReader.close();
    }

    private void messaging() throws IOException, InterruptedException {

        String toServer;
        boolean msg = true;
        Thread readThread = new Thread(new ReadFromServer(readFromServer));
        readThread.setPriority(10);
        readThread.start();
        Thread writeThread = new Thread(new WriteToServer(writeToServer,consoleReader));
        writeThread.setPriority(1);
        writeThread.start();

        readThread.join();
        writeThread.join();
    }
}
