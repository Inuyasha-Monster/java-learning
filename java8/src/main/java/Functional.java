import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

public class Functional {
    public static <T> T reduce(Iterable<T> seq, Combiner<T> combiner) {
        Iterator<T> iterator = seq.iterator();
        if (iterator.hasNext()) {
            T result = iterator.next();
            while (iterator.hasNext()) {
                result = combiner.combine(result, iterator.next());
            }
            return result;
        }
        return null;
    }

    public static <T> Collector<T> forEach(Iterable<T> seq, Collector<T> func) {
        for (T t : seq) {
            func.function(t);
        }
        return func;
    }

    public static <R, T> List<R> transform(Iterable<T> seq, UnaryFunction<R, T> func) {
        List<R> result = new ArrayList<>();
        for (T t : seq) {
            R r = func.function(t);
            result.add(r);
        }
        return result;
    }

    public static <T> List<T> filter(Iterable<T> seq, UnaryPredicate<T> func) {
        List<T> result = new ArrayList<>();
        for (T t : seq) {
            if (func.test(t)) {
                result.add(t);
            }
        }
        return result;
    }


    static class IntegerAdder<integer> implements Combiner<Integer> {

        @Override
        public Integer combine(Integer x, Integer y) {
            return x + y;
        }
    }

    static class IntegerSubtracter implements Combiner<Integer> {

        @Override
        public Integer combine(Integer x, Integer y) {
            return x - y;
        }
    }

    static class AtomicLongAdder implements Combiner<AtomicLong> {

        @Override
        public AtomicLong combine(AtomicLong x, AtomicLong y) {
            return new AtomicLong(x.addAndGet(y.get()));
        }
    }

    static class BigDecimalUlp implements UnaryFunction<BigDecimal, BigDecimal> {

        @Override
        public BigDecimal function(BigDecimal x) {
            return x.ulp();
        }
    }

    static class MultiplyingIntegerCollector implements Collector<Integer> {

        private Integer val = 1;

        @Override
        public Integer result() {
            return val;
        }

        @Override
        public Integer function(Integer x) {
            val *= x;
            return val;
        }
    }

    public static void main(String[] args) {
        List<Integer> li = Arrays.asList(1, 2, 3, 4, 5, 6, 7);

        Integer result = forEach(li, new MultiplyingIntegerCollector()).result();

        System.out.println("result = " + result);
    }
}

