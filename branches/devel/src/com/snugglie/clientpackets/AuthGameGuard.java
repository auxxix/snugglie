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
package com.snugglie.clientpackets;

import com.snugglie.SnugglieClient;
import com.snugglie.network.SendablePacket;

/**
 * @author peter.vizi
 * 
 */
public class AuthGameGuard extends SendablePacket<SnugglieClient> {

	@Override
	protected int getHeaderSize() {
		return 2;
	}

	@Override
	protected void write() {
		writeC(0x07);
		writeD(getClient().getSessionId());
		writeD(1);
		writeD(2);
		writeD(3);
		writeD(4);
	}

	@Override
	protected void writeHeader(int dataSize) {
		writeH(dataSize + this.getHeaderSize());
	}

}
