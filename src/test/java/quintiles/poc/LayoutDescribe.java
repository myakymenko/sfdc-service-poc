package quintiles.poc;

import java.util.ArrayList;

public class LayoutDescribe {
	
	private ArrayList<FieldItem> fields = new ArrayList<FieldItem>();

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
