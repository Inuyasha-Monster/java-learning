/**
 * @author djl
 * @create 2021/4/24 12:03
 */
public class Discount {
    public enum Code {
        NONE(0),
        SILVER(5),
        GOLD(10),
        PLATINUM(15),
        DIAMOND(20);


        private final int percentag;

        Code(int percentag) {

            this.percentag = percentag;
        }
    }

    private static double apply(double price, Code code) {
        Shop.delay();
        double result = price * (100 - code.percentag) / 100;
        return result;
    }

    public static String applyDiscount(Quote quote) {
        return quote.getShopName() + " discount price is " + Discount.apply(quote.getPrice(), quote.getCode());
    }
}
