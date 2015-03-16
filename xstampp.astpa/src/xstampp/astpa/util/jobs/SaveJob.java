package xstampp.astpa.util.jobs;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import messages.Messages;

import org.apache.commons.lang3.StringEscapeUtils;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.dialogs.MessageDialog;

import xstampp.model.IDataModel;
import xstampp.ui.common.ViewContainer;

import com.sun.xml.bind.marshaller.CharacterEscapeHandler;
import com.sun.xml.bind.marshaller.DataWriter;

/**
 * a runtime job which is stores a DataModel in a given File
 * 
 * @author Lukas Balzer
 * @since 2.0
 * 
 */
public class SaveJob extends Job {

	final File file;
	final IDataModel controller;
	private boolean compatibilityMode;
	private boolean ready = false;

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
	protected IStatus run(IProgressMonitor monitor) {
		monitor.beginTask(Messages.savingHaz, IProgressMonitor.UNKNOWN);
		JAXBContext context;
		try {
			context = JAXBContext.newInstance(this.controller.getClass());
			Marshaller m = context.createMarshaller();
			
			m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
			// Write to file
			m.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
			FileWriter writer = new FileWriter(this.file);
			
			if(this.compatibilityMode){
				m.marshal(this.controller,writer);
			}
			else{
				PrintWriter printWriter = new PrintWriter(writer);
				DataWriter dataWriter = new DataWriter(printWriter, "UTF-8", new MyEscapeHandler());
				m.marshal(this.controller,dataWriter);
				printWriter.close();
				
			}
			writer.close();
			
		} catch (JAXBException e) {
			ViewContainer.getLOGGER().error(e.getMessage(), e);
			return Status.CANCEL_STATUS;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
	
}

class MyEscapeHandler implements CharacterEscapeHandler{

	@Override
	public void escape(char[] ch, int start, int length, boolean isAttVal, Writer out) throws IOException {

	    String string = new String(ch);
	    String sub = string.substring(start, start+length);
	    int limit = start + length;
	    if(sub.contains("ü")){
	    	System.out.println();
	    }
	    
	    sub = StringEscapeUtils.ESCAPE_HTML4.translate(sub);
	    if (start != limit) {
	      out.write(sub);
	    }
	  
	}
	
}
