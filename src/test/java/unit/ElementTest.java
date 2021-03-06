package unit;

import com.coveros.selenified.Locator;
import com.coveros.selenified.element.Element;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNull;

public class ElementTest {

    @Test
    public void checkElementTypeNullTest() {
        Element element = new Element(null, null, null, "myId");
        assertNull(element.getType());
    }

    @Test
    public void checkElementTypeTest() {
        Element element = new Element(null, null, Locator.ID, "myId");
        assertEquals(element.getType(), Locator.ID);
    }

    @Test
    public void checkElementLocatorTest() {
        Element element = new Element(null, null, Locator.ID, "myId");
        assertEquals(element.getLocator(), "myId");
    }

    @Test
    public void checkElementMatchTest() {
        Element element = new Element(null, null, Locator.ID, "myId");
        assertEquals(element.getMatch(), 0);
    }

    @Test
    public void checkElementWebDriverTest() {
        Element element = new Element(null, null, Locator.ID, "myId");
        assertNull(element.getDriver());
    }

    @Test
    public void checkFullElementTypeTest() {
        Element element = new Element(null, null, Locator.ID, "myId", 2);
        assertEquals(element.getType(), Locator.ID);
    }

    @Test
    public void checkFullElementLocatorTest() {
        Element element = new Element(null, null, Locator.ID, "myId", 2);
        assertEquals(element.getLocator(), "myId");
    }

    @Test
    public void checkFullElementMatchTest() {
        Element element = new Element(null, null, Locator.ID, "myId", 2);
        assertEquals(element.getMatch(), 2);
    }

    @Test
    public void checkFullElementWebDriverTest() {
        Element element = new Element(null, null, Locator.ID, "myId", 2);
        assertNull(element.getDriver());
    }

    @Test
    public void checkParentElementTypeTest() {
        Element element = new Element(null, null, Locator.ID, "myId", null);
        assertEquals(element.getType(), Locator.ID);
    }

    @Test
    public void checkParentElementLocatorTest() {
        Element element = new Element(null, null, Locator.ID, "myId", null);
        assertEquals(element.getLocator(), "myId");
    }

    @Test
    public void checkParentElementMatchTest() {
        Element element = new Element(null, null, Locator.ID, "myId", null);
        assertEquals(element.getMatch(), 0);
    }

    @Test
    public void checkParentElementWebDriverTest() {
        Element element = new Element(null, null, Locator.ID, "myId", null);
        assertNull(element.getDriver());
    }

    @Test
    public void checkFullParentElementTypeTest() {
        Element element = new Element(null, null, Locator.ID, "myId", 2, null);
        assertEquals(element.getType(), Locator.ID);
    }

    @Test
    public void checkFullParentElementLocatorTest() {
        Element element = new Element(null, null, Locator.ID, "myId", 2, null);
        assertEquals(element.getLocator(), "myId");
    }

    @Test
    public void checkFullParentElementMatchTest() {
        Element element = new Element(null, null, Locator.ID, "myId", 2, null);
        assertEquals(element.getMatch(), 2);
    }

    @Test
    public void checkFullParentElementWebDriverTest() {
        Element element = new Element(null, null, Locator.ID, "myId", 2, null);
        assertNull(element.getDriver());
    }

    @Test
    public void checkPrettyOutputTest() {
        Element element = new Element(null, null, Locator.ID, "myId");
        assertEquals(element.prettyOutput(), " 1st element with <i>ID</i> of <i>myId</i> ");
    }

    @Test
    public void checkPrettyOutputParentTest() {
        Element element = new Element(null, null, Locator.ID, "myId", new Element(null, null, Locator.NAME, "parentId"));
        assertEquals(element.prettyOutput(), " 1st element with <i>ID</i> of <i>myId</i> and parent of 1st element with <i>NAME</i> of <i>parentId</i> ");
    }

    @Test
    public void checkPrettyOutputGrandParentTest() {
        Element element = new Element(null, null, Locator.ID, "myId", new Element(null, null, Locator.NAME, "parentId", new Element(null, null, Locator.TAGNAME, "grandParentId")));
        assertEquals(element.prettyOutput(), " 1st element with <i>ID</i> of <i>myId</i> and parent of 1st element with <i>NAME</i> of <i>parentId</i> and parent of 1st element with <i>TAGNAME</i> of <i>grandParentId</i> ");
    }

    @Test
    public void checkPrettyOutputStartTest() {
        Element element = new Element(null, null, Locator.ID, "myId");
        assertEquals(element.prettyOutputStart(), "1st element with <i>ID</i> of <i>myId</i>");
    }

    @Test
    public void checkPrettyOutputEndTest() {
        Element element = new Element(null, null, Locator.ID, "myId");
        assertEquals(element.prettyOutputEnd(), "1st element with <i>ID</i> of <i>myId</i>. ");
    }

    @Test
    public void checkPrettyOutputMatchTest() {
        Element element = new Element(null, null, Locator.ID, "myId", 0);
        assertEquals(element.prettyOutput(), " 1st element with <i>ID</i> of <i>myId</i> ");
    }

    @Test
    public void checkPrettyOutputMatchParentTest() {
        Element element = new Element(null, null, Locator.ID, "myId", 1, new Element(null, null, Locator.NAME, "parentId", 2));
        assertEquals(element.prettyOutput(), " 2nd element with <i>ID</i> of <i>myId</i> and parent of 3rd element with <i>NAME</i> of <i>parentId</i> ");
    }

    @Test
    public void checkPrettyOutputMatchGrandParentTest() {
        Element element = new Element(null, null, Locator.ID, "myId", 2, new Element(null, null, Locator.NAME, "parentId", new Element(null, null, Locator.TAGNAME, "grandParentId", 22)));
        assertEquals(element.prettyOutput(), " 3rd element with <i>ID</i> of <i>myId</i> and parent of 1st element with <i>NAME</i> of <i>parentId</i> and parent of 23rd element with <i>TAGNAME</i> of <i>grandParentId</i> ");
    }

    @Test
    public void checkPrettyOutputMatchStartTest() {
        Element element = new Element(null, null, Locator.ID, "myId", 3);
        assertEquals(element.prettyOutputStart(), "4th element with <i>ID</i> of <i>myId</i>");
    }

    @Test
    public void checkPrettyOutputMatchEndTest() {
        Element element = new Element(null, null, Locator.ID, "myId", 10);
        assertEquals(element.prettyOutputEnd(), "11th element with <i>ID</i> of <i>myId</i>. ");
    }
}