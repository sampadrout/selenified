/*
 * Copyright 2017 Coveros, Inc.
 * 
 * This file is part of Selenified.
 * 
 * Selenified is licensed under the Apache License, Version
 * 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy 
 * of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, 
 * software distributed under the License is distributed on 
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY 
 * KIND, either express or implied. See the License for the 
 * specific language governing permissions and limitations 
 * under the License.
 */

package tools;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.util.Arrays;
import java.util.Date;

import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import org.testng.log4testng.Logger;

import selenified.exceptions.InvalidBrowserException;
import tools.logging.TestOutput;
import tools.selenium.SeleniumHelper;
import tools.selenium.SeleniumHelper.Browsers;

@Listeners({ tools.Listener.class })
public class TestBase {

	private static final Logger log = Logger.getLogger(General.class);
	protected static GeneralFunctions genFun = new GeneralFunctions();

	protected static String testSite = "http://www.google.com/";
	protected String author = "Max Saperstone";
	protected static String version;

	@DataProvider(name = "no options", parallel = true)
	public Object[][] NoOptions() {
		return new Object[][] { new Object[] { "" }, };
	}

	public static class MasterSuiteSetupConfigurator {
		private static MasterSuiteSetupConfigurator instance;
		private boolean wasInvoked = false;

		private MasterSuiteSetupConfigurator() {
		}

		public static MasterSuiteSetupConfigurator getInstance() {
			if (instance != null) {
				return instance;
			}
			instance = new MasterSuiteSetupConfigurator();
			return instance;
		}

		public void doSetup() {
			if (wasInvoked) {
				return;
			}
			initializeSystem();
			wasInvoked = true;
		}
	}

	@BeforeSuite(alwaysRun = true)
	public void beforeSuite() {
		MasterSuiteSetupConfigurator.getInstance().doSetup();
	}

	@BeforeMethod(alwaysRun = true)
	protected void startTest(Object[] dataProvider, Method method, ITestContext test) throws IOException {
		startTest(dataProvider, method, test, true);
	}

	protected static void initializeSystem() {
		// check our browser
		if (System.getProperty("browser") == null || System.getProperty("browser").equals("${browser}")) {
			System.setProperty("browser", Browsers.HtmlUnit.toString());
		}
		// see if we are using a Selenium hub
		if (System.getProperty("hubAddress") == null || System.getProperty("hubAddress").equals("${hubAddress}")) {
			System.setProperty("hubAddress", "LOCAL");
		}
		// check to see if we are passing in a site address
		if (System.getProperty("appURL") != null && !System.getProperty("appURL").equals("${appURL}")) {
			testSite = System.getProperty("appURL");
		}
	}

	protected void startTest(Object[] dataProvider, Method method, ITestContext test, boolean selenium)
			throws IOException {
		String testName = getTestName(method, dataProvider);
		String suite = test.getName();
		String outputDir = test.getOutputDirectory();
		String extClass = test.getCurrentXmlTest().getXmlClasses().get(0).getName();
		String fileLocation = "src." + extClass;
		File file = new File(fileLocation.replaceAll("\\.", "/") + ".java");
		Date lastModified = new Date(file.lastModified());
		String description = "";
		String group = "";
		Test annotation = method.getAnnotation(Test.class);
		// set description from annotation
		if (annotation.description() != null) {
			description = annotation.description();
		}
		// adding in the group if it exists
		if (annotation.groups() != null) {
			group = Arrays.toString(annotation.groups());
			group = group.substring(1, group.length() - 1);
		}

		TestOutput output = new TestOutput(testName, outputDir, testSite, suite, General.wordToSentence(group),
				lastModified, version, author, description);
		long time = (new Date()).getTime();
		output.setStartTime(time);
		if (selenium) {
			SeleniumHelper selHelper;
			try {
				selHelper = new SeleniumHelper(output);
				test.setAttribute(testName + "SelHelper", selHelper);
			} catch (InvalidBrowserException | MalformedURLException e) {
				log.error(e);
			}
		}
		test.setAttribute(testName + "Output", output);
		test.setAttribute(testName + "Errors", output.startTestTemplateOutputFile(selenium));
	}

	@AfterMethod(alwaysRun = true)
	protected void endTest(Object[] dataProvider, Method method, ITestContext test, ITestResult result) {
		String testLink = getTestName(method, dataProvider);
		String testName = method.getName();
		if (dataProvider != null) {
			testName += " : ";
			for (Object data : dataProvider) {
				if (data == null || data.toString().startsWith("public")) {
					break;
				}
				testName += data.toString() + ", ";
			}
			testName.substring(0, testName.length() - 2);
		}
		if (test.getAttribute(testLink + "SelHelper") != null) {
			SeleniumHelper selHelper = (SeleniumHelper) test.getAttribute(testLink + "SelHelper");
			selHelper.killDriver();
		}
	}

	protected void finalize(TestOutput output) throws IOException {
		genFun.stopTest(output);
	}

	@AfterSuite(alwaysRun = true)
	protected void archiveTestResults() {
		System.out.println("\nREMEMBER TO ARCHIVE YOUR TESTS!\n\n");
	}

	protected static String getTestName(Method method, Object... dataProvider) {
		String testName = method.getName();
		if (dataProvider != null && dataProvider.length > 0 && dataProvider[0] != null
				&& !dataProvider[0].toString().startsWith("public")) {
			testName += "WithOption";
			for (Object data : dataProvider) {
				if (data == null || data.toString().startsWith("public")) {
					break;
				}
				testName += General.capitalizeFirstLetters(General.removeNonWordCharacters(data.toString()));
			}
		}
		return testName;
	}

	protected static SeleniumHelper getSelHelper(Method method, ITestContext test, Object... dataProvider) {
		String testName = getTestName(method, dataProvider);
		return (SeleniumHelper) test.getAttribute(testName + "SelHelper");
	}

	protected static TestOutput getTestOutput(Method method, ITestContext test, Object... dataProvider) {
		String testName = getTestName(method, dataProvider);
		return (TestOutput) test.getAttribute(testName + "Output");
	}

	protected static int getErrors(Method method, ITestContext test, Object... dataProvider) {
		String testName = getTestName(method, dataProvider);
		return (Integer) test.getAttribute(testName + "Errors");
	}
}