package xstampp.ui.navigation;

import java.util.Map;
import java.util.UUID;

import org.eclipse.core.runtime.IConfigurationElement;

import xstampp.ui.navigation.api.IProjectSelection;

/**
 * An object containing all the necessary information to create a sub selector in the project
 * explorer.
 * 
 * @author Lukas Balzer
 *
 */
public class TreeItemDescription {
  
  private String id;
  private String name;
  private String elementName;
  private IProjectSelection parent;
  private UUID projectId;
  private String command;
  private String namespaceIdentifier;
  private String icon;
  private String editorId;
  private IConfigurationElement[] children;
  private Map<String, Object> properties;
  private IConfigurationElement element;

  /**
   * Constructs a new instance and fills all the fields with its counterparts 
   * in the given element.
   * calls {@link TreeItemDescription#TreeItemDescription(IProjectSelection, UUID)}.
   * 
   * @param element the {@link IConfigurationElement} defined in the steppedProcess extension
   *              of the plugin
   */
  public TreeItemDescription(IConfigurationElement element, IProjectSelection parent,
      UUID projectId) {
    this(parent, projectId);
    this.element = element;
    this.command = element.getAttribute("command");
    this.id = element.getAttribute("id");
    this.name = element.getAttribute("name");
    this.elementName = element.getName();
    icon = element.getAttribute("icon");
    namespaceIdentifier = element.getNamespaceIdentifier();
    editorId = element.getAttribute("editorId");
    children = element.getChildren();

  }

  /**
   * Constructs an instance that defines only the parent selector and projectId,
   * the elementName is set to <code>step</code> and the children's list is initialized as an empty array.
   * @param parent the selector for the parent TreeItem
   * @param projectId the id of the project the selector should be defined for
   */
  public TreeItemDescription(IProjectSelection parent, UUID projectId) {
    this.parent = parent;
    this.projectId = projectId;
    this.elementName = "step";
    this.children = new IConfigurationElement[0];
  }

  /**
   * @return the id
   */
  public String getId() {
    return id;
  }

  /**
   * @param id the id to set
   */
  public void setId(String id) {
    this.id = id;
  }

  /**
   * @return the name
   */
  public String getName() {
    return name;
  }

  /**
   * @param name the name to set
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * @return the elementName
   */
  public String getElementName() {
    return elementName;
  }

  /**
   * @param elementName the elementName to set
   */
  public void setElementName(String elementName) {
    this.elementName = elementName;
  }

  /**
   * @return the command
   */
  public String getCommand() {
    return command;
  }

  /**
   * @param command the command to set
   */
  public void setCommand(String command) {
    this.command = command;
  }

  /**
   * @return the namespaceIdentifier
   */
  public String getNamespaceIdentifier() {
    return namespaceIdentifier;
  }

  /**
   * @param namespaceIdentifier the namespaceIdentifier to set
   */
  public void setNamespaceIdentifier(String namespaceIdentifier) {
    this.namespaceIdentifier = namespaceIdentifier;
  }

  /**
   * @return the icon
   */
  public String getIcon() {
    return icon;
  }

  /**
   * @param icon the icon to set
   */
  public void setIcon(String icon) {
    this.icon = icon;
  }

  /**
   * @return the editorId
   */
  public String getEditorId() {
    return editorId;
  }

  /**
   * @param editorId the editorId to set
   */
  public void setEditorId(String editorId) {
    this.editorId = editorId;
  }

  /**
   * @return the properties
   */
  public Map<String, Object> getProperties() {
    return properties;
  }

  /**
   * @param properties
   *          the properties to set
   */
  public void setProperties(Map<String, Object> properties) {
    this.properties = properties;
  }

  /**
   * @return the parent
   */
  public IProjectSelection getParent() {
    return parent;
  }

  /**
   * @return the projectId
   */
  public UUID getProjectId() {
    return projectId;
  }

  /**
   * @return the children
   */
  public IConfigurationElement[] getChildren() {
    return children;
  }

  /**
   * @return the element
   */
  public IConfigurationElement getElement() {
    return element;
  }

}