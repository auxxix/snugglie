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
package com.snugglie.serverpackets;

import net.sf.l2j.loginserver.serverpackets.LoginFail.LoginFailReason;

import com.snugglie.SnugglieClient;
import com.snugglie.network.ReceivablePacket;

/**
 * @author pvizi
 * 
 */
public class LoginFail extends ReceivablePacket<SnugglieClient> {

	private LoginFailReason _reason;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.snugglie.network.ReceivablePacket#read()
	 */
	@Override
	protected boolean read() {
		int id = readD();
		boolean result = false;
		for (LoginFailReason reason : LoginFailReason.values()) {
			if (reason.getCode() == id) {
				_reason = reason;
				result = true;
				break;
			}
		}
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.snugglie.network.ReceivablePacket#run()
	 */
	@Override
	public void run() {
		System.out.println("Login failed because of " + _reason.name());
	}

}
