package integration;

import com.coveros.selenified.DriverSetup;
import com.coveros.selenified.application.App;
import org.apache.commons.io.FileUtils;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;

public class NoLoadIT extends WebBase {

    @BeforeMethod(alwaysRun = true)
    protected void startTest(Object[] dataProvider, Method method, ITestContext test, ITestResult result) {
        super.startTest(dataProvider, method, test, result, DriverSetup.OPEN);
    }

    @SuppressWarnings("deprecation")
    @Test(groups = {"integration"},
            description = "An integration test to verify we can start a test with a browser, but won't load any app")
    public void verifyNoLoad(ITestContext context) throws IOException {
        // use this object to manipulate the app
        App app = this.apps.get();
        // verify a selenium actions class was setup
        org.testng.Assert.assertNotNull(app);
        String directory = context.getOutputDirectory();
        String file = app.getOutputFile().getFileName();
        org.testng.Assert.assertFalse(FileUtils.readFileToString(new File(directory, file))
                .contains("Opening new browser and loading up starting app"));
        // verify the app wasn't attempted to load
        app.azzert().urlEquals(getTestSite(this.getClass().getName(), context));
        // verify one issue from the above check
        finish(1);
    }
}