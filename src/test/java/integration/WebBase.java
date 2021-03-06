package integration;

import com.coveros.selenified.Selenified;
import org.mockserver.integration.ClientAndServer;
import org.testng.ITestContext;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeSuite;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.mockserver.integration.ClientAndServer.startClientAndServer;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;

public class WebBase extends Selenified {
    private ClientAndServer mockServer;
    private int mockPort = 1070;


    @BeforeClass(alwaysRun = true)
    public void beforeClass(ITestContext test) {
        // set the base URL for the tests here
        setAppURL(this, test, "http://localhost:" + mockPort + "/");
        // set the author of the tests here
        setAuthor(this, test, "Max Saperstone\n<br/>max.saperstone@coveros.com");
        // set the version of the tests or of the software, possibly with a dynamic check
        setVersion(this, test, "3.3.1");

        // when running on sauce labs, timezones can mess with the cookie expiration date, so setting it here to EST
        addAdditionalDesiredCapabilities(this, test, "timeZone", "New_York");
    }

    private static String readFile(String path) throws IOException {
        byte[] encoded = Files.readAllBytes(Paths.get(path));
        return new String(encoded);
    }

    @BeforeSuite(alwaysRun = true)
    public void startMockServer() throws IOException {
        if( System.getProperty("mockPort") != null) {
            mockPort += Integer.valueOf(System.getProperty("mockPort"));
        }
        System.setProperty("mockserver.logLevel", "OFF");
        mockServer = startClientAndServer(mockPort);
        mockServer.when(request().withMethod("GET").withPath("/"))
                .respond(response().withBody(readFile("public/index.html")));
        mockServer.when(request().withMethod("GET").withPath("/next_page.html"))
                .respond(response().withBody(readFile("public/next_page.html")));
    }

    @AfterSuite(alwaysRun = true)
    public void stopMockServer() {
        mockServer.stop();
    }
}
