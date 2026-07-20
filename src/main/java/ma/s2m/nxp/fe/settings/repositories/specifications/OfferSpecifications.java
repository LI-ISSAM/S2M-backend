package ma.s2m.nxp.fe.settings.repositories.specifications;

import ma.s2m.nxp.fe.settings.domain.offer.Offer;
import ma.s2m.nxp.fe.settings.Enums.OfferStatus;
import org.springframework.data.jpa.domain.Specification;

public class OfferSpecifications {

    private static final String NAME = "name";
    private static final String STATUS = "status";
    private static final String PROGRAM = "program";

    private OfferSpecifications() {
    }

    public static Specification<Offer> hasNameLike(String name) {
        return (root, query, cb) ->
                cb.like(cb.lower(root.get(NAME)), "%" + name.toLowerCase() + "%");
    }

    public static Specification<Offer> hasProgramId(Long programId) {
        return (root, query, cb) -> cb.equal(root.get(PROGRAM).get("id"), programId);
    }

    public static Specification<Offer> hasStatus(OfferStatus status) {
        return (root, query, cb) -> cb.equal(root.get(STATUS), status);
    }
}