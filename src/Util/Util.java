package Util;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

/**
 * Created by danielgek on 14/10/15.
 */
public class Util {

    public static java.sql.Date convertFromJAVADateToSQLDate(
            java.util.Date javaDate) {
        java.sql.Date sqlDate = null;
        if (javaDate != null) {
            sqlDate = new Date(javaDate.getTime());
        }
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

}
