package com.accusoft.tests.ocs;

import net.thucydides.jbehave.ThucydidesJUnitStories;
import org.springframework.util.SystemPropertyUtils;

public class TestSuite extends ThucydidesJUnitStories {

    private static final String STORY_NAME_PATTERN = "**/${story.name:*}.story";

    public TestSuite() {
        findStoriesCalled(storyNamesFromEnvironmentVariable());
    }

    @Override
    public void run() throws Throwable {
        super.run();
    }

    private String storyNamesFromEnvironmentVariable() {
        return SystemPropertyUtils.resolvePlaceholders(STORY_NAME_PATTERN);
    }
}
