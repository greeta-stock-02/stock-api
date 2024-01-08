package net.greeta.stock.helper;

import lombok.extern.slf4j.Slf4j;

import java.time.Duration;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

@Slf4j
public class RetryHelper {
    public static final Duration DELAY = Duration.ofMillis(700);
    public static final Duration TIMEOUT = Duration.ofSeconds(20);

    public static <T> T retry(Duration delay, Duration timeout, Supplier<T> test) {
        final long startTime = System.currentTimeMillis();
        T result = null;
        while (System.currentTimeMillis() - startTime < timeout.toMillis()) {
            try {
                TimeUnit.MILLISECONDS.sleep(delay.toMillis());
                result = test.get();
                if (result instanceof Boolean) {
                    Boolean check = (Boolean) result;
                    if (check) {
                        return result;
                    }
                } else {
                    if (result != null) {
                        return result;
                    }
                }
            } catch (Throwable t) {
                //ignore
            }
        }
        return result;
    }

    public static <T> T retry(Supplier<T> test) {
        return retry(DELAY, TIMEOUT, test);
    }
}