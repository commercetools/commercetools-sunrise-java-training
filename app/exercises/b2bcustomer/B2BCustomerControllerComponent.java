package exercises.b2bcustomer;

import com.commercetools.sunrise.framework.components.ControllerComponent;
import com.commercetools.sunrise.framework.hooks.requests.CustomerQueryHook;
import io.sphere.sdk.customergroups.CustomerGroup;
import io.sphere.sdk.customers.queries.CustomerQuery;

/**
 * In this exercise we are going to learn how to modify a CTP request before it has been executed.
 * We are going to display the {@link CustomerGroup} in "my personal details" page and for that we need to expand the reference.
 * Notice that in order to assign some customer group to our customers, we have enabled the possibility to register a B2B customer
 * via the sign up form by checking the option "Do you represent a company?"
 *
 * Step 1: Register this component to {@link controllers.myaccount.MyPersonalDetailsController}
 * Step 2: Implement the missing hook
 *   Hook: For every {@link CustomerQuery} to be executed, expand the reference {@code customerGroup}
 */
public class B2BCustomerControllerComponent implements ControllerComponent, CustomerQueryHook {

    @Override
    public CustomerQuery onCustomerQuery(final CustomerQuery customerQuery) {
        return customerQuery.plusExpansionPaths(customer -> customer.customerGroup());
    }
}