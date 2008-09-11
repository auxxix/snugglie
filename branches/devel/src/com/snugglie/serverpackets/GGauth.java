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

import com.snugglie.SnugglieClient;
import com.snugglie.SnugglieClient.ClientState;
import com.snugglie.clientpackets.RequestAuthLogin;
import com.snugglie.network.ReceivablePacket;

/**
 * @author pvizi
 * 
 */
public class GGauth extends ReceivablePacket<SnugglieClient> {

	protected int _response;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.snugglie.network.ReceivablePacket#read()
	 */
	@Override
	protected boolean read() {
		// if (getAvaliableBytes() >= 40) {
		_response = readD();
		System.out.println("Response: " + _response);
		return true;
		// } else {
		// System.err.println("Too short GGauth packet: "
		// + getAvaliableBytes());
		// return false;
		// }
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.snugglie.network.ReceivablePacket#run()
	 */
	@Override
	public void run() {
		getClient().setState(ClientState.AUTHED_GG);
		getClient().sendPacket(new RequestAuthLogin());
	}

}
