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

import java.util.Vector;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Plugin;
import org.eclipse.swt.graphics.Color;
import org.eclipse.ui.console.ConsolePlugin;
import org.eclipse.ui.console.IConsole;
import org.eclipse.ui.console.IConsoleFactory;
import org.eclipse.ui.console.MessageConsole;
import org.eclipse.ui.console.MessageConsoleStream;
import org.osgi.framework.BundleContext;

import com.snugglie.interfaces.IExceptionHandler;
import com.snugglie.interfaces.IPacketHandler;
import com.snugglie.network.AbstractPacket;
import com.snugglie.network.ReceivablePacket;

/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends Plugin implements IConsoleFactory {

	// The plug-in ID
	public static final String PLUGIN_ID = "com.snugglie";

	// The shared instance
	private static Activator plugin;

	public static MessageConsole console = new MessageConsole("Snugglie", null);
	public static MessageConsoleStream msgStream = console.newMessageStream();
	public static MessageConsoleStream errStream = console.newMessageStream();

	public static Vector<IExceptionHandler> exceptionHandlers = new Vector<IExceptionHandler>();
	public static Vector<IPacketHandler> packetHandlers = new Vector<IPacketHandler>();

	/**
	 * The constructor
	 */
	public Activator() {
		System.out.println("snugglie plugin");
		errStream.setColor(new Color(ConsolePlugin.getDefault().getWorkbench()
				.getDisplay(), 255, 0, 0));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.core.runtime.Plugins#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
		try {
			plugin = this;
			IExtensionRegistry registry = Platform.getExtensionRegistry();
			IExtensionPoint extensionPoint = registry
					.getExtensionPoint("com.snugglie.exceptionhandler");
			IConfigurationElement[] members = extensionPoint
					.getConfigurationElements();
			for (IConfigurationElement m : members) {
				Object o = m.createExecutableExtension("class");
				if (o instanceof IExceptionHandler) {
					exceptionHandlers.add((IExceptionHandler) o);
				}
			}
			extensionPoint = registry
					.getExtensionPoint("com.snugglie.packethandler");
			members = extensionPoint.getConfigurationElements();
			for (IConfigurationElement m : members) {
				Object o = m.createExecutableExtension("class");
				if (o instanceof IPacketHandler) {
					packetHandlers.add((IPacketHandler) o);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.core.runtime.Plugin#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}

	/**
	 * Returns the shared instance
	 * 
	 * @return the shared instance
	 */
	public static Activator getDefault() {
		return plugin;
	}

	@Override
	public void openConsole() {
		Activator.console.activate();
		ConsolePlugin.getDefault().getConsoleManager().addConsoles(
				new IConsole[] { Activator.console });
	}

	/**
	 * Propagates an @link {@link Exception} to the handlers by calling @link
	 * {@link IExceptionHandler#handle(String, Exception)}.
	 * 
	 * @param string
	 * @param e
	 */
	public static void propagateException(String string, Exception e) {
		for (IExceptionHandler handler : exceptionHandlers) {
			handler.handle(string, e);
		}
	}

	/**
	 * Propagates the packet to the handlers, by calling @link
	 * {@link IPacketHandler#handle(AbstractPacket)}.
	 * 
	 * @param packet
	 *            the packet to be propagated
	 */
	public static void propagatePacket(AbstractPacket<SnugglieClient> packet) {
		for (IPacketHandler h : packetHandlers) {
			h.handle(packet);
		}
	}
}
