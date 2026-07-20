package ma.s2m.nxp.fe.settings.repositories.specifications;

import ma.s2m.nxp.fe.settings.domain.member.Institution;
import org.springframework.data.jpa.domain.Specification;
import ma.s2m.nxp.fe.settings.Enums.InstitutionStatus;
import ma.s2m.nxp.fe.settings.Enums.InstitutionType;



public class InstitutionSpecifications {

    private static final String NAME = "name";
    private static final String REFERENCE ="reference";
    private static final String TYPE = "type";
    private static final String STATUS = "status";
    private static final String TAGS = "tags";

    private InstitutionSpecifications() {
    }

    public static Specification<Institution> hasNameLike(String name) {
        return (root, query, cb) ->
                cb.like(cb.lower(root.get(NAME)), "%" + name.toLowerCase() + "%");
    }
    public static Specification<Institution> hasReferenceLike(String reference){
        return (root,query,cb)->
                cb.like(cb.lower(root.get(REFERENCE)), "%"  + reference.toLowerCase() + "%");
    }

    public static Specification<Institution> hasType(InstitutionType type) {
        return (root, query, cb) -> cb.equal(root.get(TYPE), type);
    }

    public static Specification<Institution> hasStatus(InstitutionStatus status) {
        return (root, query, cb) -> cb.equal(root.get(STATUS), status);
    }

    public static Specification<Institution> hasTag(String tag) {
        return (root, query, cb) -> {
            query.distinct(true);
            return cb.isMember(tag, root.get(TAGS));
        };
    }
}