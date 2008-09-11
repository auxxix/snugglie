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
package com.snugglie.network;

import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;

/**
 * @author KenM
 *
 */
public abstract class UDPHeaderHandler<T extends MMOClient>  extends HeaderHandler<T, UDPHeaderHandler<T>>
{
    /**
     * @param subHeaderHandler
     */
    public UDPHeaderHandler(UDPHeaderHandler<T> subHeaderHandler)
    {
        super(subHeaderHandler);
    }

    private final HeaderInfo<T> _headerInfoReturn = new HeaderInfo<T>();
    
    protected abstract HeaderInfo handleHeader(ByteBuffer buf);
    
    protected abstract void onUDPConnection(SelectorThread<T> selector, DatagramChannel dc, SocketAddress key, ByteBuffer buf);
    
    /**
     * @return the headerInfoReturn
     */
    protected final HeaderInfo<T> getHeaderInfoReturn()
    {
        return _headerInfoReturn;
    }
}
