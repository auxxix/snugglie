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

import java.net.InetAddress;
import java.net.InetSocketAddress;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.TreePath;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;

import com.snugglie.Snugglie;

/**
 * @author peter.vizi
 * 
 */
public class AccountsView extends ViewPart {

	/**
	 * This is just an empty class representing the root of the tree
	 * 
	 * @author peter.vizi
	 * 
	 */
	public static class AccountsRoot {

	}

	/**
	 * This is the content provider for the accounts tree view.
	 * 
	 * @author peter.vizi
	 * 
	 */
	public static class ContentProvider implements ITreeContentProvider {

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.jface.viewers.IContentProvider#dispose()
		 */
		@Override
		public void dispose() {
			// TODO Auto-generated method stub

		}

		public Object[] getChildren(Object parent) {
			if (parent instanceof AccountsView.AccountsRoot) {
				return ApplicationWorkbenchAdvisor.accounts.toArray();
			} else if (parent instanceof AccountData) {
				return toArray((AccountData) parent);
			} else {
				return new Object[0];
			}
		}

		public Object[] getElements(Object inputElement) {
			return getChildren(inputElement);
		}

		@Override
		public Object getParent(Object element) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public boolean hasChildren(Object element) {
			return getChildren(element).length > 0;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * org.eclipse.jface.viewers.IContentProvider#inputChanged(org.eclipse
		 * .jface.viewers.Viewer, java.lang.Object, java.lang.Object)
		 */
		@Override
		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
			// TODO Auto-generated method stub

		}

		private Object[] toArray(AccountData parent) {
			if (parent.isLogedin()) {
				System.out
						.println("The account is loged in we should list the characters");
			}
			return new Object[0];
		}
	}

	/**
	 * @author pvizi
	 * 
	 */
	public static class DoubleClickListener implements IDoubleClickListener {

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * org.eclipse.jface.viewers.IDoubleClickListener#doubleClick(org.eclipse
		 * .jface.viewers.DoubleClickEvent)
		 */
		@Override
		public void doubleClick(DoubleClickEvent event) {
			if (event.getSelection() instanceof TreeSelection) {
				TreeSelection tselect = (TreeSelection) event.getSelection();
				TreePath[] tpath = (TreePath[]) tselect.getPaths();
				if (tpath.length == 1) {
					TreePath path = tpath[0];
					if (path.getSegmentCount() == 1) {
						if (path.getFirstSegment() instanceof AccountData) {
							AccountData data = (AccountData) path
									.getFirstSegment();
							Snugglie snugglie = new Snugglie(data.getUser(),
									data.getPassword(), new InetSocketAddress(
											data.getIp(), data.getPort()));
							data.setClient(snugglie.client);
						}
					}
				}
			} else {
				throw new UnsupportedOperationException();
			}
		}
	}

	/**
	 * @author peter.vizi
	 * 
	 */
	public static class LabelProvider implements ILabelProvider {

		@Override
		public void addListener(ILabelProviderListener listener) {
			// TODO Auto-generated method stub

		}

		@Override
		public void dispose() {
			// TODO Auto-generated method stub

		}

		@Override
		public Image getImage(Object element) {
			Activator.msgStream.println("getting accounts image");
			ImageDescriptor descriptor = ImageDescriptor
					.getMissingImageDescriptor();
			if (element instanceof AccountData) {
				if (((AccountData) element).isLogedin()) {
					descriptor = Activator
							.getImageDescriptor("icons/server_connect.png");
					Activator.msgStream.println("Return icons/server_connect.png");
				} else {
					descriptor = Activator
							.getImageDescriptor("icons/server.png");
					Activator.msgStream.println("Return icons/server.png");
				}
			}
			Image image = Activator.getImage(descriptor);
			return image;
		}

		@Override
		public String getText(Object element) {
			Activator.msgStream.println("getting accounts text");
			if (element instanceof AccountData) {
				AccountData data = (AccountData) element;
				return data._user + "@" + data._server_name;
			}
			return "no label";
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

	public static final String ID = "com.unclevader.AccountsView";
	public static TreeViewer treeViewer;

	@Override
	public void createPartControl(Composite parent) {
		System.out.println("Creating accoutns view");
		treeViewer = new TreeViewer(parent, SWT.MULTI | SWT.H_SCROLL
				| SWT.V_SCROLL);
		treeViewer.setContentProvider(new AccountsView.ContentProvider());
		treeViewer.setLabelProvider(new AccountsView.LabelProvider());
		treeViewer.setInput(new AccountsView.AccountsRoot());
		treeViewer
				.addDoubleClickListener(new AccountsView.DoubleClickListener());
	}

	@Override
	public void setFocus() {
		treeViewer.getControl().setFocus();
	}

}
