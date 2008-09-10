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
package com.snugglie;

import java.io.IOException;
import java.nio.ByteBuffer;

import net.sf.l2j.loginserver.crypt.LoginCrypt;

import org.mmocore.network.MMOClient;
import org.mmocore.network.MMOConnection;

/**
 * This is gonna be a client one time.
 * 
 * @author peter.vizi
 * 
 */
public class SnugglieClient extends MMOClient<MMOConnection<SnugglieClient>> {

	/**
	 * For encrypting data.
	 */
	private LoginCrypt _loginCrypt;

	public SnugglieClient(MMOConnection<SnugglieClient> con) {
		super(con);
		_loginCrypt = new LoginCrypt();
	}

	@Override
	public boolean decrypt(ByteBuffer buf, int size) {
		boolean ret = false;
		try {
			ret = _loginCrypt.decrypt(buf.array(), buf.position(), size);
		} catch (IOException e) {
			e.printStackTrace();
			closeNow();
			return false;
		}

		if (!ret) {
			byte[] dump = new byte[size];
			System.arraycopy(buf.array(), buf.position(), dump, 0, size);
			System.err.println("Wrong checksum from client: " + toString());
			closeNow();
		}

		return ret;
	}

	@Override
	public boolean encrypt(ByteBuffer buf, int size) {
		final int offset = buf.position();
		try {
			size = _loginCrypt.encrypt(buf.array(), offset, size);
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}

		buf.position(offset + size);
		return true;
	}

}
