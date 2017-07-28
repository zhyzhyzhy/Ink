package aop;


import java.lang.reflect.Method;

public class Main {
//    public static void main(String[] args) {
//        Enhancer enhancer = new Enhancer();
//        enhancer.setSuperclass(TestHello.class);
//        enhancer.setCallback(new FixedValue() {
//            @Override
//            public Object loadObject() throws Exception {
//                return "hello,cglib";
//            }
//        });
//        enhancer.setCallback(new InvocationHandler() {
//            @Override
//            public Object invoke(Object o, Method method, Object[] objects) throws Throwable {
//                System.out.printf("to invoke");
//                Object result = method.invoke(o,objects);
//                System.out.printf("finish invoke");
//                return "hello,cglib";
//            }
//        });
//        enhancer.setCallback(new MethodInterceptor() {
//            @Override
//            public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
//                System.out.printf("start intter");
//                Object result = methodProxy.invokeSuper(o, objects);
//                System.out.printf("finish inter");
//                return result;
//            }
//        });
//        TestHello testHello = (TestHello) enhancer.create();
//        System.out.printf(testHello.test());
//    }
}
//class TestHello {
//    public String test() {
//        return "Hello,world";
//    }
//}