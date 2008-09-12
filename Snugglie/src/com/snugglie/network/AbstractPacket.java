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

import java.nio.ByteBuffer;


/**
 * @author KenM
 *
 */
public abstract class AbstractPacket<T extends MMOClient>
{
    protected ByteBuffer _buf;
    
    protected T _client;
    
    protected void setClient(T client)
    {
        _client = client;
    }
    
    public T getClient()
    {
        return _client;
    }
    
    protected void setByteBuffer(ByteBuffer buf)
    {
        _buf = buf;
    }
    
    protected ByteBuffer getByteBuffer()
    {
        return _buf;
    }
}
