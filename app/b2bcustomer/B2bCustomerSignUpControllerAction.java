package b2bcustomer;

import com.commercetools.sunrise.framework.hooks.HookRunner;
import com.commercetools.sunrise.myaccount.authentication.signup.DefaultSignUpControllerAction;
import com.commercetools.sunrise.myaccount.authentication.signup.SignUpFormData;
import com.commercetools.sunrise.sessions.cart.CartInSession;
import io.sphere.sdk.client.SphereClient;
import io.sphere.sdk.customergroups.CustomerGroup;
import io.sphere.sdk.customers.CustomerDraft;
import io.sphere.sdk.customers.CustomerDraftBuilder;
import io.sphere.sdk.customers.commands.CustomerCreateCommand;

import javax.inject.Inject;
import javax.inject.Named;

public class B2bCustomerSignUpControllerAction extends DefaultSignUpControllerAction {

    private final CustomerGroup b2bCustomerGroup;

    @Inject
    public B2bCustomerSignUpControllerAction(final SphereClient sphereClient, final HookRunner hookRunner,
                                             final CartInSession cartInSession,
                                             @Named("b2b") final CustomerGroup b2bCustomerGroup) {
        super(sphereClient, hookRunner, cartInSession);
        this.b2bCustomerGroup = b2bCustomerGroup;
    }

    @Override
    protected CustomerCreateCommand buildRequest(final SignUpFormData formData) {
        final CustomerCreateCommand customerCreateCommand = super.buildRequest(formData);
        if (formData instanceof B2bCustomerSignUpFormData) {
            if (((B2bCustomerSignUpFormData) formData).isB2b()) {
                return commandWithCustomerGroup(customerCreateCommand);
            }
        }
        return customerCreateCommand;
    }

    private CustomerCreateCommand commandWithCustomerGroup(final CustomerCreateCommand customerCreateCommand) {
        final CustomerDraft draft = CustomerDraftBuilder.of(customerCreateCommand.getDraft())
                .customerGroup(b2bCustomerGroup)
                .build();
        return CustomerCreateCommand.of(draft)
                .withExpansionPaths(customerCreateCommand.expansionPaths()); // We still need to honor them
    }
}
