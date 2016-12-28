package me.ramswaroop;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.concurrent.TimeUnit;

/**
 * The below benchmarks compares the average time for a single execution of the benchmark methods for
 * lambda (non-capturing and capturing) and anonymous classes.
 * <p>
 * Some useful links:
 * 1. <a href="http://cr.openjdk.java.net/~briangoetz/lambda/lambda-translation.html">Lambda Translation</a>
 * 2. <a href="https://www.infoq.com/articles/Java-8-Lambdas-A-Peek-Under-the-Hood">Lambdas under the hood</a>
 * 3. <a href="http://wiki.jvmlangsummit.com/images/7/7b/Goetz-jvmls-lambda.pdf">Brian Goetz presentation on Lambdas</a>
 *
 * @author ramswaroop
 * @version 27/12/2016
 */
public class LambdaVsAnonymousClass {

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.MICROSECONDS)
    public void testLambda() {
        // nonCapturing expressions
        NonCapturing nonCapturing = () -> System.out.println("ram");
        nonCapturing.test();
    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.MICROSECONDS)
    public void testAnonymousClass() {
        // anonymous classes
        NonCapturing nonCapturing = new NonCapturing() {
            @Override
            public void test() {
                System.out.println("ram");
            }
        };
        nonCapturing.test();
    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.MICROSECONDS)
    public void testLambdaCapturing() {
        // lambda expressions
        final int i = 0;
        Capturing capturing = n -> System.out.println(n);
        capturing.test(i);
    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.MICROSECONDS)
    public void testAnonymousClassCapturing() {
        // anonymous classes
        final int i = 0;
        Capturing capturing = new Capturing() {
            @Override
            public void test(int n) {
                System.out.println(n);
            }
        };
        capturing.test(i);
    }

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(LambdaVsAnonymousClass.class.getSimpleName())
                .warmupIterations(5)
                .measurementIterations(5)
                .forks(1)
                .build();

        new Runner(opt).run();
    }
}

@FunctionalInterface
interface NonCapturing {
    void test();
}

@FunctionalInterface
interface Capturing {
    void test(int n);
}
