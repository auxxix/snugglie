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

import com.snugglie.Activator;
import com.snugglie.SnugglieClient;
import com.snugglie.SnugglieClient.ClientState;
import com.snugglie.lclientpackets.RequestAuthLogin;
import com.snugglie.lclientpackets.RequestServerList;
import com.snugglie.network.ReceivablePacket;

/**
 * The user sends the name and password in a {@link RequestAuthLogin} packet and
 * the server either displays the license to the user in this packet, or sends
 * the list of servers in a {@link ServerList} packet.
 * 
 * @author peter.vizi
 * 
 */
public class LoginOK extends ReceivablePacket<SnugglieClient> {

	protected int _loginOkID1;
	protected int _loginOkID2;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.snugglie.network.ReceivablePacket#read()
	 */
	@Override
	protected boolean read() {
		_loginOkID1 = readD();
		_loginOkID2 = readD();
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.snugglie.network.ReceivablePacket#run()
	 */
	@Override
	public void run() {
		getClient().setState(ClientState.AUTH_SUCCESS);
		getClient().setLoginOK1(_loginOkID1);
		getClient().setLoginOK2(_loginOkID2);
		Activator.msgStream.println("Login ok, here's the license.");

		Activator.propagatePacket(this);
		getClient().sendPacket(new RequestServerList());
	}
}
