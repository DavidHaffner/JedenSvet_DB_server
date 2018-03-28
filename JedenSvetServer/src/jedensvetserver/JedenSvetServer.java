/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jedensvetserver;

import java.net.ServerSocket;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author dhaffner
 */
public class JedenSvetServer {

    public JedenSvetServer(int port) throws IOException {
        int count = 0;
        ServerSocket servsock = new ServerSocket(port);
        while (true) {
            try {
                new ClientHandler(servsock.accept(), count++).start();
            } catch (IOException ex) {
                Logger.getLogger(ClientHandler.class.getName()).log(Level.SEVERE, "IO error in new client", ex);
                //System.out.println("IO error in new client");
            }
        }
    } // Server()

    public static void main(String[] args) {
        try {
            new JedenSvetServer(args.length > 0 ? Integer.parseInt(args[0]) : 8081);
        } catch (Exception ex) {
            Logger.getLogger(JedenSvetServer.class.getName()).log(Level.SEVERE, null, ex);
            //ex.printStackTrace(); 
        }
    }
}
