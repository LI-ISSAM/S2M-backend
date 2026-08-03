package ma.s2m.nxp.fe.settings.orchestration;

import ma.s2m.nxp.fe.settings.dto.institution.InstitutionDTO;
import ma.s2m.nxp.fe.settings.exceptions.BusinessException;
import ma.s2m.nxp.fe.settings.orchestration.impl.InstitutionsPageResponse;

import java.util.List;

public interface IInstitutionOrchestrationService {

    InstitutionsPageResponse getAllInstitutions(int page, int limit, String name,String reference, String tag);

    List<InstitutionDTO> getAllInstitutionsForExport(String name , String reference , String tag);
    InstitutionDTO getInstitutionById(Long id) throws BusinessException;

    InstitutionDTO createInstitution(InstitutionDTO dto) throws BusinessException;

    InstitutionDTO updateInstitution(Long id, InstitutionDTO dto) throws BusinessException;

    void deleteInstitution(Long id) throws BusinessException;
}