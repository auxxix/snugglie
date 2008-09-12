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
package com.snugglie.lserverpackets;

import net.sf.l2j.loginserver.serverpackets.PlayFail.PlayFailReason;

import com.snugglie.SnugglieClient;
import com.snugglie.network.ReceivablePacket;

/**
 * @author pvizi
 * 
 */
public class PlayFail extends ReceivablePacket<SnugglieClient> {

	protected PlayFailReason _reason;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.snugglie.network.ReceivablePacket#read()
	 */
	@Override
	protected boolean read() {
		int code = readC();
		boolean found = false;
		for (PlayFailReason reason : PlayFailReason.values()) {
			if (reason.getCode() == code) {
				_reason = reason;
				found = true;
				break;
			}
		}
		return found;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.snugglie.network.ReceivablePacket#run()
	 */
	@Override
	public void run() {
		System.out.println("You can not connect to the server, because of "
				+ _reason.name());
	}

}
