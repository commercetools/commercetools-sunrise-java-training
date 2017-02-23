package githubstream;

import com.commercetools.sunrise.common.models.ViewModel;

final class GitHubIssueData extends ViewModel {

    private String name;
    private String url;

    public GitHubIssueData() {
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(final String url) {
        this.url = url;
    }
}
