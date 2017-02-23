import com.commercetools.sunrise.framework.components.CartFieldsUpdaterControllerComponent;
import com.commercetools.sunrise.sessions.cart.CartInSessionControllerComponent;
import com.commercetools.sunrise.sessions.cart.CartOperationsControllerComponentSupplier;
import creditcardfee.CreditCardFeeControllerComponent;

import javax.inject.Inject;

public class TrainingCartOperationsControllerComponentSupplier extends CartOperationsControllerComponentSupplier {

    @Inject
    public TrainingCartOperationsControllerComponentSupplier(final CartFieldsUpdaterControllerComponent cartFieldsUpdaterControllerComponent,
                                                             final CartInSessionControllerComponent cartInSessionControllerComponent,
                                                             final CreditCardFeeControllerComponent creditCardFeeControllerComponent) {
        super(cartFieldsUpdaterControllerComponent, cartInSessionControllerComponent);
        add(creditCardFeeControllerComponent);
    }
}
