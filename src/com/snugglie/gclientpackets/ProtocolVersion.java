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
package com.snugglie.gclientpackets;

import com.snugglie.SnugglieClient;
import com.snugglie.network.SendablePacket;

/**
 * @author pvizi
 * 
 */
public class ProtocolVersion extends SendablePacket<SnugglieClient> {

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.snugglie.network.SendablePacket#getHeaderSize()
	 */
	@Override
	protected int getHeaderSize() {
		return 2;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.snugglie.network.SendablePacket#write()
	 */
	@Override
	protected void write() {
		writeD(851); // TODO this should be always the current protocol version
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.snugglie.network.SendablePacket#writeHeader(int)
	 */
	@Override
	protected void writeHeader(int dataSize) {
		writeH(dataSize + this.getHeaderSize());
	}

}
