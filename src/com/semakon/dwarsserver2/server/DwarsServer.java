package com.semakon.dwarsserver2.server;

import com.semakon.dwarsserver2.view.TextServerView;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Author:  M.P. de Vries
 * Date:    19-12-2018
 */
public class DwarsServer {

    private int port;
    private ServerSocket sSocket;

    private ReadWriteLock lock = new ReentrantReadWriteLock();

    private List<ClientHandler> clientHandlers;

    private TextServerView view;

    public DwarsServer(int port) {
        this.port = port;
        clientHandlers = new ArrayList<>();

        view = new TextServerView(this);
        view.start();
    }

    public void run() {
        // Start listening on the server socket
        try {
            sSocket = new ServerSocket(port);
            view.displayMsg("Listening on port " + port);
        } catch (IOException e) {
            view.displayError("Could not listen on port " + port);
            System.exit(1);
        }

        // Attach new clients and create a new ClientHandler for every new client
        try {
            while (true) {
                Socket socket = sSocket.accept();
                ClientHandler ch = new ClientHandler(this, socket);
                ch.start();

                lock.writeLock().lock();
                try {
                    clientHandlers.add(ch);
                } finally {
                    lock.writeLock().unlock();
                }
                view.displayMsg("New client (" + socket.getInetAddress() + ") connected on port " + port);
            }
        } catch (IOException e) {
            view.displayError("Failed to attach client to socket on port " + port);
        }
    }

    /**
     * Broadcasts a message to all ClientHandlers.
     * @param msg The message broadcast.
     */
    public void broadcast(String msg) {
        lock.readLock().lock();
        try {
            for (ClientHandler ch : clientHandlers) {
                ch.send(msg);
            }
        } finally {
            lock.readLock().unlock();
        }
    }

    /**
     * Removes a ClientHandler from the list of ClientHandlers.
     * @param handler the ClientHandler to be removed.
     */
    public void clientDisconncted(ClientHandler handler) {
        lock.writeLock().lock();
        try {
            clientHandlers.remove(handler);
        } finally {
            lock.writeLock().unlock();
        }
    }

    /**
     * Disconnects all ClientHandlers and closes the server socket. Terminates the program with a status.
     * @param status the status the program is terminated with.
     */
    public void shutdown(int status) {
        lock.writeLock().lock();
        try {
            for (ClientHandler ch : clientHandlers) {
                ch.disconnect();
            }
            sSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            lock.writeLock().unlock();
        }
        System.exit(status);
    }

}
