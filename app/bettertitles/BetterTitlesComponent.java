package bettertitles;

import com.commercetools.sunrise.common.pages.PageData;
import com.commercetools.sunrise.framework.ControllerComponent;
import com.commercetools.sunrise.hooks.PageDataHook;
import com.commercetools.sunrise.productcatalog.home.HomePageContent;
import io.sphere.sdk.models.Base;

import static org.apache.commons.lang3.ObjectUtils.defaultIfNull;
import static org.apache.commons.lang3.StringUtils.isEmpty;

public class BetterTitlesComponent extends Base implements ControllerComponent, PageDataHook {
    public static final String SUNRISE_DEMO_SHOP = "Sunrise Demo Shop";

    @Override
    public void acceptPageData(final PageData pageData) {
        final String newTitle = deduceNewTitle(pageData);
        pageData.getHeader().setTitle(newTitle);
    }

    private static String deduceNewTitle(final PageData pageData) {
        final String title = defaultIfNull(pageData.getHeader().getTitle(), "");
        final String newTitle1;
        if (pageData.getContent() instanceof HomePageContent) {
            newTitle1 = SUNRISE_DEMO_SHOP + " Home";
        } else if (!isEmpty(title)){
            newTitle1 = title + " ~ " + SUNRISE_DEMO_SHOP;
        } else {
            newTitle1 = SUNRISE_DEMO_SHOP;
        }
        return newTitle1;
    }

    //TODO need to wire it als multi controller component
}
