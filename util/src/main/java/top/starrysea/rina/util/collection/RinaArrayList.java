package top.starrysea.rina.util.collection;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.ToDoubleFunction;
import java.util.function.ToIntFunction;
import java.util.stream.Collectors;

public class RinaArrayList<E> extends ArrayList<E> implements List<E> {

    public <U> List<U> collectToList(Function<? super E, ? extends U> map) {
        return collectToList(this, map);
    }

    public <U> Set<U> collectToSet(Function<? super E, ? extends U> map) {
        return collectToSet(this, map);
    }

    public int intSum(ToIntFunction<? super E> map) {
        return intSum(this, map);
    }

    public double doubleSum(ToDoubleFunction<? super E> map) {
        return doubleSum(this, map);
    }

    public <T> Map<T, List<E>> groupBy(Function<? super E, ? extends T> groupBy) {
        return groupBy(this, groupBy);
    }

    public void forEach(Consumer<? super E> consumer) {
        forEach(this, consumer);
    }

    private <T, U> List<U> collectToList(List<T> list, Function<? super T, ? extends U> map) {
        return list.stream().map(map).collect(Collectors.toList());
    }

    private <T, U> Set<U> collectToSet(List<T> list, Function<? super T, ? extends U> map) {
        return list.stream().map(map).collect(Collectors.toSet());
    }

    private int intSum(List<E> list, ToIntFunction<? super E> map) {
        return list.stream().mapToInt(map).sum();
    }

    private double doubleSum(List<E> list, ToDoubleFunction<? super E> map) {
        return list.stream().mapToDouble(map).sum();
    }

    private <T> Map<T, List<E>> groupBy(List<E> list, Function<? super E, ? extends T> groupBy) {
        return list.stream().collect(Collectors.groupingBy(groupBy));
    }

    private void forEach(List<E> list, Consumer<? super E> consumer) {
        list.stream().forEach(consumer);
    }
}
