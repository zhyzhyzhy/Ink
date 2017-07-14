**still working on**
------

### Example 



First you need to implements `NoNameConfigure` and in method `beansPackage()` return the Packages where your bean is.
Like my beans is in `Package Web`

```java  
public class Configure implements NoNameConfigure{
    public String[] beansPackage() {
        return new String[]{"web"};
    }
}
```

Then you just need to start to server

```java 
public class NoNameDemo {
    
    public void start() throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        new NoName(8091, Configure.class).start();
    }
}
```

The Controller class is like this

```java
@Controller
public class IndexController {
    @GET("/love")
    public String getIndex() {
        return "hello";
    }
    @GET("/index")
    public Map<String, String> getLove() {
        return Collections.singletonMap("message", "love");
    }
    @GET("/int")
    public int getInt() {
        return 2;
    }
    @GET("/person")
    public User getUser1() {
        return new User("maoshumin","hello","cdscds");
    }
    @GET("/person")
    public User getUser() {
        return new User("zhuyichen","hello","cdscds");
    }
    @GET("/persons")
    public List<User> getUsers() {
        return new ArrayList<User>(){{
            add(new User("zhuyichen","hello","cdscds"));
            add(new User("maoshumin","hello","cdscds"));
        }};
    }
    @GET("/love/{id}")
    public Map<String, String> testPath(@PathVariable String id) {
        return Collections.singletonMap("hello", id);
    }
    @GET("/index/{id}/{name}")
    public Map<String, String> testPath2(@PathVariable String id,
                                         @PathVariable String name) {
        return Collections.singletonMap("hello", id + "  " + name);
    }

}
```
