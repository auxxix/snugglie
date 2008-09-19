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
package com.snugglie.interfaces;

import org.eclipse.core.runtime.IExecutableExtension;

/**
 * This error handler interface should be implemented by plugins which use the
 * com.snugglie.exceptionhandler extension point.
 * 
 * @author peter.vizi
 * 
 */
public interface IExceptionHandler extends IExecutableExtension {

	public void handle(String source, Exception e);

}
