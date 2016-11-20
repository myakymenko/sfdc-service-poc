package quintiles.poc.container;

import java.util.ArrayList;
import java.util.Objects;

public class ProfileItem {
	
	private String name;
	private ArrayList<SObjectItem> sObjects = new ArrayList<>();
	private ArrayList<LayoutItem> layouts = new ArrayList<>();

	public ProfileItem() {
	}

	public ProfileItem(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ArrayList<SObjectItem> getSObjects() {
		return sObjects;
	}

	public void setSObjects(ArrayList<SObjectItem> sObjects) {
		this.sObjects = sObjects;
	}

	public void setSObject(SObjectItem sObjectItem) {
		this.sObjects.add(sObjectItem);
	}

	public SObjectItem getSObjectByIndex(int index) {
		return this.sObjects.get(index);
	}

	public SObjectItem getSObjectByName(String sObjectName) {
		SObjectItem searchrdSObject = new SObjectItem(sObjectName);
		int index = this.sObjects.indexOf(searchrdSObject);
		return index != -1 ? getSObjectByIndex(index) : null;
	}

	public SObjectItem getSObject(SObjectItem sObjectItem) {
		int index = this.sObjects.indexOf(sObjectItem);
		return index != -1 ? getSObjectByIndex(index) : null;
	}

	public boolean containsSObject(SObjectItem sObjectItem) {
		return this.sObjects.contains(sObjectItem);
	}

	public ArrayList<LayoutItem> getLayouts() {
		return layouts;
	}

	public void setLayouts(ArrayList<LayoutItem> layouts) {
		this.layouts = layouts;
	}

	public void setLayout(LayoutItem layoutItem) {
		this.layouts.add(layoutItem);
	}

	public LayoutItem getLayoutByIndex(int index) {
		return this.layouts.get(index);
	}

	public LayoutItem getLayoutByName(String layoutName) {
		LayoutItem searchedLayout = new LayoutItem(layoutName);
		int index = this.layouts.indexOf(searchedLayout);
		return index != -1 ? getLayoutByIndex(index) : null;
	}

	public LayoutItem getLayout(LayoutItem layoutItem) {
		int index = this.layouts.indexOf(layoutItem);
		return index != -1 ? getLayoutByIndex(index) : null;
	}

	public boolean containsLayout(LayoutItem layoutItem) {
		return this.layouts.contains(layoutItem);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null)
			return false;
		if (getClass() != o.getClass())
			return false;

		ProfileItem sObjectItem = (ProfileItem) o;

		return Objects.equals(this.name, sObjectItem.name);
	}
}
