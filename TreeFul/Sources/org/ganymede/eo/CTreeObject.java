package org.ganymede.eo;

import com.webobjects.eoaccess.EOUtilities;
import com.webobjects.eocontrol.EOEditingContext;
import com.webobjects.eocontrol.EOEnterpriseObject;
import com.webobjects.eocontrol.EOGenericRecord;
import com.webobjects.eocontrol.EOSortOrdering;
import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSDictionary;
import com.webobjects.foundation.NSMutableArray;

public class CTreeObject extends EOGenericRecord implements EOTreeObject {

	static NSArray sorts;

	static {
		EOSortOrdering sort = EOSortOrdering.sortOrderingWithKey("distance", EOSortOrdering.CompareDescending);
		sorts = new NSArray(sort);
	}

	public static NSArray<EOTreeObject> fetchAllTreeObjects(EOEditingContext ec) {
		return EOUtilities.objectsForEntityNamed(ec, "CTree");
	}

	public boolean equals(EOTreeObject element) {
		return true;
	}

	public String fullName() {
		return ((NSArray)this.ancestors().valueForKey("name")).componentsJoinedByString(":");
	}

	public String toString() {
		return (String)this.valueForKey("name");
	}

	public NSArray<EOTreeObject> descendants() {

		NSArray joins = EOUtilities.objectsMatchingKeyAndValue(
				this.editingContext(),
				"CTreeClosure",
				"up",
				this.valueForKey("pk"));

		NSMutableArray results = new NSMutableArray();

		for (EOEnterpriseObject join : (NSArray<EOEnterpriseObject>)joins) {

			Object downPk = join.valueForKey("down");
			
			EOEnterpriseObject child = EOUtilities.objectMatchingKeyAndValue(this.editingContext(), "CTree", "pk", downPk);
			results.add(child);
		}

		System.out.println("descendants: "+results);

		return results.immutableClone();
	}

	public NSArray<EOTreeObject> children() {

		NSArray joins = EOUtilities.objectsMatchingValues(
				this.editingContext(),
				"CTreeClosure",
				new NSDictionary(
						new NSArray(new Object[] { this.valueForKey("pk"), 1 }), 
						new NSArray(new Object[] { "up", "distance" })));

		NSMutableArray results = new NSMutableArray();

		for (EOEnterpriseObject join : (NSArray<EOEnterpriseObject>)joins) {

			Object downPk = join.valueForKey("down");

			EOEnterpriseObject child = EOUtilities.objectMatchingKeyAndValue(this.editingContext(), "CTree", "pk", downPk);
			results.add(child);
		}

		System.out.println("children: "+results);

		return results.immutableClone();
	}

	public NSArray<EOTreeObject> ancestors() {

		NSArray<EOEnterpriseObject> joins = EOUtilities.objectsMatchingKeyAndValue(
				this.editingContext(),
				"CTreeClosure",
				"down",
				this.valueForKey("pk"));

		NSArray<EOEnterpriseObject> sortedJoins = EOSortOrdering.sortedArrayUsingKeyOrderArray(joins, sorts);

		NSMutableArray<EOTreeObject> results = new NSMutableArray<EOTreeObject>();

		for (EOEnterpriseObject join : sortedJoins) {

			Object upPk = join.valueForKey("up");

			CTreeObject child = (CTreeObject)EOUtilities.objectMatchingKeyAndValue(this.editingContext(), "CTree", "pk", upPk);
			results.add(child);
		}

		return results.immutableClone();
	}

	private CTreeObject ancestorAtDistance(int distance) {

		NSArray results = EOUtilities.objectsMatchingValues(
				this.editingContext(),
				"CTreeClosure",
				new NSDictionary(
						new NSArray(new Object[] { this.valueForKey("pk"), distance }), 
						new NSArray(new Object[] { "down", "distance" })));

		EOEnterpriseObject parent = null;

		if (results != null && results.size() > 0) {
			EOEnterpriseObject result = (EOEnterpriseObject)results.get(0);
			Object upPk = result.valueForKey("up");

			parent = EOUtilities.objectMatchingKeyAndValue(this.editingContext(), "CTree", "pk", upPk);
		}

		System.out.println("parent: "+parent);

		return (CTreeObject)parent;
	}

	public CTreeObject parent() {
		return this.ancestorAtDistance(1);
	}

	public void addElementUnderParent(EOTreeObject nextParent) {

		System.out.println("inserted0: "+this.editingContext().insertedObjects());

//		NSArray rows = EOUtilities.rawRowsForSQL(this.editingContext(), "Trees", "select max(pk) from c_tree_closure", new NSArray("max"));
//		System.out.println("for pk: "+rows);
//		Number maxPk = (Number)((NSDictionary)rows.get(0)).objectForKey("max");
//		System.out.println("maxPk: "+maxPk);
		
		// Create the parent join
		//
		EOEnterpriseObject parentJoin = EOUtilities.createAndInsertInstance(this.editingContext(), "CTreeClosure");
		parentJoin.takeValueForKey(this.valueForKey("pk"), "down");
		parentJoin.takeValueForKey(this.valueForKey("pk"), "up");
		parentJoin.takeValueForKey(0, "distance");

		System.out.println("parentJoin: "+parentJoin);

		// Find all the ancestor join objects of the parent
		//
		NSArray<EOEnterpriseObject> joins = EOUtilities.objectsMatchingKeyAndValue(
				this.editingContext(),
				"CTreeClosure",
				"down",
				nextParent.valueForKey("pk"));

		// For all of the ancestor join objects, make a copy of them for this object
		//
		for (EOEnterpriseObject join : joins) {
			EOEnterpriseObject nextJoin = EOUtilities.createAndInsertInstance(this.editingContext(), "CTreeClosure");
			System.out.println("template join: "+join);

			// The new join keeps the ancestor (and so the up link) but takes the new object as the down.
			//
			nextJoin.takeValueForKey(join.valueForKey("up"), "up");
			nextJoin.takeValueForKey(this.valueForKey("pk"), "down");
			
			// increment the distances
			//
			int distance = ((Number)join.valueForKey("distance")).intValue();
			distance++;
			nextJoin.takeValueForKey(distance, "distance");
			System.out.println("next join: "+nextJoin);

		}
	}

	public void moveElementToParent(EOTreeObject element, EOTreeObject nextParent) {		
	}
}
