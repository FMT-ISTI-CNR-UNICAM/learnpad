/*
 * See the NOTICE file distributed with this work for additional
 * information regarding copyright ownership.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package eu.learnpad.core.impl.cw;

import org.xwiki.component.annotation.Component;
import org.xwiki.rest.XWikiRestComponent;

import eu.learnpad.exception.LpRestException;
import eu.learnpad.exception.impl.LpRestExceptionImpl;
import eu.learnpad.cw.BridgeInterface;
import eu.learnpad.cw.rest.data.Feedbacks;
import eu.learnpad.core.rest.RestResource;

@Component
public class XwikiBridgeInterfaceRestResource extends RestResource implements
		XWikiRestComponent, BridgeInterface {

	@Override
	public byte[] getComments(String modelSetId, String artifactId)
			throws LpRestExceptionImpl {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public byte[] getResource(String modelSetId, String resourceId,
			String artifactIds, String action) throws LpRestExceptionImpl {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void modelSetImported(String modelSetId, String type) throws LpRestExceptionImpl {
		// TODO Auto-generated method stub

	}

	@Override
	public void contentVerified(String modelSetId, String artifactId,
			String resourceId, String result) throws LpRestExceptionImpl {
		// TODO Auto-generated method stub

	}

	@Override
	public void modelVerified(String modelSetId, String result)
			throws LpRestExceptionImpl {
		// TODO Auto-generated method stub
	}

	@Override
	public Feedbacks getFeedbacks(String modelSetId) throws LpRestException {
		// TODO Auto-generated method stub
		return null;
	}

}
