package ma.s2m.nxp.fe.settings.repositories.specifications;

import ma.s2m.nxp.fe.settings.domain.operation.Operation;
import org.springframework.data.jpa.domain.Specification;

public class OperationSpecifications {

    private static final String RRN = "rrn";
    private static final String MERCHANT = "merchant";
    private static final String REFERENCE = "reference";
    private static final String CUSTOMER = "customer";
    private static final String EMAIL = "email";
    private static final String BNPL_PROGRAM = "bnplProgram";
    private static final String NAME = "name";

    private OperationSpecifications() {
    }

    /**
     * Le frontend appelle ce paramètre "reference_like" mais il correspond en
     * réalité à la référence du MERCHANT (cf. OperationSpace.vue / recherche
     * "Rechercher par référence commerçant"), pas un champ propre à Operation.
     */
    public static Specification<Operation> hasMerchantReferenceLike(String reference) {
        return (root, query, cb) -> {
            query.distinct(true);
            return cb.like(cb.lower(root.join(MERCHANT).get(REFERENCE)), "%" + reference.toLowerCase() + "%");
        };
    }

    public static Specification<Operation> hasCustomerEmailLike(String email) {
        return (root, query, cb) -> {
            query.distinct(true);
            return cb.like(cb.lower(root.join(CUSTOMER).get(EMAIL)), "%" + email.toLowerCase() + "%");
        };
    }

    public static Specification<Operation> hasProgramNameLike(String programName) {
        return (root, query, cb) -> {
            query.distinct(true);
            return cb.like(cb.lower(root.join(BNPL_PROGRAM).get(NAME)), "%" + programName.toLowerCase() + "%");
        };
    }
}