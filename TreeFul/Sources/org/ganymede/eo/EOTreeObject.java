package org.ganymede.eo;

import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSKeyValueCoding;

public interface EOTreeObject extends NSKeyValueCoding {

	public String fullName();

	public EOTreeObject parent();

	public NSArray<EOTreeObject> children();

	public NSArray<EOTreeObject> descendants();

	public NSArray<EOTreeObject> ancestors();

	public void addElementUnderParent(EOTreeObject nextParent);

	public void moveElementToParent(EOTreeObject nextParent);

	public boolean equals(EOTreeObject element);
}
