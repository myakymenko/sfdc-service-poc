package quintiles.poc.container;

import java.util.ArrayList;
import java.util.Objects;

import com.google.gson.annotations.Expose;

public class LayoutItem {
	
	private String name;
	@Expose()
	private String type;
	@Expose()
	private String subtype;
	@Expose()
	private String recordTypeId;
	private String profileName;
	@Expose()
	private ArrayList<SectionItem> sections = new ArrayList<>();

	public LayoutItem() {
	}

	public LayoutItem(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getSubtype() {
		return subtype;
	}

	public void setSubtype(String subtype) {
		this.subtype = subtype;
	}

	public String getRecordTypeId() {
		return recordTypeId;
	}

	public void setRecordTypeId(String recordTypeId) {
		this.recordTypeId = recordTypeId;
	}

	public ArrayList<SectionItem> getSections() {
		return sections;
	}

	public void setSections(ArrayList<SectionItem> sections) {
		this.sections = sections;
	}

	public void setSection(SectionItem layoutItem) {
		this.sections.add(layoutItem);
	}

	public SectionItem getSectionByIndex(int index) {
		return this.sections.get(index);
	}

	public SectionItem getSectionByName(String sectionName) {
		LayoutItem searchedSection = new LayoutItem(sectionName);
		int index = this.sections.indexOf(searchedSection);
		return index != -1 ? getSectionByIndex(index) : null;
	}

	public SectionItem getSection(SectionItem sectionItem) {
		int index = this.sections.indexOf(sectionItem);
		return index != -1 ? getSectionByIndex(index) : null;
	}

	public boolean containsSection(SectionItem sectionItem) {
		return this.sections.contains(sectionItem);
	}
	
	public String getProfileName() {
		return profileName;
	}

	public void setProfileName(String profileName) {
		this.profileName = profileName;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null)
			return false;
		if (getClass() != o.getClass())
			return false;

		LayoutItem layoutItem = (LayoutItem) o;

		return Objects.equals(this.name, layoutItem.name);
	}
}
