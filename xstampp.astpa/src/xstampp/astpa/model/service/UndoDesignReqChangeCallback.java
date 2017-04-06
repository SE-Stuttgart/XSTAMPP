package xstampp.astpa.model.service;

import java.util.UUID;

import xstampp.astpa.model.interfaces.IDesignRequirementViewDataModel;

public class UndoDesignReqChangeCallback extends UndoTableModelChangeCallback<IDesignRequirementViewDataModel> {

  public UndoDesignReqChangeCallback(IDesignRequirementViewDataModel dataModel, UUID entryId) {
    super(dataModel, entryId);
  }

  @Override
  protected void undoDescription(String description) {
    getDataModel().setDesignRequirementDescription(getEntryId(), description);
  }

  @Override
  protected void undoTitle(String title) {
    getDataModel().setDesignRequirementTitle(getEntryId(), title);
  }


}
