import com.sun.xml.internal.ws.policy.privateutil.RuntimePolicyUtilsException;

import java.util.Random;
import java.util.concurrent.*;
import java.util.function.Supplier;

/**
 * @author djl
 * @create 2021/4/24 9:37
 */
public class Shop {

    private final String name;

    public Shop(String name) {
        this.name = name;
    }

    public double getPrice(String product) {
        double price = calculatePrice(product);
        return price;
    }

    public String getPriceWithDiscount(String product) {
        double price = calculatePrice(product);

        // 生成随机折扣
        Random random = new Random();
        Discount.Code code = Discount.Code.values()[random.nextInt(Discount.Code.values().length)];

        String format = String.format("%s:%.2f:%s", name, price, code);

        return format;
    }

    public String getPriceWithDiscountPlus(String product) {
        double price = calculatePricePlus(product);

        // 生成随机折扣
        Random random = new Random();
        Discount.Code code = Discount.Code.values()[random.nextInt(Discount.Code.values().length)];

        String format = String.format("%s:%.2f:%s", name, price, code);

        return format;
    }

    public Future<Double> getPriceAsyncPlus(String product) {
        // supplyAsync 方法:接受一个生产者(Supplier)作为参数,返回一个CompletableFuture对象,该对象完成异步执行后
        // 会读取调用生产者方法的返回值
        // 生产者方法会交由ForkJoinPool池中的某个执行线程(Executor)运行,但是也可以使用supplyAsync的重载方法
        // 传递第二个参数指定不同的线程来执行生产者(Supplier)方法
        CompletableFuture<Double> future = CompletableFuture.supplyAsync(new Supplier<Double>() {
            @Override
            public Double get() {
                double price = calculatePrice(product);
                return price;
            }
        });
        // 上述方式如果出现异常,CompletableFuture同样会将异常封装到结果当中,也就不用我们手动去完成这个操作了
        return future;
    }

    public Future<Double> getPriceAsync(String product) {
        // 创建 CompletableFuture 对象用它包含计算结果
        CompletableFuture<Double> futurePrice = new CompletableFuture<Double>();
        // 在另一个线程里面异步的执行计算任务
        new Thread(() -> {
            try {
                double price = calculatePrice(product);
                // 如果计算任务正常完成则设置future的完成结果
                futurePrice.complete(price);
            } catch (Exception exception) {
                System.out.println("计算过程异常...");
                // 否则则抛出导致失败的异常,将以异常完成的方式设置future
                futurePrice.completeExceptionally(exception);
            }
        }).start();
        // 无需等待计算结果返回future对象
        return futurePrice;
    }

    private double calculatePrice(String product) {
        delay();
        Random random = new Random();
//        int num = random.nextInt(10);
//        if (num >= 5) {
//            throw new RuntimeException("test");
//        }
        return random.nextDouble() * product.charAt(0) + product.charAt(1);
    }

    public static void delay() {
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            System.out.println("线程中断....");
            throw new RuntimeException(e);
        }
    }

    private double calculatePricePlus(String product) {
        randomDelay();
        Random random = new Random();
        return random.nextDouble() * product.charAt(0) + product.charAt(1);
    }

    /**
     * 随机延时 500 + 1-2000毫秒
     */
    public static void randomDelay() {
        Random random = new Random();
        int randNum = random.nextInt(2000);
        try {
            TimeUnit.MILLISECONDS.sleep(500 + randNum);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Shop shop = new Shop("BestShop");
        long start = System.nanoTime();
        Future<Double> futurePrice = shop.getPriceAsyncPlus("my favorite product");
        long invocationTime = (System.nanoTime() - start) / 1_000_000;
        System.out.println("invocationTime = " + invocationTime + " ms");

        // 执行其他工作任务
        doSomethingElse();

        try {
            // 阻塞等待结果
            Double price = futurePrice.get(2, TimeUnit.SECONDS);
            System.out.println("price = " + price);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }

        long retrievaTime = (System.nanoTime() - start) / 1_000_000;

        System.out.println("retrievaTime = " + retrievaTime + " ms");
    }

    private static void doSomethingElse() {
        System.out.println("doSomethingElse ... ");
    }

    public String getName() {
        return name;
    }
}
