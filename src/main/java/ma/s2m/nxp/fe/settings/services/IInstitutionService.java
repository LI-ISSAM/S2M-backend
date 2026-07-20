package ma.s2m.nxp.fe.settings.services;

import ma.s2m.nxp.fe.settings.domain.member.Institution;
import ma.s2m.nxp.fe.settings.exceptions.BusinessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface IInstitutionService {

    Page<Institution> getAllInstitutions(Pageable pageable, String name, String reference, String tag);

    Optional<Institution> getInstitutionById(Long id);

    Optional<Institution> getInstitutionByReference(String reference);

    Institution createInstitution(Institution institution) throws BusinessException;

    Institution updateInstitution(Long id, Institution newData) throws BusinessException;

    void deleteInstitution(Long id) throws BusinessException;

    boolean existsByReference(String reference);

    boolean existsByName(String name);
}