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

import java.io.IOException;
import java.nio.ByteBuffer;

import util.Util;

import com.snugglie.SnugglieClient;
import com.snugglie.clientpackets.AuthGameGuard;
import com.snugglie.crypt.LoginCrypt;
import com.snugglie.exception.WrongDataException;
import com.snugglie.network.MMOConnection;
import com.snugglie.network.ReceivablePacket;

/**
 * Init packet, that is sent by the server for the first time the client
 * connects.
 * 
 * <p>
 * This packet is XOR encoded with a random integer, and then encrypted with a
 * static key, that can be found in
 * {@link net.sf.l2j.loginserver.crypt.NewCrypt}. It contains a Blowfish key,
 * and an RSA key that will be used further more, and also the session id of
 * this client.
 * 
 * @author peter.vizi
 * 
 */
public class Init extends ReceivablePacket<SnugglieClient> {

	/**
	 * Blowfish key of length 16.
	 */
	protected byte[] _blowfishKey;

	/**
	 * Used for decrypting.
	 */
	protected LoginCrypt _lic;

	/**
	 * Protocol revision.
	 */
	protected int _protocolRev;

	/**
	 * RSA public key of length 128.
	 */
	protected byte[] _publicKey;

	/**
	 * This clients new session id.
	 */
	protected int _sessionId;

	/**
	 * Two first bytes of the packet.
	 */
	// protected int _size;
	//
	// public Init(ByteBuffer bbuf) {
	// this.setByteBuffer(bbuf);
	// _lic = new LoginCrypt();
	// _publicKey = new byte[128];
	// _blowfishKey = new byte[16];
	// read();
	// }
	/**
	 * Return the RSA public key, used for encription.
	 * 
	 * @return the _publicKey
	 */
	public byte[] getPublicKey() {
		return _publicKey;
	}

	/**
	 * Return the blowfish key.
	 * 
	 * @return the _blowfishKey
	 */
	public byte[] getBlowfishKey() {
		return _blowfishKey;
	}

	/**
	 * Return the protocol rev.
	 * 
	 * @return the _protocolRev
	 */
	public int getProtocolRev() {
		return _protocolRev;
	}

	/**
	 * Return this clients session id.
	 * 
	 * @return the _sessionId
	 */
	public int getSessionId() {
		return _sessionId;
	}

	/**
	 * Return the received packet's size.
	 * 
	 * @return should be 186
	 */
	public int getSize() {
		return 186;
	}

	@Override
	protected boolean read() {
		// System.out.print("received:   ");
		// _size = this.readH();
		// Util.printArray(System.out, "0x%02x ", this.getByteBuffer().array(),
		// 0,
		// _size);
		// System.out.print("before dec: ");
		// Util.printArray(System.out, "0x%02x ",
		// this.getByteBuffer().array(), 0, _size);
		// _lic.decrypt(this.getByteBuffer().array(), 2, _size - 2);
		// System.out.print("before xor: ");
		// Util.printArray(System.out, "0x%02x ",
		// this.getByteBuffer().array(), 0, _size);
		LoginCrypt.decXORPass(this.getByteBuffer().array(), 2, getSize() - 2);
		System.out.print("xor decoded: ");
		Util.printArray(System.out, "0x%02x ", this.getByteBuffer().array(), 0,
				getSize());
		_sessionId = this.readD();
		System.out.printf("session id 0x%x\n", _sessionId);
		_client.setSessionId(_sessionId);
		_protocolRev = this.readD();
		System.out.printf("protocol rev 0x%x\n", _protocolRev);
		// try {
		if (_protocolRev != 0x0000c621) {
			System.out.printf("0x%x\n", _protocolRev);
			// throw new WrongDataException();
		}
		// } catch (WrongDataException e) {
		// e.printStackTrace();
		// }

		_publicKey = new byte[128];
		readB(_publicKey);
		System.out.println("Public: ");
		Util.printArray(System.out, "0x%02x ", _publicKey);
		readD();
		readD();
		readD();
		readD();
		_blowfishKey = new byte[16];
		readB(_blowfishKey);
		System.out.print("Bfish: ");
		Util.printArray(System.out, "0x%02x ", _blowfishKey);
		return true;
	}

	@Override
	public void run() {
		_client.setPublicKey(_publicKey);
		_client.setBlowfishKey(_blowfishKey);
		_client.sendPacket(new AuthGameGuard());
	}

}
