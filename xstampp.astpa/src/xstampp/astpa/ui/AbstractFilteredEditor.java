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
 * Parts derived from this class are provided an optional filter bar, containing a Combo and a Textfield
 *  which filter for categories given in {@link AbstractFilteredEditor#getCategories()} and some methods to check the filtering
 *  or disable it (hide it)
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
	
	/**
	 * <i>This method should be implemented by subclasses, for the defaul only returns null</i>
	 * 
	 * @return an array of String describing the Categorys which can chosen in the filter combo,
	 * 			the categories must be unique since String matching is used to distinguish between categories
	 */
	protected String[] getCategories(){
		return null;
	}
	/**
	 * 
	 * @param text the text which is to be filtered
	 * @param checkMatch if true the given text is examined if it contains the filterText, independent of lower/uppercase writing
	 * @param category a registered category String, or <i>null</i> if the text should not be filtered by a category
	 * 
	 * @see AbstractFilteredEditor#getCategories()
	 * @return false if the text is not filtered and thus can be used
	 */
	private boolean int_isFiltered(String text,boolean checkMatch, String category){
		if(!useFilter || this.filterText != null){
			//if there are categories defined and there is a given category but the active category doesn't equals the given one
			//the text is filtered
			if(getActiveCategory() != null && category != null && !getActiveCategory().equals(category)){
				return true;
			}
			if(this.filterText.getText().isEmpty()){
				return false;
			}
			if(checkMatch){
				return !text.toLowerCase().matches(".*"+filterText.getText().toLowerCase() +".*");
			}
			return !text.startsWith(filterText.getText());
		}
		return false;
	}

	/**
	 * This method looks if the given string contains the filter text, independent of lower/uppercase writing
	 * @param text the text which is to be filtered
	 * 
	 * @see AbstractFilteredEditor#getCategories()
	 * @return false if the text is not filtered and thus can be used
	 */
	protected boolean isFiltered(String text){
		return int_isFiltered(text,true,null);
	}
	/**
	 * returns true if the number given does not start with the digits in the filter, 
	 * or if the filter is no integer
	 * 
	 * @param nr a number (e.g. an id)
	 * 
	 * @see AbstractFilteredEditor#getCategories()
	 * @return false if the text is not filtered and thus can be used
	 */
	protected boolean isFiltered(int nr){
		return int_isFiltered(String.valueOf(nr),false,null);
	}
	
	/**
	 * 
	 * This method looks if the given string contains the filter text, independent of lower/uppercase writing
	 * 
	 * @param text the text which is to be filtered
	 * @param category a registered category String
	 * 
	 * @see AbstractFilteredEditor#getCategories()
	 * @return false if the text is not filtered and thus can be used
	 */
	protected boolean isFiltered(String text, String category){
		return int_isFiltered(text,true,category);
	}
	
	/**
	 * returns true if the number given does not start with the digits in the filter, 
	 * or if the filter is no integer
	 * 
	 * @param nr a number (e.g. an id)
	 * @param category a registered category String
	 * 
	 * @see AbstractFilteredEditor#getCategories()
	 * @return false if the text is not filtered and thus can be used
	 */
	protected boolean isFiltered(int nr, String category){
		return int_isFiltered(String.valueOf(nr),false,category);
	}
	
	/**
	 * 
	 * @return returns the category string which is currently chosen in the combo, null if
	 * 			no filtering is used
	 */
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
