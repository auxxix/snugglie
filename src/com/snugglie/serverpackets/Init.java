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
package com.snugglie.serverpackets;

import java.io.IOException;
import java.nio.ByteBuffer;

import util.Util;

import com.snugglie.crypt.LoginCrypt;
import com.snugglie.exception.WrongDataException;
import com.snugglie.packets.ReceivablePacket;

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
public class Init extends ReceivablePacket {

	/**
	 * Blowfish key of length 16.
	 */
	protected byte[] _blowfishKey;

	/**
	 * Used for decrypting.
	 */
	protected LoginCrypt _lic;

	/**
	 * The ID of Init packets should be 0.
	 */
	private byte _packetId;

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
	protected int _size;

	public Init(ByteBuffer bbuf) {
		this.setByteBuffer(bbuf);
		_lic = new LoginCrypt();
		_publicKey = new byte[128];
		_blowfishKey = new byte[16];
		read();
	}

	/**
	 * Return the packet id of Init packet, it should be 0.
	 * 
	 * @return the _packetId
	 */
	public byte getPacketId() {
		return _packetId;
	}

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
		return _size;
	}

	@Override
	protected void read() {
		// System.out.print("received:   ");
		_size = this.readH();
		// Util.printArray(System.out, "0x%02x ", this.getByteBuffer().array(),
		// 0,
		// _size);
		try {
			// System.out.print("before dec: ");
			// Util.printArray(System.out, "0x%02x ",
			// this.getByteBuffer().array(), 0, _size);
			_lic.decrypt(this.getByteBuffer().array(), 2, _size - 2);
			// System.out.print("before xor: ");
			// Util.printArray(System.out, "0x%02x ",
			// this.getByteBuffer().array(), 0, _size);
			LoginCrypt.decXORPass(this.getByteBuffer().array(), 2, _size - 2);
			System.out.print("decrypterd: ");
			Util.printArray(System.out, "0x%02x ",
					this.getByteBuffer().array(), 0, _size);

		} catch (IOException e) {
			e.printStackTrace();
		}
		_packetId = (byte) this.readC();
		_sessionId = this.readD();
		_protocolRev = this.readD();
		try {
			if (_protocolRev != 0x0000c621) {
				System.out.printf("0x%x\n", _protocolRev);
				throw new WrongDataException();
			}
		} catch (WrongDataException e) {
			e.printStackTrace();
		}

		readB(_publicKey);
		readD();
		readD();
		readD();
		readD();
		readB(_blowfishKey);

	}

}
