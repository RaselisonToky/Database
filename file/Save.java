package file;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.Vector;
import relation.Relation;

public class Save {

    public static void save(Relation relation) throws IOException {
        FileOutputStream file = new FileOutputStream("./database/"+relation.getNom()+".txt");
        String save = relation.getNom()+"\n";
        for (int i = 0; i < relation.getListColumn().size(); i++) 
            save += relation.getListColumn().get(i)+" ";
        save += " \n";
        Vector<Vector<String>> donnee = relation.getDonnees();
        for (int i = 0; i < donnee.size(); i++) {
            for (int j = 0; j < donnee.get(i).size(); j++)
                save += donnee.get(i).get(j) + " ";
            save += " \n";
        }
        file.write(save.getBytes());
        file.close();
    }

    public static Relation input(String table) throws IOException {
        BufferedReader file = new BufferedReader(new FileReader("./database/" + table + ".txt"));
        String name = file.readLine();
        String[] colonnes = file.readLine().split(" ");
        String line = file.readLine();
        Relation relation = new Relation(name, colonnes);
        while (line != null) {
            String[] donnees = line.split(" ");
            relation.insert(donnees);
            line = file.readLine();
        }
        file.close();
        return relation;
    }
}
