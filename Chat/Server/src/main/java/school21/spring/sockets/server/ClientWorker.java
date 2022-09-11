package school21.spring.sockets.server;


import school21.spring.sockets.models.User;
import school21.spring.sockets.services.UsersService;

import java.io.*;
import java.net.Socket;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ClientWorker implements Runnable{

    private Socket clientSocket;
    private BufferedWriter out;
    private BufferedReader in;
    UsersService usersService;
    User user;

    private static Map<Socket, User> mapSocketUser = new HashMap<>();

    public ClientWorker(Socket clientSocket, UsersService usersService) throws IOException {

        this.clientSocket = clientSocket;
        this.usersService = usersService;
        this.out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
        this.in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
    }

    @Override
    public void run() {

        try {
            out.write("Hello from server!\n");
            out.flush();

            String line = in.readLine();
            while (!line.equals("signUp")) {
                out.write("I know only 'signUp' command.\n");
                out.flush();
                line = in.readLine();
            }
            out.write("ok\n");
            out.flush();
            String msg = signUp();
            if (!msg.equals("Start messaging"))
                throw new IOException();
            out.write(msg);
            out.flush();
            messaging();
            clientSocket.close();
            in.close();
            out.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void messaging() throws IOException {

        ClientWorker.mapSocketUser.put(clientSocket, user);
        String line = in.readLine();
        while (line.equals("Exit") == false) {
            System.out.println(line);
            for (Map.Entry<Socket, User> m : mapSocketUser.entrySet()) {
                BufferedWriter someOut = new BufferedWriter(new OutputStreamWriter(m.getKey().getOutputStream()));
                someOut.write(user.getName() + ": " + line + "\n");
                someOut.flush();
            }
            line = in.readLine();
        }
        out.write("You have left the chat.\n");
        out.flush();
    }
    private String signUp() throws IOException {

        out.write("Enter username:\n");
        out.flush();
        String username = in.readLine();
        out.write("Enter password:\n");
        out.flush();
        String password = in.readLine();

        this.user = new User(username, password);
        return usersService.signUp(user);
    }
}
