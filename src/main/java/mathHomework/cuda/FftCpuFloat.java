package mathHomework.cuda;

/**
 * Adaption of early Robert Sedgwick and Kevin Wayne Cooley–Tukey (recursive)
 * Radix-2 implementation Author Mark Bishop; 2005 License GNU v3; This program
 * is free software: you can redistribute it and/or modify it under the terms of
 * the GNU General Public License as published by the Free Software Foundation,
 * either version 3 of the License, or (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * <p>
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see http://www.gnu.org/licenses/.
 */

public class FftCpuFloat {

	/**
	 * @param ft a time domain function
	 * @return FFT (ht) a frequency domain function
	 */
	public static ComplexFloat[] FftRadix2_Cpu(ComplexFloat[] ft) {

		int n = ft.length;
		ComplexFloat[] hf = new ComplexFloat[n];
		ComplexCalcFloat.InitializeVector(hf);

		if (n == 1) {
			hf[0] = ft[0];
			return ft;
		}

		ComplexFloat[] even = new ComplexFloat[n / 2];
		ComplexFloat[] odd = new ComplexFloat[n / 2];
		for (int k = 0; k < n / 2; k++) {
			even[k] = ft[2 * k];
			odd[k] = ft[2 * k + 1];
		}
		ComplexFloat[] q = FftRadix2_Cpu(even);
		ComplexFloat[] r = FftRadix2_Cpu(odd);
		for (int k = 0; k < n / 2; k++) {
			float kth = (float) (-2 * k * Math.PI / n);
			ComplexFloat wk = new ComplexFloat();
			wk.Real = (float) Math.cos(kth);
			wk.Imaginary = (float) Math.sin(kth);

			hf[k] = ComplexCalcFloat.Add(q[k],
					(ComplexCalcFloat.Multiply(wk, r[k])));
			hf[k + n / 2] = ComplexCalcFloat.Subtract(q[k],
					(ComplexCalcFloat.Multiply(wk, r[k])));
		}
		return hf;
	}

	/**
	 * IFFT on CPU, Cooley–Tukey (recursive) Radix-2t
	 *
	 * @param hf          a frequency domain function
	 * @param isNormalize Set true if result should be multiplied by 1/N
	 * @return IFFT (ft) a time domain function
	 */
	public static ComplexFloat[] IFftRadix2_Cpu(ComplexFloat[] hf,
												boolean isNormalize) {
		int n = hf.length;

		// take conjugate
		for (int i = 0; i < n; i++)
			hf[i] = ComplexCalcFloat.Conjugate(hf[i]);

		// compute forward FFT
		ComplexFloat[] ft = FftRadix2_Cpu(hf);

		// take conjugate again
		for (int i = 0; i < n; i++)
			ft[i] = ComplexCalcFloat.Conjugate(ft[i]);

		// Normalization allows for amplitude reconstruction of the original
		// signal.
		if (isNormalize) {
			float rcrpln = (float) (1.0 / (float) (n));
			for (int i = 0; i < n; i++) {
				ft[i] = ComplexCalcFloat.Multiply(ft[i], rcrpln);
			}
		}

		return ft;
	}

	/**
	 * @param vector a complex vector
	 * @return L2 Norm
	 */
	public static float VectorTwoNorm(ComplexFloat[] vector) {
		// L2 Norm
		int m = vector.length;
		double L2 = 0;
		for (ComplexFloat complexFloat : vector) {
			L2 += Math.pow(complexFloat.Real, 2)
					+ Math.pow(complexFloat.Imaginary, 2);
		}

		return (float) Math.pow(L2, 0.5);
	}

	/**
	 * @param vector a complex vector
	 * @return Power Spectrum as a float[] vector
	 */
	public static float[] PowerSpectrum(ComplexFloat[] vector) {

		int n = vector.length;
		float[] result = new float[n];

		for (int i = 0; i < n; i++) {
			result[i] = (float) (Math.pow(vector[i].Real, 2) + Math.pow(
					vector[i].Imaginary, 2));
		}
		return result;
	}

	/**
	 * @param vector a real vector
	 * @return the index of the vector element that has the largest absolute
	 * value (considers only the first N/2 elements)
	 */
	public static int IndexOfMaximum(float[] vector) {

		int result = 0;
		float max = 0;
		for (int i = 0; i < vector.length / 2; i++) {
			if (Math.abs(vector[i]) > max) {
				max = Math.abs(vector[i]);
				result = i;
			}
		}
		return result;

	}

}
