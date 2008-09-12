// $Id$
/**
 * This file is part of Snugglie.
 *
 * Snugglie is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Snugglie is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Snugglie.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.snugglie.lclientpackets;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import util.Util;

import com.snugglie.SnugglieClient;
import com.snugglie.network.SendablePacket;

/**
 * 
 * @author peter.vizi
 * 
 */
public class RequestAuthLogin extends SendablePacket<SnugglieClient> {

	byte[] _raw = new byte[127];

	@Override
	protected int getHeaderSize() {
		return 2;
	}

	@Override
	protected void write() {
		System.out.println("Writing RequestAuthLogin packet");
		writeC(0x00);
		byte[] encrypted = null;
		try {
			Cipher rsaCipher = Cipher.getInstance("RSA/ECB/nopadding");
			rsaCipher.init(Cipher.ENCRYPT_MODE, getClient().getPublicKey());

			System.arraycopy(getClient().getUser().getBytes(), 0, _raw, 94,
					getClient().getUser().getBytes().length);
			System.arraycopy(getClient().getPasssword().getBytes(), 0, _raw,
					108, getClient().getPasssword().getBytes().length);

			System.out.print("The raw data: ");
			Util.printArray(_raw);
			encrypted = rsaCipher.doFinal(_raw);
			System.out.print("The encrypted data: ");
			Util.printArray(encrypted);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			e.printStackTrace();
		}
		writeB(encrypted);
	}

	@Override
	protected void writeHeader(int dataSize) {
		writeH(dataSize + this.getHeaderSize());
	}

}
