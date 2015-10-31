package Util;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by danielgek on 14/10/15.
 */
public class Util {

    public static java.sql.Date convertFromJAVADateToSQLDate(java.util.Date javaDate) {
        java.sql.Date sqlDate = null;
        if (javaDate != null) {
            sqlDate = new Date(javaDate.getTime());
        }

        System.out.println(sqlDate.toString());
        return sqlDate;
    }

    public static int readInt(int max){
        int i;
        Scanner sc = new Scanner(System.in);
        try {
            i = sc.nextInt();
            if (i < 0&&i<=max) {
                System.out.println("Invalid option, insert again: ");
                readInt(max);
            }
        } catch (Exception e) {
            System.out.println("Invalid option, insert again: ");
            return readInt(max);
        }
        return i;

    }
    public static int readInt(){
        int i;
        Scanner sc = new Scanner(System.in);
        try {
            i = sc.nextInt();

        } catch (Exception e) {
            System.out.println("Invalid option, insert again: ");
            return readInt();
        }
        return i;

    }

    public static double readDouble(){
        double i;
        Scanner sc = new Scanner(System.in);
        try {
            i = sc.nextDouble();

        } catch (Exception e) {
            System.out.println("Invalid value, insert again: ");
            return readDouble();
        }
        return i;

    }

    public static String readString(){
        String out;
        try {
            Scanner sc = new Scanner(System.in);
            out = sc.nextLine();

        } catch (Exception e) {
            System.out.println("Invalid string, insert again: ");
            out=readString();
        }
        return out;
    }

    public static String readString(String s){
        String out;
        try {
            Scanner sc = new Scanner(System.in);
            out = sc.nextLine();


        } catch (Exception e) {

            out=readString();
        }
        return out;
    }
    public static String readStringWithOptions(ArrayList<String> optionStrings ){

        String temp = readString();
        boolean verification = false;
        String option = "";
        for (int i = 0; i < optionStrings.size(); i++) {
            if(temp.equals(optionStrings.get(i))){
                verification = true;
                option = optionStrings.get(i);
            }

        }

        if(verification){
            return option;
        }else{
            System.out.println("Invalid Options!!");
            return readStringWithOptions(optionStrings);
        }
    }

}
