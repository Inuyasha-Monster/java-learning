import java.util.concurrent.*;

/**
 * @author djl
 * @create 2021/4/24 9:00
 */
public class Code11_1 {

    private static ExecutorService executorService = Executors.newCachedThreadPool();

    /**
     * 模拟耗时任务
     * @return
     */
    private static double doSomeLangComputation() {
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return 0.1;
    }

    public static void main(String[] args) {
        // 向线程池提交一个Callable任务
        Future<Double> future = executorService.submit(new Callable<Double>() {
            public Double call() throws Exception {
                return doSomeLangComputation();
            }
        });

        // 异步操作进行时候进行一些其他操作
        doSomethingElse();

        try {
            // 在指定时间内获取异步任务的结果,如果无法获得将会触发超时异常
            Double result = future.get(2, TimeUnit.SECONDS);
            System.out.println("result = " + result);
        } catch (InterruptedException e) {
            // 当前线程在等待过程中被中断
            e.printStackTrace();
        } catch (ExecutionException e) {
            // 当前线程执行计算异常
            e.printStackTrace();
        } catch (TimeoutException e) {
            // 超时异常
            e.printStackTrace();
        }

        executorService.shutdown();
    }

    private static void doSomethingElse() {
        System.out.println("doSomethingElse...");
    }

}
