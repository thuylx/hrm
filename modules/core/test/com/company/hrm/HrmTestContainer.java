package com.company.hrm;

import com.haulmont.bali.util.Dom4j;
import com.haulmont.cuba.testsupport.TestContainer;
import org.dom4j.Document;
import org.dom4j.Element;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

public class HrmTestContainer extends TestContainer {

    public HrmTestContainer() {
        super();
        appComponents = new ArrayList<>(Arrays.asList(
                "com.haulmont.cuba",
                // add CUBA premium add-ons here
                // "com.haulmont.bpm",
                // "com.haulmont.charts",
                // "com.haulmont.fts",
                // "com.haulmont.reports",
                // and custom app components if any
                "vn.tki.erp.cambpm"
        ));
        appPropertiesFiles = Arrays.asList(
                // List the files defined in your web.xml
                // in appPropertiesConfig context parameter of the core module
                "com/company/hrm/app.properties",
                // Add this file which is located in CUBA and defines some properties
                // specifically for test environment. You can replace it with your own
                // or add another one in the end.
                "test-app.properties");
        initDbProperties();
    }

    private void initDbProperties() {
//        File contextXmlFile = new File("modules/core/web/META-INF/context.xml");
//        if (!contextXmlFile.exists()) {
//            contextXmlFile = new File("web/META-INF/context.xml");
//        }
//        if (!contextXmlFile.exists()) {
//            throw new RuntimeException("Cannot find 'context.xml' file to read database connection properties. " +
//                    "You can set them explicitly in this method.");
//        }
//        Document contextXmlDoc = Dom4j.readDocument(contextXmlFile);
//        Element resourceElem = contextXmlDoc.getRootElement().element("Resource");
//
//        dbDriver = resourceElem.attributeValue("driverClassName");
//        dbUrl = resourceElem.attributeValue("url");
//        dbUser = resourceElem.attributeValue("username");
//        dbPassword = resourceElem.attributeValue("password");

        dbDriver = "org.postgresql.Driver";
        dbUrl = "jdbc:postgresql://localhost/hrmTest";
        dbUser = "postgres";
        dbPassword = "123456";
    }

    public static class Common extends HrmTestContainer {

        public static final HrmTestContainer.Common INSTANCE = new HrmTestContainer.Common();

        private static volatile boolean initialized;

        private Common() {
        }

        @Override
        public void before() throws Throwable {
            if (!initialized) {
                super.before();
                initialized = true;
            }
            setupContext();
        }

        @Override
        public void after() {
            cleanupContext();
            // never stops - do not call super
        }
    }
}