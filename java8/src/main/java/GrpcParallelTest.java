import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class GrpcParallelTest {

    static class Human {
    }

    static class Father extends Human {
    }

    static class Son extends Father {
    }

    static class LeiFeng extends Father {
    }

    public static void main(String[] args) {
        List<? extends Father> list = new LinkedList<>();
        list.add(null);

        Father father = list.get(0);
        System.out.println("father = " + father);

        //super只能添加Father和Father的子类，不能添加Father的父类,读取出来的东西只能存放在Object类里
        List<? super Father> list1 = new ArrayList<>();
        list1.add(new Father());
//        list1.add(new Human());//compile error
        list1.add(new Son());
//        Father person1 = list1.get(0);//compile error
//        Son son = list1.get(0);//compile error
        Object object1 = list1.get(0);

        System.out.println("object1 = " + object1);

//        extends 可用于返回类型限定，不能用于参数类型限定（换句话说：? extends xxx 只能用于方法返回类型限定，jdk能够确定此类的最小继承边界为xxx，只要是这个类的父类都能接收，但是传入参数无法确定具体类型，只能接受null的传入）。
//        super 可用于参数类型限定，不能用于返回类型限定（换句话说：? supper xxx 只能用于方法传参，因为jdk能够确定传入为xxx的子类，返回只能用Object类接收）。
//? 既不能用于方法参数传入，也不能用于方法返回。

    }
}
