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

import java.util.List;

import javolution.util.FastList;

import com.snugglie.SnugglieClient;
import com.snugglie.clientpackets.RequestAuthLogin;
import com.snugglie.network.ReceivablePacket;

/**
 * After the user sends his name and password in a {@link RequestAuthLogin}
 * packet, the server either sends the list of servers or shows the license to
 * the user {@link LoginOK}.
 * 
 * @author peter.vizi
 * 
 */
public class ServerList extends ReceivablePacket<SnugglieClient> {

	protected List<ServerData> _servers = new FastList<ServerData>();

	/**
	 * This class encapsulates the data about a server.
	 * 
	 * @author peter.vizi
	 * 
	 */
	class ServerData {
		protected String _ip;
		protected int _port;
		protected boolean _pvp;
		protected int _currentPlayers;
		protected int _maxPlayers;
		protected boolean _testServer;
		protected boolean _brackets;
		protected boolean _clock;
		protected int _status;
		protected int _serverId;

		ServerData(String pIp, int pPort, boolean pPvp, boolean pTestServer,
				int pCurrentPlayers, int pMaxPlayers, boolean pBrackets,
				boolean pClock, int pStatus, int pServer_id) {
			_ip = pIp;
			_port = pPort;
			_pvp = pPvp;
			_testServer = pTestServer;
			_currentPlayers = pCurrentPlayers;
			_maxPlayers = pMaxPlayers;
			_brackets = pBrackets;
			_clock = pClock;
			_status = pStatus;
			_serverId = pServer_id;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.snugglie.network.ReceivablePacket#read()
	 */
	@Override
	protected boolean read() {
		int list_size = readC();
		System.out.println("Received a list of " + list_size + " servers.");
		int last_server = readC();
		for (int i = 0; i < list_size; i++) {
			int serverId = readC();
			byte[] address = new byte[4];
			readB(address);
			String ip = address[0] + "." + address[1] + "." + address[2] + "."
					+ address[3];
			int port = readD();
			readC(); // 0x00
			boolean pvp = true ? readC() == 1 : false;
			int currentPlayers = readH();
			int maxPlayers = readH();
			int serverStatus = readC();
			int serverType = readD();
			boolean testServer = false; // TODO
			boolean clock = false; // TODO
			boolean brackets = true ? readC() == 1 : false;

			ServerData data = new ServerData(ip, port, pvp, testServer,
					currentPlayers, maxPlayers, brackets, clock, serverStatus,
					serverId);
			_servers.add(data);
		}
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.snugglie.network.ReceivablePacket#run()
	 */
	@Override
	public void run() {
		if (_servers.size() == 0) {
			System.out.println("No Game servers here O.o");
			// TODO Some fency logout and stuff.
			return;
		}

		for (ServerData data : _servers) {
			System.out
					.printf("Server %d is %d\n", data._serverId, data._status);
		}

	}

}
