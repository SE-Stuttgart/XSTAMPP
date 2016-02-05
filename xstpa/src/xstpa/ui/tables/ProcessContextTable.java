package xstpa.ui.tables;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.UUID;

import org.eclipse.core.runtime.jobs.IJobChangeEvent;
import org.eclipse.core.runtime.jobs.IJobChangeListener;
import org.eclipse.core.runtime.jobs.JobChangeAdapter;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.IColorProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

import settings.PreferencePageSettings;
import xstampp.model.ObserverValue;
import xstampp.ui.common.ProjectManager;
import xstampp.util.STPAPluginUtils;
import xstpa.model.ACTSController;
import xstpa.model.ControlActionEntry;
import xstpa.model.ProcessModelVariables;
import xstpa.ui.View;
import xstpa.ui.dialogs.AddEntryShell;
import xstpa.ui.dialogs.EditWindow;
import xstpa.ui.tables.utils.MainViewContentProvider;

public class ProcessContextTable extends AbstractTableComposite {
	
	private static final String FILTER_SHOW_NOT_HAZARDOUS = "Show Not Hazardous";
	private static final String FILTER_SHOW_HAZARDOUS = "Show Hazardous";
	private static final String FILTER_SHOW_ALL = "Show All";
	class ContextViewLabelProvider extends LabelProvider implements
	ITableLabelProvider, IColorProvider {

		@Override
		public Image getColumnImage(Object element, int columnIndex) {
			return null;
		}

		@Override
		public String getColumnText(Object element, int columnIndex) {
			
			ProcessModelVariables entry = (ProcessModelVariables) element;
				if (columnIndex == 0) {
					return String.valueOf(contextRightContent.indexOf(entry)+1);
				}
			
				if ((columnIndex != contextRightTable.getColumnCount()-1)) {
					if (columnIndex-1 < entry.getValues().size()) {
						return entry.getValues().get(columnIndex-1);		
					}
						
				}
				
			return null;
		}

		@Override
		public Color getForeground(Object element) {
			
			return null;
		}

		@Override
		public Color getBackground(Object element) {
			ProcessModelVariables entry = (ProcessModelVariables) element;
			if (entry.getConflict()) {
				return View.CONFLICT;
			}
			else {
				ArrayList<?> list = (ArrayList<?>) contextRightViewer.getInput();
				int index = list.indexOf(element);
				if ((index % 2) == 0) {
					return View.BACKGROUND;
				} else {	    
					return null;
				}
			}
			
				
		}
	}

	private Composite contextCompositeLeft;
	private Composite contextCompositeRight;
	private TableViewer contextViewer;
	private TableViewer contextRightViewer;
	private Table contextTable;
	private Table contextRightTable;
	protected int contextTableCellX;
	protected int contextTableCellY;
	private Combo filterCombo;
	private TabFolder contextContentFolder;
	protected List<ProcessModelVariables> contextRightContent;
	private Label errorLabel;
	
	public ProcessContextTable(Composite parent) {
		super(parent);
		setLayout(new FormLayout());
		contextCompositeLeft = new Composite(this, SWT.BORDER);
	    contextCompositeLeft.setLayout( new GridLayout(1, false));
	    
	    // set the formdata for context table part (middle)
	    FormData fData = new FormData();
	    fData.top = new FormAttachment( 0 );
	    fData.left = new FormAttachment( 0);
	    fData.bottom = new FormAttachment( 100 );
	    contextCompositeLeft.setLayoutData( fData );
	    
	    
	    contextCompositeRight = new Composite(this, SWT.BORDER);

	    contextCompositeRight.setLayout(new FillLayout());
	    
	    // set the formdata for context table part (right)
	    fData = new FormData();
	    fData.top = new FormAttachment( 0 );
	    fData.left = new FormAttachment( contextCompositeLeft );
	    fData.right = new FormAttachment (100);
	    fData.bottom = new FormAttachment( 100 );
	    contextCompositeRight.setLayoutData( fData ); 
	    
	 // the tabfolder for the contextRight Composite
	    contextContentFolder = new TabFolder(contextCompositeRight, SWT.NONE);
	    
	    TabItem contextRightTab1 = new TabItem(contextContentFolder, SWT.NONE);
	    contextRightTab1.setText("Control Action Provided");
	    TabItem contextRightTab2 = new TabItem(contextContentFolder, SWT.NONE);
	    contextRightTab2.setText("Control Action Not Provided");
	    
	    //Composite in which the folder items are located
	    Composite contextContentComposite = new Composite(contextContentFolder, SWT.NONE);
	    contextContentComposite.setLayout( new FormLayout());
	    
	    //Set the composite to the right tab
	    contextRightTab1.setControl(contextContentComposite);
	    contextRightTab2.setControl(contextContentComposite);
	    

	    Composite contextTableComposite = new Composite(contextContentComposite, SWT.NONE);
	    contextTableComposite.setLayout(new GridLayout(1, false));
	    
	    // set the formdata for the middle part of the context composite
	    fData = new FormData();
	    fData.top = new FormAttachment( 0 );
	    fData.left = new FormAttachment( 0 );
	    fData.right = new FormAttachment (95);
	    fData.bottom = new FormAttachment( 90 );
	    contextTableComposite.setLayoutData( fData ); 
	    
	    Composite contextCompositeOptions = new Composite(contextContentComposite, SWT.NONE);
	    contextCompositeOptions.setLayout(new FormLayout());
	    
	    // set the formdata for the right part (options) of the context composite
	    fData = new FormData();
	    fData.top = new FormAttachment( 0 );
	    fData.left = new FormAttachment( contextTableComposite );
	    fData.right = new FormAttachment (100);
	    fData.bottom = new FormAttachment( 100 );
	    contextCompositeOptions.setLayoutData( fData ); 
	    
	    Composite contextCompositeErrorLabel = new Composite(contextContentComposite, SWT.NONE);
	    contextCompositeErrorLabel.setLayout(new GridLayout(1, false));
	    
	    // set the formdata for the right part (options) of the context composite
	    fData = new FormData();
	    fData.top = new FormAttachment( contextTableComposite );
	    fData.left = new FormAttachment( 0 );
	    fData.right = new FormAttachment (100);
	    fData.bottom = new FormAttachment( 100 );
	    contextCompositeErrorLabel.setLayoutData( fData ); 
	    
	    SelectionListener refreshListener = new SelectionAdapter() {
	    	public void widgetSelected(SelectionEvent event) {
	    		  refreshTable();
		      }  
	    }; 
	 // Add a Label which displays if there are any Error Messages
	    errorLabel = new Label(contextCompositeErrorLabel, SWT.NULL);
	    errorLabel.setText("There are 0 Conflicts!  ");
	    
	 // Add a Combobox for the Filter
	    filterCombo = new Combo(contextTableComposite, SWT.READ_ONLY);
	    filterCombo.add(FILTER_SHOW_ALL);
	    filterCombo.add(FILTER_SHOW_HAZARDOUS);
	    filterCombo.add(FILTER_SHOW_NOT_HAZARDOUS);
	    filterCombo.select(0);
	    /**
		 * Listener for the filter Combobox
		 *  Filters the content of the context Table
		 */
	    filterCombo.addSelectionListener(refreshListener);
	    //==============================================================================
	    //START Definition of the available control actions table
	    //==============================================================================
	    
		    contextViewer = new TableViewer(contextCompositeLeft, SWT.FULL_SELECTION );
			contextViewer.setContentProvider(new MainViewContentProvider());
			contextViewer.setLabelProvider(new LabelProvider(){
				@Override
				public String getText(Object element) {
					return ((ControlActionEntry) element).getControlAction();
				}
			});
			contextTable = contextViewer.getTable();
			contextTable.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
			 // add columns for context tables
		    new TableColumn(contextTable, SWT.LEFT).setText(View.LIST_of_CA);
		//===============================================================================
		//END
		//================================================================================
		
		//==============================================================================
		//START of the definition of the process context table
		//==============================================================================
		
			contextRightViewer = new TableViewer(contextTableComposite, SWT.FULL_SELECTION );
			contextRightViewer.setContentProvider(new MainViewContentProvider());
			contextRightViewer.setLabelProvider(new ContextViewLabelProvider());
			contextRightTable = contextRightViewer.getTable();
			contextRightTable.setHeaderVisible(true);
		    contextRightTable.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		 // create menu for contextRightTable
		    final Menu contextRightMenu = new Menu(contextRightTable);
		    contextRightTable.setMenu(contextRightMenu);
		    MenuItem newItem = new MenuItem(contextRightMenu, SWT.NONE);
	        newItem.setText("(don't care)");
	        
	        /**
			 * Listener for the context table (middle)
			 * 	gets the selected item and creates new columns for the right table
			 */
		    contextTable.addSelectionListener(refreshListener);
		    // Listener for the TabFolder in ContextRightComposite
		    contextContentFolder.addSelectionListener(refreshListener);
		    
	        /**
		     * Listener for the Right-click menu of the contextRightTable
		     * Opens the menu to create "don't care" values
		     */
		    contextRightMenu.getItem(0).addSelectionListener(new SelectionAdapter() {
		    	public void widgetSelected(SelectionEvent event) {
		    		//this listener uses the information contextTableCellX which provides the current table item index
		    		//calculated in the contextRightTable.mouseListener
		    		if ((contextTableCellX < contextRightTable.getColumnCount()-1)&(contextTableCellX != 0)) {
		    			ProcessModelVariables contextCombie = contextRightContent.get(contextRightTable.getSelectionIndex());
		    			List<String> strings = contextCombie.getValues();
		    			List<UUID> uuids = contextCombie.getValueIds();
		    			contextCombie.setValueIds(new ArrayList<UUID>());
		    			contextCombie.setValues(new ArrayList<String>());
		    			for(int i = 0;i<strings.size();i++){
		    				if(i == contextTableCellX){
		    					contextCombie.addValue(dataController.getModel().getIgnoreLTLValue().getText());
		    					contextCombie.addValueId(dataController.getModel().getIgnoreLTLValue().getId());
		    				}else{
		    					contextCombie.addValue(strings.get(i));
		    					contextCombie.addValueId(uuids.get(i));
		    				}
		    			}
		    			dataController.storeBooleans(null);
		    			refreshTable();
		    		}
		    	}
		    });
		    // This Part is responsible for setting the Booleans in the Table, also managing the conflicts
		    contextRightTable.addListener(SWT.MouseDown, new Listener() {


				public void handleEvent(Event event) {
					Point pt = new Point(event.x, event.y);
					int index = contextRightTable.getTopIndex();
		          
					while (index < contextRightTable.getItemCount()) {
		            TableItem item = contextRightTable.getItem(index);
		            for (int i = 0; i < contextRightTable.getColumnCount(); i++) {
			            Rectangle rect = item.getBounds(i);
			            contextTableCellX = i;
			            if (contextTableCellX == contextRightTable.getColumnCount()-1 && rect.contains(pt)) {
			            	
			            	contextTableCellY = index;
		                	boolean changed = false;
		                	if (dataController.isControlActionProvided()) {
			                	int tempWidth = rect.width / 3;
			                	boolean checkboxAnytimeClicked =(rect.x < pt.x)&(pt.x < rect.x+tempWidth);
			                	boolean checkboxtooEarlyClicked =(rect.x+tempWidth < pt.x)&(pt.x < rect.x+(2*tempWidth));
					            boolean checkboxtooLateClicked =(rect.x+(2*tempWidth) < pt.x)&(pt.x < rect.x + rect.width);
			                	changed = checkboxAnytimeClicked || checkboxtooEarlyClicked || checkboxtooLateClicked;
			                	if (checkboxAnytimeClicked) {
			                		contextRightContent.get(contextTableCellY).
			                							setHAnytime(!contextRightContent.get(contextTableCellY).getHAnytime());
			                	}else if (checkboxtooEarlyClicked) {
			                		contextRightContent.get(contextTableCellY).
			                							setHEarly(!contextRightContent.get(contextTableCellY).getHEarly());
			                	}else if (checkboxtooLateClicked) {
			                		contextRightContent.get(contextTableCellY).
			                							setHLate(!contextRightContent.get(contextTableCellY).getHLate());
			                	}
		                	}else  if (!dataController.isControlActionProvided()) {
		                		contextRightContent.get(contextTableCellY).setHazardous(!contextRightContent.get(contextTableCellY).getGlobalHazardous());
		                		changed = true;
		                	}
		                	if(changed){
		                		contextRightViewer.refresh();
		    	    			// packs the columns
		    	    		  	for (int j = 0, n = contextRightTable.getColumnCount(); j < n; j++) {
		    	    		  		contextRightTable.getColumn(j).pack();	    		  		  
		    	    		  	}
		    	    		  	contextRightTable.deselectAll();
		    	    		  	dataController.storeBooleans(null);
				                setConflictLabel();
		                	}
		                	break;
			            }
		            }
			            index++;
					}
		        }
		    });
		    
		    
		    
		    // listener for the checkboxes in the context table so they get drawn right
		    contextRightTable.addListener(SWT.PaintItem, new Listener() {

		        @Override
		        public void handleEvent(Event event) {
		            // if column = Hazardous, draw the right image
		        	if (dataController.isControlActionProvided()) {
			        	ProcessModelVariables entry = (ProcessModelVariables) event.item.getData();
		            	Image anytimeImage = View.UNCHECKED;
		            	Image earlyImage = View.UNCHECKED;
		            	Image lateImage = View.UNCHECKED;

		            	
			            if (event.index == contextRightTable.getColumnCount()-1){
			            	
				            	if (entry.getHLate()){
					                lateImage = View.CHECKED;
				            	}
				            	if (entry.getHEarly()){
				            		earlyImage = View.CHECKED;
				            	}
				            	if (entry.getHAnytime()){
				            		anytimeImage = View.CHECKED;
				            	}
				            	
				            	getPosition(event, anytimeImage, earlyImage,lateImage);		            	
			            }
		        	}
		        	
		        	else {
		        		
		        		ProcessModelVariables entry = (ProcessModelVariables) event.item.getData();
		        		Image tmpImage = View.UNCHECKED;
		        		if(event.index == contextRightTable.getColumnCount()-1)  {
			            	if (entry.getGlobalHazardous()){
				                tmpImage = View.CHECKED;
			            	}
			            	getPosition(event, tmpImage);
			            }
		        	}

		        }
		        
		        public void getPosition(Event event, Image tmpImage) {

	                int tmpWidth = 0;
	                int tmpHeight = 0;
	                int tmpX = 0;
	                int tmpY = 0;

	                tmpWidth = contextRightTable.getColumn(event.index).getWidth();
	                tmpHeight = ((TableItem)event.item).getBounds().height;

	                tmpX = tmpImage.getBounds().width;
	                tmpX = (tmpWidth / 2 - tmpX / 2);
	                tmpY = tmpImage.getBounds().height;
	                tmpY = (tmpHeight / 2 - tmpY / 2);
	                if(tmpX <= 0) tmpX = event.x;
	                else tmpX += event.x;
	                if(tmpY <= 0) tmpY = event.y;
	                else tmpY += event.y;
	                event.gc.drawImage(tmpImage, tmpX, tmpY);

		        }
		        
		        public void getPosition(Event event, Image anytimeImage, Image earlyImage, Image lateImage) {

	                int tmpWidth = 0;
	                int tmpHeight = 0;
	                int tmpX = 0;
	                int tmpY = 0;

	                tmpWidth = contextRightTable.getColumn(event.index).getWidth();
	                tmpHeight = ((TableItem)event.item).getBounds().height;

	                tmpX = anytimeImage.getBounds().width;
	                tmpX = (tmpWidth / 4 - tmpX / 2);
	                tmpY = anytimeImage.getBounds().height;
	                tmpY = (tmpHeight / 2 - tmpY / 2);
	                if(tmpX <= 0) tmpX = event.x;
	                else tmpX += event.x;
	                if(tmpY <= 0) tmpY = event.y;
	                else tmpY += event.y;
	                event.gc.drawImage(anytimeImage, tmpX, tmpY);
	                event.gc.drawImage(earlyImage, tmpX+(tmpWidth/4), tmpY);
	                event.gc.drawImage(lateImage, tmpX+(tmpWidth/2), tmpY);
		        }
		    });
		//===============================================================================
		//END
		//================================================================================
			
		//==============================================================================
		//START creation of the buttons bar
		//==============================================================================
			// Add a Composite to edit all Tables
		    Composite editTableComposite = new Composite( contextCompositeOptions, SWT.NONE);
		    editTableComposite.setLayout( new GridLayout(1, false) );

		    fData = new FormData();
		    fData.top = new FormAttachment(filterCombo);
		    fData.left = new FormAttachment( 0 );
		    fData.right = new FormAttachment (100);
		    fData.bottom = new FormAttachment( 100 );   
		    editTableComposite.setLayoutData(fData);
		    
		    

		    
		    // Add a button to add table entrys to contextRightTable
		    Button generateEntry = new Button(editTableComposite, SWT.PUSH);
		    generateEntry.setToolTipText("Generates a new Table with default Settings");
		    generateEntry.setImage(View.GENERATE);
		    generateEntry.pack();
		    
		    // Add a button to add table entrys to contextRightTable
		    Button addEntry = new Button(editTableComposite, SWT.PUSH);
		    addEntry.setImage(View.ADD);
		    addEntry.setToolTipText("Adds a new Entry");
		    addEntry.pack();
		    
		    // Add a button to delete table entrys to contextRightTable
		    Button deleteEntry = new Button(editTableComposite, SWT.PUSH);
		    deleteEntry.setToolTipText("Deletes the selected Row");
		    deleteEntry.setImage(View.DELETE);
		    deleteEntry.pack();
		    
		    // Add a button to change to the settings for ACTS
		    //Button settingsBtn = new Button(contextCompositeInnerRight, SWT.PUSH);
		    Button settingsBtn = new Button(editTableComposite, SWT.PUSH);
		    settingsBtn.setToolTipText("Opens the Settings for the Combinatorial Algorithm");
		    settingsBtn.setImage(View.SETTINGS);
		    //settingsBtn.setLayoutData(new GridData(100,30));
		    settingsBtn.pack();
		    
		    Button checkConflictsBtn = new Button(editTableComposite, SWT.PUSH);
		    checkConflictsBtn.setToolTipText("Checks the Table for any (logical) Conflicts");
		    checkConflictsBtn.setImage(View.CHECK_CONFLICTS);
		    checkConflictsBtn.pack();
		    
		    /**
			 * Listener for the settings button
			 * 	opens the settings window
			 */
		    settingsBtn.addSelectionListener(new SelectionAdapter() {
			      public void widgetSelected(SelectionEvent event) {
			    	  if(System.getProperty("os.name").toLowerCase().contains("linux")){
			    		  ProjectManager.getLOGGER().debug("The Acts settings shell is not working on linux at the moment");
			    	  }
			    	  else if (dataController.getLinkedCAE().getLinkedItems().isEmpty()) {
			    		  MessageDialog.openInformation(null, "No linked Variables", "There are no Variables linked with this Control Action");
			    	  }
			    	  else{
			    	  
				    	  //open the settings window
				    	  EditWindow settingsWindow = new EditWindow(dataController.getLinkedCAE());
				    	 
				    	  if(settingsWindow.open()){
//				    			createTableColumns();
//				    			ACTSController.writeFile(false,
//	    											 	 dataController.isControlActionProvided(),
//	    											 	dataController.getLinkedCAE().getLinkedItems());
//				    			storeEntrys(ACTSController.open(false,
//				    								contextTable.getColumnCount(),
//				    								contextContentFolder.getSelectionIndex() == 0,
//				    								dataController.getLinkedCAE()), false);
				    	  }
			    	  }
			    	  
			      }  
			});
		    
		    /**
			 * Listener for the checkConflicts button
			 * 	checks the Context table for possible Conflicts
			 */
		    checkConflictsBtn.addSelectionListener(new SelectionAdapter() {
			      public void widgetSelected(SelectionEvent event) {
			    	
			    	 setConflictLabel();
			    	  
			      }  
			});
		    /**
		     * Listener for the generate Entry Button for Refined Safety
		     */
		    generateEntry.addSelectionListener(new SelectionAdapter() {
		    	public void widgetSelected(SelectionEvent event) {
			    	  
			    	  if (dataController.getLinkedCAE() == null) {
			    		  MessageDialog.openInformation(null,"Select a Control Action", "Please Select a Control Action to generate the Table!");
			    	  }
			    	  else if (!dataController.getLinkedCAE().getLinkedItems().isEmpty()) {
			    		  MessageDialog dialog = new MessageDialog(getShell(),"keep Rules",null,
			    				  "do you want to keep the Rules which are defined for this Control Action?",
			    				  MessageDialog.QUESTION_WITH_CANCEL, new String[]{IDialogConstants.YES_LABEL,
			    			  IDialogConstants.NO_LABEL,IDialogConstants.CANCEL_LABEL}, 0);
			    		  final int code = dialog.open();
			    		  if (code != 2) {

				    		  // If the Path for ACTS is not set, the PreferencePage opens
				    		  if (xstampp.Activator.getDefault().getPreferenceStore().getString("ACTS_Path")
				    				  .equals(xstampp.Activator.getDefault().getPreferenceStore().getDefaultString(("ACTS_Path")))) {
				    			  Map<String,String> values=new HashMap<>();
				    			  values.put("preferencePageId", PreferencePageSettings.ID);
				    	    	  	STPAPluginUtils.executeParaCommand("org.eclipse.ui.window.preferences", values);
				    	    	  
				    		  }
					    	  
					    	  final ACTSController job = new ACTSController(contextRightTable.getColumnCount(), dataController.getLinkedCAE());
					    	  job.addJobChangeListener(new JobChangeAdapter() {
					    		  @Override
					    		  public void done(IJobChangeEvent event) {
					    			  Display.getDefault().asyncExec(new Runnable() {
										
										@Override
										public void run() {
											// creates the correct number of rows for the context table
									    	  createTableColumns();
							    			  storeEntrys(job.getEntryList(), code == 0);
										}
									});
					    		  }
					    	  });
					    	  job.schedule();
					    	  
			    		  }
			    	  }
			    	  else {
			    		  MessageDialog.openInformation(null, "Link some Variables",
			    				  "Please link some Variables to the selected Control Action to succesfully generate a new Testset");
			    	  }
		    	}
		    });
		    
		    
		    
		    /**
		     * Listener for the add Entry Button in the Context Table
		     */
		    addEntry.addSelectionListener(new SelectionAdapter() {
		    	public void widgetSelected(SelectionEvent event) {
		    		List<UUID> idList = new ArrayList<>();
		    		for(ProcessModelVariables variables : dataController.getLinkedCAE().getLinkedItems()){
		    			idList.add(variables.getId());
		    		}
		    		AddEntryShell shell = new AddEntryShell(idList, dataController.getModel());
		    		shell.addApplyListener(new Listener() {
						
						@Override
						public void handleEvent(Event event) {
							//Create values for the Constructor
				    		List<String> pmVars = new ArrayList<String>();
				    		for (int i = 1; i<contextRightTable.getColumnCount()-1;i++) {
				    			pmVars.add(contextRightTable.getColumn(i).getText().replace(" ", "_"));
				    		}
							// add the New Variable
				    		ProcessModelVariables temp = new ProcessModelVariables(pmVars, dataController.getLinkedCAE());
				    		if(event.data instanceof String[]){
				    			
				    			for(String valueName:(String[]) event.data){
				    				temp.addValue(valueName);
				    			}
				    		}
							contextRightContent.add(temp);
							// set the ContextTableCombinations
							dataController.getLinkedCAE().setContextTableCombinations(contextRightContent);
							// refresh the Viewer
							dataController.storeBooleans(null);
							contextRightViewer.refresh();
							
						}
					});
		    		Point location = Display.getDefault().getCursorLocation();
		    		shell.open(location.x,location.y);
		    		
		    	}
		    });
		    
		    
		    /**
		     * Listener for the Delete Entry Button
		     */
		    deleteEntry.addSelectionListener(new SelectionAdapter() {
		    	public void widgetSelected(SelectionEvent event) {
		    		contextRightContent.remove(contextRightTable.getSelectionIndex());
		    		contextRightTable.remove(contextRightTable.getSelectionIndex());

		    		dataController.storeBooleans(null);
		    	}
		    });
		//===============================================================================
		//END
		//================================================================================
		setVisible(false);
	}
	
	private void updateContextInput(int formerIndex){
		// create Input for contextTableViewer
		List<ControlActionEntry> contextTableInput= new ArrayList<ControlActionEntry>();
		// if the tabfolder is on "Context Provided"
	  	if (dataController.isControlActionProvided()) {
	  		for(ControlActionEntry entry : dataController.getDependenciesIFProvided()) {
	    		if (entry.getSafetyCritical()) {
					contextTableInput.add(entry);
				}
			}
	  	}
	  	  // if its on the other tab
	  	else {
	  		for(ControlActionEntry entry : dataController.getDependenciesNotProvided()) {
	    		if (entry.getSafetyCritical()) {
					contextTableInput.add(entry);
				}
			}
	  	}
	  	contextViewer.setInput(contextTableInput);
	  	if(contextTableInput.isEmpty()){
    		//if the context table input is empty than there is nothing to be shown
    		return;
    	}
		  
		contextTable.select(formerIndex);
		if(contextTable.getSelectionIndex() == -1){
			//if the former index returns cannot be selected try 0 
			contextTable.select(0);
		}
		ControlActionEntry entry = contextTableInput.get(contextTable.getSelectionIndex());
		dataController.setLinkedCAE(entry.getId());
		
		refreshTable();
	}
	
	/**
	 * Create the correct columns in the context table
	 */
	public void createTableColumns() {
		for (TableColumn column : contextRightTable.getColumns()) {
			column.dispose();
		}
  		if(dataController.getLinkedCAE() == null){
  			return;
  		}
  		if (!dataController.getLinkedCAE().getLinkedItems().isEmpty()) {
  			new TableColumn(contextRightTable, SWT.NONE).setText(View.ENTRY_ID);
  			// creates new TableColumns dynamically so that the context table has the right size (and labels)
  			for (ProcessModelVariables entry:dataController.getLinkedCAE().getLinkedItems()) {
  				new TableColumn(contextRightTable, SWT.NONE).setText(entry.getName());
  			}
	  	
  			if (dataController.isControlActionProvided()) {
  				new TableColumn(contextRightTable, SWT.NONE).setImage(View.HEADER);
  			}else{
  				new TableColumn(contextRightTable, SWT.NONE).setText(View.IS_HAZARDOUS);
  			}
  			
  			// Create the cell editors
  		  	CellEditor[] contextEditors = new CellEditor[contextRightTable.getColumnCount()];
  		  	
  		  	for (int i = 1; i<contextRightTable.getColumnCount()-1;i++) {
  		  		// creates the cell editors
  		  		if ((i != 0) &(i!=contextRightTable.getColumnCount()-1)) {
  		  			contextEditors[i] = new TextCellEditor(contextRightTable);
  		  		}
  		  	}
  		  	View.contextProps = new String[contextRightTable.getColumnCount()];
  			for (int i=0; i<View.contextProps.length;i++) {
  				View.contextProps[i] = contextRightTable.getColumn(i).getText();
  			}
  			contextRightViewer.setColumnProperties(View.contextProps);
  			contextRightViewer.setCellEditors(contextEditors);
  		}

	  	
	  	
	}
	
	/**
	 * help method for the filter combo which filters the context table entries
	 * by the given property
	 * 
	 * @param filter a string describing the property
	 */
	public void showContent(String filter, List<ProcessModelVariables> input) {
		ArrayList<ProcessModelVariables> content = new ArrayList<>();
		// checks which option is selected and shows the right content
  	  if (filter.equals(FILTER_SHOW_ALL)) {
  		  contextRightViewer.setInput(input);
  	  }
  	  else if (filter.equals(FILTER_SHOW_HAZARDOUS)) {
  		  
  		  for (int i=0; i < contextRightContent.size(); i++) {
  			  if ((dataController.isControlActionProvided())&&(input.get(i).getHAnytime() | (input.get(i).getHEarly()) | (input.get(i).getHLate()))) {
  				content.add(contextRightContent.get(i));
  			  }
  			  else if ((!dataController.isControlActionProvided())&&(input.get(i).getGlobalHazardous())) {
  				content.add(contextRightContent.get(i));
  			  }
  		  }
  		  contextRightViewer.setInput(content);
  	  }
  	  else if (filter.equals(FILTER_SHOW_NOT_HAZARDOUS)) {
  		  
  		  for (int i=0; i < contextRightContent.size(); i++) {
  			  if ((dataController.isControlActionProvided())&&(!input.get(i).getHAnytime())) {
  				content.add(contextRightContent.get(i));
  			  }
  			  else if ((!dataController.isControlActionProvided())&&(!input.get(i).getGlobalHazardous())) {
  				content.add(contextRightContent.get(i));
  			  }
  		  }
  		  contextRightViewer.setInput(content);
  	  }
  	  // packs the columns
  	  for (int j = 0, n = contextRightTable.getColumnCount(); j < n; j++) {
  		  contextRightTable.getColumn(j).pack();	    		  		  
  	  }
    }
	
	private void storeEntrys(List<ProcessModelVariables> entrys, boolean keepOldCombies){
		if(keepOldCombies){
			for(ProcessModelVariables variable: dataController.getLinkedCAE().getContextTableCombinations()){
				if(variable.getGlobalHazardous()){
					variable.setArchived(true);
					entrys.add(variable);
				}
			}
		}
		dataController.getLinkedCAE().setContextTableCombinations(entrys);
		dataController.storeBooleans(null);
		refreshTable();
	}
	
	@Override
	public void activate() {
		
		// create Input for contextTableViewer
		List<ControlActionEntry> contextTableInput= new ArrayList<ControlActionEntry>();
    	for (ControlActionEntry entry : dataController.getDependenciesIFProvided()) {
    		if (entry.getSafetyCritical()) {
    			contextTableInput.add(entry);
    		}
    	}
    	contextViewer.setInput(contextTableInput);
  	  	
  	  	if (contextTable.getSelectionIndex() == -1) {
  		  contextTable.select(0);
  	  	}
  	  	if(contextTable.getSelectionIndex() != -1){
			ControlActionEntry entry = contextTableInput.get(contextTable.getSelectionIndex());
	  	  	dataController.setLinkedCAE(entry.getId());
	  	}else{
	  		dataController.setLinkedCAE(null);
	  	}
  	  	if(dataController.getLinkedCAE() == null){
    		contextRightViewer.setInput(null);
  	  		
  	  	}else if ((!dataController.getLinkedCAE().getContextTableCombinations().isEmpty()) & (!dataController.getLinkedCAE().getLinkedItems().isEmpty())) {
  		  createTableColumns();
  		  refreshTable();
  	  	}
  	  	else {
	    		contextRightViewer.setInput(null);
	    		writeStatus("There was no Stored Testset. Please Generate a new Testset for this Control Action");
  	  	}
  	  
  	  	// packs the columns
  		for (int i = 0, n = contextTable.getColumnCount(); i < n; i++) {
  			contextTable.getColumn(i).setWidth(contextTable.getSize().x);
  		}

  	  setVisible(true);
	}

	@Override
	public boolean refreshTable() {
		if(contextRightViewer.getControl() == null || contextRightViewer.getControl().isDisposed()){
			return false;
		}
		if(contextTable.getSelectionCount() == 0){
			dataController.setLinkedCAE(null);
		}else{
			ControlActionEntry entry = (ControlActionEntry) contextTable.getSelection()[0].getData();
	  	  	dataController.setLinkedCAE(contextContentFolder.getSelectionIndex() == 0,entry.getId());
	  	  	createTableColumns();
	  	  	
	  	  	ArrayList<ProcessModelVariables> viewerContent = new ArrayList<>();
			for(ProcessModelVariables variable : dataController.getLinkedCAE().getContextTableCombinations()){
				if(variable.getVariableIds().size() != dataController.getLinkedCAE().getLinkedItems().size()){
					continue;
				}
				boolean consider = true;
				for(int idIndex=0; idIndex<variable.getVariableIds().size();idIndex++){
					if(!variable.getVariableIds().contains(dataController.getLinkedCAE().getLinkedItem(idIndex+1).getId())){
						consider = false;
					}
				}
				if(consider && !variable.isArchived()){
					viewerContent.add(variable);
				}
			}
			if(viewerContent.isEmpty()){
				contextRightContent= null;
				contextRightViewer.setInput(null);
	    		writeStatus("There was no Stored Testset. Please Generate a new Testset for this Control Action");
			}else{
				writeStatus("");
				contextRightContent = viewerContent;
				showContent(filterCombo.getText(), viewerContent);
			}
		}
		return true;
	}

	private void setConflictLabel() {
			
		//verfies if there are no conflicts between CAProvided and not provided
		UUID caID= dataController.getLinkedCAE().getId();
		List<ProcessModelVariables> notProvidedContext =dataController.getControlActionEntry(false, caID).getContextTableCombinations();
		List<ProcessModelVariables> providedContext =dataController.getControlActionEntry(true, caID).getContextTableCombinations();
		int conflictCounter = 0;
		boolean combiesFit;
		//initially all stored contextTableCombinations are set too non conflicting
		for (ProcessModelVariables NPCombie : notProvidedContext) {
			NPCombie.setConflict(false);
		}
		for (ProcessModelVariables pCombie : providedContext) {
			pCombie.setConflict(false);
		}
		
		for (ProcessModelVariables NPCombie : notProvidedContext) {
			for (ProcessModelVariables pCombie : providedContext) {
				if(pCombie.getConflict() || NPCombie.getConflict()){
					continue;
				}
				//check if the combies are defining a combination of the same values
				combiesFit = true;
				if(NPCombie.getValueIds().size() == pCombie.getValueIds().size()){
					for(int i=0;i<NPCombie.getValueIds().size();i++){
						if(!NPCombie.getValueIds().contains(pCombie.getValueIds().get(i))){
							combiesFit =false;
						}
					}
				}
				//if the combies fit than it is checked if both cases are hazardous
				if(combiesFit){
					if(NPCombie.getHazardous() && pCombie.getGlobalHazardous()){
						conflictCounter++;
						NPCombie.setConflict(true);
						pCombie.setConflict(true);
						break;
					}else{
						NPCombie.setConflict(false);
						pCombie.setConflict(false);
					}
				}else{
					NPCombie.setConflict(false);
					pCombie.setConflict(false);
				}
			}
		}
   	 
		if(conflictCounter > 0){
   		 	errorLabel.setText("There are " +conflictCounter+ " Conflicts!");
   	 	}else{
   		 	errorLabel.setText("");
   	 	}
		contextRightViewer.refresh();
	}
	/**
	 * updates the table dynamically if something changes in the Datamodel
	 */
	@Override
	public void update(Observable o, Object updatedValue) {
		ObserverValue type = (ObserverValue) updatedValue;
		
		switch (type) {
		case CONTROL_ACTION:
		case CONTROL_STRUCTURE: {
			if(contextTable != null && !contextTable.isDisposed()){
				int formerIndex = contextTable.getSelectionIndex();
				
				updateContextInput(formerIndex);
			}
			break;
		}
		default:
			break;
		}

		
	}
}
