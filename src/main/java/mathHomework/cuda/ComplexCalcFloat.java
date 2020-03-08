package mathHomework.cuda;
/*
  Author Mark Bishop; 2003
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
  along with this program.  If not, see http://www.gnu.org/licenses/.
 */

/**
 * A utility class for complex number calculations on the CPU using the
 * ComplexFloat type. The type "ComplexFloat" and arrays of type ComplexFloat[]
 * are not interleaved data. Do not confuse this with the (GPU) cuComplex type
 * or CUDA interleaved data arrays.
 */
public class ComplexCalcFloat {

	public static ComplexFloat Add(ComplexFloat arg1, ComplexFloat arg2) {
		ComplexFloat result = new ComplexFloat();
		result.Real = arg1.Real + arg2.Real;
		result.Imaginary = arg1.Imaginary + arg2.Imaginary;
		return result;
	}

	/**
	 * @return arg1 - arg2
	 */
	public static ComplexFloat Subtract(ComplexFloat arg1, ComplexFloat arg2) {
		ComplexFloat result = new ComplexFloat();
		result.Real = arg1.Real - arg2.Real;
		result.Imaginary = arg1.Imaginary - arg2.Imaginary;
		return result;
	}

	public static ComplexFloat Multiply(ComplexFloat arg1, ComplexFloat arg2) {
		ComplexFloat result = new ComplexFloat();
		result.Real = (arg1.Real * arg2.Real)
				- (arg1.Imaginary * arg2.Imaginary);
		result.Imaginary = (arg1.Real * arg2.Imaginary)
				+ (arg1.Imaginary * arg2.Real);
		return result;
	}

	public static ComplexFloat Multiply(ComplexFloat arg1, float arg2) {
		ComplexFloat result = new ComplexFloat();
		result.Real = arg1.Real * arg2;
		result.Imaginary = arg1.Imaginary * arg2;
		return result;
	}

	public static ComplexFloat Conjugate(ComplexFloat Z) {
		ComplexFloat result = new ComplexFloat();
		result.Real = Z.Real;
		result.Imaginary = -Z.Imaginary;
		return result;
	}

	// Vector management utilities

	public static ComplexFloat[] InitializeVector(ComplexFloat[] vector) {
		for (int i = 0; i < vector.length; i++) {
			vector[i] = new ComplexFloat();
		}
		return vector;
	}

	public static ComplexFloat FloatToComplex(float real) {
		ComplexFloat result = new ComplexFloat();
		result.Real = real;
		return result;
	}

	public static ComplexFloat[] FloatToComplex(float[] real) {
		int len = real.length;
		ComplexFloat[] result = new ComplexFloat[len];
		for (int i = 0; i < len; i++) {
			result[i] = new ComplexFloat();
			result[i].Real = real[i];
		}
		return result;
	}

	public static ComplexFloat[] InterleavedToComplex(float[] InterleavedArray) {
		int len = InterleavedArray.length;
		ComplexFloat[] result = new ComplexFloat[len / 2];
		for (int i = 0; i < len; i += 2) {
			result[i / 2] = new ComplexFloat();
			result[i / 2].Real = InterleavedArray[i];
			result[i / 2].Imaginary = InterleavedArray[i + 1];
		}
		return result;
	}

	public static float[] ComplexToInterleaved(ComplexFloat[] cVector) {
		int vN = cVector.length;
		int retLen = vN * 2;
		float result[] = new float[retLen];
		for (int i = 0; i < vN; i++) {
			result[2 * i] = cVector[i].Real;
			result[2 * i + 1] = cVector[i].Imaginary;
		}
		return result;
	}

}
