package me.ramswaroop;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author ramswaroop
 * @version 22/01/2017
 */
public class StreamVsCollection {

    /**
     * Each thread running the benchmark will create its own instance of the state object.
     */
    @State(Scope.Thread)
    public static class MyState {
        // stream of pseudorandom numbers from 90 (inclusive) to 5000 (exclusive)
        public List<Double> numbers = new Random().doubles(10000, 90, 11000).boxed().collect(Collectors.toList());
    }


    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.MICROSECONDS)
    public List<Double> testStreamOperations(MyState state) {
        return state.numbers.stream()
                .filter(f -> f > 100)
                .filter(f -> f % 2 == 0)
                .sorted()
                .limit(10)
                .collect(Collectors.toList());
    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.MICROSECONDS)
    public List<Double> testParallelStreamOperations(MyState state) {
        return state.numbers.parallelStream()
                .filter(f -> f > 100)
                .filter(f -> f % 2 == 0)
                .sorted()
                .limit(10)
                .collect(Collectors.toList());
    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.MICROSECONDS)
    public List<Double> testCollectionOperations(MyState state) {
        List<Double> gt100 = new ArrayList<>();
        List<Double> evenNumbers = new ArrayList<>();
        List<Double> result = new ArrayList<>();

        for (Double n : state.numbers) {
            if (n > 100) {
                gt100.add(n);
            }
        }

        for (Double n : gt100) {
            if (n % 2 == 0) {
                evenNumbers.add(n);
            }
        }

        evenNumbers.sort(new Comparator<Double>() {
            @Override
            public int compare(Double o1, Double o2) {
                if (o1 < o2) return -1;
                else if (o1 == o2) return 0;
                else return 1;
            }
        });

        for (Double n : evenNumbers) {
            result.add(n);
            if (result.size() == 10) break;
        }
        return result;
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
