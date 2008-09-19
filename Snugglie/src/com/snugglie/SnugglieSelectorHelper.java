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
package com.snugglie;

import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.snugglie.SnugglieClient.ClientState;
import com.snugglie.lserverpackets.GGauth;
import com.snugglie.lserverpackets.Init;
import com.snugglie.lserverpackets.LoginFail;
import com.snugglie.lserverpackets.LoginOK;
import com.snugglie.lserverpackets.PlayFail;
import com.snugglie.lserverpackets.PlayOk;
import com.snugglie.lserverpackets.ServerList;
import com.snugglie.network.HeaderInfo;
import com.snugglie.network.IClientFactory;
import com.snugglie.network.IMMOExecutor;
import com.snugglie.network.IPacketHandler;
import com.snugglie.network.MMOConnection;
import com.snugglie.network.ReceivablePacket;
import com.snugglie.network.TCPHeaderHandler;

/**
 * @author peter.vizi
 * 
 */
public class SnugglieSelectorHelper extends TCPHeaderHandler<SnugglieClient>
		implements IPacketHandler<SnugglieClient>,
		IMMOExecutor<SnugglieClient>, IClientFactory<SnugglieClient> {

	protected ThreadPoolExecutor _packetThreadPool;

	protected String _password;
	protected String _user;

	public SnugglieClient _client;

	public SnugglieSelectorHelper(String user, String password) {
		super(null);
		_packetThreadPool = new ThreadPoolExecutor(4, 6, 15L, TimeUnit.SECONDS,
				new LinkedBlockingQueue<Runnable>());
		_user = user;
		_password = password;
	}

	@Override
	public SnugglieClient create(MMOConnection<SnugglieClient> con) {
		// return new SnugglieClient(con, getUser(), getPassword());
		if (_client == null) {
			_client = new SnugglieClient(con, getUser(), getPassword());
		}
		return _client;
	}

	protected void debugOpcode(int opcode, ClientState state) {
		System.err.printf("Unknown opcode 0x%x in state %s.\n", opcode, state
				.name());

	}

	@Override
	public void execute(ReceivablePacket<SnugglieClient> packet) {
		System.out.println("Executing packet" + packet);
		_packetThreadPool.execute(packet);
	}

	/**
	 * Return the user's password as plain text.
	 * 
	 * @return
	 */
	private String getPassword() {
		return _password;
	}

	/**
	 * Return the user's name.
	 * 
	 * @return
	 */
	private String getUser() {
		return _user;
	}

	/**
	 * @see net.sf.l2j.loginserver.SelectorHelper
	 */
	@Override
	public HeaderInfo<?> handleHeader(SelectionKey key, ByteBuffer buf) {
		System.out.println("Handling header.");
		if (buf.remaining() >= 2) {
			int dataPending = (buf.getShort() & 0xffff) - 2;
			SnugglieClient client = ((MMOConnection<SnugglieClient>) key
					.attachment()).getClient();
			System.out.println("elso");
			return this.getHeaderInfoReturn()
					.set(0, dataPending, false, client);
		} else {
			SnugglieClient client = ((MMOConnection<SnugglieClient>) key
					.attachment()).getClient();
			return this.getHeaderInfoReturn().set(2 - buf.remaining(), 0,
					false, client);
		}
	}

	/**
	 * @see net.sf.l2j.loginserver.L2LoginPacketHandler
	 */
	@Override
	public ReceivablePacket<SnugglieClient> handlePacket(ByteBuffer buf,
			SnugglieClient client) {
		System.out.println("Handling packet");
		int opcode = buf.get() & 0xFF;

		ReceivablePacket<SnugglieClient> packet = null;
		ClientState state = client.getState();

		System.out.println("Opcode: " + Integer.toHexString(opcode));
		System.out.println("State: " + state.name());
		switch (state) {
		case CONNECTED:
			if (opcode == 0x00) {
				// We have just connected to the server, we are supposed to get
				// an Init packet.
				packet = new Init();
			} else if (opcode == 0xb) {
				packet = new GGauth();
			} else {
				debugOpcode(opcode, state);
			}
			break;
		case AUTHED_GG:
			if (opcode == 0x03) { // LoginOK
				packet = new LoginOK();
			} else if (opcode == 0x04) { // ServerLost
				packet = new ServerList();
			} else if (opcode == 0x01) { // wrong password
				packet = new LoginFail();
			} else {
				debugOpcode(opcode, state);
			}
			break;
		case AUTH_SUCCESS:
			if (opcode == 0x04) { // we got a ServerList packet
				packet = new ServerList();
			} else if (opcode == 0x06) { // PlayFail
				packet = new PlayFail();
			} else if (opcode == 0x07) { // PlayOK
				packet = new PlayOk();
			} else {
				debugOpcode(opcode, state);
			}
			break;
		case AUTHED_LOGIN:
			// if (opcode == 0x05)
			// {
			// packet = new RequestServerList();
			// }
			// else if (opcode == 0x02)
			// {
			// packet = new RequestServerLogin();
			// }
			// else
			// { {
			debugOpcode(opcode, state);
			// }
			break;
		default:
			debugOpcode(opcode, state);
			break;
		}
		return packet;
	}
}
