package main;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.util.Vector;
import exception.Exit;
import relation.*;
import request.Request;

public class Main {


//        boolean time = true;
//        Scanner myObj = null;
//        while (time) {
//            myObj = new Scanner(System.in);
//            String sql = myObj.nextLine();
//            try {
//                System.out.println(" ");
//                affichage(request.request(sql));
//            } catch (Exit e) {
//                time = false;
//                System.out.println(e.getMessage());
//            } catch (InvocationTargetException e) {
//                System.out.println(e.getMessage());
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//        myObj.close();




    public static void affichage(Relation relation) {
        for (int i = 0; i < relation.getListColumn().size(); i++) 
            System.out.print(relation.getListColumn().get(i)+" ");
        System.out.println(" ");
        Vector<Vector<String>> donnee = relation.getDonnees();
        for (int i = 0; i < donnee.size(); i++) {
            for (int j = 0; j < donnee.get(i).size(); j++) {
                System.out.print(donnee.get(i).get(j)+" ");
            }
            System.out.println(" ");
        }
    }
}