/**
 *
 */
package eu.learnpad.simulator.uihandler.webserver.msg.task.send;

/*
 * #%L
 * LearnPAd Simulator
 * %%
 * Copyright (C) 2014 - 2015 Linagora
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * #L%
 */

import java.util.Collection;

import eu.learnpad.simulator.datastructures.document.LearnPadDocument;
import eu.learnpad.simulator.uihandler.webserver.msg.task.ITaskMsg;

/**
 * @author Tom Jorquera - Linagora
 *
 */
public class TaskDesc implements ITaskMsg {

	public String name;
	public String description;
	public String processid;
	public String processname;
	public String form;
	public Collection<LearnPadDocument> documents;

	/**
	 * @param description
	 * @param processd
	 * @param form
	 */
	public TaskDesc(String name, String description, String processid,
			String processname, String form,
			Collection<LearnPadDocument> documents) {
		super();
		this.name = name;
		this.description = description;
		this.processid = processid;
		this.processname = processname;
		this.form = form;
		this.documents = documents;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see activitipoc.uihandler.webserver.msg.task.ITaskMsg#getType()
	 */
	public TYPE getType() {
		return TYPE.TASKDESC;
	}

}
