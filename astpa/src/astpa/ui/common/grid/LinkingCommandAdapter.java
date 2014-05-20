/*******************************************************************************
 * Copyright (c) 2013 A-STPA Stupro Team Uni Stuttgart (Lukas Balzer, Adam
 * Grahovac, Jarkko Heidenwag, Benedikt Markt, Jaqueline Patzek, Sebastian
 * Sieber, Fabian Toth, Patrick Wickenhäuser, Aliaksei Babkovich, Aleksander
 * Zotov).
 * 
 * All rights reserved. This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License v1.0 which
 * accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 *******************************************************************************/

package astpa.ui.common.grid;

import org.eclipse.jface.fieldassist.ContentProposalAdapter;
import org.eclipse.jface.fieldassist.IContentProposalProvider;
import org.eclipse.jface.fieldassist.IControlContentAdapter;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.fieldassist.ContentAssistCommandAdapter;

/**
 * Adapter for accessing the openPopup() and closePopup() methods of
 * ContentProposalAdapter
 * 
 * @author Benedikt Markt
 * 
 */
public class LinkingCommandAdapter extends ContentAssistCommandAdapter {
	
	
	/**
	 * 
	 * @author Benedikt Markt
	 * 
	 * @param control The control to apply autocomplete to
	 * @param controlContentAdapter the content adapter
	 * @param proposalProvider the proposal provider
	 * @param commandId the command id
	 * @param autoActivationCharacters characters that should trigger the popup
	 *            list
	 * @param installDecoration wether or not the popup should be decorated
	 */
	public LinkingCommandAdapter(Control control, IControlContentAdapter controlContentAdapter,
		IContentProposalProvider proposalProvider, String commandId, char[] autoActivationCharacters,
		boolean installDecoration) {
		super(control, controlContentAdapter, proposalProvider, commandId, autoActivationCharacters, installDecoration);
		
		this.setProposalAcceptanceStyle(ContentProposalAdapter.PROPOSAL_INSERT);
		this.setFilterStyle(ContentProposalAdapter.FILTER_NONE);
		this.setAutoActivationCharacters(null);
		this.setAutoActivationDelay(0);
		this.setPropagateKeys(false);
		
	}
	
	@Override
	public void closeProposalPopup() {
		super.closeProposalPopup();
		
	}
	
	@Override
	public void openProposalPopup() {
		super.openProposalPopup();
//		this.setProposalPopupFocus();
		
	}
	
	
	
}
