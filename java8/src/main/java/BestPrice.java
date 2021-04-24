import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author djl
 * @create 2021/4/24 11:10
 */
public class BestPrice {
    public static void main(String[] args) {
        List<Shop> shops = Arrays.asList(new Shop("BestPrice"),
                new Shop("LetsSaveBig"),
                new Shop("MyFavoriteShop"),
                new Shop("BuyItAll"));

        long start = System.nanoTime();

        List<String> prices = findPrices(shops, "myPhone27S");
        System.out.println("prices = " + prices);
        long invocationTime = (System.nanoTime() - start) / 1_000_000;
        System.out.println("Done = " + invocationTime + " ms");
    }

    public static List<String> findPrices(List<Shop> shops, String product) {
        List<String> list = shops.stream().map(x -> {
            return String.format("%s price is %.2f", x.getName(), x.getPrice(product));
        }).collect(Collectors.toList());
        return list;
    }
}
