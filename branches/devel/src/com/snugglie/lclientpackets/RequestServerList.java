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
package com.snugglie.lclientpackets;

import com.snugglie.SnugglieClient;
import com.snugglie.lserverpackets.LoginOK;
import com.snugglie.network.SendablePacket;

/**
 * After the server sends a {@link LoginOK} we can request the servers list.
 * 
 * @author peter.vizi
 * 
 */
public class RequestServerList extends SendablePacket<SnugglieClient> {

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
		writeC(0x05);
		writeD(getClient().getLoginOK1());
		writeD(getClient().getLoginOK2());
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
