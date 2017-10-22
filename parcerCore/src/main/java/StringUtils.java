import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtils {
    private static final String PAGES_NUMBER_REGARDS = "(\\S)(\\s*)(\\d)";

    public static Integer getArticelPagesNumber(String string){

        int i = string.length();
        char number ;
        while (i-->0) {
            number = string.charAt(i);
            if(!Character.isDigit(number)){
                break;
            }
        }
        String textNumber = string.substring(i+1, string.length());
        try {
            return Integer.parseInt(textNumber);
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
