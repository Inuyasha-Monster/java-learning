import org.openjdk.jol.info.ClassLayout;

/**
 * @author djl
 * @create 2021/2/25 16:16
 */
public class Test {
    public static void main(String[] args) {
        ClassLayout classLayout = ClassLayout.parseInstance(new ObjA());
        System.out.println(classLayout.toPrintable());

        System.out.println();

        ClassLayout classLayout1 = ClassLayout.parseInstance(new ObjAA());
        System.out.println(classLayout1.toPrintable());

        System.out.println();

        ClassLayout classLayout2 = ClassLayout.parseInstance(new String());
        System.out.println(classLayout2.toPrintable());
    }

    static class ObjA {
    }

    static class ObjAA {

        private int i;

        private double d;

        private Integer io;

    }
}
