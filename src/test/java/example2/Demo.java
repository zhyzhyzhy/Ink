package example2;

import com.noname.NoName;
import com.noname.NoNameConfigure;

/**
 * Created by zhuyichen on 2017/7/12.
 */

public class Demo extends NoNameConfigure {

    //return the package where you bean is
    @Override
    public String[] beansPackage() {
        return new String[]{"example2.service", "example2.web"};
    }

    //if enable jwt authentication
    //you need to override these to method
    @Override
    public boolean anthenticationOpen() {
        return true;
    }

    //return a key for jwt encode
    @Override
    public String SecurityKey() {
        return "loveee";
    }


    public static void main(String[] args) {
        new NoName(8091, Demo.class).start();
    }

}
