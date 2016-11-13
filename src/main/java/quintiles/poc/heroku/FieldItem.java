package quintiles.poc.heroku;

import java.util.Objects;

import org.json.JSONObject;

public class FieldItem {
	
	private String name;
	private String label;
	private transient String section;
	private boolean required = false;
	private boolean readonly = false;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public String getSection() {
		return section;
	}
	public void setSection(String section) {
		this.section = section;
	}
	public boolean isRequired() {
		return required;
	}
	public void setRequired(boolean required) {
		this.required = required;
	}
	public boolean isReadonly() {
		return readonly;
	}
	public void setReadonly(boolean readonly) {
		this.readonly = readonly;
	}
	
	@Override
	public boolean equals(Object o) {
	    if (this == o)
	        return true;
	    if (o == null)
	        return false;
	    if (getClass() != o.getClass())
	        return false;
	    
	    FieldItem fieldItem = (FieldItem) o;
	    
	    return Objects.equals(this.name, fieldItem.name);
	}
	
	public String toString() { 
		JSONObject jsonObject = new JSONObject(this);
		String json  = jsonObject.toString();
        return json;
     } 
}
