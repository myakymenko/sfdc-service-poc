package quintiles.poc;

import java.util.ArrayList;
import java.util.Objects;

public class LayoutSection {
	
	private String sectionName;
	private ArrayList<FieldItem> fields = new ArrayList<FieldItem>();
	
	public String getSectionName() {
		return sectionName;
	}
	public void setSectionName(String sectionName) {
		this.sectionName = sectionName;
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
	    
	    LayoutSection layoutSection = (LayoutSection) o;
	    
	    return Objects.equals(this.sectionName, layoutSection.sectionName);
	}
}
