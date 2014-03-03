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
import org.eclipse.jface.fieldassist.IControlContentAdapter;
import org.eclipse.swt.widgets.Control;

/**
 * A custom autocomplete textfield adapter
 * 
 * @author Benedikt Markt
 * 
 */
public class AutoCompleteField {
	
	private ContentProposalProvider contentProposalProvider;
	private LinkingCommandAdapter contentProposalAdapter;
	
	
	/**
	 * 
	 * @author Benedikt Markt
	 * 
	 * @param control The control to apply autocomplete to
	 * @param controlContentAdapter IContentAdapter
	 * @param literals the strings to link
	 * @param labels short descriptive strings for each item
	 * @param descriptions long descriptions for eacht item
	 */
	public AutoCompleteField(final Control control, final IControlContentAdapter controlContentAdapter,
		final String[] literals, final String[] labels, final String[] descriptions) {
		this.contentProposalProvider = new ContentProposalProvider(literals.clone(), labels, descriptions);
		this.contentProposalProvider.setFiltering(true);
		this.contentProposalAdapter =
			new LinkingCommandAdapter(control, controlContentAdapter, this.contentProposalProvider, null, null, false);
		this.contentProposalAdapter.setPropagateKeys(true);
		this.contentProposalAdapter.setProposalAcceptanceStyle(ContentProposalAdapter.PROPOSAL_REPLACE);
	}
	
	/**
	 * 
	 * @author Benedikt Markt
	 * 
	 * @param proposals a list of proposals
	 */
	public void setProposals(final String[] proposals) {
		this.contentProposalProvider.setProposals(proposals);
	}
	
	/**
	 * 
	 * @author Benedikt Markt
	 * 
	 * @param labels a lost of labels
	 */
	public void setLabels(final String[] labels) {
		this.contentProposalProvider.setLabels(labels);
	}
	
	/**
	 * 
	 * @author Benedikt Markt
	 * 
	 * @return the content proposal provider
	 */
	public ContentProposalProvider getContentProposalProvider() {
		return this.contentProposalProvider;
	}
	
	/**
	 * 
	 * @author Benedikt Markt
	 * 
	 * @return the content proposal adapter
	 */
	public ContentProposalAdapter getContentProposalAdapter() {
		return this.contentProposalAdapter;
	}
	
	/**
	 * Opens the proposal popup immediately
	 * 
	 * @author Benedikt Markt
	 * 
	 */
	public void openPopup() {
		this.contentProposalAdapter.openProposalPopup();
		
	}
	
}
