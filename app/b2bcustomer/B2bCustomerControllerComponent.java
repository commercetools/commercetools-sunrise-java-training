package b2bcustomer;

import com.commercetools.sunrise.framework.components.ControllerComponent;
import com.commercetools.sunrise.framework.hooks.requests.CustomerQueryHook;
import io.sphere.sdk.customers.queries.CustomerQuery;

public class B2bCustomerControllerComponent implements ControllerComponent, CustomerQueryHook {

    @Override
    public CustomerQuery onCustomerQuery(final CustomerQuery customerQuery) {
        return customerQuery.plusExpansionPaths(customer -> customer.customerGroup());
    }
}