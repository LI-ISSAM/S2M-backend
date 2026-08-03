package ma.s2m.nxp.fe.settings.orchestration.impl;

import ma.s2m.nxp.fe.settings.domain.member.Institution;
import ma.s2m.nxp.fe.settings.dto.institution.InstitutionDTO;
import ma.s2m.nxp.fe.settings.exceptions.BusinessException;
import ma.s2m.nxp.fe.settings.mappers.InstitutionMapper;
import ma.s2m.nxp.fe.settings.orchestration.IInstitutionOrchestrationService;
import ma.s2m.nxp.fe.settings.services.IInstitutionService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InstitutionOrchestrationServiceImpl implements IInstitutionOrchestrationService {

    private final IInstitutionService institutionService;
    private final InstitutionMapper institutionMapper;

    public InstitutionOrchestrationServiceImpl(IInstitutionService institutionService,
                                               InstitutionMapper institutionMapper) {
        this.institutionService = institutionService;
        this.institutionMapper = institutionMapper;
    }

    @Override
    public InstitutionsPageResponse getAllInstitutions(int page, int limit, String name,String reference, String tag) {
        // Le frontend envoie des pages 1-based (json-server style) -> conversion en 0-based pour Spring
        int zeroBasedPage = Math.max(page - 1, 0);
        PageRequest pageRequest = PageRequest.of(zeroBasedPage, limit, Sort.by(Sort.Direction.ASC, "name"));

        Page<Institution> result = institutionService.getAllInstitutions(pageRequest, name,reference, tag);

        List<InstitutionDTO> content = result.getContent().stream()
                .map(institutionMapper::toDTO)
                .toList();

        return new InstitutionsPageResponse(
                content,
                result.getTotalElements(),
                result.getTotalPages(),
                page
        );
    }

    public List<InstitutionDTO> getAllInstitutionsForExport(String name , String reference , String tag){
        Page<Institution> result = institutionService.getAllInstitutions(Pageable.unpaged(),name,reference,tag);
        return result.getContent().stream()
                .map(institutionMapper::toDTO)
                .toList();
    }

    @Override
    public InstitutionDTO getInstitutionById(Long id) throws BusinessException {
        Institution institution = institutionService.getInstitutionById(id)
                .orElseThrow(() -> new BusinessException("INST_003", "Institution not found", HttpStatus.NOT_FOUND));
        return institutionMapper.toDTO(institution);
    }

    @Override
    public InstitutionDTO createInstitution(InstitutionDTO dto) throws BusinessException {
        Institution institution = institutionMapper.toEntity(dto);
        Institution saved = institutionService.createInstitution(institution);
        return institutionMapper.toDTO(saved);
    }

    @Override
    public InstitutionDTO updateInstitution(Long id, InstitutionDTO dto) throws BusinessException {
        Institution newData = institutionMapper.toEntity(dto);
        Institution updated = institutionService.updateInstitution(id, newData);
        return institutionMapper.toDTO(updated);
    }

    @Override
    public void deleteInstitution(Long id) throws BusinessException {
        institutionService.deleteInstitution(id);
    }
}