package didemo;

import com.commercetools.sunrise.common.pages.PageContent;

import java.util.List;

public class DiDemoPage extends PageContent {

    private List<String> subjects;

    public List<String> getSubjects() {
        return subjects;
    }

    public void setSubjects(final List<String> subjects) {
        this.subjects = subjects;
    }
}
