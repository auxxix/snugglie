// $Id$
/**
 * This file is part of UncleVader.
 *
 * UncleVader is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * UncleVader is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with UncleVader.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.unclevader;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.console.MessageConsole;
import org.eclipse.ui.dialogs.ListDialog;
import org.eclipse.ui.dialogs.SelectionDialog;

import com.snugglie.SnugglieClient;
import com.snugglie.SnugglieClient.ClientState;
import com.snugglie.interfaces.IPacketHandler;
import com.snugglie.network.AbstractPacket;
import com.snugglie.lserverpackets.LoginOK;
import com.snugglie.lserverpackets.ServerList;
import com.snugglie.lserverpackets.ServerList.ServerData;
import com.unclevader.window.ServerListWindow;

/**
 * @author peter.vizi
 * 
 */
public class PacketHandler implements IPacketHandler {

	/**
	 * 
	 */
	public PacketHandler() {
		// TODO Auto-generated constructor stub
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.snugglie.interfaces.IPacketHandler#handle(com.snugglie.network.
	 * AbstractPacket)
	 */
	@Override
	public void handle(AbstractPacket<SnugglieClient> packet) {
		Activator.msgStream.println("Handling packet " + packet);
		ClientState state = packet.getClient().getState();
		AccountData data = null;
		for (AccountData d : AccountData.getAccounts()) {
			if (packet.getClient().equals(d.getClient())) {
				data = d;
			}
		}
		switch (state) {
		case AUTH_SUCCESS:
			if (packet instanceof LoginOK) {
				data.setLogedin(true);
				Activator.msgStream.println("We are logged in.");
			} else if (packet instanceof ServerList) {
				ServerList pp = (ServerList) packet;
				Activator.msgStream.println("Listing servers");
				if (pp.getServers().size() > 0) {
					Activator.msgStream
							.println("pop up window with server list.");

					ServerListWindow.ServerListWindowHelper helper = new ServerListWindow.ServerListWindowHelper(
							pp);
					Display.getDefault().syncExec(helper);
					Object[] selected = helper.getSelectedElements();
					if (selected[0] instanceof ServerData) {
						pp.getClient().setServerData((ServerData) selected[0]);
					} else {
						System.err.println("Wat da heck did u select?");
					}
					Activator.msgStream.println(selected.toString());

				} else {
					MessageDialog.openError(Display.getCurrent()
							.getActiveShell(), "No servers",
							"There are no game servers on this login server.");
				}
			} else {
				Activator.msgStream.println("Unhandled packet: "
						+ packet.getClass() + " in state " + state.name());
			}
			break;
		default:
			Activator.msgStream.println("Unhandled packet: "
					+ packet.getClass() + " in state " + state.name());
			break;
		}

	}
}
