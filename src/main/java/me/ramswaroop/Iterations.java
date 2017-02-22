package me.ramswaroop;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author ramswaroop
 * @version 24/01/2017
 */
public class Iterations {

    /**
     * All threads running the benchmark share the same state object.
     */
    @State(Scope.Benchmark)
    public static class MyState {
        // stream of pseudorandom numbers from 90 (inclusive) to 5000 (exclusive)
        final List<Double> numbers = new Random().doubles(10000, 90, 11000).boxed().collect(Collectors.toList());
        Double temp;
    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.MICROSECONDS)
    public Double testForLoopIndexed(MyState state) {
        for (int i = 0; i < state.numbers.size(); i++) {
            state.temp = state.numbers.get(i);
        }
        return state.temp;
    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.MICROSECONDS)
    public Double testForEachLoop(MyState state) {
        for (Double i : state.numbers) {
            state.temp = i;
        }
        return state.temp;
    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.MICROSECONDS)
    public Double testIteratorForEachLoop(MyState state) {
        Iterator<Double> i = state.numbers.iterator();
        while (i.hasNext()) {
            state.temp = i.next();
        }
        return state.temp;
    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.MICROSECONDS)
    public Double testCollectionForEach(MyState state) {
        state.numbers.forEach(aDouble -> state.temp = aDouble);
        return state.temp;
    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.MICROSECONDS)
    public Double testStreamForEach(MyState state) {
        state.numbers.stream().forEach(aDouble -> state.temp = aDouble);
        return state.temp;
    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.MICROSECONDS)
    public Double testParallelStreamForEach(MyState state) {
        state.numbers.parallelStream().forEach(aDouble -> state.temp = aDouble);
        return state.temp;
    }

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(Iterations.class.getSimpleName())
                .warmupIterations(5)
                .measurementIterations(5)
                .forks(1)
                .build();

        new Runner(opt).run();
    }
}
