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
    DBController dBController = new DBController();

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

        String choice; // zde se bude ukládat příchozí komunikace od Clienta
        boolean quit = false; // pomocná prom. pro plnění podmínek v cyklech
        String response; // zde bude výsledek selectu z DB
            
        try {
            while (!quit) {
                // ověření přístupu do DB -> z tabulky 'pristupy'
                write("Aplikace JedenSvet_DB:  zadejte své jméno/heslo...\n");
                choice = read("", "\n");
                choice = choice.trim();
                
                String[] parsed = choice.split("/");
                response = dBController.doSelectFromPristupy(parsed[0]);

                if (response.equals(parsed[1])) {
                    write("Ověření OK...\n");
                    quit = true;
                } else {
                    write("Nepovolený vstup...\n");
                }
            }
        } catch (Exception ex) {
            Logger.getLogger(ClientHandler.class.getName()).log(Level.SEVERE, "Error in ClientHandler - signing in.", ex);
        }

        try {
            quit = false;
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
                int radku;

                switch (parsed[0]) {
                    case "1":
                        radku = dBController.doInsertToFilm(parsed[1], parsed[2], parsed[3], parsed[4]);
                        response = "Vloženo řádků: " + Integer.toString(radku);
                        break;
                    case "2":
                        filmData[Integer.parseInt(parsed[1]) - 1] = parsed[2];
                        response = dBController.doSelectFromFilm(filmData[1], filmData[2], filmData[3], filmData[4]);
                        break;
                    case "3":
                        filmData[Integer.parseInt(parsed[2]) - 1] = parsed[3];
                        radku = dBController.doUpdateToFilm(parsed[1], filmData[1], filmData[2], filmData[3], filmData[4]);
                        response = "Upraveno řádků: " + Integer.toString(radku);
                        break;
                    default:
                        response = "Chybné zadání - opakujte.";
                }
                write(response + "\n");

                write("Pokračovat? (K pro konec): \n");
                choice = read("", "\n");

                if ('K' == choice.charAt(0)) {
                    quit = true;
                }
            }
            sock.shutdownOutput();
            sock.close();
        } catch (Exception ex) {
            Logger.getLogger(ClientHandler.class.getName()).log(Level.SEVERE, "Error in ClientHandler - application running.", ex);
        }
    }
}
