package top.starrysea.rina.util.thread;

import java.util.concurrent.*;
import java.util.function.Function;
import java.util.function.Supplier;

public class ThreadUtil {

    private static final ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(
            Runtime.getRuntime().availableProcessors(),
            Runtime.getRuntime().availableProcessors() * 2,
            10,
            TimeUnit.SECONDS,
            new LinkedBlockingQueue<>()
    );

    private static final ScheduledExecutorService scheduledThreadPoolExecutor = Executors.newScheduledThreadPool(Runtime.getRuntime().availableProcessors());

    public static void exec(Supplier<?> supplier) {
        threadPoolExecutor.execute(() -> supplier.get());
    }

    public static <T> Future<T> call(Supplier<T> supplier) {
        return threadPoolExecutor.submit(() -> supplier.get());
    }

    public static <T, R> Future<R> call(Function<T, R> function, T t) {
        return threadPoolExecutor.submit(() -> function.apply(t));
    }

    public static void registerScheduleTask(Supplier<?> supplier, long delay, TimeUnit timeUnit) {
        scheduledThreadPoolExecutor.scheduleWithFixedDelay(() -> supplier.get(), 0, delay, timeUnit);
    }
}
