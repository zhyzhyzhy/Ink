package ioc;

import com.noname.ioc.annotation.Bean;
import com.noname.ioc.annotation.Component;
import com.noname.ioc.annotation.Inject;

/**
 * Created by zhuyichen on 2017/7/11.
 */

@Component
public class Girl {
    @Inject
    private Boy boy;

    public Girl(){}
    @Override
    public String toString() {
        return "girl class";
    }
    public Boy getBoy() {
        return boy;
    }
}
