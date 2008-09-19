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
package com.snugglie;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;

import com.snugglie.network.MMOClient;
import com.snugglie.network.MMOConnection;
import com.snugglie.network.SelectorConfig;
import com.snugglie.network.SelectorThread;

/**
 * Run this program to see what's gonna happen.
 * 
 * @author peter.vizi
 * 
 */
public class Snugglie {

	/**
	 * Main function, receive an Init packet and parse it.
	 * 
	 * @param args
	 *            none yet
	 */
	public static void main(String[] args) {
		Snugglie instance = new Snugglie("bela", "alma", new InetSocketAddress(
				"127.0.0.1", 2106));
		// SnugglieSelectorHelper sh = new SnugglieSelectorHelper("bela",
		// "alma");
		// SelectorConfig<SnugglieClient> ssc = new
		// SelectorConfig<SnugglieClient>(
		// null, null, sh, sh);
		// try {
		// SelectorThread<SnugglieClient> selector = SelectorThread
		// .getInstance(ssc, sh, sh, null);
		// selector.openSocket(new InetSocketAddress("127.0.0.1", 2106));
		// selector.start();
		// } catch (IOException e) {
		// e.printStackTrace();
		// }
	}

	protected String _user;
	protected InetSocketAddress _address;
	public MMOClient<MMOConnection<SnugglieClient>> client;

	public Snugglie(String user, String password, InetSocketAddress address) {
		_user = user;
		_address = address;
		SnugglieSelectorHelper sh = new SnugglieSelectorHelper(user, password);
		SelectorConfig<SnugglieClient> ssc = new SelectorConfig<SnugglieClient>(
				null, null, sh, sh);

		try {
			SelectorThread<SnugglieClient> selector = SelectorThread
					.getInstance(ssc, sh, sh, null);
			client = selector.openSocket(address);
			selector.start();

		} catch (IOException e) {
			Activator.msgStream.println(this + " " + e.getLocalizedMessage());
			Activator.propagateException("Snugglie", e);
		}
	}

	public String toString() {
		return _user + "@" + _address.getHostName();
	}
}
