package br.com.guzzmega.omnichannel.suite;

import org.junit.platform.suite.api.SelectPackages;
import org.junit.platform.suite.api.Suite;
import org.junit.platform.suite.api.SuiteDisplayName;

@Suite
@SuiteDisplayName("Omnichannel Tests Suite")
@SelectPackages({
        "br.com.guzzmega.omnichannel.domain",
        "br.com.guzzmega.omnichannel.service",
        "br.com.guzzmega.omnichannel.controller"
})
public class SuiteTest {
}
