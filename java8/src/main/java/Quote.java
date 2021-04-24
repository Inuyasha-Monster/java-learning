/**
 * @author djl
 * @create 2021/4/24 12:09
 */
public class Quote {
    private final String shopName;
    private double price;
    private final Discount.Code code;

    public Quote(String shopName, double price, Discount.Code code) {
        this.shopName = shopName;
        this.price = price;
        this.code = code;
    }

    public static Quote parse(String format) {
        String[] split = format.split(":");
        String shopName = split[0];
        Double price = Double.parseDouble(split[1]);
        Discount.Code code = Discount.Code.valueOf(split[2]);
        return new Quote(shopName, price, code);
    }

    public String getShopName() {
        return shopName;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Discount.Code getCode() {
        return code;
    }

    @Override
    public String toString() {
        return "Quote{" +
                "shopName='" + shopName + '\'' +
                ", price=" + price +
                ", code=" + code +
                '}';
    }
}
