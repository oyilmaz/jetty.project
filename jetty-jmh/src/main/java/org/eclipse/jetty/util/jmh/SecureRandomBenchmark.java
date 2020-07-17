package org.eclipse.jetty.util.jmh;

import java.security.SecureRandom;
import java.util.concurrent.TimeUnit;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Threads;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

@State(Scope.Benchmark)
@Threads(4000)
@Warmup(iterations = 4, time = 1, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 4, time = 1, timeUnit = TimeUnit.SECONDS)
public class SecureRandomBenchmark
{
    @Param({"NativePRNG", "NativePRNGNonBlocking", "SHA1PRNG"})
    String algorithm;

    SecureRandom secureRandom;

    @Setup(Level.Trial)
    public void setupTrial() throws Exception
    {
        secureRandom = SecureRandom.getInstance(algorithm);
    }

    @Benchmark
    @BenchmarkMode({Mode.Throughput})
    public void testWebSocketMask() throws Exception
    {
        byte[] mask = new byte[4];
        secureRandom.nextBytes(mask);
    }

    @Benchmark
    @BenchmarkMode({Mode.Throughput})
    public void testClientNonce() throws Exception
    {
        byte[] mask = new byte[8];
        secureRandom.nextBytes(mask);
    }

    public static void main(String[] args) throws RunnerException
    {
        Options opt = new OptionsBuilder()
            .include(SecureRandomBenchmark.class.getSimpleName())
            .forks(1)
            .build();

        new Runner(opt).run();
    }
}
