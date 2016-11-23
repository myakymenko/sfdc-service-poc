package quintiles.poc.container;

import java.util.ArrayList;
import java.util.Objects;

import com.google.gson.annotations.Expose;

public class SectionItem {
	
	@Expose()
	private String name;
	@Expose()
	private ArrayList<FieldItem> fields = new ArrayList<>();

	public SectionItem() {
	}

	public SectionItem(String name) {
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

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null)
			return false;
		if (getClass() != o.getClass())
			return false;

		SectionItem sectionItem = (SectionItem) o;

		return Objects.equals(this.name, sectionItem.name);
	}
}
