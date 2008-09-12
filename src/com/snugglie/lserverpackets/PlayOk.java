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

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.channels.SocketChannel;

import com.snugglie.SnugglieClient;
import com.snugglie.gclientpackets.ProtocolVersion;
import com.snugglie.network.ReceivablePacket;
import com.snugglie.network.SelectorThread;

/**
 * @author pvizi
 * 
 */
public class PlayOk extends ReceivablePacket<SnugglieClient> {

	protected int _playOk1, _playOk2;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.snugglie.network.ReceivablePacket#read()
	 */
	@Override
	protected boolean read() {
		_playOk1 = readD();
		_playOk2 = readD();
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.snugglie.network.ReceivablePacket#run()
	 */
	@Override
	public void run() {
		System.out.println("Yay! You can play on the server. What now?");
		try {
			SelectorThread<SnugglieClient> st = SelectorThread.getInstance();
			st.openSocket(new InetSocketAddress(
					getClient().getServerData()._ip, getClient()
							.getServerData()._port), getClient());
			System.out.println("Connected to "
					+ getClient().getServerData()._ip + ":"
					+ getClient().getServerData()._port);
			getClient().sendPacket(new ProtocolVersion());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
