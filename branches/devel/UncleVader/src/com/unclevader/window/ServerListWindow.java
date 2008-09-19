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
package com.unclevader.window;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.dialogs.ListSelectionDialog;

import com.snugglie.lserverpackets.ServerList;
import com.snugglie.lserverpackets.ServerList.ServerData;
import com.unclevader.Activator;

/**
 * @author peter.vizi
 * 
 */
public class ServerListWindow extends ListSelectionDialog {

	public ServerListWindow(Shell parentShell, Object input,
			IStructuredContentProvider contentProvider,
			ILabelProvider labelProvider, String message) {
		super(parentShell, input, contentProvider, labelProvider, message);
	}

	public static class ServerListWindowHelper implements Runnable,
			IStructuredContentProvider, ILabelProvider {
		ServerList _serverlist;
		Object[] _selected;
		ServerListWindow _win;

		public Object[] getSelectedElements() {
			return _win.getResult();
		}

		public ServerListWindowHelper(ServerList pp) {
			_serverlist = pp;
		}

		@Override
		public void run() {
			System.err.println("csinaljunk egy ablakot");
			_win = new ServerListWindow(Display.getDefault().getActiveShell(),
					_serverlist, this, this, "Select a server");
			_win.setBlockOnOpen(true);
			do {

			} while (_win.open() != Dialog.OK);
			System.out.println("Visszatertunk.");
		}

		@Override
		public Object[] getElements(Object inputElement) {
			if (inputElement.equals(_serverlist)) {
				return _serverlist.getServers().toArray();
			}
			return null;
		}

		@Override
		public void dispose() {
			// TODO Auto-generated method stub

		}

		@Override
		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
			// TODO Auto-generated method stub

		}

		@Override
		public Image getImage(Object element) {
			ImageDescriptor descriptor = ImageDescriptor
					.getMissingImageDescriptor();
			if (element instanceof ServerData) {
				ServerData data = (ServerData) element;
				if (data.getStatus()) {
					descriptor = Activator
							.getImageDescriptor("icons/connect.png");
				} else {
					descriptor = Activator
							.getImageDescriptor("icons/disconnect.png");
				}
			}
			return Activator.getImage(descriptor);
		}

		@Override
		public String getText(Object element) {
			if (element instanceof ServerData) {
				ServerData data = (ServerData) element;
				return ServerList.ServerName.values()[data.getServerId()]
						.name();
			}
			return null;
		}

		@Override
		public void addListener(ILabelProviderListener listener) {
			// TODO Auto-generated method stub

		}

		@Override
		public boolean isLabelProperty(Object element, String property) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public void removeListener(ILabelProviderListener listener) {
			// TODO Auto-generated method stub

		}

	}

}
