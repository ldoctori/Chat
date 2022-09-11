package school21.spring.sockets.server;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import school21.spring.sockets.models.User;
import school21.spring.sockets.repositories.UsersRepository;
import school21.spring.sockets.services.UsersService;


import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class Server {

    private int port;
    private static Server instance;

    private ServerSocket server;

    private UsersService usersService;

    private static Map<Socket, User> mapUsersSockets = new HashMap<>();
    private Server() {}

    public static Server getServer() {
        if (instance == null) {
            instance = new Server();
        }
        return instance;
    }

    public void runServer(int port) throws IOException, InterruptedException {
        server = new ServerSocket(port);
        List<Thread> threads = new ArrayList<Thread>();
        System.out.println("Server is run.");
        while (true) {
            Socket clientSocket = server.accept();
            System.out.println("Accepted new client.");
            threads.add(new Thread(new ClientWorker(clientSocket, usersService)));
            threads.get(threads.size() - 1).start();
        }
    }

    @Autowired
    @Qualifier("usersServiceImpl")
    public void setUsersService(UsersService usersService) {
        this.usersService = usersService;
    }
}
