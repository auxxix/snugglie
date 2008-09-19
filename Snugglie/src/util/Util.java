// $Id$
/**
 * This file is part of Snugglie.
 *
 * Foobar is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Foobar is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Foobar.  If not, see <http://www.gnu.org/licenses/>.
 */
package util;

import java.io.PrintStream;

/**
 * Some utility functions which are not part of the system.
 * 
 * @author peter.vizi
 * 
 */
public class Util {

	/**
	 * Print a byte array to the standard output in hexadecimal format.
	 * 
	 * @param array
	 */
	public static void printArray(final byte[] array) {
		printArray(System.out, "0x%02x ", array);
	}

	/**
	 * Print a byte array to the PrintStream in hexadecimal format.
	 * 
	 * @param out
	 * @param array
	 */
	public static void printArray(PrintStream out, String format,
			final byte[] array) {
		printArray(out, format, array, 0, array.length);
	}

	/**
	 * Print a byte array to the PrintStream in hexadecimal format.
	 * 
	 * @param out
	 *            the sink
	 * @param array
	 *            the data
	 * @param offset
	 *            the first byte to print
	 * @param size
	 *            how many bytes to print
	 */
	public static void printArray(PrintStream out, String format,
			final byte[] array, int offset, int size) {
		out.print("{");
		for (int i = offset; i < offset + size; i++) {
			out.printf(format, array[i]);
		}
		out.println("}");
	}

	public static void arrayDiff(byte[] left, byte[] right) {
		arrayDiff(left, 0, left.length, right, 0, right.length);
	}

	public static void arrayDiff(byte[] left, int left_offset, int left_size,
			byte[] right, int right_offset, int right_size) {
		if (left_size != right_size) {
			System.out.printf("Not the same size: %3d %3d\n", left_size,
					right_size);
			return;
		}
		boolean identical = true;
		for (int i = 0; i < left_size; i++) {
			if (left[left_offset + i] != right[right_offset + i]) {
				System.out.printf("%3d 0x%02x 0x%02x\n", i, left[i], right[i]);
				identical = false;
			}
		}
		if (identical) {
			System.out.println("Arrays are identical.");
		}
	}
}
