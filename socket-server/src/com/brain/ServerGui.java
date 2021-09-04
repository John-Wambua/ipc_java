package com.brain;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.ServerSocket;

public class ServerGui implements ActionListener {

    private ServerSocket serverSocket;
    private Thread serverThread;
    private int portNumber = 0;

    ///Swing UI
    private JFrame jFrame;
    private JPanel jPanel;
    private JLabel lblPortNumber;
    private JTextField txtPortNumber;
    private JButton btnStartServer;

    public static void main(String[] args) {
        new ServerGui();
    }

    public ServerGui() {
        buildGui();
    }

    //build the server GUI
    public void buildGui() {
        jFrame = new JFrame("Socket server config");
        jPanel = new JPanel();
        jPanel.setLayout(null); //absolute positioning

        lblPortNumber = new JLabel("Please enter port number, enter 0 for random port");
        lblPortNumber.setBounds(60, 30, 400, 30);

        txtPortNumber = new JTextField();
        txtPortNumber.setBounds(60, 60, 400, 30);
        txtPortNumber.setText("4065");

        //button to spin up the server
        btnStartServer = new JButton("Start server");
        btnStartServer.setBounds(60, 100, 400, 50);
        btnStartServer.addActionListener(this);

        //add elements to the panel
        jPanel.add(lblPortNumber);
        jPanel.add(txtPortNumber);
        jPanel.add(btnStartServer);

        //add panel to the frame
        jFrame.add(jPanel);

        //make frame fixed
        jFrame.setResizable(false);
        jFrame.setSize(500, 230);
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //set to be visible
        centreWindow(jFrame);
        jFrame.setVisible(true);

    }

    //get the port number input
    @Override
    public void actionPerformed(ActionEvent e) {
        String strPortNumber = txtPortNumber.getText();
        if (strPortNumber.length() <= 0) {
            JOptionPane.showMessageDialog(null, "A port number must be provided!");
            return;
        }

        portNumber = Integer.parseInt(strPortNumber);
        startServer(portNumber);
    }


    public void centreWindow(Window frame) {
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (int) ((dimension.getWidth() - frame.getWidth()) / 2);
        int y = (int) ((dimension.getHeight() - frame.getHeight()) / 2);
        frame.setLocation(x, y);
    }

    //start the server
    private void startServer(int portNumber) {
        try {
            serverThread = new Server(portNumber);
            serverThread.start();
        } catch (Exception i) {
            System.out.println("Transaction failed: " + i);
        }
    }
}
