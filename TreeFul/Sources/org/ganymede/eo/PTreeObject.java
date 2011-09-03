package org.ganymede.eo;

import com.webobjects.eoaccess.EOUtilities;
import com.webobjects.eocontrol.EOEditingContext;
import com.webobjects.eocontrol.EOGenericRecord;
import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSMutableArray;
import com.webobjects.foundation.NSMutableSet;

public class PTreeObject extends EOGenericRecord implements EOTreeObject {

	public boolean equals(EOTreeObject element) {
		return true;
	}

	public static NSArray<EOTreeObject> fetchAllTreeObjects(EOEditingContext ec) {
		return EOUtilities.objectsForEntityNamed(ec, "PTree");
	}

	public String fullName() {

		NSMutableArray<String> names = new NSMutableArray<String>((String)this.valueForKey("name"));

		PTreeObject current = this;
		while (current.valueForKey("parent") != null) {
			current = (PTreeObject)current.valueForKey("parent");
			names.add((String)current.valueForKey("name"));
		}
		
		NSMutableArray<String> next = new NSMutableArray<String>();
		for (int idx = names.size() - 1; idx >= 0; idx--) {
			next.add(names.get(idx));
		}

		return next.componentsJoinedByString(":");
	}

	public String toString() {
		return (String)this.valueForKey("name");
	}

	public NSArray<EOTreeObject> descendants() {

		NSMutableSet<EOTreeObject> currentDescendants = new NSMutableSet<EOTreeObject>(this);

		NSMutableSet<EOTreeObject> nextDescendants = new NSMutableSet<EOTreeObject>();

		int currentCount = 0;

		do {
			currentCount = currentDescendants.size();

			for (EOTreeObject descendant : currentDescendants) {
				nextDescendants.addAll((NSArray<EOTreeObject>)descendant.valueForKey("children"));
			}
			
			currentDescendants.addAll(nextDescendants);
		} while (currentDescendants.size() > currentCount);

		return currentDescendants.allObjects();
	}

	public NSArray<EOTreeObject> ancestors() {

		NSMutableArray<EOTreeObject> ancestors = new NSMutableArray<EOTreeObject>(this);

		PTreeObject current = this;
		while (current.valueForKey("parent") != null) {
			current = (PTreeObject)current.valueForKey("parent");
			ancestors.add(current);
		}

		return ancestors.immutableClone();
	}

	public EOTreeObject parent() {
		return (EOTreeObject)this.storedValueForKey("parent");
	}

	public NSArray<EOTreeObject> children() {
		return (NSArray<EOTreeObject>)this.storedValueForKey("children");
	}

	public void addElementUnderParent(EOTreeObject nextParent) {
		if (nextParent == null)
			return;

		this.takeStoredValueForKey(nextParent, "parent");
	}

	public void moveElementToParent(EOTreeObject nextParent) {

		if (nextParent == null) return;

		this.takeStoredValueForKey(nextParent, "parent");
	}
}
