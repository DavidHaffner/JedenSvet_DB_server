/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jedensvetserver;

import java.net.ServerSocket;
import java.io.IOException;

/**
 *
 * @author dhaffner
 */
public class JedenSvetServer {

    static MyLogger myLogger = new MyLogger();
    
    
    public JedenSvetServer(int port) throws IOException {
        int count = 0;
        ServerSocket servsock = new ServerSocket(port);
        while (true) {
            try {
                new ClientHandler(servsock.accept(), count++).start();
            } catch (IOException ex) {
                myLogger.saveLog(ClientHandler.class.getName(), "IO chyba při vytváření nového spojení.", ex);
                //Logger.getLogger(ClientHandler.class.getName()).log(Level.SEVERE, "IO error in new client", ex);
                
            }
        }
    } 

    public static void main(String[] args) {
        try {
            new JedenSvetServer(args.length > 0 ? Integer.parseInt(args[0]) : 8082);
        } catch (Exception ex) {
            myLogger.saveLog(JedenSvetServer.class.getName(), "Chyba při spuštění main", ex);
            //Logger.getLogger(JedenSvetServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
