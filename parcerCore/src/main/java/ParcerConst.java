import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class ParcerConst {
    public static Set<String> STOP_WORDS = new HashSet<>();

    static {
        // TODO сделать чтение из файла настроек
        STOP_WORDS.addAll(Arrays.asList("Указатель авторов","Знания-Онтологии-Теории (ЗОНТ-17)"));
    }

}
