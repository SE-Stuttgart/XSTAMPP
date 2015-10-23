package export;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import xstpa.ControlActionEntrys;

@XmlRootElement(namespace = "xstpa.model")
public class ExportContent {
	
	@XmlElementWrapper(name = "providedca")
	@XmlElement(name = "controlactions")
	private List<ControlActionEntrys> providedCA = new ArrayList<ControlActionEntrys>();
	
	@XmlElementWrapper(name = "notprovidedca")
	@XmlElement(name = "controlactions")
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
}

