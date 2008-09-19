// $$Id$$
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

import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Color;
import org.eclipse.ui.application.IWorkbenchConfigurer;
import org.eclipse.ui.application.IWorkbenchWindowConfigurer;
import org.eclipse.ui.application.WorkbenchAdvisor;
import org.eclipse.ui.application.WorkbenchWindowAdvisor;
import org.eclipse.ui.console.ConsolePlugin;

public class ApplicationWorkbenchAdvisor extends WorkbenchAdvisor {

	public static String SELECT_ACCOUNTS = "SELECT id, user, password, server_name, server_ip, server_port FROM accounts";

	public static String INIT_DATABASE = "CREATE TABLE accounts ( id INTEGER NOT NULL, user TEXT, password TEXT, server_name TEXT, server_ip TEXT, server_port INTEGER );";

	private static Connection conn;

	protected static Vector<AccountData> accounts = new Vector<AccountData>();

	public static Connection getConnection() throws ClassNotFoundException,
			SQLException {
		if (conn == null) {
			Class.forName("org.sqlite.JDBC");
			conn = DriverManager.getConnection("jdbc:sqlite:data.db");
		}
		return conn;
	}

	public static void initDatabase() throws ClassNotFoundException,
			SQLException {
		Statement st = getConnection().createStatement();
		st.execute(INIT_DATABASE);
		st.clearBatch();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.application.WorkbenchAdvisor#initialize(org.eclipse.ui
	 * .application.IWorkbenchConfigurer)
	 */
	@Override
	public void initialize(IWorkbenchConfigurer configurer) {
		super.initialize(configurer);

		try {
			Statement stat = getConnection().createStatement();
			ResultSet rs = null;
			try {
				rs = stat.executeQuery(SELECT_ACCOUNTS);
			} catch (SQLException e) {
				// creating database
				System.out.println("Initializing database.");
				initDatabase();
				rs = stat.executeQuery(SELECT_ACCOUNTS);
			}
			while (rs.next()) {
				accounts.add(new AccountData(rs.getInt("id"), rs
						.getString("user"), rs.getString("password"), rs
						.getString("server_name"), rs.getString("server_ip"),
						rs.getInt("server_port")));
			}
			stat.close();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		if (conn == null) {
			System.err.println("Not good!");
		}
		System.out.println("Init done.");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.application.WorkbenchAdvisor#postStartup()
	 */
	@Override
	public void postStartup() {

		super.postStartup();
		Activator.errStream.setColor(new Color(ConsolePlugin.getDefault()
				.getWorkbench().getDisplay(), 255, 0, 0));
	}

	public WorkbenchWindowAdvisor createWorkbenchWindowAdvisor(
			IWorkbenchWindowConfigurer configurer) {
		return new ApplicationWorkbenchWindowAdvisor(configurer);
	}

	public String getInitialWindowPerspectiveId() {
		return Perspective.ID;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.application.WorkbenchAdvisor#postShutdown()
	 */
	@Override
	public void postShutdown() {
		super.postShutdown();
		try {
			getConnection().close();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

}
