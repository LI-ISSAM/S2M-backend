package ma.s2m.nxp.fe.settings.repositories.specifications;

import ma.s2m.nxp.fe.settings.domain.program.Program;
import ma.s2m.nxp.fe.settings.enums.ProgramStatus;
import ma.s2m.nxp.fe.settings.enums.ProgramType;
import org.springframework.data.jpa.domain.Specification;

public class ProgramSpecifications {

    private static final String NAME = "name";
    private static final String TYPE = "type";
    private static final String STATUS = "status";
    private static final String INSTITUTION = "institution";

    private ProgramSpecifications() {
    }

    public static Specification<Program> hasNameLike(String name) {
        return (root, query, cb) ->
                cb.like(cb.lower(root.get(NAME)), "%" + name.toLowerCase() + "%");
    }

    public static Specification<Program> hasInstitutionId(Long institutionId) {
        return (root, query, cb) -> cb.equal(root.get(INSTITUTION).get("id"), institutionId);
    }

    public static Specification<Program> hasType(ProgramType type) {
        return (root, query, cb) -> cb.equal(root.get(TYPE), type);
    }

    public static Specification<Program> hasStatus(ProgramStatus status) {
        return (root, query, cb) -> cb.equal(root.get(STATUS), status);
    }
}