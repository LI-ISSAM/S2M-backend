package ma.s2m.nxp.fe.settings.repositories.specifications;

import ma.s2m.nxp.fe.settings.domain.installment.CustomerInstallment;
import ma.s2m.nxp.fe.settings.enums.CustomerInstallmentStatus;
import org.springframework.data.jpa.domain.Specification;

public class CustomerInstallmentSpecifications {

    private static final String CUSTOMER = "customer";
    private static final String FULL_NAME = "fullName";
    private static final String EMAIL = "email";
    private static final String STATUS = "status";

    private CustomerInstallmentSpecifications() {
    }

    public static Specification<CustomerInstallment> hasCustomerNameLike(String customerName) {
        return (root, query, cb) -> {
            query.distinct(true);
            return cb.like(cb.lower(root.join(CUSTOMER).get(FULL_NAME)), "%" + customerName.toLowerCase() + "%");
        };
    }

    public static Specification<CustomerInstallment> hasCustomerEmailLike(String customerEmail) {
        return (root, query, cb) -> {
            query.distinct(true);
            return cb.like(cb.lower(root.join(CUSTOMER).get(EMAIL)), "%" + customerEmail.toLowerCase() + "%");
        };
    }

    public static Specification<CustomerInstallment> hasCustomerId(Long customerId) {
        return (root, query, cb) -> cb.equal(root.get(CUSTOMER).get("id"), customerId);
    }

    public static Specification<CustomerInstallment> hasStatus(CustomerInstallmentStatus status) {
        return (root, query, cb) -> cb.equal(root.get(STATUS), status);
    }
}