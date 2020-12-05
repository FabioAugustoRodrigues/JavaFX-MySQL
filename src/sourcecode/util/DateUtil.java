package sourcecode.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 *
 * @author Fábio Augusto Rodrigues
 */
public class DateUtil {
    
    public static String formatDate(LocalDate localDate){  
        DateTimeFormatter formatDate = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return localDate.format(formatDate);
    }
    
    public static LocalDate formatDate(String date){
        try{
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            LocalDate localDate = LocalDate.parse(date, formatter);
            return localDate;
        }catch(Exception e){
            return null;
        }
    }
    
    public static boolean checkAge(LocalDate date, int age){
        if ((LocalDate.now().getYear()-date.getYear()) >= age){
            return true;
        }else{
            return false;
        }
    }
}
