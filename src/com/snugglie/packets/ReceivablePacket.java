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
package com.snugglie.packets;

import javolution.text.TextBuilder;

/**
 * These packets can be received, eg. there is a ByteBuffer which contains data
 * received from the server. This data can be parsed into a java class.
 * 
 * @author peter.vizi
 * 
 */
public abstract class ReceivablePacket extends AbstractPacket {

	/**
	 * Read the packet from the {@link java.nio.ByteBuffer}.
	 */
	protected abstract void read();

	/**
	 * Read a bytes into an array, the number of bytes read is the length of the
	 * array.
	 * 
	 * @param dst
	 *            destination array
	 */
	protected void readB(byte[] dst) {
		this.getByteBuffer().get(dst);
	}

	/**
	 * Read a character (one byte) from the buffer.
	 * 
	 * @return the next character in the buffer
	 */
	protected int readC() {
		return this.getByteBuffer().get() & 0xFF;
	}

	/**
	 * Read a short (two bytes) from the buffer.
	 * 
	 * @return the next two bytes
	 */
	protected int readH() {
		return this.getByteBuffer().getShort() & 0xFFFF;
	}

	/**
	 * Read an integer (8 bytes) from the buffer.
	 * 
	 * @return the next integer
	 */
	protected int readD() {
		return this.getByteBuffer().getInt();
	}

	/**
	 * Read a long from the buffer.
	 * 
	 * @return the next long
	 */
	protected long readQ() {
		return this.getByteBuffer().getLong();
	}

	/**
	 * Read a double from the buffer.
	 * 
	 * @return the next double
	 */
	protected double readF() {
		return this.getByteBuffer().getDouble();
	}

	/**
	 * Read a null terminated character sequence from the buffer.
	 * 
	 * @return the next string ending with 0
	 */
	protected String readS() {
		TextBuilder tb = TextBuilder.newInstance();
		char ch;

		while ((ch = this.getByteBuffer().getChar()) != 0) {
			tb.append(ch);
		}
		String str = tb.toString();
		TextBuilder.recycle(tb);
		return str;
	}

}
