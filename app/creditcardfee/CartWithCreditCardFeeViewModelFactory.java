package creditcardfee;

import com.commercetools.sunrise.common.models.addresses.AddressViewModelFactory;
import com.commercetools.sunrise.common.models.carts.*;
import com.commercetools.sunrise.common.utils.PriceFormatter;
import io.sphere.sdk.carts.Cart;

import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.money.CurrencyUnit;

import static creditcardfee.CreditCardFeeUtils.findAppliedCreditCardFee;

public class CartWithCreditCardFeeViewModelFactory extends CartViewModelFactory {

    @Inject
    public CartWithCreditCardFeeViewModelFactory(final CurrencyUnit currency, final PriceFormatter priceFormatter,
                                                 final ShippingInfoViewModelFactory shippingInfoViewModelFactory,
                                                 final PaymentInfoViewModelFactory paymentInfoViewModelFactory,
                                                 final AddressViewModelFactory addressViewModelFactory,
                                                 final LineItemExtendedViewModelFactory lineItemExtendedViewModelFactory) {
        super(currency, priceFormatter, shippingInfoViewModelFactory, paymentInfoViewModelFactory, addressViewModelFactory, lineItemExtendedViewModelFactory);
    }

    @Override
    protected CartViewModel newViewModelInstance(@Nullable final Cart cart) {
        final CartViewModel viewModel = super.newViewModelInstance(cart);
        fillBulkyGoods(viewModel, cart);
        return viewModel;
    }

    private void fillBulkyGoods(final CartViewModel viewModel, @Nullable final Cart cart) {
        if (cart != null) {
            findAppliedCreditCardFee(cart)
                    .ifPresent(creditCardFee -> {
                        final String creditCardfee = getPriceFormatter().format(creditCardFee.getTotalPrice());
                        viewModel.put("creditCardFee", creditCardfee);
                    });
        }
    }
}
