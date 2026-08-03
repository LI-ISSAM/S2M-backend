package ma.s2m.nxp.fe.settings.repositories.specifications;

import jakarta.persistence.criteria.Join;
import ma.s2m.nxp.fe.settings.enums.SubscriptionStatus;
import ma.s2m.nxp.fe.settings.domain.customer.Customer;
import ma.s2m.nxp.fe.settings.domain.subscription.Subscription;
import org.springframework.data.jpa.domain.Specification;

public class SubscriptionSpecifications {

    private static final String CUSTOMER = "customer";
    private static final String FULL_NAME = "fullName";
    private static final String EMAIL = "email";
    private static final String CONTACT = "contact";
    private static final String PROGRAM = "program";
    private static final String STATUS = "status";
    private static final String ID = "id";

    private SubscriptionSpecifications() {
    }

    public static Specification<Subscription> hasCustomerNameLike(String name) {
        return (root, query, cb) -> {
            Join<Subscription, Customer> customer = root.join(CUSTOMER);
            return cb.like(cb.lower(customer.get(FULL_NAME)), "%" + name.toLowerCase() + "%");
        };
    }

    public static Specification<Subscription> hasProgramId(Long programId) {
        return (root, query, cb) -> cb.equal(root.get(PROGRAM).get(ID), programId);
    }

    public static Specification<Subscription> hasCustomerEmailLike(String email) {
        return (root, query, cb) -> {
            Join<Subscription, Customer> customer = root.join(CUSTOMER);
            return cb.like(cb.lower(customer.get(CONTACT).get(EMAIL)), "%" + email.toLowerCase() + "%");
        };
    }
    public static Specification<Subscription> hasStatus(SubscriptionStatus status) {
        return (root, query, cb) -> cb.equal(root.get(STATUS), status);
    }
}