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

package com.coveros.selenified.selenium.element;

import com.coveros.selenified.tools.OutputFile;
import com.coveros.selenified.tools.OutputFile.Success;

/**
 * State extends Asserts to provide some additional verification capabilities.
 * It will handle all verifications performed on the actual element. These
 * asserts are custom to the framework, and in addition to providing easy object
 * oriented capabilities, they take screenshots with each verification to
 * provide additional traceability, and assist in troubleshooting and debugging
 * failing tests. State checks that elements are in a particular state.
 * 
 * @author Max Saperstone
 * @version 3.0.0
 * @lastupdate 8/13/2017
 */
public class State extends Assert {

    // constants
    private static final String PRESENT = " is present on the page";
    private static final String NOTPRESENT = " is not present on the page";
    private static final String VISIBLE = " is visible on the page";
    private static final String NOTVISIBLE = " is not visible on the page";
    private static final String CHECKED = " is checked on the page";
    private static final String NOTCHECKED = " is not checked on the page";
    private static final String IS = " is ";

    public State(Element element, OutputFile file) {
        this.element = element;
        this.file = file;
    }

    // ///////////////////////////////////////
    // assessing functionality
    // ///////////////////////////////////////

    /**
     * checks to see if an element is present on the page
     *
     */
    public void present() {
        // wait for the element
        if (!isPresent()) {
            return;
        }
        // file.record the element
        file.recordExpected(EXPECTED + element.prettyOutput() + PRESENT);
        file.recordActual(element.prettyOutputStart() + PRESENT, Success.PASS);
    }

    /**
     * checks to see if an element is not present on the page
     *
     */
    public void notPresent() {
        // wait for the element
        if (element.is().present()) {
            element.waitFor().notPresent();
            if (element.is().present()) {
                return;
            }
        }
        // file.record the element
        file.recordExpected(EXPECTED + element.prettyOutput() + NOTPRESENT);
        file.recordActual(element.prettyOutputStart() + NOTPRESENT, Success.PASS);
    }

    /**
     * checks to see if an element is visible on the page
     *
     */
    public void displayed() {
        // wait for the element
        if (!isPresent()) {
            return;
        }
        // file.record the element
        file.recordExpected(EXPECTED + element.prettyOutput() + VISIBLE);
        // check for the object to the visible
        if (!element.is().displayed()) {
            file.recordActual(element.prettyOutputStart() + NOTVISIBLE, Success.FAIL);
            file.addError();
            return;
        }
        file.recordActual(element.prettyOutputStart() + VISIBLE, Success.PASS);
    }

    /**
     * checks to see if an element is not visible on the page
     *
     */
    public void notDisplayed() {
        // wait for the element
        if (!isPresent()) {
            return;
        }
        // file.record the element
        file.recordExpected(EXPECTED + element.prettyOutput() + NOTVISIBLE);
        // check for the object to the visible
        if (element.is().displayed()) {
            file.recordActual(element.prettyOutputStart() + VISIBLE, Success.FAIL);
            file.addError();
            return;
        }
        file.recordActual(element.prettyOutputStart() + NOTVISIBLE, Success.PASS);
    }

    /**
     * checks to see if the actual element is editable
     * 
     * @param presence
     *            - what additional attribute is expected from the element
     */
    private void editable(String presence) {
        // check for the object to the editable
        if (!element.is().input()) {
            file.recordActual(element.prettyOutputStart() + IS + presence + " but not an input on the page",
                    Success.FAIL);
            file.addError();
            return;
        }
        if (!element.is().enabled()) {
            file.recordActual(element.prettyOutputStart() + IS + presence + " but not editable on the page",
                    Success.FAIL);
            file.addError();
            return;
        }
        file.recordActual(element.prettyOutputStart() + IS + presence + " and editable on the page", Success.PASS);
    }

    /**
     * checks to see if the actual element is editable
     * 
     * @param presence
     *            - what additional attribute is expected from the element
     */
    private void notEditable(String presence) {
        // check for the object to the editable
        if (element.is().input() && element.is().enabled()) {
            file.recordActual(element.prettyOutputStart() + IS + presence + " but editable on the page", Success.FAIL);
            file.addError();
            return;
        }
        file.recordActual(element.prettyOutputStart() + IS + presence + " and not editable on the page", Success.PASS);
    }

    /**
     * checks to see if an object is checked on the page
     *
     */
    public void checked() {
        // wait for the element
        if (!isPresent()) {
            return;
        }
        // file.record the element
        file.recordExpected(EXPECTED + element.prettyOutput() + CHECKED);
        // check for the object to the visible
        if (!element.is().checked()) {
            file.recordActual(element.prettyOutputStart() + NOTCHECKED, Success.FAIL);
            file.addError();
            return;
        }
        file.recordActual(element.prettyOutputStart() + CHECKED, Success.PASS);
    }

    /**
     * checks to see if an object is not checked on the page
     *
     */
    public void notChecked() {
        // wait for the element
        if (!isPresent()) {
            return;
        }
        // outputFile.record the element
        file.recordExpected(EXPECTED + element.prettyOutput() + NOTCHECKED);
        // check for the object to the visible
        if (element.is().checked()) {
            file.recordActual(element.prettyOutputStart() + " checked on the page", Success.FAIL);
            file.addError();
            return;
        }
        file.recordActual(element.prettyOutputStart() + NOTCHECKED, Success.PASS);
    }

    /**
     * checks to see if an object is visible and checked on the page
     *
     */
    public void displayedAndChecked() {
        // wait for the element
        if (!isPresent()) {
            return;
        }
        // file.record the element
        file.recordExpected(EXPECTED + element.prettyOutput() + CHECKED);
        // check for the object to the visible
        if (!element.is().displayed()) {
            file.recordActual(element.prettyOutputStart() + NOTVISIBLE, Success.FAIL);
            file.addError();
            return;
        }
        // check for the object to the checked
        if (!element.is().checked()) {
            file.recordActual(element.prettyOutputStart() + NOTCHECKED, Success.FAIL);
            file.addError();
            return;
        }
        file.recordActual(element.prettyOutputStart() + " is checked and visible on the page", Success.PASS);
    }

    /**
     * checks to see if an object is visible and not checked on the page
     *
     */
    public void displayedAndUnchecked() {
        // wait for the element
        if (!isPresent()) {
            return;
        }
        // file.record the element
        file.recordExpected(EXPECTED + element.prettyOutput() + NOTCHECKED);
        // check for the object to the visible
        if (!element.is().displayed()) {
            file.recordActual(element.prettyOutputStart() + NOTVISIBLE, Success.FAIL);
            file.addError();
            return;
        }
        // check for the object to the visible
        if (element.is().checked()) {
            file.recordActual(element.prettyOutputStart() + CHECKED, Success.FAIL);
            file.addError();
            return;
        }
        file.recordActual(element.prettyOutputStart() + " is not checked and visible on the page", Success.PASS);
    }

    /**
     * checks to see if an element is editable on the page
     *
     */
    public void editable() {
        // wait for the element
        if (!isPresent()) {
            return;
        }
        // file.record the element
        file.recordExpected(EXPECTED + element.prettyOutput() + " editable on the page");
        editable("present");
    }

    /**
     * checks to see if an element is not editable on the page
     *
     */
    public void notEditable() {
        // wait for the element
        if (!isPresent()) {
            return;
        }
        // outputFile.record the element
        file.recordExpected(EXPECTED + element.prettyOutput() + " not editable on the page");
        notEditable("present");
    }

    /**
     * checks to see if an element is visible and editable on the page
     *
     */
    public void displayedAndEditable() {
        // wait for the element
        if (!isPresent()) {
            return;
        }
        file.recordExpected(EXPECTED + element.prettyOutput() + " visible and editable on the page");
        // check for the object to the visible
        if (!element.is().displayed()) {
            file.recordActual(element.prettyOutputStart() + NOTVISIBLE, Success.FAIL);
            file.addError();
            return;
        }
        editable("visable");
    }

    /**
     * checks to see if an element is visible and not editable on the page
     *
     */
    public void displayedAndNotEditable() {
        // wait for the element
        if (!isPresent()) {
            return;
        }
        // file.record the element
        file.recordExpected(EXPECTED + element.prettyOutput() + " visible and not editable on the page");
        // check for the object to the visible
        if (!element.is().displayed()) {
            file.recordActual(element.prettyOutputStart() + NOTVISIBLE, Success.FAIL);
            file.addError();
            return;
        }
        notEditable("visible");
    }
}