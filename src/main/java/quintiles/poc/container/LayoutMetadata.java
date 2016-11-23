package quintiles.poc.container;

import java.util.ArrayList;
import java.util.Arrays;

import quintiles.poc.api.IMetadata;

public class LayoutMetadata implements IMetadata {

	private ArrayList<String> availableObjects = new ArrayList<>();
	private ArrayList<String> availableLayouts = new ArrayList<>();
	private ArrayList<String> availableProfiles = new ArrayList<>();
	private ArrayList<SObjectItem> sObjects = new ArrayList<>();
	private ArrayList<ProfileItem> profiles = new ArrayList<>();
	private ArrayList<LayoutItem> layouts = new ArrayList<>();
	private ArrayList<LayoutItem> processedLayouts = new ArrayList<>();

	public LayoutMetadata() {
	}
	
	public LayoutMetadata(String[] profileNames, String[] processedObjects, String[] processedLayouts) {
		this.availableProfiles = new ArrayList<>(Arrays.asList(profileNames));
		this.availableObjects = new ArrayList<>(Arrays.asList(processedObjects));
		this.availableLayouts = new ArrayList<>(Arrays.asList(processedLayouts));
	}

	public ArrayList<String> getAvailableObjects() {
		return availableObjects;
	}

	public void setAvailableObjects(ArrayList<String> availableObjects) {
		this.availableObjects = availableObjects;
	}

	public ArrayList<String> getAvailableProfiles() {
		return availableProfiles;
	}

	public void setAvailableProfiles(ArrayList<String> availableProfiles) {
		this.availableProfiles = availableProfiles;
	}

	public ArrayList<String> getAvailableLayouts() {
		return availableLayouts;
	}

	public void setAvailbleLayouts(ArrayList<String> availableLayouts) {
		this.availableLayouts = availableLayouts;
	}

	public ArrayList<ProfileItem> getProfiles() {
		return profiles;
	}

	public void setProfiles(ArrayList<ProfileItem> profiles) {
		this.profiles = profiles;
	}
	
	public void setProfile(ProfileItem profile) {
		this.profiles.add(profile);
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

	public ArrayList<LayoutItem> getProcessedLayouts() {
		return processedLayouts;
	}

	public void setProcessedLayouts(ArrayList<LayoutItem> processedLayouts) {
		this.processedLayouts = processedLayouts;
	}

	public void setProcessedLayout(LayoutItem processedLayout) {
		this.processedLayouts.add(processedLayout);
	}

	public boolean hasAvailableSObject(String sObjectName) {
		return availableObjects.contains(sObjectName);
	}
}
