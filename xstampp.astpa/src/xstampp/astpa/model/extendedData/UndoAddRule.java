package xstampp.astpa.model.extendedData;

import xstampp.astpa.model.interfaces.IExtendedDataModel.ScenarioType;
import xstampp.astpa.model.linking.LinkController;
import xstampp.astpa.model.linking.UndoAddLinkedComponent;
import xstampp.model.ObserverValue;

public class UndoAddRule extends UndoAddLinkedComponent {

  private ExtendedDataController causalController;
  private ScenarioType type;
  private RefinedSafetyRule safetyRule;

  public UndoAddRule(ExtendedDataController causalController, RefinedSafetyRule safetyRule,  ScenarioType type,
      LinkController linkController) {
    super(linkController, safetyRule.getId(), 2);
    this.causalController = causalController;
    this.safetyRule = safetyRule;
    this.type = type;
  }

  @Override
  public void undo() {
    this.causalController.removeRefinedSafetyRule(type, false, safetyRule.getId(), getLinkController());
  }

  @Override
  public void redo() {
    super.redo();
    this.causalController.addRefinedRule(safetyRule,this.type,getLinkController());
  }

  @Override
  public ObserverValue getChangeConstant() {
    return ObserverValue.Extended_DATA;
  }

  @Override
  public String getChangeMessage() {
    return "Add a " + this.type.name();
  }

}