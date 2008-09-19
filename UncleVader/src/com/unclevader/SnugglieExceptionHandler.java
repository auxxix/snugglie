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

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;

import com.snugglie.interfaces.IExceptionHandler;

/**
 * @author peter.vizi
 * 
 */
public class SnugglieExceptionHandler implements IExceptionHandler {

	public static final String ID = "com.unclevader.SnugglieExceptionHandler";

	/**
	 * 
	 */
	public SnugglieExceptionHandler() {
		// TODO Auto-generated constructor stub
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.snugglie.interfaces.IExceptionHandler#handle(java.lang.String,
	 * java.lang.Exception)
	 */
	@Override
	public void handle(String source, Exception e) {
		MessageDialog.openError(Display.getCurrent().getActiveShell(), source,
				e.getMessage());
	}

	@Override
	public void setInitializationData(IConfigurationElement config,
			String propertyName, Object data) throws CoreException {

	}

}
