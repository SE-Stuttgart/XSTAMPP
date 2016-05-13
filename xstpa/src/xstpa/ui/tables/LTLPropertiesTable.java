package xstpa.ui.tables;

import java.util.List;
import java.util.Observable;

import xstampp.astpa.model.DataModelController;
import xstampp.astpa.ui.sds.AbstractFilteredTableView;
import xstampp.model.AbstractLTLProvider;
import xstampp.model.ObserverValue;
import xstampp.ui.common.ProjectManager;
import xstpa.ui.LTLEntryFilter;
import xstpa.ui.View;

public class LTLPropertiesTable extends AbstractFilteredTableView{

	public LTLPropertiesTable() {
		super(new LTLEntryFilter(), new String[]{View.ENTRY_ID,View.LTL_RULES});
		setColumnWeights(new int[]{-1,15});
	}

	@Override
	public String getId() {
		return "xstpa.editor.ltl";
	}

	@Override
	public void dispose() {
		ProjectManager.getContainerInstance().getDataModel(getProjectID()).deleteObserver(LTLPropertiesTable.this);
	}
	@Override
	public void update(final Observable dataModelController, Object updatedValue) {
		final ObserverValue value= (ObserverValue) updatedValue; 
		switch(value){
			case Extended_DATA:
				packColumns();
		default:
			break;
		}
	}
	
	@Override
	protected CSCLabelProvider getColumnProvider(int columnIndex) {

		switch(columnIndex){
		case 0: 
			return new CSCLabelProvider(){
				@Override
				public String getText(Object element) {
					return  "SSR1." + ((AbstractLTLProvider) element).getNumber();
				}
			};
		case 1:
			return new CSCLabelProvider(){
				
				@Override
				public String getText(Object element) {
					return  ((AbstractLTLProvider) element).getLtlProperty();
				}
			};
		}
		return null;
	}
	
	@Override
	protected List<?> getInput() {
		return ((DataModelController) ProjectManager.getContainerInstance().getDataModel(getProjectID())).getLTLPropertys();
	}

	@Override
	protected boolean hasEditSupport() {
		// TODO Auto-generated method stub
		return false;
	}
	}

