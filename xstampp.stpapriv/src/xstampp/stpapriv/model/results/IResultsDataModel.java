package xstampp.stpapriv.model.results;

import java.util.List;

import xstampp.astpa.model.interfaces.ICommonTables;
import xstampp.model.IDataModel;

public interface IResultsDataModel extends IDataModel, ICommonTables {

	List<ConstraintResult> getAllConstraintResults();
}
