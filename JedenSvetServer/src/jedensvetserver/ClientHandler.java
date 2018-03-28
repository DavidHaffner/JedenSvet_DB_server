/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jedensvetserver;

import java.net.Socket;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author dhaffner
 */
class ClientHandler extends ParseMessage implements Runnable {

    Socket sock;
    int id;

    ClientHandler(Socket iSocket, int iID) throws IOException {
        sock = iSocket;
        id = iID;
        out = sock.getOutputStream();
        in = sock.getInputStream();

    }

    public void start() {
        new Thread(this).start();
    }

    void log(String str) {
        System.out.println(str);
    }

    @Override
    public void run() {

        try {
            String choice;
            boolean quit = false;

            while (!quit) {
                // úvodní výběr z metod
                
                //TODO
                write("JedenSvet DB input schema: 1/E/text; insert, please...\n");
                
                choice = read("", "\n");

                String [] parsed = choice.split("/");
                
                //TODO
                if ("1".equals(parsed[0]) && ("E".equals(parsed[1]) || "D".equals(parsed[1])) ) {
                    // spustí query do DB
                    DBController dBController = new DBController();
                        if ("E".equals(parsed[1])) {
                            String code = "Encrypted code is: " + enigma.encrypt() +"\n";
                            write(code);
                        }    
                        if ("D".equals(parsed[1])) { 
                            String code = "Decrypted code is: " + enigma.decrypt() +"\n";
                            write(code);
                        }    
                } else {
                    write("Invalid choice ...\n");
                }
                write ("Once again? (E for end): \n");
                choice = read("", "\n");
                
                if ('E' == choice.charAt(0)) {
                    quit = true;
                }
            }
            sock.shutdownOutput();
            sock.close();
        } catch (Exception ex) {
            Logger.getLogger(ClientHandler.class.getName()).log(Level.SEVERE, "Error in ClientHandler running.", ex);
        }
    }
}
