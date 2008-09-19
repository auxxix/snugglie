/**
 * 
 */
package com.snugglie.interfaces;

import com.snugglie.SnugglieClient;
import com.snugglie.network.AbstractPacket;

/**
 * This interface should be implemented by the extenders of
 * com.snugglie.packethandler point.
 * 
 * @author peter.vizi
 * 
 */
public interface IPacketHandler {

	public void handle(AbstractPacket<SnugglieClient> packet);

}
