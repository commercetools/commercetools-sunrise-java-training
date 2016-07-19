package bulkygoods;


import com.commercetools.sunrise.framework.ControllerComponent;
import io.sphere.sdk.carts.Cart;
import io.sphere.sdk.carts.CustomLineItem;
import io.sphere.sdk.carts.LineItem;
import io.sphere.sdk.carts.commands.CartUpdateCommand;
import io.sphere.sdk.carts.commands.updateactions.AddCustomLineItem;
import io.sphere.sdk.carts.commands.updateactions.RemoveCustomLineItem;
import io.sphere.sdk.client.SphereClient;
import io.sphere.sdk.expansion.ExpansionPathContainer;
import io.sphere.sdk.models.LocalizedString;
import io.sphere.sdk.models.Referenceable;
import io.sphere.sdk.taxcategories.TaxCategory;
import io.sphere.sdk.taxcategories.queries.TaxCategoryQuery;
import io.sphere.sdk.utils.MoneyImpl;

import javax.inject.Inject;
import javax.money.MonetaryAmount;
import java.util.Locale;
import java.util.Optional;
import java.util.concurrent.CompletionStage;

import static io.sphere.sdk.utils.CompletableFutureUtils.successful;

public class BulkyGoodsComponent implements ControllerComponent {
    public static final MonetaryAmount MONETARY_AMOUNT = MoneyImpl.ofCents(321, "EUR");
    public static final String BULKY_FEE_SLUG = "bulkyFee";
    public static final LocalizedString NAME = LocalizedString.of(Locale.GERMAN, "Sperrgutzuschlag", Locale.ENGLISH, "bulky goods fee");
    public static final String TAX_CATEGORY_NAME = "standard";

    @Inject
    private SphereClient sphereClient;

    private CompletionStage<Cart> filterCart(final Cart cart, final ExpansionPathContainer<Cart> expansionPathContainer) {
        final boolean containsBulkyGoods = containsBulkyGoods(cart);
        final Optional<CustomLineItem> customLineItemOptional = findBulkyGoodsFee(cart);
        if (containsBulkyGoods && !customLineItemOptional.isPresent()) {
            return addBulkyGoodsFee(cart, expansionPathContainer);
        } else if (!containsBulkyGoods && customLineItemOptional.isPresent()) {
            return removeBulkyGoodsFee(cart, expansionPathContainer, customLineItemOptional.get());
        } else {
            return successful(cart);
        }
    }

    private CompletionStage<Cart> removeBulkyGoodsFee(final Cart cart, final ExpansionPathContainer<Cart> expansionPathContainer, final CustomLineItem customLineItem) {
        return sphereClient.execute(CartUpdateCommand.of(cart, RemoveCustomLineItem.of(customLineItem))
                .plusExpansionPaths(expansionPathContainer));//honor the requested expansions
    }

    private CompletionStage<Cart> addBulkyGoodsFee(final Cart cart, final ExpansionPathContainer<Cart> expansionPathContainer) {
        return sphereClient.execute(TaxCategoryQuery.of().byName(TAX_CATEGORY_NAME))
                .thenCompose(res -> {
                    final TaxCategory taxCategory = res.head().orElseThrow(() -> new RuntimeException("TaxCategory Standard is Missing"));
                    final CartUpdateCommand cmd = CartUpdateCommand.of(cart, createLineItemDraft(taxCategory))
                            .plusExpansionPaths(expansionPathContainer);//honor the requested expansions
                    return sphereClient.execute(cmd);
                });
    }

    private AddCustomLineItem createLineItemDraft(final Referenceable<TaxCategory> taxCategory) {
        return AddCustomLineItem.of(NAME, BULKY_FEE_SLUG, MONETARY_AMOUNT, taxCategory, 1);
    }

    private boolean containsBulkyGoods(final Cart cart) {
        return cart.getLineItems().stream().mapToLong(LineItem::getQuantity).sum() > 3;
    }

    private Optional<CustomLineItem> findBulkyGoodsFee(final Cart cart) {
        return cart.getCustomLineItems().stream().filter(item -> BULKY_FEE_SLUG.equals(item.getSlug())).findFirst();
    }
}