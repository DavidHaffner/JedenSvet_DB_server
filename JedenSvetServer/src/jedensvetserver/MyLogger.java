/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jedensvetserver;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 *
 * @author dhaffner
 */
public class MyLogger {

    Logger logger;
    FileHandler fh;

    
    // jediná metoda -> ukládá logy do souboru
    public void saveLog(String className, String theMessage, Exception ex) {
        
        try {
            // inicializace loggeru
            logger = Logger.getLogger("Třída " + className);
            
            // konfigurace loggeru přes handler a formatter  
            fh = new FileHandler("C:/temp/JedenSvetLog.log");
            logger.addHandler(fh);
            SimpleFormatter formatter = new SimpleFormatter();
            fh.setFormatter(formatter);
            // zakázání defaultního výpisu do konzole
            logger.setUseParentHandlers(false);

            // vlastní definice obsahu logu: 
            // k datu a času (default u SimpleFormatteru) a názvu třídy přidá vlastní text zprávy a text výjimky
            String logMessage = theMessage + "\n" + ex + "\n";
            logger.severe(logMessage);
            
            
        } catch (SecurityException | IOException e) {
            System.out.println("Selhal logging.");
            e.printStackTrace();
        }
    }
}
