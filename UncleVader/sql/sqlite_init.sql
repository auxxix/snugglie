-- $Id$

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

/**
 * @file
 * This file defines the database tables used by the program.
 * @author peter.vizi
 */

/**
 * This table is used for storing the account data
 *
 * @author peter.vizi
 */
CREATE TABLE accounts (
       id INTEGER NOT NULL,
       user TEXT,
       password TEXT,
       server_name TEXT,
       server_ip TEXT,	
       server_port INTEGER
);
