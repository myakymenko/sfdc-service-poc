package quintiles.poc.container;

import java.util.ArrayList;
import java.util.Objects;

public class SObjectItem {
	
	private String name;
	private ArrayList<FieldItem> fields = new ArrayList<>();

	public SObjectItem() {
	}

	public SObjectItem(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ArrayList<FieldItem> getFields() {
		return fields;
	}

	public void setFields(ArrayList<FieldItem> fields) {
		this.fields = fields;
	}

	public void setField(FieldItem field) {
		this.fields.add(field);
	}

	public FieldItem getFieldByIndex(int index) {
		return this.fields.get(index);
	}

	public FieldItem getFieldByName(String fieldName) {
		FieldItem searchedField = new FieldItem(fieldName);
		int index = this.fields.indexOf(searchedField);
		return index != -1 ? getFieldByIndex(index) : null;
	}

	public FieldItem getField(FieldItem fieldItem) {
		int index = this.fields.indexOf(fieldItem);
		return index != -1 ? getFieldByIndex(index) : null;
	}

	public boolean containsField(FieldItem fieldItem) {
		return this.fields.contains(fieldItem);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null)
			return false;
		if (getClass() != o.getClass())
			return false;

		SObjectItem sObjectItem = (SObjectItem) o;

		return Objects.equals(this.name, sObjectItem.name);
	}
}
