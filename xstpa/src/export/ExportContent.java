package export;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import xstpa.model.ControlActionEntry;

@XmlRootElement(namespace = "xstpa.model")
public class ExportContent {
	

	private List<ControlActionEntry> providedCA = new ArrayList<ControlActionEntry>();

	private List<ControlActionEntry> notProvidedCA = new ArrayList<ControlActionEntry>();
	

	private List<String> tableHeaders = new ArrayList<String>();
	
	public ExportContent (List<ControlActionEntry> providedCA, List<ControlActionEntry> notProvidedCA) {
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
	public List<ControlActionEntry> getProvidedCA() {
		return providedCA;
	}
	public void setProvidedCA(Collection<ControlActionEntry> providedCA) {
		this.providedCA = new ArrayList<>();
		this.providedCA.addAll(providedCA);
		
	}
	
	@XmlElementWrapper(name = "notprovidedca")
	@XmlElement(name = "controlactions")
	public List<ControlActionEntry> getNotProvidedCA() {
		return notProvidedCA;
	}
	public void setNotProvidedCA(Collection<ControlActionEntry> notProvidedCA) {
		this.notProvidedCA = new ArrayList<>();
		this.notProvidedCA.addAll(notProvidedCA);
	}
	
	
}

