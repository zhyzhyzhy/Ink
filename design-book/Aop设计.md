# Aop设计
相比较代理，设计的其实更像Filter一点

看一个例子
```
@Proxy
public class IndexAop {

    @Before(value = "/index/.*", args = "name")
    public boolean beforeIndex(String name) {
        System.out.printf("before...");
        System.out.println("name = " + name);
        return true;
    }

    @After("/index/.*")
    public void afterIndex() {
        System.out.printf("after ... ");
    }
}
```

@Proxy注解是类注解，表示这是一个Aop类
其中的方法是作为Filter

方法级别的注解有两个
@Before
@After

### @Before
可选参数有Response，Request
```
@Before(value = "/index/.*")
public boolean beforeIndex(Response response, Request request) {
    System.out.printf("before...");
    return true;
}
```
这样Response和Request会自动注入进去

如果你需要被代理方法的参数
```
@Controller
public class IndexController {

    @GET("/index/{name}")
    public String getIndex(@PathVariable String name) {
        return name;
    }

}
```
比如这个，你想要得到name参数
可以这么写
```
@Before(value = "/index/.*", args = "name")
public boolean beforeIndex(Response response, Request request, String name) {
    System.out.printf("before...");
    System.out.println("name = " + name);
    return true;
}
```
如果有多个参数，以逗号分割
其中返回值如果为true，表示继续执行下一个代理方法
返回false，就直接返回响应

### @After
@After方法同样的，可以得到Request和Response参数
唯一的区别就是没有返回值，就算有也会被忽略。
而且也无法得到args，因为作者觉得没有意义
如果哪天觉得有意义了，再来做