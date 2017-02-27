package creditcardfee;

import com.commercetools.sunrise.framework.components.CartFieldsUpdaterControllerComponent;
import com.commercetools.sunrise.sessions.cart.CartInSessionControllerComponent;
import com.commercetools.sunrise.sessions.cart.CartOperationsControllerComponentSupplier;
import creditcardfee.CreditCardFeeControllerComponent;

import javax.inject.Inject;

public class CreditCardFeeCartOperationsControllerComponentSupplier extends CartOperationsControllerComponentSupplier {

    @Inject
    public CreditCardFeeCartOperationsControllerComponentSupplier(final CartFieldsUpdaterControllerComponent cartFieldsUpdaterControllerComponent,
                                                                  final CartInSessionControllerComponent cartInSessionControllerComponent,
                                                                  final CreditCardFeeControllerComponent creditCardFeeControllerComponent) {
        super(cartFieldsUpdaterControllerComponent, cartInSessionControllerComponent);
        add(creditCardFeeControllerComponent);
    }
}
