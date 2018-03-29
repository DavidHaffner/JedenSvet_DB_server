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
            
            // záklaní ověření přístupu - přihl. jméno má být "root" a heslo "1111"
            write("Aplikace JedenSvet_DB:  zadejte své jméno/heslo...\n");
            choice = read("", "\n");
            choice = choice.trim();
            
            if (choice.equals("root/1111")) {
                write("Ověření OK...");
            } else {
                write("Nepovolený vstup...");
                quit = true;
            }

            while (!quit) {

                // pro zjednodušení předpokládáme, že druhá strana zná schéma, které má poslat
                // 1-insert:  1/jménoFilmu/rok/režisér/popis
                // 2-select:  2/čísloSloupce/hodnota
                // 3-update:  3/idFilmu/čísloSloupce/hodnota
                // pořadí sloupců: idFilmu, jmenoFilmu, rok, reziser, popis
                write("Vložte svůj příkaz...\n");

                choice = read("", "\n");
                choice = choice.trim();

                String[] parsed = choice.split("/");

                String[] filmData = {"", "", "", "", ""};
                String response;

                DBController dBController = new DBController();
                switch (parsed[0]) {
                    case "1":
                        dBController.doInsertToFilm(parsed[1], parsed[2], parsed[3], parsed[4]);
                        response = "Vloženo.";
                        break;
                    case "2":
                        filmData[Integer.parseInt(parsed[1]) - 1] = parsed[2];
                        response = dBController.doSelectFromFilm(filmData[1], filmData[2], filmData[3], filmData[4]);
                        break;
                    case "3":
                        filmData[Integer.parseInt(parsed[2]) - 1] = parsed[3];
                        dBController.doUpdateToFilm(parsed[1], filmData[1], filmData[2], filmData[3], filmData[4]);
                        response = "Upraveno.";
                        break;
                    default:
                        response = "Chybné zadání.";
                }
                write(response + "\n");

                write("Once again? (E for end): \n");
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
