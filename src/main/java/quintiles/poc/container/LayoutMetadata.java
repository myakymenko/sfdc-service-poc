package quintiles.poc.container;

import java.util.ArrayList;
import java.util.Arrays;

public class LayoutMetadata {
	
	private transient String profileName = null;
	private transient String recordType = null;
	
	private ArrayList<String> processedObjects = new ArrayList<>();
	
	private ProfileItem profile = null; 
	private ArrayList<SObjectItem> sObjects = new ArrayList<>();
	private ArrayList<LayoutItem> layouts = new ArrayList<>();
	
	public LayoutMetadata() {
	}
	
	public LayoutMetadata(String profileName, String sObjectName, String recordType) {
		this.profileName = profileName;
		this.recordType = recordType;
		this.processedObjects.add(sObjectName);
	}

	public LayoutMetadata(String profileName, String[] processedObjects) {
		this.profileName = profileName;
		this.processedObjects = new ArrayList<>(Arrays.asList(processedObjects));
	}

	public String getProfileName() {
		return profileName;
	}

	public void setProfileName(String profileName) {
		this.profileName = profileName;
	}

	public String getRecordType() {
		return recordType;
	}

	public void setRecordType(String recordType) {
		this.recordType = recordType;
	}

	public ArrayList<String> getProcessedObjects() {
		return processedObjects;
	}

	public void setProcessedObjects(ArrayList<String> processedObjects) {
		this.processedObjects = processedObjects;
	}
	
	public ProfileItem getProfile() {
		return profile;
	}

	public void setProfile(ProfileItem profile) {
		this.profile = profile;
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

	public boolean hasProcessedObject(String sObjectName) {
		return processedObjects.contains(sObjectName);
	}
	
}
