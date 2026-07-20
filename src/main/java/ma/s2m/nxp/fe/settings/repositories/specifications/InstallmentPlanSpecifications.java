package ma.s2m.nxp.fe.settings.repositories.specifications;

import ma.s2m.nxp.fe.settings.domain.installmentplan.InstallmentPlan;
import ma.s2m.nxp.fe.settings.Enums.InstallmentPlanStatus;
import org.springframework.data.jpa.domain.Specification;

public class InstallmentPlanSpecifications {

    private static final String CUSTOMER = "customer";
    private static final String OFFER = "offer";
    private static final String FULL_NAME = "fullName";
    private static final String NAME = "name";
    private static final String STATUS = "status";

    private InstallmentPlanSpecifications() {
    }

    /**
     * Jointure vers Customer.fullName, requis par le frontend (customerName_like).
     */
    public static Specification<InstallmentPlan> hasCustomerNameLike(String customerName) {
        return (root, query, cb) -> {
            query.distinct(true);
            return cb.like(cb.lower(root.join(CUSTOMER).get(FULL_NAME)), "%" + customerName.toLowerCase() + "%");
        };
    }

    /**
     * Jointure vers Offer.name, requis par le frontend (offerName_like).
     * Utilise un LEFT JOIN car offer est optionnel (nullable) sur le plan.
     */
    public static Specification<InstallmentPlan> hasOfferNameLike(String offerName) {
        return (root, query, cb) -> {
            query.distinct(true);
            return cb.like(cb.lower(root.join(OFFER, jakarta.persistence.criteria.JoinType.LEFT).get(NAME)),
                    "%" + offerName.toLowerCase() + "%");
        };
    }

    public static Specification<InstallmentPlan> hasCustomerId(Long customerId) {
        return (root, query, cb) -> cb.equal(root.get(CUSTOMER).get("id"), customerId);
    }

    public static Specification<InstallmentPlan> hasOfferId(Long offerId) {
        return (root, query, cb) -> cb.equal(root.get(OFFER).get("id"), offerId);
    }

    public static Specification<InstallmentPlan> hasStatus(InstallmentPlanStatus status) {
        return (root, query, cb) -> cb.equal(root.get(STATUS), status);
    }
}