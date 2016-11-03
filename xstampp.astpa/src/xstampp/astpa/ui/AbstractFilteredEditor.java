package xstampp.astpa.ui;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

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
	private Combo filterText;
	private String activeCategory;
	private String globalCategory;
	private Map<String, String[]> comboChoicesToEntrys;
	private Map<String, String[]> comboValuesToEntrys;
  private Composite filter;
	

	@Override
	public void createPartControl(Composite parent) {
		if(useFilter){
			parent.setLayout(new GridLayout(1, false));
			filter= new Composite(parent, SWT.None){
			  @Override
			  public void dispose() {
			  }
			};
			filter.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
			filter.setLayout(new GridLayout(3, false));
			if(getCategories() != null && getCategories().size() > 0){
			  createCategoryWidget(new String());
			}
		createFilter();
		}
	}
	
	/**
	 * creates the label and category choose widget of the filter
	 * 
	 * @param category a category which can be set as initial category if the 
	 *                 given string is a valid category
	 */
	private boolean createCategoryWidget(String category){
	  if(useFilter){
  	  Label label= new Label(filter,SWT.LEFT);
      label.setText("Filter Categories");
      this.categoryCombo = new Combo(filter, SWT.READ_ONLY|SWT.None);
      activeCategory = new String();
      int initialCategory = 0;
      for (int i=0;i < getCategoryArray().length;i++) {
        this.categoryCombo.add(getCategoryArray()[i]);
        if(getCategoryArray()[i].equals(category)){
          activeCategory = category;
          initialCategory = i;
        }
      }
      this.categoryCombo.select(initialCategory);
      this.categoryCombo.addSelectionListener(new SelectionAdapter() {
        @Override
        public void widgetSelected(SelectionEvent e) {
          filterText.setText("");
          activeCategory= e.text;
          if(comboChoicesToEntrys != null && comboChoicesToEntrys.get(categoryCombo.getText()) != null){
            filterText.setItems(comboChoicesToEntrys.get(categoryCombo.getText()));
          }else{
            filterText.removeAll();
          }
          updateFilter();
        }
      });
      return true;
	  }
	  return false;
	}
	
	/**
	 * creates the filter input combo which can contain certain suggestions
	 */
	private boolean createFilter(){
    if(useFilter){
  	  this.filterText= new Combo(filter, SWT.LEFT |SWT.BORDER);
      this.filterText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
      this.filterText.addSelectionListener(new SelectionAdapter() {
        @Override
        public void widgetSelected(SelectionEvent e) {
          filterText.setText(comboValuesToEntrys.get(categoryCombo.getText())[filterText.getSelectionIndex()]);
        }
      });
      this.filterText.addModifyListener(new ModifyListener() {
        
        @Override
        public void modifyText(ModifyEvent e) {
          updateFilter();
        }
      });
      return true;
    }
    return false;
	}
	protected final void addChoices(String id, String[] choices){
		if(comboChoicesToEntrys == null){
			comboChoicesToEntrys = new HashMap<>();
		}
		comboChoicesToEntrys.put(id, choices);
	}
	protected final void addChoiceValues(String id, String[] values){
		if(comboValuesToEntrys == null){
			comboValuesToEntrys = new HashMap<>();
		}
		comboValuesToEntrys.put(id, values);
	}
	
	
	protected void updateFilter(){
		
	}
	
	/**
	 * <i>This method should be implemented by subclasses, for the defaul only returns null</i>
	 * 
	 * @return a Map containing booleans mapped to Strings describing the Categories which can be chosen in the filter combo,
	 * 			the categories must be unique since String matching is used to distinguish between categories.
	 * 			The booleans describe whether or not the category is handled as exclusive category,
	 * 			what means that all other explicit categories are filtered 
	 */
	protected abstract Map<String, Boolean> getCategories();
	
	protected abstract String[] getCategoryArray();
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
			
			if(getActiveCategory() != null && category != null && !getActiveCategory().equals(globalCategory)){
					//if the active category is explicit then all other expizit categories are filtered
				if(getCategories().get(getActiveCategory()) && !getActiveCategory().equals(category)){
					return getCategories().get(category);
				}//if the active category is not explicit
				if(!getCategories().get(getActiveCategory())&& !getActiveCategory().equals(category)){
					return false;
				}
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
	protected void setUseFilter(boolean useFilter) {
		this.useFilter = useFilter;
	}
	
	/**
	 * Sets one of the categories given by {@link AbstractFilteredEditor#getCategories()} as global category what means that all categories
	 * will be ignored in the isFiltered(..) methods when the given category is chosen in the combo
	 * 
	 * @param global must be one of the categories given in {@link AbstractFilteredEditor#getCategories()}, will be ignored otherwise
	 */
	protected void setGlobalCategory(String global){
		if(getCategories().containsKey(global)){
			this.globalCategory = global;
		}
		
	}
	
}
