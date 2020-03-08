package mathHomework.controllers;

import jcuda.Pointer;
import jcuda.runtime.JCuda;
import mathHomework.cuda.*;
import mathHomework.models.RIPEMD160;
import mathHomework.utils.BitwiseFunction;
import mathHomework.utils.Stopwatch;

import java.security.SecureRandom;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class MainController {

    private String privateKey = "a2d43efac7e99b7e3cf4c07ebfebb3c349d8f2b5b0e1062d9cef93c170d22d4f";

//    public static void main(String args[]){
//        log("Starting Homework");
//
//        log("Encode Private Key");
//        log("Public Key");
//        log("PKHash Step 1: Sha256");
//        log("PKHash Step 2: RIPEMD160");
//        log("CheckSum");
//        log("Address");
//
//        log("Decode Private Key");
//        log("Address");
//        log("PKHash");
//    }


    /**
     * Frequency of sin test signal
     */
    private static final float FREQ = 11.0f;

    // We will synthesize a signal = sin(2*pi*f)
    // To experiment, try changing these constants.
    /**
     * Length of complex vector (number of complex number pairs). Choose N such
     * that: N = 2^n, n = 1, 2, 3, ,,,
     */
    private static final int N = 16777216 / 16;
    /**
     * Delta t for sampling function
     */
    private static final float dT = 0.00005f;

    public static void main(String[] args) {
        JCudaTest();
    }

    private static void JCudaTest() {

        seed();
//        SinTest();
        RIPETest();
    }

    private static void RIPETest() {
        Stopwatch stopWatch;
        //create 128 sample input
        Map<String, String> messages = new HashMap<>();
        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[32]; // 128 bits are converted to 16 bytes;
        for (int i = 0; i < 128; i++) {
            random.nextBytes(bytes);
            messages.put(BitwiseFunction.byteArrayToHexString(bytes), null);
        }
        Map<String, String> messagesCPU = new HashMap<>(messages);
        Map<String, String> messagesCPUThreaded = new HashMap<>(messages);
        Map<String, String> messagesGPU = new HashMap<>(messages);
        //create 128 RIPEMD results
        messages.replaceAll((e, v) -> RIPEMD160.ripemd160(e));


        System.out.println("Performing 128 RIPDEMD160 on CPU...");
        stopWatch = new Stopwatch();
        messagesCPU.replaceAll((e, v) -> RIPEMD160.ripemd160(e));
        System.out.println("CPU time: " + stopWatch.elapsedTime()
                + " seconds \n");


        System.out.println("Performing 128 RIPDEMD160 on CPU Threaded...");
        stopWatch = new Stopwatch();
        messagesCPUThreaded = RIPEMD160Threaded(messagesCPUThreaded, 4);
        System.out.println("CPU threaded time: " + stopWatch.elapsedTime()
                + " seconds \n");


        System.out.println("Performing 128 RIPDEMD160 on GPU...");
        stopWatch = new Stopwatch();
        messagesGPU = RIPEMD160Cuda.ripemd160(messagesGPU);
        System.out.println("GPU time: " + stopWatch.elapsedTime()
                + " seconds \n");
    }

    private static Map<String, String> RIPEMD160Threaded(Map<String, String> messages, int numberOfThreads) {
        Map<String, String> results = new HashMap<>();
        messages = Collections.synchronizedMap(messages);

        ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads);

        messages.keySet().forEach(key -> executorService
                .execute(() -> results.put(key, RIPEMD160.ripemd160(key))));

        executorService.shutdown();
        try {
            executorService.awaitTermination(5, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return results;
    }

    /**
     * Synthesize a signal and demonstrate GPU vs CPU FFT/IFFT performance.
     */
    private static void SinTest() {

        System.out.println("Creating sin wave input data: Frequency = " + FREQ
                + ", N = " + N + ", dt = " + dT + " ...\n");

        // Note: gpuIn[] is an interleaved data array and is 2X the length of
        // the desired complex input vector.
        float gpuIn[] = sin2pif(FREQ, N, dT);

        // The use of the ComplexFloat class simplifies the code for
        // computations
        // performed on the CPU.
        // Note: cpuIn is the same length as the complex input vector (half the
        // length of the interleaved data array).
        ComplexFloat[] cpuIn = ComplexCalcFloat.InterleavedToComplex(gpuIn);

        System.out.println("L2 Norm of original signal: "
                + FftCpuFloat.VectorTwoNorm(cpuIn) + "\n");

        System.out.println("Performing a 1D C2C FFT on GPU with JCufft...");
        Stopwatch stopWatch = new Stopwatch();
        float gpuFft[] = FftGpuFloat.C2C_1D(gpuIn);
        System.out.println("GPU FFT time: " + stopWatch.elapsedTime()
                + " seconds \n");

        System.out.println("Performing a 1D C2C FFT on CPU...");
        stopWatch = new Stopwatch();
        ComplexFloat[] cpuFft = FftCpuFloat.FftRadix2_Cpu(cpuIn);
        System.out.println("CPU time: " + stopWatch.elapsedTime()
                + " seconds \n");

        // Convert to ComplexNumber to simplify code for CPU.
        ComplexFloat[] cGpuFft = ComplexCalcFloat.InterleavedToComplex(gpuFft);

        float gpuFftNorm = FftCpuFloat.VectorTwoNorm(cGpuFft);
        System.out.println("GPU FFT L2 Norm: " + gpuFftNorm);

        float cpuFftNorm = FftCpuFloat.VectorTwoNorm(cpuFft);
        System.out.println("CPU FFT L2 Norm: " + cpuFftNorm + "\n");

        float[] pGpuFft = FftCpuFloat.PowerSpectrum(cGpuFft);
        int indexMax = FftCpuFloat.IndexOfMaximum(pGpuFft);
        System.out.println("Index at maximum in GPU power spectrum = "
                + indexMax + ", " + "frequency = " + (float) indexMax / dT
                / (float) N);

        float[] pCpuFft = FftCpuFloat.PowerSpectrum(cpuFft);
        indexMax = FftCpuFloat.IndexOfMaximum(pCpuFft);
        System.out.println("Index at maximum in CPU power spectrum = "
                + indexMax + ", " + "frequency = " + (float) indexMax / dT
                / (float) N);
        System.out.println("\n");

        // Inverse FFT of above results (Normalized for signal reconstruction)

        System.out.println("Performing 1D C2C IFFT(FFT) on GPU with JCufft...");
        stopWatch = new Stopwatch();
        float gpuIFft[] = FftGpuFloat.InverseC2C_1D(gpuFft, true);
        System.out.println("GPU time: " + stopWatch.elapsedTime()
                + " seconds \n");

        System.out.println("Performing 1D C2C IFFT(FFT) on CPU...");
        stopWatch = new Stopwatch();
        ComplexFloat cpuIFftt[] = FftCpuFloat.IFftRadix2_Cpu(cpuFft, true);
        System.out.println("CPU time: " + stopWatch.elapsedTime()
                + " seconds \n");

        ComplexFloat[] cGpuIFft = ComplexCalcFloat
                .InterleavedToComplex(gpuIFft);

        float gpuL2 = FftCpuFloat.VectorTwoNorm(cGpuIFft);
        System.out.println("GPU IFFT L2 Norm: " + gpuL2);

        float cpuIFftNorm = FftCpuFloat.VectorTwoNorm(cpuIFftt);
        System.out.println("CPU IFFT L2 Norm: " + cpuIFftNorm);
    }

    /**
     * Test signal synthesis
     *
     * @param f  Frequency
     * @param N  vector length for requested signal (You will get an
     *           interleaved complex data array of length 2*N.)
     * @param dt sampling function increment (delta t).
     * @return an interleaved array of length 2*N representing a sampled
     * function: sin(2*pi*freq)
     */
    private static float[] sin2pif(float f, int N, float dt) {

        float result[] = new float[N * 2];

        float step = 0;
        for (int i = 0; i < result.length; i += 2) {
            float angle = (float) (2 * Math.PI * f * step);
            result[i] = (float) Math.sin(angle);
            step += dt;
        }
        return result;
    }

    /**
     * GPU initialization. Running this first appears to speed up the first GPU
     * computation run in application.
     */
    private static void seed() {
        Pointer pointer = new Pointer();
        JCuda.cudaMalloc(pointer, 4);
        JCuda.cudaFree(pointer);
    }
}
