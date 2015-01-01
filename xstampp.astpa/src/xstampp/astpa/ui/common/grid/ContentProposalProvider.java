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

package xstampp.astpa.ui.common.grid;

import java.util.ArrayList;

import org.eclipse.jface.fieldassist.IContentProposal;
import org.eclipse.jface.fieldassist.IContentProposalProvider;

class ContentProposalProvider implements IContentProposalProvider {

	private String[] proposals;
	private String[] labels;
	private String[] descriptions;
	private IContentProposal[] contentProposals;
	private boolean filterProposals = false;

	public ContentProposalProvider(final String[] proposals,
			final String[] labels, final String[] descriptions) {
		super();
		this.proposals = proposals.clone();
		this.labels = labels.clone();
		this.descriptions = descriptions.clone();
	}

	@Override
	public IContentProposal[] getProposals(String contents, int position) {
		if (this.filterProposals) {
			ArrayList<IContentProposal> list = new ArrayList<IContentProposal>();
			for (int i = 0; i < this.proposals.length; i++) {
				if (this.matches(this.proposals[i], this.labels[i], contents)) {
					list.add(this.makeContentProposal(this.proposals[i],
							this.labels[i], this.descriptions[i]));
				}
			}
			return list.toArray(new IContentProposal[list.size()]);
		}
		if (this.contentProposals == null) {
			this.contentProposals = new IContentProposal[this.proposals.length];
			for (int i = 0; i < this.proposals.length; i++) {
				this.contentProposals[i] = this
						.makeContentProposal(this.proposals[i], this.labels[i],
								this.descriptions[i]);
			}
		}
		return this.contentProposals;
	}

	public void setProposals(String[] items) {
		this.proposals = items.clone();
		this.contentProposals = null;
	}

	public void setLabels(String[] items) {
		this.labels = items.clone();
		this.contentProposals = null;
	}

	public void setFiltering(boolean filterProposals) {
		this.filterProposals = filterProposals;
		this.contentProposals = null;
	}

	private boolean matches(String proposal, String label, String contents) {
		if (contents.isEmpty()) {
			return true;
		}

		String tmpProp = proposal.replaceAll("[-,.^:!?]", "").toLowerCase() //$NON-NLS-1$ //$NON-NLS-2$
				.trim();
		String tmpCont = contents.replaceAll("[-,.^:!?]", "").toLowerCase() //$NON-NLS-1$ //$NON-NLS-2$
				.trim();
		boolean matches = tmpProp.contains(tmpCont) || label.contains(tmpCont);
		return matches;
	}

	private IContentProposal makeContentProposal(final String proposal,
			final String label, final String description) {
		return new IContentProposal() {

			@Override
			public String getContent() {
				return proposal;
			}

			@Override
			public String getDescription() {
				return description;
			}

			@Override
			public String getLabel() {
				return proposal + " - " + label; //$NON-NLS-1$
			}

			@Override
			public int getCursorPosition() {
				return proposal.length();
			}
		};
	}
}