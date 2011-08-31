package org.ganymede;

import org.junit.runner.JUnitCore;

import er.extensions.appserver.ERXApplication;

public class Application extends ERXApplication {
	public static void main(String[] argv) {
		ERXApplication.main(argv, Application.class);
	}

	public Application() {
		ERXApplication.log.info("Welcome to " + name() + " !");
		/* ** put your initialization code in here ** */
	}

	@Override
	public void didFinishLaunching() {

		super.didFinishLaunching();

		JUnitCore core = new JUnitCore();
		core.addListener(new TreeTestSuite.TreeTestListener());
		core.run(TreeTestSuite.suite());
	}
}
