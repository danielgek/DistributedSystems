package Util;

/**
 * Created by danielgek on 09/10/15.
 */
public class Debug {
    private static boolean enabled = true;

    public static void message(String message){
        if (enabled){
            System.out.println(message);
        }
    }
}
