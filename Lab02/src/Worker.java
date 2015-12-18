import java.util.function.Function;

public class Worker<T, R> implements Runnable {
    private T input;
    private Function<T, R> function;
    private R result;

    public Worker(T input, Function<T, R> function) {
        this.input = input;
        this.function = function;
    }

    @Override
    public void run() {
        result = function.apply(input);
    }

    public R getResult() {
        return result;
    }
}
