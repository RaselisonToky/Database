package relation;

import java.io.IOException;
import java.util.Vector;
import file.Save;
import request.*;

public class Relation {
    
    String nom;
    Vector<String> listColumn;
    Vector<Vector<String>> donnees = new Vector<Vector<String>>();

    public Vector<Vector<String>> getDonnees() {
        return donnees;
    }
    
    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setListColumn(Vector<String> listColumn) {
        this.listColumn = listColumn;
    }

    public Relation() {

    }

    public Relation(String nom, Vector<String> listeColumn) {
        setNom(nom);
        setListColumn(listeColumn);
    }

    public Relation(String nom, String[] listeColumn) {
        setNom(nom);
        setListColumn(convert(listeColumn));
    }

    public static Vector<String> convert(String[] listeColumn) {
        Vector<String> colonnes = new Vector<String>();
        for (String column : listeColumn)
            colonnes.add(column);
        return colonnes;
    }

    public Vector<String> getColumn(Relation relation) {
        Vector<String> column = new Vector<>();
        for (int i = 0; i < this.getListColumn().size(); i++) {
            if (!relation.getListColumn().contains(this.getListColumn().get(i)))
                column.add(this.getListColumn().get(i));
        }
        return column;
    }

    public Relation division(String table) throws Exception {
        Relation relation = Save.input(table);
        String c = convertTo(getColumn(relation));
        Relation r1 = this.projection(c);
        Relation ensemble = relation.multiplication(r1);
        Relation e = ensemble.difference(this);
        Relation r2 =  e.projection(c);
        Relation r = r1.difference(r2);
        deleteDouble(r);
        return r;
    }

    public static String convertToString(Vector<String> relation) {
        String ligne = null;
        for (int j = 0; j < relation.size(); j++)
            ligne += relation.get(j);
        return ligne;
    }

    public static void deleteDouble(Relation relation) {
        Relation.sort(relation);
        if (relation.getDonnees().isEmpty())
            return;
        Vector<Vector<String>> newDonnees = new Vector<Vector<String>>();
        for (int i = 0; i + 1 < relation.getDonnees().size(); i++) {
            if (convertToString(relation.getDonnees().get(i)).compareTo(convertToString(relation.getDonnees().get(i + 1))) != 0)
                newDonnees.add(relation.getDonnees().get(i));
        }
        newDonnees.add(relation.getDonnees().lastElement());
        relation.setDonnee(newDonnees);
    }

    public static void sort(Relation relation) {
        for (int i = 0; i < relation.getDonnees().size(); ++i) {
            for (int j = i + 1; j < relation.getDonnees().size(); ++j) {
                if (convertToString(relation.getDonnees().get(i)).compareTo(convertToString(relation.getDonnees().get(j))) > 0) {
                    Vector<String> tmp = relation.getDonnees().get(i);
                    relation.getDonnees().set(i, relation.getDonnees().get(j));
                    relation.getDonnees().set(j, tmp);
                }
            }
        }
    }

    public String convertTo(Vector<String> column) {
        String colonnes = "";
        for (int i = 0; i < column.size(); i++) {
            colonnes += column.get(i);
            if (i != column.size() - 1) colonnes += ",";
        }
        return colonnes;
    }

    public void insert(Vector<String> donnee) {
        if (donnee.size() == listColumn.size())
            this.donnees.add(donnee);
    }

    public void insert(String[] donnees) {
        Vector<String> donnee = convert(donnees);
        if (donnee.size() == listColumn.size())
            this.donnees.add(donnee);
    }

    public Relation difference(String table) throws IOException {
        Relation relation = Save.input(table);
        Relation newRelation = new Relation(this.getNom(), this.getListColumn());
        for (Vector<String> ligne : this.getDonnees()) {
            if (!relation.checkLigne(ligne))
                newRelation.insert(ligne);
        }
        deleteDouble(newRelation);
        return newRelation;
    }

    public Relation difference(Relation relation) throws IOException {
        Relation newRelation = new Relation(this.getNom(), this.getListColumn());
        for (Vector<String> ligne : this.getDonnees()) {
            if (!relation.checkLigne(ligne))
                newRelation.insert(ligne);
        }
        deleteDouble(newRelation);
        return newRelation;
    }

    public boolean checkLigne(Vector<String> ligne) {
        for (int i = 0; i < this.getDonnees().size(); i++) {
            if (this.check(ligne, i))
                return true;
        }
        return false;
    }

    public boolean check(Vector<String> ligne, int index) {
        for (int i = 0; i < ligne.size(); i++) {
            if (ligne.get(i).compareTo(this.getDonnees().get(index).get(i)) != 0)
                return false;
        }
        return true;
    }

    public void insert(String request) throws Exception {
        String[] donnees = request.split(",");
        Vector<String> ligne = new Vector<String>();
        for (String string : donnees)
            ligne.add(string);
        if (ligne.size() != listColumn.size())
            throw new Exception("Argument not invalid");
        this.donnees.add(ligne);
        Save.save(this);
    }
    
    public void supprimer(String predicat) throws IOException {
        String[] requests = predicat.split("=");
        int index = listColumn.indexOf(requests[0]);
        Vector<Vector<String>> liste = new Vector<Vector<String>>();
        for (Vector<String> donnee : this.getDonnees()) {
            if (donnee.get(index).compareTo(requests[1]) != 0)
                liste.add(donnee);
        }
        this.setDonnee(liste);
        Save.save(this);
    }

    protected void setDonnee(Vector<Vector<String>> donnee) {
        this.donnees = donnee;
    }

    public Vector<String> getListColumn() {
        return listColumn;
    }

    public String[] getNameColumn() {
        String[] colonnes = new String[this.listColumn.size()];
        for (int i = 0; i < colonnes.length; i++)
            colonnes[i] = this.listColumn.get(i);
        return colonnes;
    }

    public Relation union(String table) throws Exception {
        Relation relation = Save.input(table);
        if (relation.getListColumn().size() != this.getListColumn().size())
            throw new Exception("Invalid Operation");
        for (int i = 0; i < relation.getDonnees().size(); i++)
            insert(new Vector<String>(relation.getDonnees().get(i)));
        deleteDouble(this);
        return this;
    }

    public Relation selection(String predicat) throws Exception {
        String[] requests = predicat.split("=");
        int index = listColumn.indexOf(requests[0]);
        Vector<Vector<String>> liste = new Vector<Vector<String>>();
        for (int i = 0; i < donnees.size(); i++) {
            if (donnees.get(i).get(index).compareTo(requests[1])==0)
                liste.add(donnees.get(i));
        }
        Relation relation = new Relation(this.getNom(), new Vector<String>(this.getListColumn()));
        relation.setDonnee(liste);
        deleteDouble(relation);
        return relation;
    }

    public Relation projection(String column) throws Exception {
        String[] columns = (column.compareTo("TOUS") == 0) ? getNameColumn() : column.split(",");
        Vector<Vector<String>> liste = new Vector<Vector<String>>();
        for (int i = 0; i < donnees.size(); i++) {
            for (int j = 0; j < columns.length; j++) {
                try {
                    liste.get(i).add(donnees.get(i).get(listColumn.indexOf(columns[j])));
                } catch (Exception e) {
                    liste.add(new Vector<String>());
                    liste.get(i).add(donnees.get(i).get(listColumn.indexOf(columns[j])));
                }
            }
        }
        Vector<String> listeColonne = new Vector<String>();
        for (int i = 0; i < columns.length; i++)
            listeColonne.add(columns[i]);
        Relation relation = new Relation(this.getNom(), listeColonne);
        relation.setDonnee(liste);
        deleteDouble(relation);
        return relation;
    }

    public Relation multiplication(String name) throws Exception {
        Relation relation = Request.findRelation(name);
        Vector<Vector<String>> liste = new Vector<Vector<String>>();
        for (int i = 0; i < this.getDonnees().size(); i++) {
            for (int j = 0; j < relation.getDonnees().size(); j++) {
                Vector<String> newListe = new Vector<String>();
                newListe.addAll(this.getDonnees().get(i));
                newListe.addAll(relation.getDonnees().get(j));
                liste.add(newListe);
            }
        }
        Vector<String> colonne = new Vector<String>();
        colonne.addAll(this.getListColumn());
        colonne.addAll(relation.getListColumn());
        Relation newRelation = new Relation(this.getNom(), colonne);
        newRelation.setDonnee(liste);
        deleteDouble(newRelation);
        return newRelation;
    }

    public Relation multiplication(Relation relation) throws Exception {
        Vector<Vector<String>> liste = new Vector<Vector<String>>();
        for (int i = 0; i < this.getDonnees().size(); i++) {
            for (int j = 0; j < relation.getDonnees().size(); j++) {
                Vector<String> newListe = new Vector<String>();
                newListe.addAll(this.getDonnees().get(i));
                newListe.addAll(relation.getDonnees().get(j));
                liste.add(newListe);
            }
        }
        Vector<String> colonne = new Vector<String>();
        colonne.addAll(this.getListColumn());
        colonne.addAll(relation.getListColumn());
        Relation newRelation = new Relation(this.getNom(), colonne);
        newRelation.setDonnee(liste);
        deleteDouble(newRelation);
        return newRelation;
    }

    public int[] getIndex(String colonne) {
        int[] index = new int[2];
        int p = 0;
        for (int i = 0; i < listColumn.size(); i++) {
            if (listColumn.get(i).compareTo(colonne) == 0) {
                index[p] = i;
                p++;
            }
        }
        return index;
    }

    public Relation jointure(String name) throws Exception {
        String[] predicat = name.split(",");
        Relation multiplication = this.multiplication(predicat[0]);
        int[] index = multiplication.getIndex(predicat[1]);
        Vector<Vector<String>> liste = new Vector<Vector<String>>();
        for (int i = 0; i < multiplication.getDonnees().size(); i++) {
            if (multiplication.getDonnees().get(i).get(index[0]).compareTo(multiplication.getDonnees().get(i).get(index[1])) == 0)
                liste.add(multiplication.getDonnees().get(i));
        }
        Relation newRelation = new Relation(this.getNom(), multiplication.getListColumn());
        newRelation.setDonnee(liste);
        deleteDouble(newRelation);
        return newRelation;
    }

    public String toString() {
        String colonne="";
        for (int i = 0; i < this.getListColumn().size(); i++)
            colonne+=(this.getListColumn().get(i)+" ");
        colonne+=("\n");
        Vector<Vector<String>> donnee = this.getDonnees();
        String ligne="";
        for (int i = 0; i < donnee.size(); i++) {
            for (int j = 0; j < donnee.get(i).size(); j++) {
                ligne+=(donnee.get(i).get(j)+" ");
            }
            ligne+=("\n");
        }
        colonne+=ligne;
        return colonne;
    }
}