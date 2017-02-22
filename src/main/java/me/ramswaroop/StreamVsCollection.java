package me.ramswaroop;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import static java.util.stream.Collectors.toList;

/**
 * This is to test stream vs iterators in collections.
 * Note: The for-each loop is converted to the uglier iterator
 * later on.
 *
 * @author ramswaroop
 * @version 22/01/2017
 */
public class StreamVsCollection {

    /**
     * All threads running the benchmark share the same state object.
     */
    @State(Scope.Benchmark)
    public static class MyState {
        // stream of pseudorandom numbers from 90 (inclusive) to 5000 (exclusive)
        final List<Double> numbers = new Random().doubles(10000, 90, 11000).boxed().collect(toList());
    }


    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.MICROSECONDS)
    public double testStreamOperations(MyState state) {
        return state.numbers.stream()
                .mapToDouble(Double::doubleValue)
                .filter(f -> f > 100 && f < 500)
                .sorted()
                .limit(10)
                .sum();
    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.MICROSECONDS)
    public double testParallelStreamOperations(MyState state) {
        return state.numbers.parallelStream()
                .mapToDouble(Double::doubleValue)
                .filter(f -> f > 100 && f < 500)
                .sorted()
                .limit(10)
                .sum();
    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.MICROSECONDS)
    public double testCollectionOperations(MyState state) {
        List<Double> temp = new ArrayList<>();
        double sum = 0;

        for (Double n : state.numbers) {
            if (n > 100 && n < 500) {
                temp.add(n);
            }
        }

        temp.sort(new Comparator<Double>() {
            @Override
            public int compare(Double o1, Double o2) {
                if (o1 < o2) return -1;
                else if (o1 == o2) return 0;
                else return 1;
            }
        });

        for (int i = 0; i < 10 && i < temp.size(); i++) {
            sum += temp.get(i);
        }
        return sum;
    }

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(StreamVsCollection.class.getSimpleName())
                .warmupIterations(5)
                .measurementIterations(5)
                .forks(1)
                .build();

        new Runner(opt).run();
    }
}
