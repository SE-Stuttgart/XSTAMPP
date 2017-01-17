/*************************************************************************
 * Copyright (c) 2014-2016 Lukas Balzer, Asim Abdulkhaleq, Stefan Wagner
 * Institute of Software Technology, Software Engineering Group
 * University of Stuttgart, Germany
 * 
 * All rights reserved. This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License v1.0 which
 * accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 **************************************************************************/

package xstampp.ui.editors;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

/**
 * Parts derived from this class are provided an optional filter bar, containing
 * a Combo and a Textfield which filter for categories given in
 * {@link AbstractFilteredEditor#getCategories()} and some methods to check the
 * filtering or disable it (hide it)
 * 
 * @author Lukas Balzer
 * @since 2.0.2
 */
public abstract class AbstractFilteredEditor extends StandartEditorPart {

  private boolean useFilter;
  private Map<String, String[]> comboChoicesToEntrys;
  private Map<String, Object[]> filterValuesToCategories;
  private Composite filter;
  protected String activeCategory;
  private String globalCategory;
  private Object filterValue;

  /**
   * if the filter is used this method expects a parent composite 
   * with a gridLayout.
   */
  @Override
  public void createPartControl(Composite parent) {
    if (useFilter) {
      parent.setLayout(new GridLayout(1, false));
      filter = new Composite(parent, SWT.None) {
        @Override
        public void dispose() {
        }
      };
      filter.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
      filter.setLayout(new GridLayout(3, false));
      createCategoryWidget(getCategoryArray()[0]);
    }
  }

  /**
   * creates the label and category choose widget of the filter.
   * 
   * @param category
   *          a category which can be set as initial category if the given
   *          string is a valid category
   */
  private boolean createCategoryWidget(String category) {
    if (useFilter && getCategories() != null && getCategories().size() > 0) {
      Label label = new Label(filter, SWT.LEFT);
      label.setText("Filter Categories");
      final Combo categoryCombo = new Combo(filter, SWT.READ_ONLY | SWT.None);
      int initialCategory = 0;
      for (int i = 0; i < getCategoryArray().length; i++) {
        categoryCombo.add(getCategoryArray()[i]);
        if (getCategoryArray()[i].equals(category)) {
          initialCategory = i;
        }
      }

      activeCategory = categoryCombo.getText();
      final Combo filterText = new Combo(filter, SWT.LEFT | SWT.BORDER);
      filterText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
      
      //add listeners to the filter widgets to interact with each other
      //and to also use the comboChoicesToEntrys and filterValuesToEntrys maps
      categoryCombo.addSelectionListener(new SelectionAdapter() {
        @Override
        public void widgetSelected(SelectionEvent event){
          activeCategory = categoryCombo.getText();
          if (comboChoicesToEntrys != null 
              && comboChoicesToEntrys.get(activeCategory) != null) {
            filterText.setItems(comboChoicesToEntrys.get(activeCategory));
            
          } else {
            filterText.removeAll();
          }
          filterText.setText("");
          updateFilter();
        }
      });
      
      filterText.addSelectionListener(new SelectionAdapter() {
        @Override
        public void widgetSelected(SelectionEvent e){
          //if the there is an extra filter value stored for the selected entry
          //than this values is assigned as filter
          if(filterValuesToCategories.containsKey(categoryCombo.getText())){
            Object[] values = filterValuesToCategories.get(categoryCombo.getText());
            filterValue = values[filterText.getSelectionIndex()];
          }else{
            filterValue = filterText.getText();
          }
          
        }
      });
      filterText.addVerifyListener(new VerifyListener() {
        
        @Override
        public void verifyText(VerifyEvent e) {
          if(!e.text.isEmpty() && filterText.getItemCount() > 0){
            for(String obj: filterText.getItems()){
              if(obj.equals(e.text)){
                e.doit = true;
                return;
              }
            }
            e.doit = false;
          }
          
        }
      });
      filterText.addModifyListener(new ModifyListener() {

        @Override
        public void modifyText(ModifyEvent e){


          if ( filterText.getSelectionIndex() >= 0) {
            Object[] values = filterValuesToCategories.get(categoryCombo.getText());
            if ( values != null) {
              filterValue = values[filterText.getSelectionIndex()];
            }
          } else {
            filterValue = filterText.getText();
          }
          updateFilter();
        }
      });
      //Initialize the filter widget by selecting the initial category and adding the 
      //initial values to the filterText
      if (comboChoicesToEntrys != null 
          && comboChoicesToEntrys.get(category) != null) {
        filterText.setItems(comboChoicesToEntrys.get(category));
        filterText.select(0);
      }
      categoryCombo.select(initialCategory);
      activeCategory = getCategoryArray()[initialCategory];
      return true;
    }
    return false;
  }


  /**
   * adds filter entrys to the filter with the given 
   * category id. These entries (or the values by {@link #addChoiceValues(String, String[])})
   * than appear in the drop down list of the filter input
   * 
   * @param id the id of the category , must be one of the category id's
   *        given in {@link #getCategories()}
   * @param choices a String array of entries(or selectors for values) 
   */
  protected final void addChoices(String id, String[] choices) {
    if (comboChoicesToEntrys == null) {
      comboChoicesToEntrys = new HashMap<>();
    }
    comboChoicesToEntrys.put(id, choices);
  }

  /**
   * adds filter values for the 'choices' array set in {@link #addChoices(String, String[])}
   * if the respective choices array is set for the same id, than the value with the same index 
   * in the <code>values</code> array will be set as filter
   * @param id the id of the category , must be one of the category id's
   *        given in {@link #getCategories()}
   * @param values an array of strings with filter values, 
   *      <br><i>must be of the same length as the choices-array given in {@link #addChoices(String, String[])}</i>
   */
  protected final void addChoiceValues(String id, Object[] values) {
    if (filterValuesToCategories == null) {
      filterValuesToCategories = new HashMap<>();
    }
    filterValuesToCategories.put(id, values);
  }

  protected void updateFilter() {

  }

  /**
   * <i>This method should be implemented by subclasses, for the defaul only
   * returns null</i>
   * 
   * @return a Map containing booleans mapped to Strings describing the
   *         Categories which can be chosen in the filter combo, the categories
   *         must be unique since String matching is used to distinguish between
   *         categories. The booleans describe whether or not the category is
   *         handled as exclusive category, what means that all other explicit
   *         categories are filtered
   */
  protected abstract Map<String, Boolean> getCategories();

  protected abstract String[] getCategoryArray();

  /**
   * 
   * @param candidate
   *          the object which is to be filtered,
   *          <ul>
   *          <li><b>Strings</b> are checked with startWith(filterValue)
   *             or matches(".*" + filterValue + ".*")
   *          <li><b>Lists</b> are checked for containment of the filterValue   
   *          <li><b>other Objects</b> are checked for equality
   *               with the {@link #getFilterValue()} 
   *         </ul>
   * @param checkMatch
   *          if true the given text is examined if it contains the filterText,
   *          independent of lower/uppercase writing
   * @param testCategory
   *          a registered category String, or <i>null</i> if the text should
   *          not be filtered by a category
   * 
   * @see AbstractFilteredEditor#getCategories()
   * @return false if the text is not filtered and thus can be used
   */
  private boolean int_isFiltered(Object candidate, boolean checkMatch, String testCategory) {
    
    if (useFilter && filterValue != null && activeCategory != null && testCategory != null) {
     
      // if the global category is active any value is tested
      // if not than the candidate is only tested if the category given is active
      if (!activeCategory.equals(globalCategory)) {
        // if the active category is explicit then all other explicit categories
        // are filtered
        //if the active category is explicit and unequal 
        if (getCategories().get(testCategory) == getCategories().get(getActiveCategory())) {
          if(!activeCategory.equals(testCategory)){
            //if the test category is not active the return value is whether or not the 
            //active category is explicit
            return getCategories().get(activeCategory);
          }
        }else{
          return false;
        }
      }
      if (filterValue.getClass().equals(String.class) 
          && filterValue.equals(new String())) {
        return false;
      }
      //if the testant and also the filterValue is a string than  
      if(candidate.getClass().equals(String.class) 
          && filterValue.getClass().equals(String.class)){
        String filteText = ((String) filterValue).toLowerCase();
        String testString = ((String)candidate).toLowerCase();
        
        if (checkMatch) {
          //if checkMatch is given as true whether the testString does not contain the filterText
          //surrounding the regular Exp. by ".*" ... ".*" means that anything
          //can stand before or after the checked substring
          System.out.println(testString.contains(filteText));
          return !testString.contains(filteText);
        }
        if(testString.startsWith(filteText)){
          return false;
        }else{
          return true;
        }
      }
      else if(candidate instanceof List<?>){
        return !((List<?>) candidate).contains(filterValue);
      }else{
        return candidate.equals(filterValue);
      }
    }
    return false;
  }

  public Object getFilterValue() {
    return filterValue;
  }
  
  /**
   * This method looks if the given string contains the filter text, independent
   * of lower/uppercase writing calls {@link #int_isFiltered(Object, boolean, String)}
   * with <code>int_isFiltered(obj,true,null)</code>.
   * 
   * @param obj
   *          the text which is to be filtered
   * 
   * @see AbstractFilteredEditor#getCategories()
   * @return false if the text is not filtered and thus can be used
   */
  protected boolean isFiltered(Object obj) {
    return int_isFiltered(obj, true, null);
  }

  /**
   * returns true if the number given does not start with the digits in the
   * filter, or if the filter is no integer calls {@link #int_isFiltered(Object, boolean, String)}
   * with <code>int_isFiltered(String.valueOf(nr),true,null)</code>.
   * 
   * @param nr
   *          a number (e.g. an id)
   * 
   * @see AbstractFilteredEditor#getCategories()
   * @return false if the text is not filtered and thus can be used
   */
  protected boolean isFiltered(int nr) {
    return int_isFiltered(String.valueOf(nr), false, null);
  }

  /**
   * This method looks if the given string contains the filter text, independent
   * of lower-/upper-case writing calls {@link #int_isFiltered(Object, boolean, String)}
   * with <code>int_isFiltered(obj,true,category)</code>.
   * 
   * @param obj
   *          the text which is to be filtered
   * @param category
   *          a registered category String
   * 
   * @see AbstractFilteredEditor#getCategories()
   * @return false if the text is not filtered and thus can be used
   */
  protected boolean isFiltered(Object obj, String category) {
    return int_isFiltered(obj, true, category);
  }

  /**
   * returns true if the number given does not start with the digits in the
   * filter, or if the filter is no integer calls {@link #int_isFiltered(Object, boolean, String)}
   * with <code>int_isFiltered(String.valueOf(nr),true,category)</code>.
   * 
   * @param nr
   *          a number (e.g. an id)
   * @param category
   *          a registered category String
   * 
   * @see AbstractFilteredEditor#getCategories()
   * @return false if the text is not filtered and thus can be used
   */
  protected boolean isFiltered(int nr, String category) {
    return int_isFiltered(String.valueOf(nr), false, category);
  }

  /**
   * 
   * @return returns the category string which is currently chosen in the combo,
   *         null if no filtering is used
   */
  protected String getActiveCategory() {
    if (this.activeCategory == null) {
      return null;
    }
    return this.activeCategory;
  }

  protected void setUseFilter(boolean useFilter) {
    this.useFilter = useFilter;
  }

  /**
   * Sets one of the categories given by
   * {@link AbstractFilteredEditor#getCategories()} as global category what
   * means that all categories will be ignored in the isFiltered(..) methods
   * when the given category is chosen in the combo
   * 
   * @param global
   *          must be one of the categories given in
   *          {@link AbstractFilteredEditor#getCategories()}, will be ignored
   *          otherwise
   */
  protected void setGlobalCategory(String global) {
    if (getCategories().containsKey(global)) {
      this.globalCategory = global;
    }

  }

}
