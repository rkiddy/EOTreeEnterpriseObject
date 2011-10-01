package org.ganymede.ui;

import org.ganymede.eo.CTreeObject;
import org.ganymede.eo.EOTree;
import org.ganymede.eo.EOTreeObject;
import org.ganymede.eo.PTreeObject;

import com.webobjects.appserver.WOActionResults;
import com.webobjects.appserver.WOContext;
import com.webobjects.eoaccess.EOUtilities;
import com.webobjects.eocontrol.EOEditingContext;
import com.webobjects.foundation.NSArray;

import er.extensions.components.ERXComponent;

public class Main extends ERXComponent {

	public Main(WOContext context) {
		super(context);
	}

	public boolean treesAreEqual() {
		EOTreeObject pTreeElement = this.pObjs().get(0);
		EOTree pTree = new EOTree(pTreeElement);

		EOTreeObject cTreeElement = this.cObjs().get(0);
		EOTree cTree = new EOTree(cTreeElement);

		return cTree.equals(pTree);
	}

	public EOTreeObject pObj;
	public EOTreeObject subPObj;
	public NSArray<EOTreeObject> pObjs() { return PTreeObject.fetchAllTreeObjects(session().defaultEditingContext()); }

	public EOTreeObject cObj;
	public EOTreeObject subCObj;

	public NSArray<EOTreeObject> cObjs() { return CTreeObject.fetchAllTreeObjects(session().defaultEditingContext()); }

	public String message;

	public String objName1;
	public String objName2;
	public String addToParentName;

	public WOActionResults editTree() {

		message = null;

		if (objName1 == null && objName2 == null) {
			message = "Please give me a node name to add or remove.";
			return null;
		}
		if (addToParentName != null && objName2 != null) {
			message = "Please pick either a tree element to add to or a tree element to move to, but not both.";
			return null;
		}

		System.out.println("objName1: "+objName1);
		System.out.println("objName2: "+objName2);
		System.out.println("addToParentName: "+addToParentName);

		EOEditingContext ec = session().defaultEditingContext();

		if (addToParentName != null) {
			System.out.println("Adding element under parent: "+addToParentName);

			PTreeObject pObj = (PTreeObject)EOUtilities.createAndInsertInstance(ec, "PTree");
			pObj.takeValueForKey(objName1, "name");

			CTreeObject cObj = (CTreeObject)EOUtilities.createAndInsertInstance(ec, "CTree");
			cObj.takeValueForKey(objName1, "name");

			ec.saveChanges();

			System.out.println("after save: pObj is "+pObj+" and cObj is "+cObj);

			PTreeObject pParent = (PTreeObject)EOUtilities.objectMatchingKeyAndValue(ec, "PTree", "name", addToParentName);
			pObj.addElementUnderParent(pParent);

			CTreeObject cParent = (CTreeObject)EOUtilities.objectMatchingKeyAndValue(ec, "CTree", "name", addToParentName);
			cObj.addElementUnderParent(cParent);

			ec.saveChanges();

			// I have to do this (really???) because I add an object and the parent does not immediately see
			// it as one of its children. This only happens with the PTree. Something else is probably going on
			// here. What is it? -rrk
			//
			ec.refaultObject(pParent);
			ec.refaultObject(cParent);
		}

		if (objName2 != null) {
			System.out.println("Removing element: "+objName2);

			pObj = (PTreeObject)EOUtilities.objectMatchingKeyAndValue(ec, "PTree", "name", objName2);
			pObj.removeElement();
			ec.deleteObject(pObj);

			cObj = (CTreeObject)EOUtilities.objectMatchingKeyAndValue(ec, "CTree", "name", objName2);
			cObj.removeElement();
			ec.deleteObject(cObj);

			ec.saveChanges();
		}

		return pageWithName(Main.class);
	}

	public WOActionResults clearEdits() {
		message = null;
		objName1 = null;
		objName2 = null;
		addToParentName = null;
		return null;
	}
}
