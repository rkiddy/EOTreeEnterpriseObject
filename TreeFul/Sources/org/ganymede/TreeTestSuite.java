package org.ganymede;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.junit.runner.Description;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunListener;

public class TreeTestSuite {

	public static Test suite() {
		TestSuite suite = new TestSuite();
		suite.addTestSuite(TreeTest.class);
		return suite;
	}

	public static class TreeTestListener extends RunListener {
		 public void testFailure(Failure failure) { System.out.print("x"); }
		 public void testFinished(Description description) { System.out.print("."); }
		 public void testRunFinished(Result result) { System.out.println(""); }
	}

	public static class TreeTest extends TestCase {

		public void testFullName() {	
		}

		public void testToString() {
		}

		public void testDescendants() {
		}

		public void testChildren() {
		}

		public void testAncestors() {
		}

		public void testAncestorAtDistance() {
		}

		public void testParent() {
		}

		public void testAddElementUnderParent() {
		}

		public void testMoveElementUnderParentToParent() {		
		}
	}
}
