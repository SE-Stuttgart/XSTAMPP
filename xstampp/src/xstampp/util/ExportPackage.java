package xstampp.util;

public class ExportPackage {
	private String name;
	private String filePath;
	private String xslName;
	private Class<?> DataModelClazz;
	private float textSize;
	private float titleSize;
	private float tableHeadSize;

	public ExportPackage(String name, String filePath, String xslName, Class<?> clazz) {
		this.name = name;
		this.filePath = filePath;
		this.xslName = xslName;
		DataModelClazz = clazz;
		this.tableHeadSize = 14;
		this.titleSize = 24;
		this.textSize = 12;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public String getXslName() {
		return xslName;
	}

	public void setXslName(String xslName) {
		this.xslName = xslName;
	}

	public float getTextSize() {
		return textSize;
	}

	public void setTextSize(float textSize) {
		this.textSize = textSize;
	}

	public float getTitleSize() {
		return titleSize;
	}

	public void setTitleSize(float titleSize) {
		this.titleSize = titleSize;
	}

	public float getTableHeadSize() {
		return tableHeadSize;
	}

	public void setTableHeadSize(float tableHeadSize) {
		this.tableHeadSize = tableHeadSize;
	}

	public Class<?> getDataModelClazz() {
		return DataModelClazz;
	}

	public void setDataModelClazz(Class<?> dataModelClazz) {
		DataModelClazz = dataModelClazz;
	}
}
