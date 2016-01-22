package export;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

public class CsvExport {
	
	private String filepath;
	private ExportContent exportContent;
    private final String crlf = "\r\n";
    private String delimiter = ";"; 
	
	public CsvExport (String filepath, ExportContent exportContent) {
		this.filepath = filepath;
		this.exportContent = exportContent;
		
		writeRefinedSafetyTable();
		writeContextTable();
		writeLtlTable();
	}
	
	public Boolean writeRefinedSafetyTable() {
		PrintWriter writer = null;
		int counter = 1;
		try {
			
				writer = new PrintWriter(filepath+"RefinedSafetyTable.csv", "UTF-8");

		} catch (FileNotFoundException e) {
			
			e.printStackTrace();
			return false;
		} catch (UnsupportedEncodingException e) {
			
			e.printStackTrace();
			return false;
		}
		// Writes the Headers of the TableColumns
		writer.print("sep=;" + crlf);

		writer.print("ID" + delimiter);
		writer.print("Control_Action" + delimiter);
		writer.print("Context" + delimiter);
		writer.print("Critical_Combinations" + delimiter);
		writer.print("Refined_Safety_Requirements" + delimiter + crlf);
		
		for (int i=0; i<exportContent.getProvidedCA().size();i++) {
			
			for (int j=0; j<exportContent.getProvidedCA().get(i).getContextTableCombinations().size(); j++) {
				if (exportContent.getProvidedCA().get(i).getContextTableCombinations().get(j).getHAnytime() == true) {
					// Prints the ID
					writer.print(counter + delimiter);
					
					// Prints the Control Action
					writer.print(exportContent.getProvidedCA().get(i).getContextTableCombinations().get(j).getLinkedControlActionName() + delimiter);
					
					// Prints the Context
					writer.print("Provided" + delimiter);
					
					// Prints the critical Combinations
					writer.print(exportContent.getProvidedCA().get(i).getContextTableCombinations().get(j).getPmValues("==", false) + delimiter);
					
					// Prints the SafetyRequirements
					writer.print(exportContent.getProvidedCA().get(i).getContextTableCombinations().get(j).getRefinedSafetyRequirements() + delimiter + crlf);
					
					// Counts Up, this is for the ID
					counter++;
				}
			}
			
		}
		
		for (int i=0; i<exportContent.getProvidedCA().size();i++) {
			
			for (int j=0; j<exportContent.getNotProvidedCA().get(i).getContextTableCombinations().size(); j++) {
				if (exportContent.getNotProvidedCA().get(i).getContextTableCombinations().get(j).getGlobalHazardous() == true) {
					// Prints the ID
					writer.print(counter + delimiter);
					
					// Prints the Control Action
					writer.print(exportContent.getNotProvidedCA().get(i).getContextTableCombinations().get(j).getLinkedControlActionName() + delimiter);
					
					// Prints the Context
					writer.print("Provided" + delimiter);
					
					// Prints the critical Combinations
					writer.print(exportContent.getNotProvidedCA().get(i).getContextTableCombinations().get(j).getPmValues("==", false) + delimiter);
					
					// Prints the SafetyRequirements
					writer.print(exportContent.getNotProvidedCA().get(i).getContextTableCombinations().get(j).getRefinedSafetyRequirements() + delimiter + crlf);
					
					// Counts Up, this is for the ID
					counter++;
				}
			}
			
		}

		writer.flush();
		writer.close();
		return true;
	}
	
	public Boolean writeContextTable() {
		PrintWriter writer = null;
		Boolean headerExists = false;
		Boolean headerExists2 = false;
		int counter = 1;
		int counter2 = 1;
		try {
			
				writer = new PrintWriter(filepath+"ContextTable.csv", "UTF-8");

		} catch (FileNotFoundException e) {
			
			e.printStackTrace();
			return false;
		} catch (UnsupportedEncodingException e) {
			
			e.printStackTrace();
			return false;
		}
		// Information for Excel
		writer.print("sep=;" + crlf);
		// Info, that Context Provided table is printed
		writer.print("CONTEXT_PROVIDED" + delimiter + crlf);

		// Write the Data to csv
		for (int i=0; i<exportContent.getProvidedCA().size();i++) {
			
			for (int j=0; j<exportContent.getProvidedCA().get(i).getContextTableCombinations().size(); j++) {
				if ((!exportContent.getProvidedCA().get(i).getContextTableCombinations().isEmpty())){
					if ((!headerExists)) {
						
						// Write the Control Action Name
						writer.print(exportContent.getProvidedCA().get(i).getControlAction().toUpperCase() + delimiter +crlf);
						
						// Create the first Column for CA Provided
						writer.print("ID" + delimiter);
						
						for (int z=0; z<exportContent.getProvidedCA().get(i).getContextTableCombinations().get(j).getPmVariables().size(); z++) {
							writer.print(exportContent.getProvidedCA().get(i).getContextTableCombinations().get(j).getPmVariables().get(z) + delimiter);
						}
						writer.print("Hazardous_Anytime" + delimiter);
						writer.print("Hazardous_To_Early" + delimiter);
						writer.print("Hazardous_To_Late" + delimiter + crlf);
						
						headerExists=true;
					}
					
					
					// Prints the ID
					writer.print(counter + delimiter);
					
					// Prints the Values
					for (int z=0; z<exportContent.getProvidedCA().get(i).getContextTableCombinations().get(j).getSizeOfValues(); z++) {
						writer.print(exportContent.getProvidedCA().get(i).getContextTableCombinations().get(j).getValues().get(z) + delimiter);
					}
					
					// Prints the value of hAnytime
					writer.print(exportContent.getProvidedCA().get(i).getContextTableCombinations().get(j).getHAnytime().toString() + delimiter);
						
					// Prints the value of hEarly
					writer.print(exportContent.getProvidedCA().get(i).getContextTableCombinations().get(j).getHEarly().toString() + delimiter);
						
					// Prints the value of hLate
					writer.print(exportContent.getProvidedCA().get(i).getContextTableCombinations().get(j).getHLate().toString() + delimiter + crlf);
					
					// Counts Up, this is for the ID
					counter++;
				}
				
			}
			headerExists=false;
			// Blank Space for the Looks
			writer.print("" + crlf);
			
		}
		
		// Leave 2 Rows Blank, then Start new Table
		writer.print("" + crlf);
		// Info, that Context Not Provided table is printed
		writer.print("CONTEXT_NOT_PROVIDED" + delimiter + crlf);
		
		
		// Write the Data to csv
		for (int i=0; i<exportContent.getNotProvidedCA().size();i++) {
			
			for (int j=0; j<exportContent.getNotProvidedCA().get(i).getContextTableCombinations().size(); j++) {
				
				if (!exportContent.getNotProvidedCA().get(i).getContextTableCombinations().isEmpty()){
					if (!headerExists2) {
						
						// Write the Control Action Name
						writer.print(exportContent.getNotProvidedCA().get(i).getControlAction().toUpperCase() + delimiter +crlf);
						
						// Create the first Column for CA Not Provided
						writer.print("ID" + delimiter);
						
						for (int z=0; z<exportContent.getNotProvidedCA().get(i).getContextTableCombinations().get(j).getPmVariables().size(); z++) {
							writer.print(exportContent.getNotProvidedCA().get(i).getContextTableCombinations().get(j).getPmVariables().get(z) + delimiter);
						}
						writer.print("Hazardous" + delimiter + crlf);
	
						
						headerExists2=true;
					}
					
					
					// Prints the ID
					writer.print(counter2 + delimiter);
					
					// Prints the Values
					for (int z=0; z<exportContent.getNotProvidedCA().get(i).getContextTableCombinations().get(j).getSizeOfValues(); z++) {
						writer.print(exportContent.getNotProvidedCA().get(i).getContextTableCombinations().get(j).getValues().get(z) + delimiter);
					}
					
					// Prints the value of hazardous
					writer.print(exportContent.getNotProvidedCA().get(i).getContextTableCombinations().get(j).getGlobalHazardous().toString() + delimiter + crlf);
					
					// Counts Up, this is for the ID
					counter2++;
				}
				
			}
			headerExists2=false;
			// Blank Space for the Looks
			writer.print("" + crlf);
			
		}

		writer.flush();
		writer.close();
		return true;
	}
	
	public Boolean writeLtlTable() {
		PrintWriter writer = null;
		int counter = 1;
		try {
			
				writer = new PrintWriter(filepath+"LtlTable.csv", "UTF-8");

		} catch (FileNotFoundException e) {
			
			e.printStackTrace();
			return false;
		} catch (UnsupportedEncodingException e) {
			
			e.printStackTrace();
			return false;
		}
		// Information for Excel
		writer.print("sep=;" + crlf);
		// Writes the Headers of the TableColumns
		writer.print("ID" + delimiter);
		writer.print("LTL Formula" + delimiter + crlf);
		
		for (int i=0; i<exportContent.getProvidedCA().size();i++) {
			
			for (int j=0; j<exportContent.getProvidedCA().get(i).getContextTableCombinations().size(); j++) {
				if (exportContent.getProvidedCA().get(i).getContextTableCombinations().get(j).getHAnytime() == true) {
					// Prints the ID
					writer.print(counter + delimiter);
					
					
					
					String temp = "G (";
					String nameOfControlAction = null;
					
					for (int z=0; z<exportContent.getProvidedCA().get(i).getContextTableCombinations().get(j).getValues().size(); z++){
						if (exportContent.getProvidedCA().get(i).getContextTableCombinations().get(j).getValues().get(z).equals("(don't care)")) {
							
						}
						else {
							
							temp = temp.concat("(");					
							temp = temp.concat(exportContent.getProvidedCA().get(i).getContextTableCombinations().get(j).getPmVariables().get(z));
							temp = temp.concat("==");
							temp = temp.concat(exportContent.getProvidedCA().get(i).getContextTableCombinations().get(j).getValues().get(z));
							temp = temp.concat(")");
							if (!(z==exportContent.getProvidedCA().get(i).getContextTableCombinations().get(j).getSizeOfValues()-1)) {
								temp = temp.concat(" && ");
							}
							else {
								nameOfControlAction = exportContent.getProvidedCA().get(i).getContextTableCombinations().get(j).getLinkedControlActionName();
							}
						}
					}
					temp = temp.concat(" ->! ");
					temp = temp.concat("(controlaction==" +nameOfControlAction+ "))");
					
					writer.print(temp+ delimiter + crlf);
										
					// Counts Up, this is for the ID
					counter++;
				}
			}
			
		}
		
		for (int i=0; i<exportContent.getProvidedCA().size();i++) {
			
			for (int j=0; j<exportContent.getNotProvidedCA().get(i).getContextTableCombinations().size(); j++) {
				if (exportContent.getNotProvidedCA().get(i).getContextTableCombinations().get(j).getGlobalHazardous() == true) {
					// Prints the ID
					writer.print(counter + delimiter);
					
					String temp = "G (";
					String nameOfControlAction = null;
					
					for (int z=0; z<exportContent.getNotProvidedCA().get(i).getContextTableCombinations().get(j).getValues().size(); z++){
						temp = temp.concat("(");
						temp = temp.concat(exportContent.getNotProvidedCA().get(i).getContextTableCombinations().get(j).getPmVariables().get(z));
						temp = temp.concat("==");
						temp = temp.concat(exportContent.getNotProvidedCA().get(i).getContextTableCombinations().get(j).getValues().get(z));
						temp = temp.concat(")");
						if (!(z==exportContent.getNotProvidedCA().get(i).getContextTableCombinations().get(j).getSizeOfValues()-1)) {
							temp = temp.concat(" && ");
						}
						else {
							nameOfControlAction = exportContent.getNotProvidedCA().get(i).getContextTableCombinations().get(j).getLinkedControlActionName();
						}
					}
					temp = temp.concat(" -> ");
					temp = temp.concat("(controlaction==" +nameOfControlAction+ "))");
					
					writer.print(temp+ delimiter + crlf);
					
					// Counts Up, this is for the ID
					counter++;
				}
						
			}
			
		}

		writer.flush();
		writer.close();
		return true;
	}

}
