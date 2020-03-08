package mathHomework.cuda;

/*
 * Demo Adaption  Mark Bishop; 2012
 * License GNU v3;
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * <p>
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see http://www.gnu.org/licenses/.
 */

import jcuda.Pointer;
import jcuda.Sizeof;
import jcuda.cuComplex;
import jcuda.jcublas.JCublas;
import jcuda.runtime.JCuda;
import jcuda.runtime.cudaMemcpyKind;

/**
 * Convenience Class y(n) = Ax(n) + y(n)
 */
public class CaxpyGpu {

	/**
	 * @param scalar : a cuComplex number object
	 * @param x      an interleaved complex number data array
	 * @param y      an interleaved complex number data array
	 * @return y = ax + y
	 */
	public static float[] CaxpyComplexScalar(cuComplex scalar, float[] x,
											 float[] y) {

		int complexElements = x.length / 2;
		int floatElements = x.length;
		int memorySize = floatElements * Sizeof.FLOAT;

		// Allocate memory on the device using JCuda.
		Pointer deviceX = new Pointer();
		Pointer deviceY = new Pointer();
		JCuda.cudaMalloc(deviceX, memorySize);
		JCuda.cudaMalloc(deviceY, memorySize);

		// Copy memory from host to device using JCuda.
		JCuda.cudaMemcpy(deviceX, Pointer.to(x), memorySize,
				cudaMemcpyKind.cudaMemcpyHostToDevice);
		JCuda.cudaMemcpy(deviceY, Pointer.to(y), memorySize,
				cudaMemcpyKind.cudaMemcpyHostToDevice);

		JCublas.cublasInit();
		JCublas.cublasCaxpy(complexElements, scalar, deviceX, 1, deviceY, 1);

		// Copy the result from the device to the host.
		JCuda.cudaMemcpy(Pointer.to(y), deviceY, memorySize,
				cudaMemcpyKind.cudaMemcpyDeviceToHost);

		// Clean up
		JCuda.cudaFree(deviceX);
		JCuda.cudaFree(deviceY);
		JCublas.cublasShutdown();

		return y;
	}

	/**
	 * @param scalar : real only as float
	 * @param x      an interleaved complex number data array
	 * @return y=ax
	 */
	public static float[] CaxpyFloatScalar(float scalar, float[] x) {

		int complexElements = x.length / 2;
		int floatElements = x.length;
		int memorySize = floatElements * Sizeof.FLOAT;
		float y[] = new float[floatElements];

		// Allocate memory on the device using JCuda.
		Pointer deviceX = new Pointer();
		Pointer deviceY = new Pointer();
		JCuda.cudaMalloc(deviceX, memorySize);
		JCuda.cudaMalloc(deviceY, memorySize);

		// Copy memory from host to device using JCuda.
		JCuda.cudaMemcpy(deviceX, Pointer.to(x), memorySize,
				cudaMemcpyKind.cudaMemcpyHostToDevice);
		JCuda.cudaMemcpy(deviceY, Pointer.to(y), memorySize,
				cudaMemcpyKind.cudaMemcpyHostToDevice);

		// Convert scalar to compatible cuComplex number object.
		cuComplex alpha = cuComplex.cuCmplx(scalar, 0.0f);

		// Perform a complex y=a*x+y operation using JCublas .
		JCublas.cublasInit();
		JCublas.cublasCaxpy(complexElements, alpha, deviceX, 1, deviceY, 1);

		// Copy the result from the device to the host.
		JCuda.cudaMemcpy(Pointer.to(y), deviceY, memorySize,
				cudaMemcpyKind.cudaMemcpyDeviceToHost);

		// Clean up
		JCuda.cudaFree(deviceX);
		JCuda.cudaFree(deviceY);
		JCublas.cublasShutdown();

		return y;
	}

	/**
	 * @param scalar a cuComplex number object
	 * @param x      an interleaved complex number data array
	 * @return y=ax
	 */
	public static float[] CaxpyComplexScalar(cuComplex scalar, float[] x) {

		int complexElements = x.length / 2;
		int floatElements = x.length;
		int memorySize = floatElements * Sizeof.FLOAT;

		float y[] = new float[floatElements];

		// Allocate memory on the device using JCuda.
		Pointer deviceX = new Pointer();
		Pointer deviceY = new Pointer();
		JCuda.cudaMalloc(deviceX, memorySize);
		JCuda.cudaMalloc(deviceY, memorySize);

		// Copy memory from host to device using JCuda.
		JCuda.cudaMemcpy(deviceX, Pointer.to(x), memorySize,
				cudaMemcpyKind.cudaMemcpyHostToDevice);
		JCuda.cudaMemcpy(deviceY, Pointer.to(y), memorySize,
				cudaMemcpyKind.cudaMemcpyHostToDevice);

		JCublas.cublasInit();
		JCublas.cublasCaxpy(complexElements, scalar, deviceX, 1, deviceY, 1);

		// Copy the result from the device to the host.
		JCuda.cudaMemcpy(Pointer.to(y), deviceY, memorySize,
				cudaMemcpyKind.cudaMemcpyDeviceToHost);

		// Clean up
		JCuda.cudaFree(deviceX);
		JCuda.cudaFree(deviceY);
		JCublas.cublasShutdown();

		return y;
	}
}
