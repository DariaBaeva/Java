import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        IterativeParallelism parallelism = new IterativeParallelismImpl();
        List<Integer> input = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            input.add(i + 1);
        }
        System.out.println("input: " + input);

        int threadsNumber = 3;
        System.out.println("min: " + parallelism.minimum(threadsNumber, input, Comparator.naturalOrder()));
        System.out.println("max: " + parallelism.maximum(threadsNumber, input, Comparator.naturalOrder()));
        System.out.println("all > 10: " + parallelism.all(threadsNumber, input, value -> value > 10));
        System.out.println("any > 10: " + parallelism.any(threadsNumber, input, value -> value > 10));
        System.out.println("filter > 10: " + parallelism.filter(threadsNumber, input, value -> value > 10));
        System.out.println("map x -> x + 1: " + parallelism.map(threadsNumber, input, value -> value + 1));
    }
}
