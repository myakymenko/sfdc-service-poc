package quintiles.poc.heroku;

import java.util.ArrayList;
import java.util.HashMap;

public class Layout {
	private String layoutName = null;
	
	private HashMap<String, ArrayList<FieldItem>> fieldSections = new HashMap<String, ArrayList<FieldItem>>();

	public String getLayoutName() {
		return layoutName;
	}

	public void setLayoutName(String layoutName) {
		this.layoutName = layoutName;
	}

	public HashMap<String, ArrayList<FieldItem>> getFieldSections() {
		return fieldSections;
	}

	public void setFieldSections(HashMap<String, ArrayList<FieldItem>> fields) {
		this.fieldSections = fields;
	}
}
