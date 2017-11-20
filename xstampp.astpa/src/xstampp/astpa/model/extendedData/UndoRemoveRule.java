package xstampp.astpa.model.extendedData;

import xstampp.astpa.model.interfaces.IExtendedDataModel.ScenarioType;
import xstampp.astpa.model.linking.LinkController;
import xstampp.astpa.model.linking.UndoRemoveLinkedComponent;
import xstampp.model.ObserverValue;

public class UndoRemoveRule extends UndoRemoveLinkedComponent {

  private ExtendedDataController causalController;
  private ScenarioType type;
  private RefinedSafetyRule safetyRule;

  public UndoRemoveRule(ExtendedDataController causalController, RefinedSafetyRule safetyRule,  ScenarioType type,
      LinkController linkController) {
    super(linkController, safetyRule.getId(), 2);
    this.causalController = causalController;
    this.safetyRule = safetyRule;
    this.type = type;
  }

  @Override
  public void undo() {
    super.undo();
    this.causalController.addRefinedRule(safetyRule, this.type, getLinkController());
  }

  @Override
  public void redo() {
    this.causalController.removeRefinedSafetyRule(type, false, safetyRule.getId(), getLinkController());
  }

  @Override
  public ObserverValue getChangeConstant() {
    return ObserverValue.Extended_DATA;
  }
}