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

import java.io.IOException;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPublicKeySpec;
import java.security.spec.X509EncodedKeySpec;

import sun.security.rsa.RSAKeyFactory;
import util.Util;

import net.sf.l2j.gameserver.LoginServerThread.SessionKey;

import com.snugglie.crypt.LoginCrypt;
import com.snugglie.lserverpackets.ServerList.ServerData;
import com.snugglie.network.MMOClient;
import com.snugglie.network.MMOConnection;
import com.snugglie.network.SendablePacket;

/**
 * This is gonna be a client one time.
 * 
 * @author peter.vizi
 * 
 */
public class SnugglieClient extends MMOClient<MMOConnection<SnugglieClient>> {
	public static enum ClientState {
		AUTHED_GG, AUTHED_LOGIN, CONNECTED, AUTH_SUCCESS
	}

	/**
	 * Descramble the received RSA key.
	 * 
	 * @param scrambled
	 */
	public static byte[] deScrambleModulus(byte[] scrambled) {
		// step 4
		for (int i = 0; i < 0x40; i++) {
			scrambled[0x40 + i] = (byte) (scrambled[0x40 + i] ^ scrambled[i]);
		}
		// step 3
		for (int i = 0; i < 4; i++) {
			scrambled[0x0d + i] = (byte) (scrambled[0x0d + i] ^ scrambled[0x34 + i]);
		}
		// step 2
		for (int i = 0; i < 0x40; i++) {
			scrambled[i] = (byte) (scrambled[i] ^ scrambled[0x40 + i]);
		}
		// step 1
		for (int i = 0; i < 4; i++) {
			byte temp = scrambled[0x00 + i];
			scrambled[0x00 + i] = scrambled[0x4d + i];
			scrambled[0x4d + i] = temp;
		}
		byte[] result = new byte[129];
		System.arraycopy(scrambled, 0, result, 1, 128);
		return result;
	}

	private int _accessLevel;
	private String _account;

	private long _connectionStartTime;
	private boolean _joinedGS;
	private int _lastServer;
	private LoginCrypt _loginCrypt;

	private RSAPublicKey _pubKey;
	private int _sessionId;
	private SessionKey _sessionKey;

	protected ClientState _state;
	private boolean _usesInternalIP;
	protected String _user;
	protected String _password;
	private int _loginOK1;
	private int _loginOK2;
	private ServerData _serverData;

	/**
	 * Create a new client, which uses the given connection.
	 * 
	 * @param con
	 *            the network connection object
	 * @param user
	 *            the user's name
	 * @param password
	 *            the user's password as plain text
	 */
	public SnugglieClient(MMOConnection<SnugglieClient> con, String user,
			String password) {
		super(con);
		_connectionStartTime = System.currentTimeMillis();
		_state = ClientState.CONNECTED;
		_loginCrypt = new LoginCrypt();
		_user = user;
		_password = password;
	}

	@Override
	public boolean decrypt(ByteBuffer buf, int size) {
		Util.printArray(System.out, "0x%02x ", buf.array(), buf.position(),
				size);
		boolean ret = false;
		try {
			ret = _loginCrypt.decrypt(buf.array(), buf.position(), size);
		} catch (IOException e) {
			e.printStackTrace();
			closeNow();
			return false;
		}

		if (!ret) {
			System.err.println("Not correct decryption.");
			// closeNow();
		}
		// throw new UnsupportedOperationException();
		return true; // Major Problem!!!

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

	public RSAPublicKey getPublicKey() {
		return this._pubKey;
	}

	public int getSessionId() {
		return this._sessionId;
	}

	/**
	 * Return the state of this client
	 * 
	 * @return
	 */
	public ClientState getState() {
		return _state;
	}

	/**
	 * This client wants to send a packet
	 * 
	 * @param packet
	 */
	public void sendPacket(SendablePacket<SnugglieClient> packet) {
		getConnection().sendPacket(packet);
	}

	public void setBlowfishKey(byte[] key) {
		this._loginCrypt.setKey(key);
	}

	public void setPublicKey(byte[] key) {
		key = deScrambleModulus(key);
		// this._loginCrypt.setKey(key);
		try {
			KeyFactory factory = KeyFactory.getInstance("RSA");
			System.out.print("Descrambled key: ");
			Util.printArray(key);
			byte[] exponent = { (byte) 0x01, (byte) 0x00, (byte) 0x01 };
			BigInteger exponentBI = new BigInteger(exponent);
			System.out.print("Exponent: ");
			Util.printArray(exponentBI.toByteArray());
			RSAPublicKeySpec spec = new RSAPublicKeySpec(new BigInteger(key),
					exponentBI);
			// X509EncodedKeySpec spec = new X509EncodedKeySpec(key);
			this._pubKey = (RSAPublicKey) factory.generatePublic(spec);
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidKeySpecException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void setSessionId(int id) {
		this._sessionId = id;
	}

	public void setState(ClientState newstate) {
		this._state = newstate;
	}

	/**
	 * Return the user name.
	 * 
	 * @return
	 */
	public String getUser() {
		return this._user;
	}

	/**
	 * Return the password.
	 * 
	 * @return
	 */
	public String getPasssword() {
		return this._password;
	}

	/**
	 * Return the loginOK1 value sent by the server.
	 * 
	 * @return
	 */
	public int getLoginOK1() {
		return _loginOK1;
	}

	public void setLoginOK1(int value) {
		_loginOK1 = value;
	}

	/**
	 * Return the loginOK2 value sent by the server.
	 * 
	 * @return
	 */
	public int getLoginOK2() {
		return _loginOK2;
	}

	public void setLoginOK2(int value) {
		_loginOK2 = value;
	}

	public void setServerData(ServerData data) {
		_serverData = data;
	}

	public ServerData getServerData() {
		return _serverData;
	}
};
