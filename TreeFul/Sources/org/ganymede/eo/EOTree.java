package org.ganymede.eo;

import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSSet;

public class EOTree {

	private EOTreeObject head = null;

	public EOTree() {
		super();
	}

	public EOTree(EOTreeObject element) {
		setHeadFromAncestorOf(element);
	}

	/**
	 * If needed, this initialization method will create and fill the Closure table needed for 
	 * this tree structure.
	 * 
	 * @param treeEntityName the EO which is to be accessed via the tree structure.
	 * @param parentAttributeName attribute name which points from an object to its parent.
	 * @param joinEntityName if this entity exists, it will be assumed to have the proper data.
	 */
	public static void initializeTree(String treeEntityName, String parentAttributeName, String joinEntityName) {
		// TBD
	}

	public EOTreeObject head() {
		return head;
	}

	public void setHeadFromAncestorOf(EOTreeObject element) {
		if (element == null) return;
		EOTreeObject current = element;
		while (current.parent() != null) { current = current.parent(); }
		head = current;
	}

	public boolean equals(EOTree tree) {

		if (tree == null) return false;

		if (this.head == null && tree.head() == null) return true;

		if (this.head != null && tree.head() == null) return false;

		if (this.head == null && tree.head() != null) return false;

		NSSet treeSet = new NSSet((NSArray<String>)head.descendants().valueForKey("name"));

		NSSet otherTreeSet = new NSSet((NSArray<String>)tree.head().descendants().valueForKey("name"));

		return treeSet.equals(otherTreeSet);
	}
}
