package com.brain;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;

public class Server extends Thread {
    private ObjectInputStream inputStream;
    private ObjectOutputStream outputStream;
    private final ServerSocket serverSocket;
    public int portNumber = 0;
    String message;
    String serverResponse;

    //initialize a new socket connection
    public Server(int portNumber) throws IOException {
        serverSocket = new ServerSocket(portNumber);
//        serverSocket.setSoTimeout(60000);
        this.portNumber = serverSocket.getLocalPort();
        System.out.println("Waiting for client connection ons port " + serverSocket.getLocalPort() + " for 10 seconds...");
    }


    @Override
    public void run() {
        try {
            //connect to the client socket
            Socket clientSocket = serverSocket.accept();
            outputStream = new ObjectOutputStream(clientSocket.getOutputStream());
            inputStream = new ObjectInputStream(clientSocket.getInputStream());

            System.out.println("Just connected to " + clientSocket.getRemoteSocketAddress());

            //connect to the server protocol and process the server inputs
            ServerProtocol myProtocol = new ServerProtocol();
            serverResponse = myProtocol.processInput(null);
            while ((message = (String) inputStream.readObject()) != null) {

                serverResponse = myProtocol.processInput(message);
                String respString = message + "\n" + serverResponse;
                //give server response if the connection is still alive
                if (message.contains("connected")) {
                    respString = serverResponse;
                }
                outputStream.writeObject(respString);

                //close the client connection if the response contains the word "bye"
                if (serverResponse.contains("Bye.")) {
                    clientSocket.close();
                    break;
                }
                //output client message
                System.out.println("Message received from client :" + message);
            }
        } catch (EOFException s) {
            System.out.println("Socket closed: " + s.getMessage());
        } catch (SocketException s) {
            System.out.println("Socket error: " + s.getMessage());
        } catch (SocketTimeoutException s) {
            System.out.println("socket connection timed out: " + s.getMessage());
        } catch (IOException | ClassNotFoundException ioEx) {
            ioEx.printStackTrace();
            System.out.println("Exception caught when trying to listen on port " + portNumber + " or listening for a connection");
            System.out.println(ioEx.getMessage());
        }
    }


}
