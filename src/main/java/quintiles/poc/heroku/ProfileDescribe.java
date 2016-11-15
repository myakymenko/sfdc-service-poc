package quintiles.poc.heroku;

import java.util.ArrayList;

public class ProfileDescribe {
	private ArrayList<String> layouts = new ArrayList<String>();
	private String layoutName;
	private ArrayList<FieldItem> fields = new ArrayList<FieldItem>();
	
	public ArrayList<String> getLayouts() {
		return layouts;
	}

	public void setLayout(String layout) {
		this.layouts.add(layout);
	}
	
	public String getLayoutByIndex(int index) {
		return this.layouts.get(index);
	}
	
	public FieldItem getLayout(String layout) {
		int index = this.layouts.indexOf(layout);
		return index != -1 ? getFieldByIndex(index) : null;
	}
	public String getLayoutName() {
		return layoutName;
	}

	public void setLayoutName(String layoutName) {
		this.layoutName = layoutName;
	}

	public void setField(FieldItem field) {
		this.fields.add(field);
	}
	
	public FieldItem getFieldByIndex(int index) {
		return this.fields.get(index);
	}
	
	public FieldItem getField(FieldItem fieldItem) {
		int index = this.fields.indexOf(fieldItem);
		return index != -1 ? getFieldByIndex(index) : null;
	}
	
	public boolean containsField(FieldItem fieldItem) {
		return this.fields.contains(fieldItem);
	}
	
	public ArrayList<FieldItem> getFields() {
		return fields;
	}

	public void setFields(ArrayList<FieldItem> fields) {
		this.fields = fields;
	}
}
