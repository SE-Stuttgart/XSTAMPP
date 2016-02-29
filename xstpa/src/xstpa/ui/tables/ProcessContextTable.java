package xstpa.ui.tables;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.UUID;

import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.IJobChangeEvent;
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

import xstampp.astpa.model.controlstructure.interfaces.IRectangleComponent;
import xstampp.model.ObserverValue;
import xstampp.ui.common.ProjectManager;
import xstampp.util.STPAPluginUtils;
import xstpa.Messages;
import xstpa.model.ACTSController;
import xstpa.model.ContextTableCombination;
import xstpa.model.ControlActionEntry;
import xstpa.model.ProcessModelVariables;
import xstpa.settings.PreferencePageSettings;
import xstpa.ui.View;
import xstpa.ui.dialogs.AddEntryShell;
import xstpa.ui.dialogs.EditWindow;
import xstpa.ui.tables.utils.ContextCheckJob;
import xstpa.ui.tables.utils.MainViewContentProvider;

public class ProcessContextTable extends AbstractTableComposite {
	
	private static final String FILTER_SHOW_NOT_HAZARDOUS = Messages.ContextTableFilter_NotHazardous;
	private static final String FILTER_SHOW_HAZARDOUS = Messages.ContextTableFilter_Hazardous;
	private static final String FILTER_SHOW_ALL = Messages.ContextTableFilter_All;
	class ContextViewLabelProvider extends LabelProvider implements
	ITableLabelProvider, IColorProvider {

		@Override
		public Image getColumnImage(Object element, int columnIndex) {
			return null;
		}

		@Override
		public String getColumnText(Object element, int columnIndex) {
			
			ContextTableCombination entry = (ContextTableCombination) element;
			int variableIndex = columnIndex -1;
			if (columnIndex == 0) {
				return String.valueOf(contextRightContent.indexOf(entry)+1);
			}else if(dataController.getLinkedCAE() != null 
					&&variableIndex < dataController.getLinkedCAE().getLinkedItems().size()){
				UUID id = entry.getValueIDForVariable(dataController.getLinkedCAE().getLinkedItem(columnIndex-1).getId());
				IRectangleComponent component = dataController.getModel().getComponent(id);
				if(component != null){
					return component.getText();
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
			ContextTableCombination entry = (ContextTableCombination) element;
			if (entry.getConflict()) {
				return View.CONFLICT;
			}
			else {
				ArrayList<?> list = (ArrayList<?>) contextTableViewer.getInput();
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
	private TableViewer controlActionViewer;
	private TableViewer contextTableViewer;
	private Table controlActionTable;
	private Table contextTable;
	protected int contextTableCellX;
	protected int contextTableCellY;
	private Combo filterCombo;
	private TabFolder contextContentFolder;
	protected List<ContextTableCombination> contextRightContent;
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
	    contextRightTab1.setText(Messages.ContextTableContext_Provided);
	    TabItem contextRightTab2 = new TabItem(contextContentFolder, SWT.NONE);
	    contextRightTab2.setText(Messages.ContextTableContext_NotProvided);
	    
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
	    		  updateContextTable();
		      }  
	    }; 
	 // Add a Label which displays if there are any Error Messages
	    errorLabel = new Label(contextCompositeErrorLabel, SWT.NULL);
	    errorLabel.setText("There are 0 Conflicts!  "); //$NON-NLS-1$
	    
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
	    
		    controlActionViewer = new TableViewer(contextCompositeLeft, SWT.FULL_SELECTION );
			controlActionViewer.setContentProvider(new MainViewContentProvider());
			controlActionViewer.setLabelProvider(new LabelProvider(){
				@Override
				public String getText(Object element) {
					return ((ControlActionEntry) element).getControlAction();
				}
			});
			controlActionTable = controlActionViewer.getTable();
			controlActionTable.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
			 // add columns for context tables
		    new TableColumn(controlActionTable, SWT.LEFT).setText(View.LIST_of_CA);
		//===============================================================================
		//END
		//================================================================================
		
		//==============================================================================
		//START of the definition of the process context table
		//==============================================================================
		
			contextTableViewer = new TableViewer(contextTableComposite, SWT.FULL_SELECTION );
			contextTableViewer.setContentProvider(new MainViewContentProvider());
			contextTableViewer.setLabelProvider(new ContextViewLabelProvider());
			contextTable = contextTableViewer.getTable();
			contextTable.setHeaderVisible(true);
		    contextTable.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		    
	        /**
			 * Listener for the context table (middle)
			 * 	gets the selected item and creates new columns for the right table
			 */
		    controlActionTable.addSelectionListener(refreshListener);
		    // Listener for the TabFolder in ContextRightComposite
		    contextContentFolder.addSelectionListener(refreshListener);
		    
		    // This Part is responsible for setting the Booleans in the Table, also managing the conflicts
		    contextTable.addListener(SWT.MouseDown, new Listener() {


				public void handleEvent(Event event) {
					Point pt = new Point(event.x, event.y);
					if(contextTable.getSelection() == null ||contextTable.getSelectionCount() == 0){
						return;
					}
					TableItem item = contextTable.getSelection()[0];
					//this for loop iterates over all visible table items
					if(item != null) {

			            for (int i = 0; i < contextTable.getColumnCount(); i++) {
			            	//for each column we check whether the mouse is inside the related tableitem
				            Rectangle rect = item.getBounds(i);
			            	contextTableCellY = contextTable.getSelectionIndex();
			            	boolean doesContain = rect.contains(pt);
				            if(doesContain){
					            contextTableCellX = i;
				            
					            if (contextTableCellX == contextTable.getColumnCount()-1) {
	
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
				                		contextTableViewer.refresh(false);
				    	    		  	dataController.storeBooleans(null, ObserverValue.COMBINATION_STATES);
						                
				                	}
					            }
			                	return;
				            }
			            }
			            
					}
		        }
		    });
		    
		    contextTable.addSelectionListener(new SelectionAdapter() {
				
				@Override
				public void widgetSelected(SelectionEvent e) {
					if(contextTableCellX > 0 && contextTableCellX < contextTable.getColumnCount() -1){
					    contextTable.setMenu(new Menu(contextTable));
						updateMenu(contextTableCellX -1);
					}else{
					    contextTable.setMenu(null);						
					}
				}
			});
		    
		    // listener for the checkboxes in the context table so they get drawn right
		    contextTable.addListener(SWT.PaintItem, new Listener() {

		        @Override
		        public void handleEvent(Event event) {
		            // if column = Hazardous, draw the right image
		        	if (dataController.isControlActionProvided()) {
		        		ContextTableCombination entry = (ContextTableCombination) event.item.getData();
		            	Image anytimeImage = View.UNCHECKED;
		            	Image earlyImage = View.UNCHECKED;
		            	Image lateImage = View.UNCHECKED;

		            	
			            if (event.index == contextTable.getColumnCount()-1){
			            	
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
		        		
		        		ContextTableCombination entry = (ContextTableCombination) event.item.getData();
		        		Image tmpImage = View.UNCHECKED;
		        		if(event.index == contextTable.getColumnCount()-1)  {
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

	                tmpWidth = contextTable.getColumn(event.index).getWidth();
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

	                tmpWidth = contextTable.getColumn(event.index).getWidth();
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
		    generateEntry.setToolTipText(Messages.GenerateNewTableMsg);
		    generateEntry.setImage(View.GENERATE);
		    generateEntry.pack();
		    
		    // Add a button to add table entrys to contextRightTable
		    Button addEntry = new Button(editTableComposite, SWT.PUSH);
		    addEntry.setImage(View.ADD);
		    addEntry.setToolTipText(Messages.AddsNewTableEntry);
		    addEntry.pack();
		    
		    // Add a button to delete table entrys to contextRightTable
		    Button deleteEntry = new Button(editTableComposite, SWT.PUSH);
		    deleteEntry.setToolTipText(Messages.DeletesRow);
		    deleteEntry.setImage(View.DELETE);
		    deleteEntry.pack();
		    
		    // Add a button to change to the settings for ACTS
		    //Button settingsBtn = new Button(contextCompositeInnerRight, SWT.PUSH);
		    Button settingsBtn = new Button(editTableComposite, SWT.PUSH);
		    settingsBtn.setToolTipText(Messages.OpensSettingsACTS);
		    settingsBtn.setImage(View.SETTINGS);
		    //settingsBtn.setLayoutData(new GridData(100,30));
		    settingsBtn.pack();
		    
		    Button checkConflictsBtn = new Button(editTableComposite, SWT.PUSH);
		    checkConflictsBtn.setToolTipText(Messages.ChecksTableForConflicts);
		    checkConflictsBtn.setImage(View.CHECK_CONFLICTS);
		    checkConflictsBtn.pack();
		    
		    /**
			 * Listener for the settings button
			 * 	opens the settings window
			 */
		    settingsBtn.addSelectionListener(new SelectionAdapter() {
			      public void widgetSelected(SelectionEvent event) {
			    	  if(System.getProperty("os.name").toLowerCase().contains("linux")){ //$NON-NLS-1$ //$NON-NLS-2$
			    		  ProjectManager.getLOGGER().debug("The Acts settings shell is not working on linux at the moment"); //$NON-NLS-1$
			    	  }
			    	  else if (dataController.getLinkedCAE().getLinkedItems().isEmpty()) {
			    		  MessageDialog.openInformation(null, Messages.NoLinkedVariables, Messages.NoLinkedVariablesMsg);
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
			    		  MessageDialog.openInformation(null,"Select a Control Action", Messages.SelectAControlAction); //$NON-NLS-1$
			    	  }
			    	  else if (!dataController.getLinkedCAE().getLinkedItems().isEmpty()) {
			    		  MessageDialog dialog = new MessageDialog(getShell(),Messages.KeepRules,null,
			    				  Messages.PreserveOldRules,
			    				  MessageDialog.QUESTION_WITH_CANCEL, new String[]{IDialogConstants.YES_LABEL,
			    			  IDialogConstants.NO_LABEL,IDialogConstants.CANCEL_LABEL}, 0);
			    		  int result;
			    		  if(dataController.getLinkedCAE().getContextTableCombinations(false) == null){
			    			  result = 1;
			    		  }else{
			    			  result = dialog.open();
			    		  }
			    		  final int code = result;
			    		  if (code != 2) {

			    			String location = xstampp.Activator.getDefault().getPreferenceStore().getString("ACTS_Path");
			    			
				    		  // If the Path for ACTS is not set, the PreferencePage opens
				    		  if(location.isEmpty()){
				    			  File root = new File(Platform.getInstallLocation().getURL().getFile() + File.separator +"features");
				    				for (File feature : root.listFiles()) {
				    					if(location.isEmpty() && feature.getName().startsWith("xstpa")){
				    						for(File algFile:feature.listFiles()){
				    							if(algFile.getName().contains("acts")){
				    								location = algFile.getAbsolutePath();
				    								break;
				    							}
				    						}
				    					}
				    				}
				    		  }
				    		  if (location.isEmpty()) {
				    			  Map<String,String> values=new HashMap<>();
				    			  values.put("preferencePageId", PreferencePageSettings.ID); //$NON-NLS-1$
				    	    	  	STPAPluginUtils.executeParaCommand("org.eclipse.ui.window.preferences", values); //$NON-NLS-1$
				    	    	  	location = xstampp.Activator.getDefault().getPreferenceStore().getString("ACTS_Path");
				    		  }
					    	  
					    	  final ACTSController job = new ACTSController(contextTable.getColumnCount(), dataController,location);
					    	  job.addJobChangeListener(new JobChangeAdapter() {
					    		  @Override
					    		  public void done(IJobChangeEvent event) {
					    			  if(event.getResult().equals(Status.OK_STATUS)){
						    			  Display.getDefault().asyncExec(new Runnable() {
											
											@Override
											public void run() {
												// creates the correct number of rows for the context table
										    	  createTableColumns();
								    			  storeEntrys(job.getEntryList(), code == 0);
											}
						    			  });
					    			  }
					    		  }
					    	  });
					    	  job.schedule();
					    	  
			    		  }
			    	  }
			    	  else {
			    		  MessageDialog.openInformation(null, Messages.LinkSomeVariables,
			    				  Messages.LinkVariablesMessage);
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
							
							// add the New Variable
							ContextTableCombination temp = (ContextTableCombination) event.data;
				    		temp.setLinkedControlActionName(dataController.getLinkedCAE().getControlAction(),
				    				dataController.getLinkedCAE().getId());
							contextRightContent.add(temp);
							// set the ContextTableCombinations
							dataController.getLinkedCAE().addContextTableCombination(temp);
							// refresh the Viewer
							dataController.storeBooleans(null, ObserverValue.COMBINATION_STATES);
							contextTableViewer.refresh();
							
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
		    		contextRightContent.remove(contextTable.getSelectionIndex());
		    		contextTable.remove(contextTable.getSelectionIndex());

		    		dataController.storeBooleans(null, null);
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
	  	controlActionViewer.setInput(contextTableInput);
	  	if(contextTableInput.isEmpty()){
    		//if the context table input is empty than there is nothing to be shown
    		return;
    	}
		  
		controlActionTable.select(formerIndex);
		if(controlActionTable.getSelectionIndex() == -1){
			//if the former index returns cannot be selected try 0 
			controlActionTable.select(0);
		}
		ControlActionEntry entry = contextTableInput.get(controlActionTable.getSelectionIndex());
		dataController.setLinkedCAE(entry.getId());
		
		refreshTable();
	}
	
	/**
	 * Create the correct columns in the context table
	 */
	public void createTableColumns() {
		for (TableColumn column : contextTable.getColumns()) {
			column.dispose();
		}
  		if(dataController.getLinkedCAE() == null){
  			return;
  		}
  		if (!dataController.getLinkedCAE().getLinkedItems().isEmpty()) {
  			new TableColumn(contextTable, SWT.NONE).setText(View.ENTRY_ID);
  			// creates new TableColumns dynamically so that the context table has the right size (and labels)
  			for (int i=0;i<dataController.getLinkedCAE().getLinkedItems().size();i++) {
  				UUID id = dataController.getLinkedCAE().getLinkedItems().get(i).getId();
  				new TableColumn(contextTable, SWT.NONE).setText(dataController.getModel().getComponent(id).getText());
  			}
	  	
  			if (dataController.isControlActionProvided()) {
  				new TableColumn(contextTable, SWT.NONE).setImage(View.HEADER);
  			}else{
  				new TableColumn(contextTable, SWT.NONE).setText(View.IS_HAZARDOUS);
  			}
  			
  			// Create the cell editors
  		  	CellEditor[] contextEditors = new CellEditor[contextTable.getColumnCount()];
  		  	
  		  	for (int i = 1; i<contextTable.getColumnCount()-1;i++) {
  		  		// creates the cell editors
  		  		if ((i != 0) &(i!=contextTable.getColumnCount()-1)) {
  		  			contextEditors[i] = new TextCellEditor(contextTable);
  		  		}
  		  	}
  		  	View.contextProps = new String[contextTable.getColumnCount()];
  			for (int i=0; i<View.contextProps.length;i++) {
  				View.contextProps[i] = contextTable.getColumn(i).getText();
  			}
  			contextTableViewer.setColumnProperties(View.contextProps);
  			contextTableViewer.setCellEditors(contextEditors);
  		}

	  	
	  	
	}
	
	/**
	 * help method for the filter combo which filters the context table entries
	 * by the given property
	 * 
	 * @param filter a string describing the property
	 */
	public void showContent(String filter, List<ContextTableCombination> input) {
		ArrayList<ContextTableCombination> content = new ArrayList<>();
		// checks which option is selected and shows the right content
  	  if (filter.equals(FILTER_SHOW_ALL)) {
  		  contextTableViewer.setInput(input);
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
  		  contextTableViewer.setInput(content);
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
  		  contextTableViewer.setInput(content);
  	  }
  	  // packs the columns
  	  for (int j = 0, n = contextTable.getColumnCount(); j < n; j++) {
  		  contextTable.getColumn(j).pack();	    		  		  
  	  }
    }
	
	private void storeEntrys(List<ContextTableCombination> entrys, boolean keepOldCombies){
		if(keepOldCombies && dataController.getLinkedCAE().getContextTableCombinations(false) != null){
			for(ContextTableCombination variable: dataController.getLinkedCAE().getContextTableCombinations(false)){
				if(variable.getGlobalHazardous()){
					variable.setArchived(true);
					entrys.add(variable);
				}
			}
		}
		dataController.getLinkedCAE().setContextTableCombinations(entrys);
		dataController.storeBooleans(null, ObserverValue.CONTROL_ACTION);
		refreshTable();
	}
	
	@Override
	public void activate() {
		
  		  refreshTable();
  	  	// packs the columns
  		for (int i = 0, n = controlActionTable.getColumnCount(); i < n; i++) {
  			controlActionTable.getColumn(i).setWidth(controlActionTable.getSize().x);
  		}

  	  setVisible(true);
	}

	@Override
	public boolean refreshTable() {
		if(contextTableViewer.getControl() == null || contextTableViewer.getControl().isDisposed()){
			//if the context table is null or disposed than this method return false
			return false;
		}
		// create Input for contextTableViewer
		List<ControlActionEntry> contextTableInput= new ArrayList<ControlActionEntry>();
    	for (ControlActionEntry entry : dataController.getDependenciesIFProvided()) {
    		if (entry.getSafetyCritical()) {
    			contextTableInput.add(entry);
    		}
    	}
    	controlActionViewer.setInput(contextTableInput);
    	return updateContextTable();
	}

	private boolean updateContextTable(){
		contextTableViewer.setInput(null);
		//if there are no control action entrys available then the input and linked control action is set to null
    	if(controlActionViewer.getInput() == null || ((List<?>)controlActionViewer.getInput()).isEmpty()){
    		dataController.setLinkedCAE(null);
    		contextRightContent = null;
    	}
    	else{
    		//if the entry set is not empty but there is no selected entry in the list than the first entry is selected 
    		controlActionTable.select(Math.max(0, controlActionTable.getSelectionIndex()));
    		ControlActionEntry entry = (ControlActionEntry) controlActionTable.getSelection()[0].getData();
    		dataController.setLinkedCAE(contextContentFolder.getSelectionIndex() == 0,entry.getId());
    		if(dataController.getLinkedCAE() != null){
    			contextRightContent = dataController.getLinkedCAE().getContextTableCombinations(true);
    		}
    	}
    	
		Display.getCurrent().asyncExec(new Runnable() {
			
			@Override
			public void run() {
				
				createTableColumns();
				if(contextRightContent == null || contextRightContent.isEmpty()){
					contextTableViewer.setInput(null);
		    		writeStatus(Messages.NOTestsetsAvailable);
				}else{
					writeStatus(null); //$NON-NLS-1$
					showContent(filterCombo.getText(), contextRightContent);
				}
			}
		});
		return true;
	}
	private void setConflictLabel() {
		final ContextCheckJob checkJob =new ContextCheckJob("Check Value Combinations",dataController);
		
		checkJob.addJobChangeListener(new JobChangeAdapter(){
			@Override
			public void done(IJobChangeEvent event) {
				if(event.getResult() ==Status.OK_STATUS){
					if(checkJob.getConflictCounter() > 0){
			   		 	errorLabel.setText(Messages.Param_ConflictsMsg +checkJob.getConflictCounter()+ " Conflicts!");
			   	 	}else{
			   		 	errorLabel.setText(""); //$NON-NLS-1$
			   	 	}
					contextTableViewer.refresh();
				}
			}
		});
		checkJob.schedule();
		
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
			if(controlActionTable != null && !controlActionTable.isDisposed()){
				int formerIndex = controlActionTable.getSelectionIndex();
				
				updateContextInput(formerIndex);
			}
			break;
		}
		default:
			break;
		}

		
	}
	
	private void updateMenu(final int index){
		for(MenuItem item : contextTable.getMenu().getItems()){
			item.dispose();
		}
		MenuItem dontcareItem = new MenuItem(contextTable.getMenu(), SWT.PUSH);
		dontcareItem.setText(dataController.getModel().getIgnoreLTLValue().getText()); //$NON-NLS-1$
		dontcareItem.setData(dataController.getModel().getIgnoreLTLValue().getId());
	    
		final UUID id = dataController.getLinkedCAE().getLinkedItem(index).getId();
		for(IRectangleComponent comp : dataController.getModel().getComponent(id).getChildren()){
			MenuItem newItem = new MenuItem(contextTable.getMenu(), SWT.PUSH);
			newItem.setText(comp.getText()); //$NON-NLS-1$
			newItem.setData(comp.getId());
		}
		for(MenuItem item : contextTable.getMenu().getItems()){
			item.addSelectionListener(new SelectionAdapter() {
		    	public void widgetSelected(SelectionEvent event) {
		    		//this listener uses the information contextTableCellX which provides the current table item index
		    		//calculated in the contextRightTable.mouseListener
	    			ContextTableCombination contextCombie = (ContextTableCombination) contextTable.getSelection()[0].getData();
	    			contextCombie.addValueMapping(id, ((UUID)((MenuItem)event.getSource()).getData()));
	    			dataController.storeBooleans(null, ObserverValue.CONTROL_ACTION);
		    	}
		    });
		}
	}
}
