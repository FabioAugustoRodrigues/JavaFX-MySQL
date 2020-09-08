package sourcecode.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 *
 * @author Fábio Augusto Rodrigues
 */
public class DateUtil {
    
    /**
    * Recebe um objeto do tipo LocalDate e retorna no tipo String
    * @param localDate
    */
    public static String formatDate(LocalDate localDate){  
        DateTimeFormatter formatDate = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return localDate.format(formatDate);
    }
    
    /**
    * Recebe um objeto do tipo String e retorna no tipo LocalDate
    * @param date
    */
    public static LocalDate formatDate(String date){
        try{
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            LocalDate localDate = LocalDate.parse(date, formatter);
            return localDate;
        }catch(Exception e){
            return null;
        }
    }
    
    /**
    * Faz uma comparação entre anos e retorna true se date.getYears() for maior que age
    * @param date
    * @param age
    */
    public static boolean checkAge(LocalDate date, int age){
        if ((LocalDate.now().getYear()-date.getYear()) >= age){
            return true;
        }else{
            return false;
        }
    }
}
