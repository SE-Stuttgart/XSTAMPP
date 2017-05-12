package xstampp.ui.navigation;

import java.util.Map;
import java.util.UUID;

import org.eclipse.core.runtime.IConfigurationElement;

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
  private Map<String, String> properties;
  private IConfigurationElement element;
  private String[] commandAdditions;

  /**
   * Constructs a new instance and fills all the fields with its counterparts in the given element.
   * calls {@link TreeItemDescription#TreeItemDescription(IProjectSelection, UUID)}.
   * 
   * @param element
   *          the {@link IConfigurationElement} defined in the steppedProcess extension of the
   *          plugin
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
   * Constructs an instance that defines only the parent selector and projectId, the elementName is
   * set to <code>step</code> and the children's list is initialized as an empty array.
   * 
   * @param parent
   *          the selector for the parent TreeItem
   * @param projectId
   *          the id of the project the selector should be defined for
   */
  public TreeItemDescription(IProjectSelection parent, UUID projectId) {
    this.parent = parent;
    this.projectId = projectId;
    this.elementName = "step";
    this.children = new IConfigurationElement[0];
    this.commandAdditions = new String[0];
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getElementName() {
    return elementName;
  }

  public void setElementName(String elementName) {
    this.elementName = elementName;
  }

  public String getCommand() {
    return command;
  }

  public void setCommand(String command) {
    this.command = command;
  }

  public String getNamespaceIdentifier() {
    return namespaceIdentifier;
  }

  public void setNamespaceIdentifier(String namespaceIdentifier) {
    this.namespaceIdentifier = namespaceIdentifier;
  }

  public String getIcon() {
    return icon;
  }

  public void setIcon(String icon) {
    this.icon = icon;
  }

  public String getEditorId() {
    return editorId;
  }

  public void setEditorId(String editorId) {
    this.editorId = editorId;
  }

  public Map<String, String> getProperties() {
    return properties;
  }

  public void setProperties(Map<String, String> properties) {
    this.properties = properties;
  }

  public IProjectSelection getParent() {
    return parent;
  }

  public UUID getProjectId() {
    return projectId;
  }

  public IConfigurationElement[] getChildren() {
    return children;
  }

  public IConfigurationElement getElement() {
    return element;
  }

  public void setCommandAdditions(String[] arrayList) {
    this.commandAdditions = arrayList;
  }

  public String[] getCommandAdditions() {
    return commandAdditions;
  }
}