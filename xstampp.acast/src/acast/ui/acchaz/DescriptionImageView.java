package acast.ui.acchaz;

import messages.Messages;

import org.eclipse.jface.preference.PreferenceConverter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import xstampp.preferences.IPreferenceConstants;
import xstampp.ui.editors.StandartEditorPart;
import xstampp.ui.editors.interfaces.IEditorBase;

public abstract class DescriptionImageView extends StandartEditorPart {

	private Text descriptionWidget;
	private Label descriptionLabel;
	private Composite parent;



	public void createDescriptionImageView(Composite parent) {
		
		this.parent = parent;
		SashForm sashForm = new SashForm(parent, SWT.HORIZONTAL);

		final Composite textContainer = new Composite(sashForm, SWT.NONE);
		textContainer
				.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		textContainer.setLayout(new GridLayout(1, false));

		this.descriptionWidget = new Text(textContainer, SWT.MULTI | SWT.BORDER
				| SWT.V_SCROLL | SWT.WRAP);

		this.descriptionWidget.setEnabled(false);
		this.descriptionWidget.setLayoutData(new GridData(SWT.FILL, SWT.FILL,
				true, true));

		// KeyListener for ctrl + a (SelectAll) in the description
		this.descriptionWidget.addKeyListener(new KeyAdapter() {

			@Override
			public void keyReleased(final KeyEvent e) {
				if (((e.stateMask == SWT.CTRL) || (e.stateMask == SWT.COMMAND))
						&& (e.keyCode == 'a')) {
					DescriptionImageView.this.getDescriptionWidget()
							.selectAll();
				}
				switch (e.keyCode) {
				case SWT.ARROW_LEFT: {
					((Text) e.getSource()).setSelection(
							((Text) e.getSource()).getSelection().x - 1,
							((Text) e.getSource()).getSelection().x - 1);
					break;
				}
				case SWT.ARROW_RIGHT: {
					((Text) e.getSource()).setSelection(
							((Text) e.getSource()).getSelection().x + 1,
							((Text) e.getSource()).getSelection().x + 1);
				}
				}
			}

		});

		Composite rightHeadComposite = new Composite(textContainer, SWT.NONE);
		rightHeadComposite.setLayoutData(new GridData(SWT.FILL, SWT.NONE, true,
				false));
		rightHeadComposite.setLayout(new GridLayout(2, true));

		this.descriptionLabel = new Label(rightHeadComposite, SWT.LEAD);
		this.descriptionLabel.setFont(new Font(Display.getCurrent(),
				PreferenceConverter.getFontData(IEditorBase.STORE,
						IPreferenceConstants.DEFAULT_FONT)));
		this.descriptionLabel.setText(Messages.DescriptionNotes);

	}

	public Text getDescriptionWidget() {
		return this.descriptionWidget;
	}

	public void setDescriptionWidget(Text descriptionWidget) {
		this.descriptionWidget = descriptionWidget;
	}

}
