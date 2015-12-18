import java.util.*;
import java.util.Collection;
import java.util.Comparator;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class IterativeParallelismImpl implements IterativeParallelism {
    @Override
    public <T> T minimum(int threads, List<T> list, Comparator<T> comparator) {
        return run(threads, list, listPart -> listPart.stream().min(comparator).get(),
                partResults -> partResults.stream().min(comparator).get());
    }

    @Override
    public <T> T maximum(int threads, List<T> list, Comparator<T> comparator) {
        return run(threads, list, listPart -> listPart.stream().max(comparator).get(),
                partResults -> partResults.stream().max(comparator).get());
    }

    @Override
    public <T> boolean all(int threads, List<T> list, Predicate<T> predicate) {
        return run(threads, list, listPart -> listPart.stream().allMatch(predicate),
                partResults -> partResults.stream().allMatch(val -> val));
    }

    @Override
    public <T> boolean any(int threads, List<T> list, Predicate<T> predicate) {
        return run(threads, list, listPart -> listPart.stream().anyMatch(predicate),
                partResults -> partResults.stream().anyMatch(val -> val));
    }

    @Override
    public <T> List<T> filter(int threads, List<T> list, Predicate<T> predicate) {
        return run(threads, list, listPart -> listPart.stream().filter(predicate).collect(Collectors.toList()),
                partResults -> partResults.stream().flatMap(Collection::stream).collect(Collectors.toList()));
    }

    @Override
    public <T, R> List<R> map(int threads, List<T> list, Function<T, R> function) {
        return run(threads, list, listPart -> listPart.stream().map(function).collect(Collectors.toList()),
                partResults -> partResults.stream().flatMap(Collection::stream).collect(Collectors.toList()));

    }

    public <T> List<List<T>> splitData(int threads, List<T> list) {
        List<List<T>> result = new ArrayList<>();
        int itemsPerThread = list.size() / threads;
        for (int i = 0; i < threads - 1; i++) {
            result.add(list.subList(0, itemsPerThread));
            list = list.subList(itemsPerThread, list.size());
        }
        result.add(list);
        return result;
    }

    public <T, P, R> R run(int threads, List<T> list, Function<List<T>, P> function,
                           Function<List<P>, R> combiner) {
        if (list.contains(null)) {
            throw new IllegalArgumentException("List should not contain null elements");
        }
        if (list.isEmpty()) {
            throw new IllegalArgumentException("List should not be empty");
        }
        threads = Math.max(Math.min(threads, list.size()), 1);
        List<List<T>> parts = splitData(threads, list);
        List<Thread> threadsList = new ArrayList<>();
        List<Worker<List<T>, P>> workers = new ArrayList<>();
        for (int i = 0; i < threads; i++) {
            workers.add(new Worker<>(parts.get(i), function));
            threadsList.add(new Thread(workers.get(i)));
            threadsList.get(i).start();
        }
        for (Thread t : threadsList) {
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        List<P> partResults = workers.stream().map(Worker::getResult).collect(Collectors.toList());
        return combiner.apply(partResults);
    }
}
