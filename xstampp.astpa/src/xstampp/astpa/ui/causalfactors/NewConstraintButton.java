package xstampp.astpa.ui.causalfactors;

import java.util.UUID;

import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;

import xstampp.astpa.model.causalfactor.interfaces.CausalFactorEntryData;
import xstampp.astpa.model.interfaces.ICausalFactorDataModel;
import xstampp.ui.common.grid.CellButton;
import xstampp.ui.common.grid.GridWrapper;

public class NewConstraintButton extends CellButton {

    private UUID componentId;
    private UUID factorId;
    private UUID entryId;
    private ICausalFactorDataModel datamodel;

		public NewConstraintButton(UUID componentId, UUID factorId,UUID entryId, ICausalFactorDataModel datamodel) {
			super(new Rectangle(
          -1, -1,
          GridWrapper.getAddButton16().getBounds().width,
          GridWrapper.getAddButton16().getBounds().height),
          GridWrapper.getAddButton16());
      this.componentId = componentId;
      this.factorId = factorId;
      this.entryId = entryId;
      this.datamodel = datamodel;
		}

		@Override
		public void onButtonDown(Point relativeMouse, Rectangle cellBounds) {
	    CausalFactorEntryData data = new CausalFactorEntryData(entryId);
	    data.setConstraint(new String());
	    datamodel.lockUpdate();
	    datamodel.changeCausalEntry(componentId, factorId, data);
      datamodel.releaseLockAndUpdate(null);
		}
	}