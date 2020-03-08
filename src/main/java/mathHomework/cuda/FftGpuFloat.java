package mathHomework.cuda;
/*
  Adaption of JCuda.org example.
  Author Mark Bishop; 2012
  License GNU v3;
  This program is free software: you can redistribute it and/or modify
  it under the terms of the GNU General Public License as published by
  the Free Software Foundation, either version 3 of the License, or
  (at your option) any later version.
  <p>
  This program is distributed in the hope that it will be useful,
  but WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  GNU General Public License for more details.
  <p>
  You should have received a copy of the GNU General Public License
  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

import jcuda.jcufft.JCufft;
import jcuda.jcufft.cufftHandle;
import jcuda.jcufft.cufftType;

/**
 * Computation class for FFT on Central Processor Unit. and related utilities.
 */

public class FftGpuFloat {

	/**
	 * @param input an interleaved array (ft)
	 * @return C2C FFT (hf)
	 */
	public static float[] C2C_1D(float[] input) {

		float outputJCufft[] = input.clone();

		// The plan uses the number of complex elements in the input array, i.e:
		// length/2.
		cufftHandle plan = new cufftHandle();
		JCufft.cufftPlan1d(plan, input.length / 2, cufftType.CUFFT_C2C, 1);
		JCufft.cufftExecC2C(plan, outputJCufft, outputJCufft,
				JCufft.CUFFT_FORWARD);
		JCufft.cufftDestroy(plan);

		return outputJCufft;
	}

	/**
	 * @param input       an interleaved array (hf)
	 * @param isNormalize Set true if result should be multiplied by 1/N
	 * @return C2C IFFT (ft)
	 */
	public static float[] InverseC2C_1D(float[] input, boolean isNormalize) {

		float outputJCufft[] = input.clone();

		// The plan uses the number of complex elements in the input array, i.e:
		// length/2.
		cufftHandle plan = new cufftHandle();
		JCufft.cufftPlan1d(plan, input.length / 2, cufftType.CUFFT_C2C, 1);
		JCufft.cufftExecC2C(plan, outputJCufft, outputJCufft,
				JCufft.CUFFT_INVERSE);
		JCufft.cufftDestroy(plan);

		// Normalization allows for amplitude reconstruction of the original
		// signal.
		if (isNormalize) {
			float normalize = 2 / (float) outputJCufft.length;
			outputJCufft = CaxpyGpu.CaxpyFloatScalar(normalize, outputJCufft);
		}

		return outputJCufft;
	}

}
