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

import java.net.InetSocketAddress;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import com.snugglie.SnugglieClient;
import com.snugglie.SnugglieClient.ClientState;
import com.snugglie.network.MMOClient;
import com.snugglie.network.MMOConnection;

/**
 * @author peter.vizi
 * 
 */
public class AccountData implements Comparable<SnugglieClient> {

	public static final String GET_NEW_ID = "SELECT count(id) FROM accounts";

	public static final String ID = "com.unclevader.accountdata";
	public static final String INSERT = "INSERT INTO accounts (id, user, password, server_name, server_ip, server_port) VALUES (?, ?, ?, ?, ?, ?)";

	public static List<AccountData> getAccounts() {
		return ApplicationWorkbenchAdvisor.accounts;
	}

	protected int _id;

	protected boolean _logedin;
	protected String _password;
	protected String _server_ip;
	protected String _server_name;
	protected int _server_port;
	protected SnugglieClient _snugglie;
	protected String _user;

	public AccountData(int id, String user, String password,
			String server_name, String server_ip, int server_port) {
		_id = id;
		_user = user;
		_password = password;
		_server_name = server_name;
		_server_ip = server_ip;
		_server_port = server_port;

		_logedin = false;
	}

	public AccountData(String user, String password, String server_name,
			String server_ip, int server_port) {
		this(0, user, password, server_name, server_ip, server_port);
	}

	@Override
	public int compareTo(SnugglieClient o) {
		if (this.toString().equalsIgnoreCase(o.toString())) {
			return 0;
		}
		return 1;
	}

	public String getIp() {
		return _server_ip;
	}

	public String getPassword() {
		return _password;
	}

	public int getPort() {
		return _server_port;
	}

	public SnugglieClient getSnugglie() {
		return _snugglie;
	}

	public String getUser() {
		return _user;
	}

	public boolean isLogedin() {
		return _logedin;
	}

	public boolean save() {
		boolean result = false;
		try {
			if (_id == 0) {
				Statement stm = ApplicationWorkbenchAdvisor.getConnection()
						.createStatement();
				ResultSet st = stm.executeQuery(GET_NEW_ID);
				if (st.next()) {
					_id = st.getInt(1) + 1;
				}
				stm.close();
			}
			PreparedStatement stm = ApplicationWorkbenchAdvisor.getConnection()
					.prepareStatement(INSERT);
			stm.setInt(1, _id);
			stm.setString(2, _user);
			stm.setString(3, _password);
			stm.setString(4, _server_name);
			stm.setString(5, _server_ip);
			stm.setInt(6, _server_port);
			result = stm.executeUpdate() > 0 ? true : false;
			stm.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return result;
	}

	public void setLogedin(boolean logedin) {
		_logedin = logedin;
		AccountsView.treeViewer.getControl().getDisplay().syncExec(
				new Runnable() {
					@Override
					public void run() {
						AccountsView.treeViewer.refresh();
					}
				});
	}

	public void setPassword(String password) {
		_password = password;
	}

	public void setSnugglie(SnugglieClient snugglie) {
		_snugglie = snugglie;
	}

	public void setUser(String user) {
		_user = user;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return _user + "@" + (new InetSocketAddress(_server_ip, _server_port));
	}

	public void setClient(MMOClient<MMOConnection<SnugglieClient>> client) {
		_snugglie = (SnugglieClient) client;

	}

	public SnugglieClient getClient() {
		return _snugglie;
	}
}
