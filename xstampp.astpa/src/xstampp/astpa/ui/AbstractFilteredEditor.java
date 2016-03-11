package xstampp.astpa.ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import xstampp.ui.editors.StandartEditorPart;

/**
 * 
 * @author Lukas Balzer
 *@since 2.0.2
 */
public abstract class AbstractFilteredEditor extends StandartEditorPart {

	private Combo categoryCombo;
	private boolean useFilter;
	private Text filterText;
	

	@Override
	public void createPartControl(Composite parent) {
		if(useFilter){
			parent.setLayout(new GridLayout(1, false));
			Composite filter= new Composite(parent, SWT.None);
			filter.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
			filter.setLayout(new GridLayout(3, false));
			if(getCategories() != null && getCategories().length > 0){
				Label label= new Label(filter,SWT.LEFT);
				label.setText("Filter Categories");
				this.categoryCombo = new Combo(filter, SWT.READ_ONLY|SWT.None);
				for (String string : getCategories()) {
					this.categoryCombo.add(string);
				}
				this.categoryCombo.select(0);
				this.categoryCombo.addSelectionListener(new SelectionAdapter() {
					@Override
					public void widgetSelected(SelectionEvent e) {
						filterText.setText("");
						updateFilter();
					}
				});
			}
			
			this.filterText= new Text(filter, SWT.LEFT |SWT.BORDER);
			this.filterText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
			
			this.filterText.addModifyListener(new ModifyListener() {
				
				@Override
				public void modifyText(ModifyEvent e) {
					updateFilter();
				}
			});
		}
	}

	protected void updateFilter(){
		
	}
	
	protected String[] getCategories(){
		return null;
	}
	private boolean int_isFiltered(String text,boolean checkMatch, String category){
		if(this.filterText != null && !this.filterText.getText().isEmpty()
				&& getActiveCategory() != null && getActiveCategory().equals(category)){
			if(checkMatch){
				return !text.toLowerCase().matches(".*"+filterText.getText().toLowerCase() +".*");
			}
			return !text.startsWith(filterText.getText());
		}
		return false;
	}
	
	protected boolean isFiltered(String text){
		return int_isFiltered(text,true,"");
	}
	
	protected boolean isFiltered(int nr){
		return int_isFiltered(String.valueOf(nr),false,"");
	}
	
	protected boolean isFiltered(String text, String category){
		return int_isFiltered(text,true,category);
	}
	
	protected boolean isFiltered(int nr, String category){
		return int_isFiltered(String.valueOf(nr),false,category);
	}
	protected String getActiveCategory(){
		if(this.categoryCombo == null){
			return null;
		}
		return this.categoryCombo.getText();
	}
	public void setUseFilter(boolean useFilter) {
		this.useFilter = useFilter;
	}
}
