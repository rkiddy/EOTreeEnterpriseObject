package org.ganymede.eo;

import com.webobjects.eocontrol.EOEnterpriseObject;
import com.webobjects.foundation.NSArray;

public interface EOTreeObject extends EOEnterpriseObject {

	public String fullName();

	public EOTreeObject parent();

	public NSArray<EOTreeObject> children();

	public NSArray<EOTreeObject> descendants();

	public NSArray<EOTreeObject> ancestors();

	public void addElementUnderParent(EOTreeObject nextParent);

	public void removeElement();

	public boolean equals(EOTreeObject element);
}
