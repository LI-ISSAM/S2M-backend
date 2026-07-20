package ma.s2m.nxp.fe.settings.repositories.specifications;

import ma.s2m.nxp.fe.settings.domain.merchant.Merchant;
import ma.s2m.nxp.fe.settings.Enums.MerchantStatus;
import ma.s2m.nxp.fe.settings.Enums.MerchantType;
import org.springframework.data.jpa.domain.Specification;

public class MerchantSpecifications {

    private static final String NAME = "name";
    private static final String REFERENCE = "reference";
    private static final String TYPE = "type";
    private static final String STATUS = "status";
    private static final String INSTITUTION = "institution";

    private MerchantSpecifications() {
    }

    public static Specification<Merchant> hasNameLike(String name) {
        return (root, query, cb) ->
                cb.like(cb.lower(root.get(NAME)), "%" + name.toLowerCase() + "%");
    }

    public static Specification<Merchant> hasReferenceLike(String reference) {
        return (root, query, cb) ->
                cb.like(cb.lower(root.get(REFERENCE)), "%" + reference.toLowerCase() + "%");
    }

    public static Specification<Merchant> hasInstitutionId(Long institutionId) {
        return (root, query, cb) -> cb.equal(root.get(INSTITUTION).get("id"), institutionId);
    }

    public static Specification<Merchant> hasType(MerchantType type) {
        return (root, query, cb) -> cb.equal(root.get(TYPE), type);
    }

    public static Specification<Merchant> hasStatus(MerchantStatus status) {
        return (root, query, cb) -> cb.equal(root.get(STATUS), status);
    }
}