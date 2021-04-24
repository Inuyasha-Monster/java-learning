import com.sun.glass.ui.Size;

import javax.xml.transform.Result;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author djl
 * @create 2021/4/24 12:24
 */
public class BestPriceWithDiscount {

    /**
     * 模拟单线程串行计算每个商店的产品价格以及折扣计算
     *
     * @param shops
     * @param product
     * @return
     */
    public static List<String> findPricesWithDiscount(List<Shop> shops, String product) {
        List<String> list = shops.stream()
                .map(shop -> shop.getPriceWithDiscount(product))
                .map(x -> Quote.parse(x))
                .map(x -> Discount.applyDiscount(x))
                .collect(Collectors.toList());
        return list;
    }

    public static void printCurrentThreadInfo(String source) {
        String format = String.format("%s | %s %s", source, Thread.currentThread().getId(), Thread.currentThread().getName());
        System.out.println(format);
    }

    /**
     * 采用自定义线程池的异步流计算
     *
     * @param shops
     * @param product
     * @param executor
     * @return
     */
    public static List<String> findPricesWithDiscountByCompletableFuture(List<Shop> shops, String product, Executor executor) {
        // 构建一个基于自定义的线程池的异步流执行
        List<CompletableFuture<String>> futures = shops.stream()
                // 以异步的方式获取商品在每个商店的原始价格
                .map(shop -> CompletableFuture.supplyAsync(() -> {
                    printCurrentThreadInfo("getPriceWithDiscount:" + shop.getName());
                    return shop.getPriceWithDiscount(product);
                }, executor))
                // 以同步的方式在前面的异步线程中执行类型转换(说白了:这一步不用异步的原因是内存CPU操作且不用切换线程上下文导致性能损失)
                .map(future -> future.thenApply(priceFormat -> {
                    printCurrentThreadInfo("Quote.parse:" + priceFormat);
                    Quote quote = Quote.parse(priceFormat);
                    return quote;
                }))
                // 使用另一个新的的异步任务来执行折扣服务,返回新的future
                .map(future -> future.thenCompose(quote -> CompletableFuture.supplyAsync(() -> {
                    printCurrentThreadInfo("applyDiscount:" + quote);
                    return Discount.applyDiscount(quote);
                }, executor)))
                .collect(Collectors.toList());
        // 阻塞获取所有的计算结果
        List<String> result = futures.stream().map(CompletableFuture::join).collect(Collectors.toList());
        return result;

        // Java 8的CompletableFuture API提供了名为thenCompose的方法，它就是专门为这一目的而设计的，
        // thenCompose方法允许你对两个异步操作进行流水线，第一个操作完成时(划重点)，将其结果作为参数传递给第二个操作(划重点)
        // thenComposeAsync 提供了异步的方式 ->
        // 通常而言，名称中不带Async的方法和它的前一个任务一样，在同一个线程中运行；
        // 而名称以Async结尾的方法会将后续的任务提交到一个线程池，所以每个任务是由不同的线程处理的
        // 我们选择thenCompose方法的原因是因为它更高效一些，因为少了很多线程切换的开销

        //        getPriceWithDiscount:Shop1 | 12 Thread-0
        //        getPriceWithDiscount:Shop2 | 13 Thread-1
        //        getPriceWithDiscount:Shop3 | 14 Thread-2
        //        getPriceWithDiscount:Shop4 | 15 Thread-3
        //        Quote.parse:Shop4:159.75:PLATINUM | 15 Thread-3
        //        Quote.parse:Shop3:158.62:NONE | 14 Thread-2
        //        Quote.parse:Shop2:158.97:GOLD | 13 Thread-1
        //        Quote.parse:Shop1:161.79:SILVER | 12 Thread-0
        //        applyDiscount:Quote{shopName='Shop3', price=158.62, code=NONE} | 18 Thread-7
        //        applyDiscount:Quote{shopName='Shop2', price=158.97, code=GOLD} | 16 Thread-5
        //        applyDiscount:Quote{shopName='Shop1', price=161.79, code=SILVER} | 17 Thread-4
        //        applyDiscount:Quote{shopName='Shop4', price=159.75, code=PLATINUM} | 19 Thread-6
        // 根据上述测试可以发现: 第一步与第二步的操作是同一个线程,第三步是另外的线程(如果线程池数量不够的情况会复用此前的线程)
    }

    /**
     * @param shops
     * @param product
     * @param executor
     * @return
     */
    public static List<String> findPricesWithDiscountAndRMBRateByCompletableFuture(List<Shop> shops, String product, Executor executor) {
        List<CompletableFuture<String>> futures = shops.stream()
                // 自定义线程池异步获取所有商店的产品原始价格
                .map(shop -> CompletableFuture.supplyAsync(() -> {
                    printCurrentThreadInfo("getPriceWithDiscount:" + shop.getName());
                    return shop.getPriceWithDiscount(product);
                }, executor))
                // 同步转换为折扣类型
                .map(future -> future.thenApply(Quote::parse))
                // 自定义线程池异步获取人民币汇率执行结果合并
                .map(future -> future.thenCombine(CompletableFuture.supplyAsync(() -> ExchangeService.getRMBRate(), executor), (quote, rate) -> {
                    printCurrentThreadInfo("future.thenCombine:" + quote.getShopName());
                    double rmbPrice = quote.getPrice() * rate;
                    quote.setPrice(rmbPrice);
                    return quote;
                }))
                // 自定义线程池执行折扣服务,前提是等待前一步的异步结果
                .map(future -> future.thenCompose(quote -> CompletableFuture.supplyAsync(() -> Discount.applyDiscount(quote), executor)))
                .collect(Collectors.toList());

        List<String> list = futures.stream().map(x -> x.join()).collect(Collectors.toList());
        return list;

        // invocation = 2027 这里总体也是消耗2s,是前提是线程池线程数量比较足够的情况下且 Combine 的异步任务可以在独立线程中执行
        // 具体来讲就是: 获取商店商品价格的异步任务与获取RMB汇率的异步任务可以并发/并行的同时运行,在双方都执行完成拿到结果的时候进行数据'合并'操作
        // 补充测试:如果线程池大小=shops.size(),则汇率异步任务执行线程不够需要额外的时间才可以,此时就需要扩大线程池

        //        getPriceWithDiscount:Shop1 | 20 Thread-8
        //        getPriceWithDiscount:Shop2 | 22 Thread-10
        //        getPriceWithDiscount:Shop4 | 26 Thread-14
        //        getPriceWithDiscount:Shop3 | 24 Thread-12
        //        future.thenCombine:Shop1 | 20 Thread-8
        //        future.thenCombine:Shop3 | 24 Thread-12
        //        future.thenCombine:Shop2 | 22 Thread-10
        //        future.thenCombine:Shop4 | 26 Thread-14
        // 测试结果证明: 在 combine 的情况下非Async的情况, 合并处理逻辑的执行线程与调用者线程相同
    }

    public static Stream<CompletableFuture<String>> findPricesStream(List<Shop> shops, String product, Executor executor) {
        Stream<CompletableFuture<String>> futureStream = shops.stream()
                .map(shop -> CompletableFuture.supplyAsync(() -> shop.getPriceWithDiscountPlus(product), executor))
                .map(future -> future.thenApply(Quote::parse))
                .map(future -> future.thenCompose(quote -> CompletableFuture.supplyAsync(() -> Discount.applyDiscount(quote), executor)));
        return futureStream;
    }

    public static void main(String[] args) {
        List<Shop> shops = new ArrayList<>();
        shops.add(new Shop("Shop1"));
        shops.add(new Shop("Shop2"));
        shops.add(new Shop("Shop3"));
        shops.add(new Shop("Shop4"));

        System.out.println("--- findPricesWithDiscount ---");
        long start = System.nanoTime();
        List<String> iphone8 = findPricesWithDiscount(shops, "Iphone8");
        System.out.println("iphone8 = " + iphone8);
        long invocation = ((System.nanoTime() - start)) / 1_000_000;
        System.out.println("invocation = " + invocation);
        // invocation = 8120 耗时:8s( 因为串行且计算价格与优惠折扣计算都会耗时1s,所以4个商店一共是 4*2=8s )

        System.out.println("--- findPricesWithDiscountByCompletableFuture ---");

        Executor executor = Executors.newFixedThreadPool(Math.min(shops.size(), 100), new ThreadFactory() {
            @Override
            public Thread newThread(Runnable r) {
                Thread thread = new Thread(r);
                thread.setDaemon(true); // 使用守护线程的方式不会阻止程序关停
                return thread;
            }
        });

        long start1 = System.nanoTime();
        List<String> iphone81 = findPricesWithDiscountByCompletableFuture(shops, "Iphone8", executor);
        System.out.println("iphone8 = " + iphone81);
        long invocation1 = ((System.nanoTime() - start1)) / 1_000_000;
        System.out.println("invocation = " + invocation1);

        System.out.println("--- findPricesWithDiscountAndRMBRateByCompletableFuture ---");

        long start2 = System.nanoTime();
        List<String> iphone82 = findPricesWithDiscountAndRMBRateByCompletableFuture(shops, "Iphone8", executor);
        System.out.println("iphone8 = " + iphone82);
        long invocation2 = ((System.nanoTime() - start2)) / 1_000_000;
        System.out.println("invocation = " + invocation2);

        System.out.println("--- findPricesStream ---");

        long start3 = System.nanoTime();
        Stream<CompletableFuture<String>> stream = findPricesStream(shops, "Iphone8", executor);
        CompletableFuture[] completableFutures = stream
                .map(x -> x.thenAccept(result -> {
                    System.out.println(result + " Done cost " + (System.nanoTime() - start3) / 1_000_000 + " ms");
                }))
                .toArray(size -> new CompletableFuture[size]);
        CompletableFuture<Void> allOfFuture = CompletableFuture.allOf(completableFutures);
        allOfFuture.join();
        System.out.println("All shops Done: " + (System.nanoTime() - start3) / 1_000_000 + " ms");

        //        Shop3 discount price is 164.635 Done cost 1888 ms
        //        Shop4 discount price is 118.21799999999999 Done cost 2109 ms
        //        Shop2 discount price is 156.654 Done cost 2690 ms
        //        Shop1 discount price is 159.61900000000003 Done cost 2912 ms
        //        All shops Done: 2912 ms

        // 未完待续:1.future本身执行超时时间设置(可以是内部方法也可以是completableFuture本身支持) 2.异步任务异常处理
    }

}
