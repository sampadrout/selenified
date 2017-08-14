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

import java.util.Map;
import java.util.Set;

import com.coveros.selenified.tools.OutputFile;
import com.coveros.selenified.tools.OutputFile.Success;

/**
 * Assert will handle all verifications performed on the actual element. These
 * asserts are custom to the framework, and in addition to providing easy object
 * oriented capabilities, they take screenshots with each verification to
 * provide additional traceability, and assist in troubleshooting and debugging
 * failing tests.
 * 
 * @author Max Saperstone
 * @version 3.0.0
 * @lastupdate 8/13/2017
 */
public class Assert {

    // this will be the name of the file we write all commands out to
    protected OutputFile file;

    // what element are we trying to interact with on the page
    protected Element element;

    // constants
    protected static final String EXPECTED = "Expected to find ";
    protected static final String CLASS = "class";

    protected static final String NOTINPUT = " is not an input on the page";

    protected static final String VALUE = " has the value of <b>";
    protected static final String TEXT = " has the text of <b>";
    protected static final String HASVALUE = " contains the value of <b>";
    protected static final String HASNTVALUE = " does not contain the value of <b>";
    protected static final String HASTEXT = " contains the text of <b>";
    protected static final String HASNTTEXT = " does not contain the text of <b>";
    protected static final String ONLYVALUE = ", only the values <b>";
    protected static final String CLASSVALUE = " has a class value of <b>";

    protected static final String NOTSELECT = " is not a select on the page";
    protected static final String NOTTABLE = " is not a table on the page";

    protected boolean isPresent() {
        if (!element.is().present()) {
            element.waitFor().present();
            if (!element.is().present()) {
                return false;
            }
        }
        return true;
    }

    /**
     * Determines if the element is a select
     * 
     * @return Boolean: whether the element is an select or not
     */
    protected boolean isSelect() {
        if (!element.is().select()) {
            file.recordActual(element.prettyOutputStart() + NOTSELECT, Success.FAIL);
            file.addError();
            return false;
        }
        return true;
    }

    /**
     * Determines if the element is a table
     * 
     * @return Boolean: whether the element is an table or not
     */
    protected boolean isTable() {
        if (!element.is().table()) {
            file.recordActual(element.prettyOutputStart() + NOTTABLE, Success.FAIL);
            file.addError();
            return false;
        }
        return true;
    }

    /**
     * Determines if the element is a present, and if it is, it is a select
     * 
     * @param expected
     *            - the expected outcome
     * @return Boolean: whether the element is a select or not
     */
    protected boolean isPresentSelect(String expected) {
        // wait for the element
        if (!isPresent()) {
            return false;
        }
        file.recordExpected(expected);
        // verify this is a select element
        return isSelect();
    }

    /**
     * Determines if the element is a present, and if it is, it is a table
     * 
     * @param expected
     *            - the expected outcome
     * @return Boolean: whether the element is an table or not
     */
    protected boolean isPresentTable(String expected) {
        // wait for the element
        if (!isPresent()) {
            return false;
        }
        file.recordExpected(expected);
        // verify this is a select element
        return isTable();
    }

    protected String[] getAttributes(String attribute, String expected) {
        // wait for the element
        if (!isPresent()) {
            return null; // NOSONAR - returning an empty array could be confused
                            // with no attributes
        }
        file.recordExpected(EXPECTED + element.prettyOutput() + " " + expected + " attribute <b>" + attribute + "</b>");
        // check our attributes
        Map<String, String> attributes = element.get().allAttributes();
        if (attributes == null) {
            file.recordActual("Unable to assess the attributes of " + element.prettyOutput(), Success.FAIL);
            file.addError();
            return null; // NOSONAR - returning an empty array could be confused
                            // with no attributes
        }
        Set<String> keys = attributes.keySet();
        return keys.toArray(new String[keys.size()]);
    }

    protected String getValue(String value, String expected) {
        // wait for the element
        if (!isPresent()) {
            return null;
        }
        // file.record the element
        file.recordExpected(EXPECTED + element.prettyOutput() + expected + value + "</b>");
        // verify this is an input element
        if (!element.is().input()) {
            file.recordActual(element.prettyOutputStart() + NOTINPUT, Success.FAIL);
            file.addError();
            return null;
        }
        // check for the object to the present on the page
        return element.get().value();
    }
}