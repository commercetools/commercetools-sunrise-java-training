package creditcardfee;

import com.commercetools.sunrise.framework.components.ControllerComponent;
import com.commercetools.sunrise.framework.hooks.actions.CartUpdatedActionHook;
import com.commercetools.sunrise.framework.hooks.events.CartUpdatedHook;
import com.commercetools.sunrise.framework.hooks.requests.CartQueryHook;
import com.commercetools.sunrise.framework.hooks.requests.CartUpdateCommandHook;
import io.sphere.sdk.carts.Cart;
import io.sphere.sdk.carts.commands.CartUpdateCommand;
import io.sphere.sdk.carts.queries.CartQuery;
import io.sphere.sdk.client.SphereClient;
import io.sphere.sdk.commands.UpdateAction;
import io.sphere.sdk.expansion.ExpansionPathContainer;
import io.sphere.sdk.taxcategories.TaxCategory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.List;
import java.util.concurrent.CompletionStage;
import java.util.stream.Collectors;

import static creditcardfee.CreditCardFeeUtils.buildApplicableUpdateActions;
import static java.util.concurrent.CompletableFuture.completedFuture;

/**
 * Hook 1: On every {@link CartUpdateCommand}, expand the reference {@code cart.paymentInfo.payments}
 * Hook 2: Every time the cart has been updated, call {@link #updateCartWithCreditCardFee(Cart, ExpansionPathContainer)} to update the credit card fee
 */
public class CreditCardFeeControllerComponent implements ControllerComponent, CartUpdateCommandHook, CartUpdatedActionHook {

    private final Logger LOGGER = LoggerFactory.getLogger(CreditCardFeeControllerComponent.class);

    private final SphereClient sphereClient;
    private final TaxCategory taxCategory;

    @Inject
    public CreditCardFeeControllerComponent(final SphereClient sphereClient, @Named("standard") final TaxCategory taxCategory) {
        this.sphereClient = sphereClient;
        this.taxCategory = taxCategory;
    }

    @Override
    public CartUpdateCommand onCartUpdateCommand(final CartUpdateCommand cartUpdateCommand) {
        LOGGER.debug("Adding expansion paths");
        return cartUpdateCommand.plusExpansionPaths(cart -> cart.paymentInfo().payments());
    }

    @Override
    public CompletionStage<Cart> onCartUpdatedAction(final Cart cart, final ExpansionPathContainer<Cart> expansionPathContainer) {
        LOGGER.debug("WTF WTF WTF WTF %%%%%%%%%%%% {} ", cart.getVersion());
        return updateCartWithCreditCardFee(cart, expansionPathContainer);
    }

    /**
     * Checks if the cart needs to add or remove the credit card fee and changes the cart accordingly.
     * The fee is applied when the credit card is selected as payment method in the cart.
     *
     * Notice: It requires {@code cart.paymentInfo.payments} to be expanded to work!
     *
     * @param cart the cart where to apply the credit card fee if necessary
     * @param expansionPathContainer the expansion paths to be honored
     * @return the updated cart with the applied changes and honored expanded paths
     */
    private CompletionStage<Cart> updateCartWithCreditCardFee(final Cart cart, final ExpansionPathContainer<Cart> expansionPathContainer) {
        final List<UpdateAction<Cart>> updateActions = buildApplicableUpdateActions(cart, taxCategory);
        if (!updateActions.isEmpty()) {
            LOGGER.debug("Applying {} update actions for credit card fee", updateActions.stream().map(UpdateAction::getAction).collect(Collectors.joining(", ")));
            final CartUpdateCommand command = CartUpdateCommand.of(cart, updateActions);
            return sphereClient.execute(command.withExpansionPaths(expansionPathContainer));
        } else {
            LOGGER.debug("No changes applied for credit card fee");
            return completedFuture(cart);
        }
    }
}