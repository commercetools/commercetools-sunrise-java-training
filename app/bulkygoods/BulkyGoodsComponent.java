package bulkygoods;


import com.commercetools.sunrise.framework.ControllerComponent;
import io.sphere.sdk.carts.Cart;
import io.sphere.sdk.carts.CustomLineItem;
import io.sphere.sdk.carts.commands.updateactions.AddCustomLineItem;
import io.sphere.sdk.models.LocalizedString;
import io.sphere.sdk.models.Referenceable;
import io.sphere.sdk.taxcategories.TaxCategory;
import io.sphere.sdk.utils.MoneyImpl;

import javax.money.MonetaryAmount;
import java.util.Locale;
import java.util.Optional;

public class BulkyGoodsComponent implements ControllerComponent {
    public static final MonetaryAmount MONETARY_AMOUNT = MoneyImpl.ofCents(321, "EUR");
    public static final String BULKY_FEE = "bulkyFee";
    public static final LocalizedString NAME = LocalizedString.of(Locale.GERMAN, "Sperrgutzuschlag", Locale.ENGLISH, "bulky goods fee");
    public static final String TAX_CATEGORY_NAME = "standard";

    //task honor the expansion paths!


    private AddCustomLineItem createLineItemDraft(final Referenceable<TaxCategory> taxCategory) {
        return AddCustomLineItem.of(NAME, BULKY_FEE, MONETARY_AMOUNT, taxCategory, 1);
    }

    private boolean containsBulkyGoods(final Cart cart) {
        return cart.getLineItems().stream().mapToLong(item -> item.getQuantity()).sum() > 3;
    }

    private Optional<CustomLineItem> findBulkyGoodsFeeCustomObject(final Cart cart) {
        return cart.getCustomLineItems().stream().filter(item -> BULKY_FEE.equals(item.getSlug())).findFirst();
    }
}
