package reflect;

import com.noname.web.annotation.GET;
import com.noname.web.annotation.PathVariable;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Created by zhuyichen on 2017/7/13.
 */
public class Main {
    public String test(@PathVariable String name, @PathVariable String password) {
        return "hello,world";
    }
}
class Demo {
    public static void main(String[] args) throws NoSuchMethodException, IllegalAccessException, InstantiationException {
        String path = "/love/{id}";
        System.out.println(path);
        path = path.replace("{" + "id"+"}","[.]*");
        System.out.println(path);
        String test = "/love/cdcd";
        Pattern pattern = Pattern.compile("/love/[0-9\\d\\D]*");
        System.out.println(pattern.matcher(test).matches());
    }
}
