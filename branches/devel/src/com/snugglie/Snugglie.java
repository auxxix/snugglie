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
package com.snugglie;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.SocketChannel;

import util.Util;

import com.snugglie.serverpackets.Init;

/**
 * Run this program to see what's gonna happen.
 * 
 * @author peter.vizi
 * 
 */
public class Snugglie {

	static int BUFFER_SIZE = 64 * 1024;

	/**
	 * Main function, receive an Init packet and parse it.
	 * 
	 * @param args
	 *            none yet
	 */
	public static void main(String[] args) {
		try {
			SocketChannel sch = SocketChannel.open(new InetSocketAddress(
					"127.0.0.1", 2106));

			ByteBuffer bb = ByteBuffer.wrap(new byte[Snugglie.BUFFER_SIZE]);
			bb.order(ByteOrder.LITTLE_ENDIAN);
			sch.read(bb);

			bb.flip();

			Init ini = new Init(bb);

			System.out.println("Size: " + ini.getSize());
			System.out.printf("Session id: 0x%x\n", ini.getSessionId());
			System.out.print("Public key: ");
			Util.printArray(ini.getPublicKey());
			System.out.print("Bfish key: ");
			Util.printArray(ini.getBlowfishKey());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
