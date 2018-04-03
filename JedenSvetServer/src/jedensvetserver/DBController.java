/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jedensvetserver;

import java.sql.*;

/**
 *
 * @author dhaffner
 */
public class DBController implements IDBController {

    public final String DB_PATH = "jdbc:mysql://localhost:3306/jeden_svet?user=root&password=chelsea";

    @Override
    public int doInsertToFilm(String jmenoFilmu, String rok, String reziser, String popis) {
        
        int radku = 0;

        try (Connection spojeni = DriverManager.getConnection(DB_PATH);
                PreparedStatement dotaz = spojeni.prepareStatement("INSERT INTO film (jmeno_filmu, rok, reziser, popis) VALUES (?, ?, ?, ?)");) {
            dotaz.setString(1, jmenoFilmu);
            dotaz.setString(2, rok);
            dotaz.setString(3, reziser);
            dotaz.setString(4, popis);
            radku = dotaz.executeUpdate();
            System.out.printf("Do DB uloženo %d řádků.\n", radku);
        } catch (SQLException ex) {
            System.out.println("Chyba při komunikaci s databází - insert.");
        }

        return radku;
    }

    @Override
    public String doSelectFromFilm(String jmenoFilmu, String rok, String reziser, String popis) {

        String queryText = "SELECT * FROM film WHERE 1";
        String selectResult = "";

        int count = 0;
        String[] selectParams = new String[4];

        if (!"".equals(jmenoFilmu)) {
            queryText += " AND jmeno_filmu =?";
            selectParams[count] = jmenoFilmu;
            count++;
        }
        if (!"".equals(rok)) {
            queryText += " AND rok =?";
            selectParams[count] = rok;
            count++;
        }
        if (!"".equals(reziser)) {
            queryText += " AND reziser =?";
            selectParams[count] = reziser;
            count++;
        }
        if (!"".equals(popis)) {
            queryText += " AND popis =?";
            selectParams[count] = popis;
            count++;
        }
        queryText += ";";

        try (Connection spojeni = DriverManager.getConnection(DB_PATH);
                PreparedStatement dotaz = spojeni.prepareStatement(queryText);) {

            for (int i = 0; i < count; i++) {
                dotaz.setString(i + 1, selectParams[i]);
            }
            ResultSet vysledky = dotaz.executeQuery();

            while (vysledky.next()) {
                selectResult += "jméno filmu: " + vysledky.getString("jmeno_filmu")
                        + ", rok: " + vysledky.getString("rok")
                        + ", režisér: " + vysledky.getString("reziser")
                        + ", popis: " + vysledky.getString("popis")
                        + "\n";
            }

        } catch (SQLException ex) {
            System.out.println("Chyba při komunikaci s databází - select.");
        }

        return selectResult;
    }

    @Override
    public int doUpdateToFilm(String idFilmu, String jmenoFilmu, String rok, String reziser, String popis) {

        String updateText = "UPDATE film SET";
        int radku = 0;

        int count = 0;
        String[] updateParams = new String[4];

        if (!"".equals(jmenoFilmu)) {
            updateText += " jmeno_filmu =?";
            updateParams[count] = jmenoFilmu;
            count++;
        }
        if (!"".equals(rok)) {
            if (count != 0) {
                updateText += ",";
            }
            updateText += " rok =?";
            updateParams[count] = rok;
            count++;
        }
        if (!"".equals(reziser)) {
            if (count != 0) {
                updateText += ",";
            }
            updateText += " reziser =?";
            updateParams[count] = reziser;
            count++;
        }
        if (!"".equals(popis)) {
            if (count != 0) {
                updateText += ",";
            }
            updateText += " popis =?";
            updateParams[count] = popis;
            count++;
        }
        updateText += " WHERE idfilm =?;";

        try (Connection spojeni = DriverManager.getConnection(DB_PATH);
                PreparedStatement dotaz = spojeni.prepareStatement(updateText);) {

            for (int i = 0; i < count; i++) {
                dotaz.setString(i + 1, updateParams[i]);
            }
            dotaz.setString(count + 1, idFilmu);
            radku = dotaz.executeUpdate();
            System.out.printf("V DB přepsáno řádků: %d.\n", radku);

        } catch (SQLException ex) {
            System.out.println("Chyba při komunikaci s databází - update.");
        }
        
        return radku;
    }

    @Override
    public String doSelectFromPristupy(String jmeno) {

        String queryText = "SELECT heslo FROM pristupy WHERE jmeno=?;";
        String selectResult = "";

        try (Connection spojeni = DriverManager.getConnection(DB_PATH);
                PreparedStatement dotaz = spojeni.prepareStatement(queryText);) {

            dotaz.setString(1, jmeno);
            ResultSet vysledky = dotaz.executeQuery();
            
            vysledky.next();
            selectResult = vysledky.getString("heslo");

        } catch (SQLException ex) {
            System.out.println("Chyba při komunikaci s databází - select na přístup.");
        }

        return selectResult;
    }
}
