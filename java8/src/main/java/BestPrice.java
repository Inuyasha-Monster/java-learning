import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
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
                new Shop("BuyItAll"),
                new Shop("Shop1"),
                new Shop("Shop2"),
                new Shop("Shop3"),
                new Shop("Shop4"),
                new Shop("Shop5"),
                new Shop("Shop6"),
                new Shop("Shop7"),
                new Shop("Shop8"),
                new Shop("Shop9"),
                new Shop("Shop10"),
                new Shop("Shop11"),
                new Shop("Shop12"),
                new Shop("Shop13"),
                new Shop("Shop14"));

        int availableProcessors = Runtime.getRuntime().availableProcessors();
        System.out.println("availableProcessors = " + availableProcessors);

        long start = System.nanoTime();
        List<String> prices = findPrices(shops, "myPhone27S");
        System.out.println("prices = " + prices);
        long invocationTime = (System.nanoTime() - start) / 1_000_000;
        System.out.println("Done = " + invocationTime + " ms");

        System.out.println("---- findPricesParallel ----");

        long start1 = System.nanoTime();
        List<String> pricesParallel = findPricesParallel(shops, "myPhone27S");
        System.out.println("pricesParallel = " + pricesParallel);
        long invocationTime1 = (System.nanoTime() - start1) / 1_000_000;
        System.out.println("Done = " + invocationTime1 + " ms");


        System.out.println("---- findPricesWithCompletableFuture ----");
        long start2 = System.nanoTime();
        List<String> withCompletableFuture = findPricesWithCompletableFuture(shops, "myPhone27S");
        System.out.println("withCompletableFuture = " + withCompletableFuture);
        long invocationTime2 = (System.nanoTime() - start2) / 1_000_000;
        System.out.println("Done = " + invocationTime2 + " ms");

//        availableProcessors = 12
//        prices = [BestPrice price is 223.23, LetsSaveBig price is 153.28, MyFavoriteShop price is 164.64, BuyItAll price is 189.95, Shop1 price is 121.54, Shop2 price is 173.57, Shop3 price is 183.85, Shop4 price is 140.71, Shop5 price is 204.53, Shop6 price is 196.56, Shop7 price is 171.27, Shop8 price is 127.46, Shop9 price is 190.94, Shop10 price is 219.65, Shop11 price is 139.56, Shop12 price is 144.23, Shop13 price is 135.08, Shop14 price is 229.46]
//        Done = 18217 ms
//                ---- findPricesParallel ----
//        pricesParallel = [BestPrice price is 142.16, LetsSaveBig price is 161.98, MyFavoriteShop price is 139.99, BuyItAll price is 151.81, Shop1 price is 210.12, Shop2 price is 144.54, Shop3 price is 202.25, Shop4 price is 187.62, Shop5 price is 172.50, Shop6 price is 225.88, Shop7 price is 152.49, Shop8 price is 178.07, Shop9 price is 169.09, Shop10 price is 223.30, Shop11 price is 184.93, Shop12 price is 132.25, Shop13 price is 173.34, Shop14 price is 187.90]
//        Done = 2029 ms
//                ---- findPricesWithCompletableFuture ----
//        withCompletableFuture = [BestPrice price is 169.77, LetsSaveBig price is 203.47, MyFavoriteShop price is 221.59, BuyItAll price is 182.74, Shop1 price is 187.61, Shop2 price is 196.88, Shop3 price is 132.65, Shop4 price is 192.25, Shop5 price is 165.54, Shop6 price is 225.39, Shop7 price is 178.45, Shop8 price is 228.06, Shop9 price is 217.86, Shop10 price is 159.08, Shop11 price is 139.37, Shop12 price is 191.38, Shop13 price is 226.47, Shop14 price is 194.72]
//        Done = 2019 ms

        // 通过上述得知,当前计算机核心12个,所以单线程同步执行的情况下消耗18s,并行流2s,异步2s( 2s是因为可以最多同时处理12个商店的产品价格获取,所以需要2次 )
        // 具体原因是: 它们内部采用的是同样的通用线程池，默认都使用固定数目的线程，具体线程数取决于Runtime.getRuntime().availableProcessors() = 12的返回值。


        System.out.println("--- findPricesWithCompletableFutureAndCustomExecutor ----");

        Executor executor = Executors.newFixedThreadPool(Math.min(shops.size(), 100), new ThreadFactory() {
            @Override
            public Thread newThread(Runnable r) {
                Thread thread = new Thread(r);
                thread.setDaemon(true); // 使用守护线程的方式不会阻止程序关停
                return thread;
            }
        });

        long start3 = System.nanoTime();
        List<String> myPhone27S = findPricesWithCompletableFuture(shops, "myPhone27S", executor);
        System.out.println("myPhone27S = " + myPhone27S);
        long invocation3 = ((System.nanoTime() - start3)) / 1_000_000;
        System.out.println("invocation3 = " + invocation3);
        // invocation3 = 1017 证明可以将时间缩短在1s内完成

    }

    /**
     * 单线程串行获取每一个商店该产品的价格
     *
     * @param shops
     * @param product
     * @return
     */
    public static List<String> findPrices(List<Shop> shops, String product) {
        List<String> list = shops.stream().map(x -> {
            return String.format("%s price is %.2f", x.getName(), x.getPrice(product));
        }).collect(Collectors.toList());
        return list;
    }

    /**
     * 直接使用 parallel 并行流的方式并行的执行获取商店价格这个事儿达到缩短时间
     *
     * @param shops
     * @param product
     * @return
     */
    public static List<String> findPricesParallel(List<Shop> shops, String product) {
        List<String> list = shops.parallelStream().map(x -> {
            return String.format("%s price is %.2f", x.getName(), x.getPrice(product));
        }).collect(Collectors.toList());
        return list;
    }

    public static List<String> findPricesWithCompletableFuture(List<Shop> shops, String product) {
        List<CompletableFuture<String>> futureList = shops.stream().map(shop -> {
            return CompletableFuture.supplyAsync(() -> {
                double price = shop.getPrice(product);
                return String.format("%s price is %.2f", shop.getName(), price);
            });
        }).collect(Collectors.toList());

        //
        List<String> prices = futureList.stream().map(future -> {
            String priceFormat = future.join();
            return priceFormat;
        }).collect(Collectors.toList());

        return prices;

    }

    public static List<String> findPricesWithCompletableFuture(List<Shop> shops, String product, Executor executor) {
        List<CompletableFuture<String>> futureList = shops.stream().map(shop -> {
            return CompletableFuture.supplyAsync(() -> {
                double price = shop.getPrice(product);
                return String.format("%s price is %.2f", shop.getName(), price);
            }, executor); // 使用自定义线程池尽可能扩大并发度,缩短整体时间,增加吞吐量
        }).collect(Collectors.toList());

        //
        List<String> prices = futureList.stream().map(future -> {
            String priceFormat = future.join();
            return priceFormat;
        }).collect(Collectors.toList());

        return prices;

    }
}
