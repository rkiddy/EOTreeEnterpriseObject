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
