package exercises.creditcardfee;

import io.sphere.sdk.carts.Cart;
import io.sphere.sdk.carts.CustomLineItem;
import io.sphere.sdk.carts.commands.updateactions.AddCustomLineItem;
import io.sphere.sdk.carts.commands.updateactions.RemoveCustomLineItem;
import io.sphere.sdk.commands.UpdateAction;
import io.sphere.sdk.models.LocalizedString;
import io.sphere.sdk.models.Reference;
import io.sphere.sdk.models.Referenceable;
import io.sphere.sdk.payments.Payment;
import io.sphere.sdk.taxcategories.TaxCategory;
import io.sphere.sdk.utils.MoneyImpl;

import javax.money.MonetaryAmount;
import java.util.*;

import static java.util.stream.Collectors.joining;

final class CreditCardFeeUtils {

    private static final String FEE_SLUG = "creditCardFee";
    private static final String CREDIT_CARD_PAYMENT_METHOD = "creditcard";

    private CreditCardFeeUtils() {
    }

    static List<UpdateAction<Cart>> buildApplicableUpdateActions(final Cart cart, final TaxCategory taxCategory) {
        final Optional<CustomLineItem> appliedCreditCardFee = findAppliedCreditCardFee(cart);
        final Optional<Payment> creditCardPayment = findCreditCardPayment(cart);
        final boolean hasCreditCardFee = appliedCreditCardFee.isPresent();
        final boolean hasCreditCardPayment = creditCardPayment.isPresent();

        final List<UpdateAction<Cart>> updateActions = new ArrayList<>();
        if (hasCreditCardPayment && !hasCreditCardFee) {
            updateActions.add(addCreditCardFeeActions(cart, taxCategory));
        } else if (!hasCreditCardPayment && hasCreditCardFee){
            updateActions.add(removeCreditCardFeeActions(appliedCreditCardFee.get()));
        }
        // TODO Missing to update the payment to the new price!
        return updateActions;
    }

    static Optional<CustomLineItem> findAppliedCreditCardFee(final Cart cart) {
        return cart.getCustomLineItems().stream()
                .filter(item -> FEE_SLUG.equals(item.getSlug()))
                .findAny();
    }

    static String printableUpdateActions(final List<UpdateAction<Cart>> updateActions) {
        return updateActions.stream()
                .map(UpdateAction::getAction)
                .collect(joining(", "));
    }

    private static UpdateAction<Cart> addCreditCardFeeActions(final Cart cart, final Referenceable<TaxCategory> taxCategory) {
        final LocalizedString name = LocalizedString.of(Locale.GERMAN, "Kreditkartengebühr", Locale.ENGLISH, "Credit card fee");
        final MonetaryAmount fee = MoneyImpl.ofCents(321, cart.getCurrency());
        return AddCustomLineItem.of(name, FEE_SLUG, fee, taxCategory, 1);
    }

    private static UpdateAction<Cart> removeCreditCardFeeActions(final CustomLineItem customLineItem) {
        return RemoveCustomLineItem.of(customLineItem);
    }

    private static Optional<Payment> findCreditCardPayment(final Cart cart) {
        return Optional.ofNullable(cart.getPaymentInfo())
                .flatMap(paymentInfo -> paymentInfo.getPayments().stream()
                        .map(Reference::getObj)
                        .filter(Objects::nonNull)
                        // Only the last one is valid (Sunrise removes the old ones but later in time)
                        .sorted((payment1, payment2) -> payment2.getLastModifiedAt().compareTo(payment1.getLastModifiedAt()))
                        .findFirst()
                        .filter(payment -> CREDIT_CARD_PAYMENT_METHOD.equals(payment.getPaymentMethodInfo().getMethod())));
    }
}
