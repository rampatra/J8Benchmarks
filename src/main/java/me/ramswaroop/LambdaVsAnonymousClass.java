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
 * lambda (non-FI and FI) and anonymous classes.
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
    @OutputTimeUnit(TimeUnit.NANOSECONDS)
    public void testLambdaNonCapturing() {
        // lambda expressions
        FI f1 = () -> {
            return 2 + 2;
        };
        f1.test();
        // lambda expressions
        FI f2 = () -> {
            return 2 + 2;
        };
        f2.test();
    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.NANOSECONDS)
    public void testAnonymousClassNonCapturing() {
        // anonymous classes
        FI f1 = new FI() {
            @Override
            public int test() {
                return 5;
            }
        };
        f1.test();
        // anonymous classes
        FI f2 = new FI() {
            @Override
            public int test() {
                return 5;
            }
        };
        f2.test();
    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.NANOSECONDS)
    public void testLambdaCapturing() {
        // lambda expressions
        final int i = 1;
        FI f1 = () -> i + 5;
        f1.test();
        // lambda expressions
        final int j = 2;
        FI f2 = () -> {
            return j + 5;
        };
        f2.test();
    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.NANOSECONDS)
    public void testAnonymousClassCapturing() {
        // anonymous classes
        final int i = 1;
        FI f1 = new FI() {
            @Override
            public int test() {
                return i + 5;
            }
        };
        f1.test();
        // anonymous classes
        final int j = 2;
        FI f2 = new FI() {
            @Override
            public int test() {
                return j + 5;
            }
        };
        f2.test();
    }

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(LambdaVsAnonymousClass.class.getSimpleName())
                .warmupIterations(0)
                .measurementIterations(2)
                .forks(1)
                .build();

        new Runner(opt).run();
    }
}

@FunctionalInterface
interface FI {
    int test();
}
