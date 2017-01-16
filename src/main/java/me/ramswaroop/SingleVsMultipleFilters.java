package me.ramswaroop;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author ramswaroop
 * @version 05/01/2017
 */
public class SingleVsMultipleFilters {

    @Benchmark
    @BenchmarkMode(Mode.All)
    @OutputTimeUnit(TimeUnit.MICROSECONDS)
    public long testStreamWithSingleFilter() {
        List<Double> doubles = new Random().doubles(1_000, 1, 4).boxed().collect(Collectors.toList());
        return doubles
                .stream()
                .filter(d -> d < Math.PI
                        && d > Math.E
                        && d != 3.10040970053377777
                        && d != 2.96240970053377777)
                .count();
    }

    @Benchmark
    @BenchmarkMode(Mode.All)
    @OutputTimeUnit(TimeUnit.MICROSECONDS)
    public long testStreamWithMultipleFilters() {
        List<Double> doubles = new Random().doubles(1_000, 1, 4).boxed().collect(Collectors.toList());
        return doubles
                .stream()
                .filter(d -> d > Math.E)
                .filter(d -> d < Math.PI)
                .filter(d -> d != 3.10040970053377777)
                .filter(d -> d != 2.96240970053377777)
                .count();
    }

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(SingleVsMultipleFilters.class.getSimpleName())
                .warmupIterations(5)
                .measurementIterations(5)
                .forks(1)
                .build();

        new Runner(opt).run();
    }

}
