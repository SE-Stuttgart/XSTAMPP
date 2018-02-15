/*************************************************************************
 * Copyright (c) 2014-2017 Lukas Balzer, Asim Abdulkhaleq, Stefan Wagner Institute of Software
 * Technology, Software Engineering Group University of Stuttgart, Germany
 * 
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 **************************************************************************/

package xstampp.ui.editors;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import messages.Messages;

/**
 * Parts derived from this class are provided an optional filter bar, containing a Combo and a
 * Textfield which filter for categories given in {@link AbstractFilteredEditor#getCategories()} and
 * some methods to check the filtering or disable it (hide it)
 * 
 * @author Lukas Balzer
 * @since 2.0.2
 */
public abstract class AbstractFilteredEditor extends StandartEditorPart {

  protected class Category {

    private String name;
    private boolean useFilter = true;
    private Object[] filterValues;
    private String[] comboChoices;

    public Category(String name) {
      this.name = name;
    }

    public Object[] getFilterValues() {
      return filterValues;
    }

    public void setFilterValues(Object[] values) {
      this.filterValues = values;
    }

    public String[] getComboChoices() {
      return comboChoices;
    }

    public void setComboChoices(String[] comboChoices) {
      this.comboChoices = comboChoices;
    }

    public String getName() {
      return name;
    }

    @Override
    public boolean equals(Object obj) {
      return obj instanceof String && ((String) obj).equals(this.name);
    }

    public boolean isUseFilter() {
      return useFilter;
    }

    public void setUseFilter(boolean useFilter) {
      this.useFilter = useFilter;
    }
  }

  private boolean useFilter;
  private Composite filter;
  private Category activeCategory;
  private List<Category> categories;
  private String globalCategory;
  private Object filterValue;

  /**
   * if the filter is used this method expects a parent composite with a gridLayout.
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
      filter.setLayout(new GridLayout(4, false));
      createCategoryWidget(getCategoryArray()[0]);
    }
  }

  /**
   * creates the label and category choose widget of the filter.
   * 
   * @param category
   *          a category which can be set as initial category if the given string is a valid
   *          category
   */
  private boolean createCategoryWidget(String category) {
    if (useFilter && getCategories() != null && getCategories().size() > 0) {
      Label label = new Label(filter, SWT.LEFT);
      label.setText(Messages.AbstractFilteredEditor_FilterLabel);
      final Combo categoryCombo = new Combo(filter, SWT.READ_ONLY | SWT.None);
      categoryCombo.setToolTipText(Messages.AbstractFilteredEditor_CategoryToolTip);
      int initialCategory = 0;
      this.categories = new ArrayList<>();
      for (int i = 0; i < getCategoryArray().length; i++) {
        categoryCombo.add(getCategoryArray()[i]);
        this.categories.add(new Category(getCategoryArray()[i]));
        if (getCategoryArray()[i].equals(category)) {
          initialCategory = i;
        }
      }
      Composite filterInput = new Composite(filter, SWT.None);
      filterInput.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
      filterInput.setLayout(new FormLayout());
      FormData data = new FormData();
      data.bottom = new FormAttachment(100);
      data.left = new FormAttachment(0);
      data.right = new FormAttachment(100);
      data.top = new FormAttachment(0);

      final Combo filterCombo = new Combo(filterInput, SWT.DROP_DOWN | SWT.SIMPLE | SWT.READ_ONLY);
      filterCombo.setToolTipText(Messages.AbstractFilteredEditor_FilterDropDowmToolTip);
      filterCombo.setLayoutData(data);

      final Text filterText = new Text(filterInput, SWT.LEFT | SWT.BORDER);
      filterText.setToolTipText(Messages.AbstractFilteredEditor_FilterTextToolTip);
      filterText.setLayoutData(data);
      filterCombo.setVisible(false);
      filterText.setVisible(true);
      Button clearFilter = new Button(filter, SWT.PUSH);
      clearFilter.setText(Messages.AbstractFilteredEditor_ClearFilter);
      clearFilter.addSelectionListener(new SelectionAdapter() {
        @Override
        public void widgetSelected(SelectionEvent e) {

          categoryCombo.setItems(getCategoryArray());
          filterCombo.setItems(new String[0]);
          filterText.setText(""); //$NON-NLS-1$
          activeCategory = null; // $NON-NLS-1$
          filterValue = ""; //$NON-NLS-1$
          updateFilter();
        }
      });
      // add listeners to the filter widgets to interact with each other
      // and to also use the comboChoicesToEntrys and filterValuesToEntrys maps
      categoryCombo.addSelectionListener(new SelectionAdapter() {
        @Override
        public void widgetSelected(SelectionEvent event) {
          activeCategory = categories.get(categoryCombo.getSelectionIndex());
          if (activeCategory != null && activeCategory.getComboChoices() != null) {
            filterCombo.setItems(activeCategory.getComboChoices());
            filterCombo.setVisible(true);
            filterCombo.setEnabled(activeCategory.useFilter);
            filterText.setVisible(false);
          } else {
            filterCombo.setVisible(false);
            filterText.setVisible(true);
            filterText.setEnabled(activeCategory.useFilter);
            filterText.setText(""); //$NON-NLS-1$
          }
          filterValue = ""; //$NON-NLS-1$
          updateFilter();
        }
      });

      filterCombo.addSelectionListener(new SelectionAdapter() {
        @Override
        public void widgetSelected(SelectionEvent e) {
          // if the there is an extra filter value stored for the selected entry
          // than this values is assigned as filter
          if (activeCategory != null && activeCategory.getFilterValues() != null) {
            filterValue = activeCategory.getFilterValues()[filterCombo.getSelectionIndex()];
            updateFilter();
          }

        }
      });
      filterText.addModifyListener(new ModifyListener() {

        @Override
        public void modifyText(ModifyEvent e) {
          filterValue = filterText.getText();
          updateFilter();
        }
      });
      // Initialize the filter widget by selecting the initial category and adding the
      // initial values to the filterText
      if (activeCategory != null && activeCategory.getComboChoices() != null) {
        filterCombo.setItems(activeCategory.getComboChoices());
        filterCombo.select(0);
      }
      categoryCombo.select(initialCategory);
      activeCategory = this.categories.get(initialCategory);
      return true;
    }

    return false;
  }

  /**
   * adds filter entrys to the filter with the given category id. These entries (or the values by
   * {@link #addChoiceValues(String, String[])}) than appear in the drop down list of the filter
   * input
   * 
   * @param id
   *          the id of the category , must be one of the category id's given in
   *          {@link #getCategories()}
   * @param choices
   *          a String array of entries(or selectors for values)
   */
  protected final void addChoices(String id, String[] choices) {
    categories.forEach(data -> {
      if (data.getName().equals(id)) {
        data.setComboChoices(choices);
      }
    });
  }

  /**
   * adds filter values for the 'choices' array set in {@link #addChoices(String, String[])} if the
   * respective choices array is set for the same id, than the value with the same index in the
   * <code>values</code> array will be set as filter
   * 
   * @param id
   *          the id of the category , must be one of the category id's given in
   *          {@link #getCategories()}
   * @param values
   *          an array of strings with filter values, <br>
   *          <i>must be of the same length as the choices-array given in
   *          {@link #addChoices(String, String[])}</i>
   */
  protected final void addChoiceValues(String id, Object[] values) {
    categories.forEach(data -> {
      if (data.getName().equals(id)) {
        data.setFilterValues(values);
      }
    });
  }

  /**
   * @param id
   *          the id of the category , must be one of the category id's given in
   *          {@link #getCategories()}
   * @param useFilter
   *          <ul>
   *          <li>when true is given than the category will use the text/combo box filter widget to
   *          further define the filter
   *          <li>when false than the filter will be determined soley by the category and the
   *          text/combo box filter will be disabled
   *          </ul>
   */
  protected final void setUseFilter(String id, boolean useFilter) {
    categories.forEach(data -> {
      if (data.getName().equals(id)) {
        data.setUseFilter(useFilter);
      }
    });
  }

  protected void updateFilter() {

  }

  /**
   * <i>This method should be implemented by subclasses, for the defaul only returns null</i>
   * 
   * @return a Map containing booleans mapped to Strings describing the Categories which can be
   *         chosen in the filter combo, the categories must be unique since String matching is used
   *         to distinguish between categories. The booleans describe whether or not the category is
   *         handled as exclusive category, what means that all other explicit categories are
   *         filtered
   */
  protected abstract Map<String, Boolean> getCategories();

  protected abstract String[] getCategoryArray();

  /**
   * 
   * @param candidate
   *          the object which is to be filtered,
   *          <ul>
   *          <li><b>Strings</b> are checked with startWith(filterValue) or matches(".*" +
   *          filterValue + ".*")
   *          <li><b>Lists</b> are checked for containment of the filterValue
   *          <li><b>other Objects</b> are checked for equality with the {@link #getFilterValue()}
   *          </ul>
   * @param checkMatch
   *          if true the given text is examined if it contains the filterText, independent of
   *          lower/uppercase writing
   * @param testCategory
   *          a registered category String, or <i>null</i> if the text should not be filtered by a
   *          category
   * 
   * @see AbstractFilteredEditor#getCategories()
   * @return false if the text is not filtered and thus can be used
   */
  private boolean int_isFiltered(Object candidate, boolean checkMatch, String testCategory) {

    if (useFilter && filterValue != null) {
      if (activeCategory != null && testCategory != null) {
        // if the global category is active any value is tested
        // if not than the candidate is only tested if the category given is active
        if (!activeCategory.getName().equals(globalCategory)) {
          // if the active category is explicit then all other explicit categories
          // are filtered
          // if the active category is explicit and unequal
          if (!testCategory.equals(getActiveCategory())) {
            // if the test category is not active the return value is whether or not the
            // active category is explicit
            return false;
          }
        }
      }
      if (candidate == null) {
        return true;
      }
      if (filterValue.getClass().equals(String.class) && filterValue.equals(new String())) {
        return false;
      }
      // if the testant and also the filterValue is a string than
      if (candidate.getClass().equals(String.class)
          && filterValue.getClass().equals(String.class)) {
        String filteText = ((String) filterValue).toLowerCase();
        String testString = ((String) candidate).toLowerCase();

        if (checkMatch) {
          // if checkMatch is given as true whether the testString does not contain the filterText
          // surrounding the regular Exp. by ".*" ... ".*" means that anything
          // can stand before or after the checked substring
          return !testString.contains(filteText);
        }
        if (testString.startsWith(filteText)) {
          return false;
        } else {
          return true;
        }
      } else if (candidate instanceof List<?>) {
        return !((List<?>) candidate).contains(filterValue);
      } else {
        return !candidate.equals(filterValue);
      }
    }
    return false;
  }

  public Object getFilterValue() {
    return filterValue;
  }

  /**
   * This method looks if the given string contains the filter text, independent of lower/uppercase
   * writing calls {@link #int_isFiltered(Object, boolean, String)} with
   * <code>int_isFiltered(obj,true,null)</code>.
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
   * returns true if the number given does not start with the digits in the filter, or if the filter
   * is no integer calls {@link #int_isFiltered(Object, boolean, String)} with
   * <code>int_isFiltered(String.valueOf(nr),true,null)</code>.
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
   * This method looks if the given string contains the filter text, independent of
   * lower-/upper-case writing calls {@link #int_isFiltered(Object, boolean, String)} with
   * <code>int_isFiltered(obj,true,category)</code>.
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
   * returns true if the number given does not start with the digits in the filter, or if the filter
   * is no integer calls {@link #int_isFiltered(Object, boolean, String)} with
   * <code>int_isFiltered(String.valueOf(nr),true,category)</code>.
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
   * @return returns the category string which is currently chosen in the combo, null if no
   *         filtering is used
   */
  protected String getActiveCategory() {
    if (this.activeCategory == null) {
      return "";
    }
    return this.activeCategory.getName();
  }

  /**
   * This enables or disables the whole filter widget, if useFilter is false than the filter widgets
   * will not be drawn
   * <p>
   * NOTE: the view must be redrawn manually if this option is changed after the first call of
   * {@link AbstractFilteredEditor#createPartControl(Composite)}
   * 
   * @param useFilter
   *          <ul>
   *          <li>when true is given than the filter widget will not be displayed/used at all
   *          <li>when false than the filter will be enabled
   */
  protected void setUseFilter(boolean useFilter) {
    this.useFilter = useFilter;
  }

  /**
   * Sets one of the categories given by {@link AbstractFilteredEditor#getCategories()} as global
   * category what means that all categories will be ignored in the isFiltered(..) methods when the
   * given category is chosen in the combo
   * 
   * @param global
   *          must be one of the categories given in {@link AbstractFilteredEditor#getCategories()},
   *          will be ignored otherwise
   */
  protected void setGlobalCategory(String global) {
    if (getCategories().containsKey(global)) {
      this.globalCategory = global;
    }

  }

}
