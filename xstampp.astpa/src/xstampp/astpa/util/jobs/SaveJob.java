package xstampp.astpa.util.jobs;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.Writer;
import java.util.Observable;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;

import messages.Messages;

import org.apache.commons.lang3.StringEscapeUtils;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;

import xstampp.astpa.haz.HAZController;
import xstampp.astpa.haz.IHAZModel;
import xstampp.model.IDataModel;
import xstampp.ui.common.ProjectManager;
import xstampp.util.XstamppJob;

import com.sun.xml.bind.marshaller.CharacterEscapeHandler;
import com.sun.xml.bind.marshaller.DataWriter;

/**
 * a runtime job which is stores a DataModel in a given File
 * 
 * @author Lukas Balzer
 * @since 2.0
 * 
 */
public class SaveJob extends XstamppJob {

	final File file;
	final IDataModel controller;
	private boolean compatibilityMode;
	private boolean ready = false;
	private StringBuffer oldContent;

	/**
	 * 
	 * 
	 * @author Lukas Balzer
	 * 
	 * @param file
	 *            the file in which the job should store the results
	 * @param controller
	 *            the Data model which should be stored, this must be a
	 *            JAXBContext
	 */
	public SaveJob(File file, IDataModel controller) {
		super(Messages.saveHaz);
		this.compatibilityMode = false;
		this.file = file;
		this.controller = controller;
		
	}
	

	@Override
	protected Observable getModelObserver() {
		return (Observable) controller;
	}
	
	@Override
	protected IStatus run(IProgressMonitor monitor) {
		monitor.beginTask(Messages.savingHaz, IProgressMonitor.UNKNOWN);
		JAXBContext context;
		try {
			if(file.exists()){
				initiateRecovery(file);
			}else{
				file.createNewFile();
			}
			
				Object haz;
				if(this.file.getName().endsWith("haz")){
					haz = new HAZController((IHAZModel) this.controller);
				}else{
					haz = this.controller;
				}
				context = JAXBContext.newInstance(haz.getClass());
				
				Marshaller m = context.createMarshaller();
				
				m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
				// Write to file
				m.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
				FileWriter writer = new FileWriter(this.file);
				
				if(this.compatibilityMode){
					m.marshal(haz,writer);
				}
				else{
					PrintWriter printWriter = new PrintWriter(writer);
					DataWriter dataWriter = new DataWriter(printWriter, "UTF-8", new MyEscapeHandler());
					m.marshal(haz,dataWriter);
					printWriter.close();
					
				}
				writer.close();
				
		} catch (Exception e) {
			recover(file);
			ProjectManager.getLOGGER().error(e.getMessage(), e);
			return Status.CANCEL_STATUS;
		}
		
		return Status.OK_STATUS;
	}

	public void setCompabillityMode(boolean compatibilityMode) {
		this.compatibilityMode = compatibilityMode;
		this.ready=true;
	}
	public boolean isReady() {
		return this.ready;
	}
	
	private void initiateRecovery(File file) throws IOException{
		BufferedReader reader = new BufferedReader(new FileReader(file));
		String line = reader.readLine();
		oldContent= new StringBuffer();
		while(line != null){
			oldContent.append(line);
			oldContent.append(Character.toChars(Character.LINE_SEPARATOR));
			line = reader.readLine();
		}
		reader.close();
	}
	
	private void recover(File file){
		if(oldContent != null && !oldContent.toString().isEmpty()){
				
			
			BufferedWriter writer;
			try {
				writer = new BufferedWriter(new FileWriter(file));
				BufferedReader reader = new BufferedReader(new StringReader(oldContent.toString()));
				String line = reader.readLine();
				while(line != null){
					writer.write(line);
					writer.newLine();
					line = reader.readLine();
				}
				writer.close();
				reader.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}

class MyEscapeHandler implements CharacterEscapeHandler{

	@Override
	public void escape(char[] ch, int start, int length, boolean isAttVal, Writer out) throws IOException {

	    String string = new String(ch);
	    String sub = string.substring(start, start+length);
	    int limit = start + length;
	    
	    sub = StringEscapeUtils.ESCAPE_HTML4.translate(sub);
	    if (start != limit) {
	      out.write(sub);
	    }
	  
	}
	
}
