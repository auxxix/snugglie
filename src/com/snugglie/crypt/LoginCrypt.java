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
package com.snugglie.crypt;

import java.io.IOException;

import net.sf.l2j.loginserver.crypt.NewCrypt;
import net.sf.l2j.util.Rnd;

/**
 * This class is used for encrypting and decrypting login packets.
 * 
 * @author peter.vizi
 * 
 */
public class LoginCrypt {
	private static final byte[] STATIC_BLOWFISH_KEY = { (byte) 0x6b,
			(byte) 0x60, (byte) 0xcb, (byte) 0x5b, (byte) 0x82, (byte) 0xce,
			(byte) 0x90, (byte) 0xb1, (byte) 0xcc, (byte) 0x2b, (byte) 0x6c,
			(byte) 0x55, (byte) 0x6c, (byte) 0x6c, (byte) 0x6c, (byte) 0x6c };

	private NewCrypt _staticCrypt = new NewCrypt(STATIC_BLOWFISH_KEY);
	private NewCrypt _crypt;
	private boolean _static = true;

	/**
	 * Set the new Blowfish key.
	 * 
	 * @param key
	 */
	public void setKey(byte[] key) {
		_crypt = new NewCrypt(key);
	}

	/**
	 * Decrypt an array with the Blowfish engine.
	 * 
	 * @param raw
	 * @param offset
	 * @param size
	 * @return
	 * @throws IOException
	 */
	public boolean decrypt(byte[] raw, final int offset, final int size)
			throws IOException {
		if (_crypt == null) {
			_staticCrypt.decrypt(raw, offset, size);
		} else {
			_crypt.decrypt(raw, offset, size);
		}
		return NewCrypt.verifyChecksum(raw, offset, size);
	}

	/**
	 * Encrypt data with the Blowfish engine and XOR encoding also.
	 * 
	 * @param raw
	 * @param offset
	 * @param size
	 * @return
	 * @throws IOException
	 */
	public int encrypt(byte[] raw, final int offset, int size)
			throws IOException {
		// reserve checksum
		size += 4;

		if (_static) {
			// reserve for XOR "key"
			size += 4;

			// padding
			size += 8 - size % 8;
			NewCrypt.encXORPass(raw, offset, size, Rnd.nextInt());
			_staticCrypt.crypt(raw, offset, size);

			_static = false;
		} else {
			// padding
			size += 8 - size % 8;
			NewCrypt.appendChecksum(raw, offset, size);
			_crypt.crypt(raw, offset, size);
		}
		return size;
	}

	public static void decXORPass(byte raw[]) {
		decXORPass(raw, 0, raw.length);
	}

	/**
	 * Decode XOR encoded data.
	 * 
	 * Thanks to nerubai.
	 * 
	 * @param raw
	 * @param offset
	 * @param size
	 */
	public static void decXORPass(byte raw[], int offset, int size) {
		int pos = size - offset - 1;// - 4;
		int ecx = 0;
		ecx = (raw[pos--] & 0xff) << 24;
		// System.out.printf("0x%02x\n", ecx);
		ecx |= (raw[pos--] & 0xff) << 16;
		// System.out.printf("0x%02x\n", ecx);
		ecx |= (raw[pos--] & 0xff) << 8;
		// System.out.printf("0x%02x\n", ecx);
		ecx |= (raw[pos--] & 0xff);
		// System.out.printf("deckey: 0x%02x\n", ecx);
		while (pos > 8) {
			int edx = (raw[pos] & 0xff) << 24;
			edx |= (raw[pos - 1] & 0xff) << 16;
			edx |= (raw[pos - 2] & 0xff) << 8;
			edx |= (raw[pos - 3] & 0xff);

			// System.out.printf("ecx: 0x%x\n", ecx);
			edx ^= ecx;
			// System.out.printf("ki xorolt edx: 0x%x\n", edx);

			// System.out.printf("xorolt edx: 0x%x\n", edx);
			ecx -= edx;
			raw[pos--] = (byte) (edx >> 24 & 0xff);
			raw[pos--] = (byte) (edx >> 16 & 0xff);
			raw[pos--] = (byte) (edx >> 8 & 0xff);
			raw[pos--] = (byte) (edx & 0xff);
			// System.out.println();
		}
	}
}
