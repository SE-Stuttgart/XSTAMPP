/*******************************************************************************
 * Copyright (c) 2013, 2016 Lukas Balzer, Asim Abdulkhaleq, Stefan Wagner
 * Institute of Software Technology, Software Engineering Group
 * University of Stuttgart, Germany
 *  
 * All rights reserved. This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License v1.0 which
 * accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package xstpapriv.model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;

import xstampp.ui.common.ProjectManager;
import xstpapriv.settings.PreferenceInitializer;
import xstpapriv.settings.XSTPAPreferenceConstants;
import xstpapriv.ui.dialogs.EditWindow;

public class ACTSController extends Job{
	
	private final static String INPUT = Platform.getInstanceLocation().getURL().getPath()+".metadata"+File.separator+"input.txt";

	private final static String OUTPUT =  Platform.getInstanceLocation().getURL().getPath()+".metadata"+File.separator+"output.txt";

	private List<ContextTableCombination> entryList;
	private List<ProcessModelValue> valueList;

	private ControlActionEntry context;

	private int columns;

	private String location;
	
	public ACTSController(int columns,XSTPADataController controller,String location) {
		super("Calculating Combinations..");
		if(!PreferenceInitializer.store.contains(XSTPAPreferenceConstants.ACTS_ALGORITHMUS)){
		
        	PreferenceInitializer.store.setValue(XSTPAPreferenceConstants.ACTS_STRENGTH,1);
        	PreferenceInitializer.store.setValue(XSTPAPreferenceConstants.ACTS_ALGORITHMUS,"ipog");
        	PreferenceInitializer.store.setValue(XSTPAPreferenceConstants.ACTS_MODE,"scratch");
        	PreferenceInitializer.store.setValue(XSTPAPreferenceConstants.ACTS_CHANDLER,"forbiddentuples");
		}

		this.context = controller.getLinkedCAE();
		this.valueList = controller.getValuesList(true);
		this.columns = context.getLinkedItems().size();
		this.location = location;
	}
	
	@Override
	protected IStatus run(IProgressMonitor monitor) {
		// Run ACTS in a separate system process
		File algFile = new File(location);
		if(!algFile.exists()){
			ProjectManager.getLOGGER().error("Context Table generation failed because the given location for the algorithm was wrong");
			return Status.CANCEL_STATUS;
		}
		try {
			Process proc;
			String modes;
			String alg = PreferenceInitializer.store.getString(XSTPAPreferenceConstants.ACTS_ALGORITHMUS);
        	int strength = PreferenceInitializer.store.getInt(XSTPAPreferenceConstants.ACTS_STRENGTH);
        	String mode = PreferenceInitializer.store.getString(XSTPAPreferenceConstants.ACTS_MODE);
        	String chandler = PreferenceInitializer.store.getString(XSTPAPreferenceConstants.ACTS_CHANDLER);
        	
			if (strength > columns) {
				strength = columns;	
			}
			
			modes = " -Dalgo="+alg+" ";
			
			if (columns-2 <= 6) {
				modes = " -Ddoi=" + Integer.toString(columns-2) + " ";	
			}
			if (EditWindow.relations.isEmpty()) {
				modes = modes.concat("-Ddoi="+strength+" ");
			}
			else {
				modes = modes.concat("-Ddoi=-1 ");
			}
			modes = modes.concat("-Dmode="+mode+" ");
			
			modes = modes.concat("-Dchandler="+chandler+" ");
				
				
				// clear so that the default mode gets selected again
			

			//location = location.substring(1, location.length());
			writeFile(context.getLinkedItems());
			proc = Runtime.getRuntime().exec("java"+modes+"-jar " +location+" cmd "+INPUT+ " " + OUTPUT);
			
			// Then retreive the process output
			InputStream in = proc.getInputStream();
			proc.getErrorStream();
			BufferedReader reader = new BufferedReader(new InputStreamReader(in));
	        StringBuilder out = new StringBuilder();
	        String line;
	        while ((line = reader.readLine()) != null) {
	            out.append(line);
	        }
	        String message = out.toString();
	        System.out.println(message);   //Prints the string content read from input stream
	        reader.close();
	        if(message.contains("cancelled")){
	        	return Status.CANCEL_STATUS;
	        }
	        entryList = getEntrysFromFile(reader,context);
			return Status.OK_STATUS;
		}catch (IOException e) {
			entryList = new ArrayList<>();
			return Status.CANCEL_STATUS;
		}
		
		
	}
	
	
	private List<ContextTableCombination> getEntrysFromFile (BufferedReader reader,ControlActionEntry context) {
		List<ContextTableCombination> contextEntries = new ArrayList<ContextTableCombination>();
		try {
			reader = new BufferedReader(new FileReader(OUTPUT));
			
			// go to the fourth line in the file to read the number of Configurations
			reader.readLine();
			reader.readLine();
			String line = reader.readLine();
			String parameter_nr=new String();
			for(char c :line.toCharArray()){
				if(Character.isDigit(c)){
					parameter_nr = parameter_nr + c;
				}
			}
			
			int paramCount = Integer.parseInt(parameter_nr);
			
			char temp = line.charAt(line.length()-1);
			
			line = reader.readLine();
			temp = line.charAt(line.length()-1);
			Character.getNumericValue(temp);

			while ((line = reader.readLine()) != null) {
				if (line.contains("Configuration #")) {
					
					ContextTableCombination entry = new ContextTableCombination();
					entry.setLinkedControlActionName(context.getControlAction(), null);
					entry.setLinkedControlActionID(context.getId());
					temp = line.charAt(line.length()-2);
					Character.getNumericValue(temp);
					reader.readLine();
					entry.clearIDsMap();
					entry.clearNameMap();
					UUID valueID,variableID;
					String valueName,variableName;
					for (int i = 0; i<paramCount; i++) {
						valueID = null;
						variableID = null;
						valueName = null;
						variableName = null;
						line = reader.readLine();
						temp = line.charAt(0);
						Character.getNumericValue(temp);
						try {
							line = line.substring(line.indexOf("=")+2, line.length());
							String var = line.substring(0, line.indexOf("="));
							for(ProcessModelVariables linkedVariable:context.getLinkedItems()){
								if(linkedVariable.getName().replaceAll(" ","_").equals(var)){
									variableName = linkedVariable.getName();
									variableID = linkedVariable.getId();
									var =linkedVariable.getName();
									break;
								}
							}
							
							//entry.setName(line.substring(line.indexOf("=")+2, line.length()));
							line = line.substring(line.indexOf("=")+1, line.length());
							for(ProcessModelValue value : valueList){
								if(value.getPMV() != null && value.getPMV().equals(var) && value.getValueText().replaceAll(" |,","_").equals(line)){
									valueName = value.getValueText();
									valueID = value.getId();
									continue;
								}
							}

							if(valueID == null || valueName == null || variableID == null || variableName == null){
								
							}else{
								entry.addValueMapping(variableID, valueID);
							}

						}
						catch (StringIndexOutOfBoundsException siobe) {
							
						}
						
									
					}
					contextEntries.add(entry);
				}
				
			}
		} catch (IOException e) {			
			e.printStackTrace();
		}
		return contextEntries;
	}
	
	/**
	 * Writing the later used input file for ACTS
	 * @param items the ProcessModelVariables that where linked to the selected control action 
	 * 				in the currently active context
	 * @return whether the file could be written successfully or not 
	 */
	private boolean writeFile(List<ProcessModelVariables> items) {
		
		try {
			PrintWriter writer = null;
			String paramName = null;
			writer = new PrintWriter(INPUT, "UTF-8");
			
			writer.println("[System]");
			
			writer.println("Name : test");
			// Print the Parameters
			writer.println("");
			writer.println("[Parameter]");
			for (ProcessModelVariables entry : items) {
				List<String> values = entry.getValues();
				paramName = entry.getName().replace(" ", "_");
				
				writer.write(paramName+ " (enum)" + " : ");
				for (int i = 0, size = entry.getSizeOfValues();i<size; i++) {
					writer.write(values.get(i).replaceAll(" |,", "_"));
					if (i < entry.getSizeOfValues()-1) {
						writer.write(", ");
					}
				}
				writer.println("");
				
			}
			// Print the Relations
			writer.println("");
			writer.println("[Relation]");
			for (int entry = 0; entry<EditWindow.relations.size(); entry++) {
				String temp = "";
		    	List<String> tempList = EditWindow.relations.get(entry).getVariables();
		    	for (int i =0; i<tempList.size(); i++) {
		    		
		    		if (i == tempList.size()-1) {
		    			temp = temp.concat(tempList.get(i).replace(" ", "_"));
		    		}
		    		else {
		    			temp = temp.concat(tempList.get(i).replace(" ", "_").concat(", "));
		    		}
		    	}
				writer.println("R"+entry+ " : ("+temp+", "+EditWindow.relations.get(entry).getStrength()+")");
			}
			
			// Print the Constraints
			writer.println("");
			writer.println("[Constraint]");
			
			for (String entry : EditWindow.constraints) {
				writer.println(entry);
			}
			writer.println("");
			
			writer.close();
			return true;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return false;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * @return the entryList
	 */
	public List<ContextTableCombination> getEntryList() {
		return this.entryList;
	}
}
