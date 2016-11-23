package quintiles.poc.container;

import java.util.ArrayList;
import java.util.Objects;

import com.google.gson.annotations.Expose;

public class FieldItem {

	private String section;
	private String objectName;
	@Expose()
	private String name;
	@Expose()
	private String label;
	@Expose()
	private String type;
	@Expose()
	private boolean required = false;
	@Expose()
	private boolean readonly = false;

	private boolean visible = true;
	@Expose()
	private String relatedObject;
	@Expose()
	private ArrayList<OptionItem> options = null;

	public FieldItem() {
	}

	public FieldItem(String name) {
		this.name = name;
	}

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

	public String getRelatedObject() {
		return relatedObject;
	}

	public void setRelatedObject(String relatedObject) {
		this.relatedObject = relatedObject;
	}

	public String getSection() {
		return section;
	}

	public void setSection(String section) {
		this.section = section;
	}

	public String getObjectName() {
		return objectName;
	}

	public void setObjectName(String object) {
		this.objectName = object;
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

	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	public ArrayList<OptionItem> getOptions() {
		return options;
	}

	public void setOptions(ArrayList<OptionItem> options) {
		this.options = options;
	}

	public void setOption(OptionItem option) {
		if (options == null) {
			options = new ArrayList<>();
		}
		this.options.add(option);
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
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
}
