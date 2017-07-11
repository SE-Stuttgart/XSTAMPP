/*******************************************************************************
 * Copyright (c) 2013, 2017 Lukas Balzer, Asim Abdulkhaleq, Stefan Wagner
 * Institute of Software Technology, Software Engineering Group
 * University of Stuttgart, Germany
 *  
 * All rights reserved. This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License v1.0 which
 * accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package xstampp.stpapriv.util.jobs;

import java.awt.Desktop;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;

import messages.Messages;
import xstampp.astpa.model.controlaction.interfaces.IControlAction;
import xstampp.model.AbstractLTLProvider;
import xstampp.model.IDataModel;
import xstampp.model.IValueCombie;
import xstampp.stpapriv.messages.SecMessages;
import xstampp.stpapriv.model.PrivacyController;

public class XCSVExportJob extends Job {
	/**
	 * 
	 * @author Lukas Balzer
	 */
	public static final ArrayList<String> STEPS = new ArrayList<String>(){

		
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		{
			this.add(Messages.ContextTables);
			this.add(Messages.LTLFormulasTable);
			this.add("Refined Rules Table");
			this.add(SecMessages.RefinedSecurityConstraintsTable);
			this.add(SecMessages.RefinedUnsecureControlActions);
		}
		
	};
	public static final int CONTEXT_TABLES = 1 << 0;
	public static final int LTL_FORMULAS = 1 << 1;
	public static final int RULES_TABLE = 1 << 2;
	public static final int REFINED_CONSTRAINTS = 1 << 3;
	public static final int REFINED_UCA = 1 << 4;
	public static final int REFINED_DATA = CONTEXT_TABLES | LTL_FORMULAS |REFINED_CONSTRAINTS | REFINED_UCA | RULES_TABLE;

	private PrivacyController controller;
	private int tableConstant;
	private String filepath;
	private String seperator = ";";
	private boolean enablePreview;

	public XCSVExportJob(String name, String filePath, char seperator2,
			IDataModel model, int tableConstant) {
		super(name);

		Assert.isLegal(model instanceof PrivacyController,
						"This Export can only be executed for a data model of type PrivacyController");
		this.controller = (PrivacyController) model;
		this.tableConstant = tableConstant;
		this.filepath = filePath;
	}

	@Override
	protected IStatus run(IProgressMonitor monitor) {
		
		try {

				File tableCSV=new File(filepath);
				PrintWriter writer = new PrintWriter(tableCSV);
				if((tableConstant & CONTEXT_TABLES) != 0){
					getContextTableString(writer);
				}if((tableConstant & RULES_TABLE) != 0){
					getRulesTableString(writer);
				}if((tableConstant & REFINED_CONSTRAINTS) != 0){
					getRefinedConstraintsString(writer);
				}if((tableConstant & LTL_FORMULAS) != 0){
					getLTLTableString(writer);
				}if((tableConstant & REFINED_UCA) != 0){
					getRUCATableString(writer);
				}
				writer.close();
				if (this.enablePreview && tableCSV.exists() && Desktop.isDesktopSupported()) {
					Desktop.getDesktop().open(tableCSV);
				}
		} catch (FileNotFoundException e) {
			
			e.printStackTrace();
			return Status.CANCEL_STATUS;
		} catch (UnsupportedEncodingException e) {
			
			e.printStackTrace();
			return Status.CANCEL_STATUS;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return Status.OK_STATUS;
	}

	private void getContextTableString(PrintWriter writer){
		writer.println(Messages.ContextTables + " of project " + controller.getProjectName());
		writer.println();
		for(IControlAction controlAction: controller.getAllControlActions()){
			//Context Table for context provided
			writer.println("Context Table of control action " + controlAction.getTitle() + "in context provided");
			
			writer.print(Messages.ControlAction + seperator);
			for(UUID variableID : controller.getCAProvidedVariables(controlAction.getId())){
				writer.print(seperator);
			}
			writer.print("Vulnerable " + seperator);
			writer.print("when" + seperator);
			writer.print("provided");
			writer.println();
			
			writer.print(controlAction.getTitle() + seperator);
			for(UUID variableID : controller.getCAProvidedVariables(controlAction.getId())){
				writer.print(controller.getComponent(variableID).getText() + seperator);
			}
			writer.print("at any time" + seperator);
			writer.print("too early" + seperator);
			writer.print("too late");
			writer.println();
			for(IValueCombie combie: controller.getIvaluesWhenCAProvided(controlAction.getId())){
				if(combie.getValueList().size() != controller.getCAProvidedVariables(controlAction.getId()).size()){
					continue;
				}
				writer.print(controlAction.getTitle() + seperator);
				for(UUID valueID : combie.getValueList()){
					writer.print(controller.getComponent(valueID).getText() + seperator);
				}
				for(String type: new String[]{IValueCombie.TYPE_ANYTIME,IValueCombie.TYPE_TOO_EARLY,IValueCombie.TYPE_TOO_LATE}){
					if(combie.isCombiHazardous(type)){
						writer.print("yes" + seperator);
					}else{
						writer.print("no" + seperator);
					}
				}
				writer.println();
			}
			writer.println();
			
			writer.println("Context Table of control action " + controlAction.getTitle() + "in context not provided");
			writer.print(controlAction.getTitle() + seperator);
			for(UUID variableID : controller.getCANotProvidedVariables(controlAction.getId())){
				writer.print(controller.getComponent(variableID).getText() + seperator);
			}
			writer.print("Vulnerable");
			writer.println();
			
			for(IValueCombie combie: controller.getIValuesWhenCANotProvided(controlAction.getId())){
				if(combie.getValueList().size() != controller.getCANotProvidedVariables(controlAction.getId()).size()){
					continue;
				}
				writer.print(controlAction.getTitle() + seperator);
				for(UUID valueID : combie.getValueList()){
					writer.print(controller.getComponent(valueID).getText() + seperator);
				}
				if(combie.isCombiHazardous(IValueCombie.TYPE_NOT_PROVIDED)){
					writer.print("yes" + seperator);
				}else{
					writer.print("no" + seperator);
				}
				writer.println();
			}
			writer.println();
		}
	}

	private void getLTLTableString(PrintWriter writer){
		writer.println(Messages.LTLFormulasTable +" of project " + controller.getProjectName());
		writer.print("ID" +seperator);
		writer.println("LTL Formulas");
		for(AbstractLTLProvider provider: controller.getLTLPropertys()){
			writer.print("SPR1." +provider.getNumber() +seperator);
			writer.println(provider.getLtlProperty());
		}
		writer.println();
	}
	private void getRulesTableString(PrintWriter writer){
		writer.println(Messages.RulesTable + " of project " + controller.getProjectName());
		writer.print("ID" +seperator);
		writer.print("Type" +seperator);
		writer.print("Links" +seperator);
		writer.println(Messages.RulesTable);
		for(AbstractLTLProvider provider: controller.getLTLPropertys()){
			writer.print("RPR1."+provider.getNumber() +seperator);
			writer.print(provider.getType() +seperator);
			writer.print(provider.getLinks() +seperator);
			writer.println(provider.getRefinedSafetyConstraint());
		}
		writer.println();
	}
	private void getRUCATableString(PrintWriter writer){
		writer.println(SecMessages.RefinedUnsecureControlActions + " of project " + controller.getProjectName());
		ArrayList<AbstractLTLProvider> list_notProvided;
		ArrayList<AbstractLTLProvider> list_provided;
		ArrayList<AbstractLTLProvider> list_wrongProvided;

		writer.print(Messages.ControlAction);
		writer.print("Vulnerable if not provided");
		writer.print("Vulnerable if provided");
		writer.print("Vulnerable if wrong provided");
		for(IControlAction action : controller.getAllControlActionsU()){

			list_notProvided = new ArrayList<>();
			list_provided = new ArrayList<>();
			list_wrongProvided = new ArrayList<>();
			for(AbstractLTLProvider provider: action.getAllRefinedRules()){
				if(provider.getType().equals(IValueCombie.TYPE_NOT_PROVIDED)){
					list_notProvided.add(provider);
				}else if(provider.getType().equals(IValueCombie.TYPE_ANYTIME)){
					list_provided.add(provider);
				}else if(provider.getType().equals(IValueCombie.TYPE_TOO_EARLY)){
					list_wrongProvided.add(provider);
				}else if(provider.getType().equals(IValueCombie.TYPE_TOO_LATE)){
					list_wrongProvided.add(provider);
				}
			}
			writer.println();
			writer.println(action.getTitle() + seperator + seperator + seperator + seperator);
			int loopSize = Math.max(list_notProvided.size(), list_provided.size());
			loopSize = Math.max(loopSize, list_wrongProvided.size());
			for(int i=0;i<loopSize;i++){
				
				writer.print(seperator);
				writer.print(getRucaID(list_notProvided, i));
				writer.print(getRucaID(list_provided, i));
				writer.print(getRucaID(list_wrongProvided, i));
				
				writer.println();
				writer.print(seperator);
				//print line of ruca descriptions
				writer.print(getRUCA(list_notProvided, i));
				writer.print(getRUCA(list_provided, i));
				writer.print(getRUCA(list_wrongProvided, i));
				
				writer.println();
				writer.print(seperator);
				//
				writer.print(getLinks(list_notProvided, i));
				writer.print(getLinks(list_provided, i));
				writer.print(getLinks(list_wrongProvided, i));
				writer.println();
				writer.println(seperator + seperator + seperator + seperator);
				
			}
		}
		
		writer.println();
	}
	private String getLinks(List<AbstractLTLProvider> list, int index){
		if(index < list.size() && list.get(index).getLinks() != null){
			return list.get(index).getLinks() + seperator;
		}
		return seperator;
		
	}
	private String getRUCA(List<AbstractLTLProvider> list, int index){
		if(index < list.size()){
			return list.get(index).getRefinedUCA() + seperator;
		}
		return seperator;
		
	}
	private String getRucaID(List<AbstractLTLProvider> list, int index){
		if(index < list.size()){
			return "RPCA1." +list.get(index).getNumber() + seperator;
		}
		return seperator;
		
	}
	private void getRefinedConstraintsString(PrintWriter writer){
		writer.println(SecMessages.RefinedSecurityConstraintsTable + " of project " + controller.getProjectName());
		writer.print("ID" +seperator);
		writer.println("Refined Privacy Constraint");
		for(AbstractLTLProvider provider: controller.getLTLPropertys()){
			writer.print("PC3." +provider.getNumber() +seperator);
			writer.println(provider.getRefinedSafetyConstraint());
		}
		writer.println();
	}

	public void setEnablePreview(boolean enablePreview) {
		this.enablePreview = enablePreview;
	}
}
