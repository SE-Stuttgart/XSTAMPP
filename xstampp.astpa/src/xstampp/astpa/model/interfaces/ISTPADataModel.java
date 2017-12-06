package xstampp.astpa.model.interfaces;

import xstampp.astpa.model.causalfactor.ICausalController;
import xstampp.astpa.model.controlaction.IControlActionController;
import xstampp.astpa.model.controlstructure.ControlStructureController;
import xstampp.astpa.model.extendedData.interfaces.IExtendedDataController;
import xstampp.astpa.model.hazacc.IHazAccController;
import xstampp.astpa.model.linking.LinkController;
import xstampp.astpa.model.projectdata.ProjectDataController;
import xstampp.astpa.model.sds.ISDSController;
import xstampp.model.IDataModel;

public interface ISTPADataModel extends IDataModel {

  LinkController getLinkController();

  ControlStructureController getControlStructureController();

  IControlActionController getControlActionController();

  IHazAccController getHazAccController();

  ISDSController getSdsController();

  ICausalController getCausalFactorController();

  IExtendedDataController getExtendedDataController();

  ProjectDataController getProjectDataManager();

}