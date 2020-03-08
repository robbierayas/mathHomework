package mathHomework.cuda;

import jcuda.Pointer;
import jcuda.Sizeof;

import java.util.Map;

import static jcuda.runtime.JCuda.*;
import static jcuda.runtime.cudaMemcpyKind.cudaMemcpyDeviceToDevice;
import static jcuda.runtime.cudaMemcpyKind.cudaMemcpyDeviceToHost;

public class RIPEMD160Cuda {
    public static Map<String, String> ripemd160(Map<String, String> messages) {

        //TODO loop map
        Pointer message = new Pointer();
        Pointer newMessage = new Pointer();
        Pointer A = new Pointer();
        Pointer Acopy = new Pointer();
        cudaMalloc(message, Sizeof.POINTER);
        cudaMemcpy(A, Acopy, Sizeof.DOUBLE * 2, cudaMemcpyDeviceToDevice);
        //TODO calculate
        cudaMemset(message, 0, Sizeof.INT);
        cudaMemcpy(Pointer.to(newMessage), message, Sizeof.INT, cudaMemcpyDeviceToHost);
        cudaDeviceSynchronize();
//        TODO messages=newMessage;
        cudaFree(Acopy);
        cudaFree(newMessage);
        cudaFree(message);
        cudaFree(A);
        return messages;
    }

}
