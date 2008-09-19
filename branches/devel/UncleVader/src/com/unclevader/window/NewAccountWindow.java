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

import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import com.unclevader.AccountData;

/**
 * @author peter.vizi
 * 
 */
public class NewAccountWindow extends TitleAreaDialog {

	protected Text _user;
	protected Text _password;
	protected Text _server_name;
	protected Text _server_ip;
	protected Text _server_port;

	public class NewAccontHelper extends SelectionAdapter {
		public void widgetSelected(SelectionEvent e) {
			String error = "";
			boolean not_ready = false;
			if (_user.getText().length() == 0) {
				error += "You must provide an account name.\n";
				not_ready = true;
			}
			if (_server_name.getText().length() == 0) {
				error += "You must provide a server name.\n";
				not_ready = true;
			}

			if (not_ready) {
				setErrorMessage(error);
			} else {
				AccountData data = new AccountData(_user.getText(), _password
						.getText(), _server_name.getText(), _server_ip
						.getText(), Integer.parseInt(_server_port.getText()));
				boolean ret = data.save();
				if (ret) {
					close();
				} else {
					setErrorMessage("Unable to save into database.");
				}
			}
		}
	}

	public NewAccountWindow(Shell parentShell) {
		super(parentShell);
		// TODO Auto-generated constructor stub
	}

	protected Control createContents(Composite parent) {
		Control contents = super.createContents(parent);
		// Set the title
		setTitle("Create a new account");
		// Set the message
		// setMessage("Provide information about ",
		// IMessageProvider.INFORMATION);
		return contents;
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		// return super.createDialogArea(parent);
		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		parent.setLayout(layout);

		Label userLabel = new Label(parent, SWT.NONE);
		userLabel.setText("User name");
		_user = new Text(parent, SWT.BORDER);

		Label passwordLabel = new Label(parent, SWT.PASSWORD);
		passwordLabel.setText("Password");
		_password = new Text(parent, SWT.BORDER);

		Label server_nameLabel = new Label(parent, SWT.NONE);
		server_nameLabel.setText("Server name");
		_server_name = new Text(parent, SWT.BORDER);

		Label server_ipLabel = new Label(parent, SWT.NONE);
		server_ipLabel.setText("Server ip");
		_server_ip = new Text(parent, SWT.BORDER);

		Label server_portLabel = new Label(parent, SWT.NONE);
		server_portLabel.setText("Server prot");
		_server_port = new Text(parent, SWT.BORDER);

		return parent;
	}

	protected void createButtonsForButtonBar(Composite parent) {
		((GridLayout) parent.getLayout()).numColumns++;
		Button button = new Button(parent, SWT.PUSH);
		button.setText("OK");
		button.setFont(JFaceResources.getDialogFont());
		button.addSelectionListener(new NewAccontHelper());
	}

}
