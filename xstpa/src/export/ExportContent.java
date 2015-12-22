package export;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import xstpa.model.ControlActionEntrys;

@XmlRootElement(namespace = "xstpa.model")
public class ExportContent {
	

	private List<ControlActionEntrys> providedCA = new ArrayList<ControlActionEntrys>();

	private List<ControlActionEntrys> notProvidedCA = new ArrayList<ControlActionEntrys>();
	

	private List<String> tableHeaders = new ArrayList<String>();
	
	public ExportContent (List<ControlActionEntrys> providedCA, List<ControlActionEntrys> notProvidedCA) {
		this.notProvidedCA = notProvidedCA;
		this.providedCA = providedCA;
		
	}
	
	public ExportContent() {
		// Constructor for JAXb
	}
	
	@XmlElementWrapper(name = "tableHeaders")
	@XmlElement(name = "tableHeader")
	public List<String> getTableHeaders() {
		return tableHeaders;
	}
	public void setTableHeaders(List<String> tableHeaders) {
		this.tableHeaders = tableHeaders;
	}
	@XmlElementWrapper(name = "providedca")
	@XmlElement(name = "controlactions")
	public List<ControlActionEntrys> getProvidedCA() {
		return providedCA;
	}
	public void setProvidedCA(List<ControlActionEntrys> providedCA) {
		this.providedCA = providedCA;
	}
	
	@XmlElementWrapper(name = "notprovidedca")
	@XmlElement(name = "controlactions")
	public List<ControlActionEntrys> getNotProvidedCA() {
		return notProvidedCA;
	}
	public void setNotProvidedCA(List<ControlActionEntrys> notProvidedCA) {
		this.notProvidedCA = notProvidedCA;
	}
	
	
}

