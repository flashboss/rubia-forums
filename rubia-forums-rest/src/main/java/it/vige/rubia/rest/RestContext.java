package it.vige.rubia.rest;

public class RestContext {
	
	private String managedClass;
	
	private String managedAction;
	
	private String contextType;

	public String getManagedClass() {
		return managedClass;
	}

	public void setManagedClass(String managedClass) {
		this.managedClass = managedClass;
	}

	public String getManagedAction() {
		return managedAction;
	}

	public void setManagedAction(String managedAction) {
		this.managedAction = managedAction;
	}

	public String getContextType() {
		return contextType;
	}

	public void setContextType(String contextType) {
		this.contextType = contextType;
	}

}
