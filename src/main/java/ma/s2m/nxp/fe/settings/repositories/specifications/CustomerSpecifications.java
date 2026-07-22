package ma.s2m.nxp.fe.settings.repositories.specifications;

import ma.s2m.nxp.fe.settings.domain.customer.Customer;
import ma.s2m.nxp.fe.settings.Enums.SubBin;
import org.springframework.data.jpa.domain.Specification;

public class CustomerSpecifications {

    private static final String FULL_NAME = "fullName";
    private static final String SUB_BIN = "subBin";
    private static final String LAST_NAME ="lastName";
    private static final String EMAIL ="email";

    private CustomerSpecifications() {
    }

    public static Specification<Customer> hasFullNameLike(String fullName) {
        return (root, query, cb) ->
                cb.like(cb.lower(root.get(FULL_NAME)), "%" + fullName.toLowerCase() + "%");
    }
    public static Specification<Customer> hasEmailLike(String email) {
        return (root, query, cb) ->
                cb.like(cb.lower(root.get(EMAIL)), "%" + email.toLowerCase() + "%");
    }
    public static Specification<Customer> hasLastNameLike(String lastName){
        return (root, query, cb) ->

                cb.like(cb.lower(root.get(LAST_NAME)), "%" + lastName.toLowerCase() + "%");

    }

    public static Specification<Customer> hasSubBin(SubBin subBin) {
        return (root, query, cb) -> cb.equal(root.get(SUB_BIN), subBin);
    }
}