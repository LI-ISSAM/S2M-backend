package ma.s2m.nxp.fe.settings.orchestration.impl;

import ma.s2m.nxp.fe.settings.domain.program.Program;
import ma.s2m.nxp.fe.settings.DTO.program.ProgramDTO;
import ma.s2m.nxp.fe.settings.exceptions.BusinessException;
import ma.s2m.nxp.fe.settings.mappers.ProgramMapper;
import ma.s2m.nxp.fe.settings.orchestration.IProgramOrchestrationService;
import ma.s2m.nxp.fe.settings.services.IProgramService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProgramOrchestrationServiceImpl implements IProgramOrchestrationService {

    private final IProgramService programService;
    private final ProgramMapper programMapper;

    public ProgramOrchestrationServiceImpl(IProgramService programService, ProgramMapper programMapper) {
        this.programService = programService;
        this.programMapper = programMapper;
    }

    @Override
    public ProgramsPageResponse getAllPrograms(int page, int limit, String name, Long institutionId) {
        int zeroBasedPage = Math.max(page - 1, 0);
        PageRequest pageRequest = PageRequest.of(zeroBasedPage, limit, Sort.by(Sort.Direction.ASC, "name"));

        Page<Program> result = programService.getAllPrograms(pageRequest, name, institutionId);

        List<ProgramDTO> content = result.getContent().stream()
                .map(programMapper::toDTO)
                .toList();

        return new ProgramsPageResponse(content, result.getTotalElements(), result.getTotalPages(), page);
    }

    @Override
    public ProgramDTO getProgramById(Long id) throws BusinessException {
        Program program = programService.getProgramById(id)
                .orElseThrow(() -> new BusinessException("PGM_002", "Program not found", HttpStatus.NOT_FOUND));
        return programMapper.toDTO(program);
    }

    @Override
    public ProgramDTO createProgram(ProgramDTO dto) throws BusinessException {
        Program program = programMapper.toEntity(dto);
        Program saved = programService.createProgram(program, dto.getInstitutionId());
        return programMapper.toDTO(saved);
    }

    @Override
    public ProgramDTO updateProgram(Long id, ProgramDTO dto) throws BusinessException {
        Program newData = programMapper.toEntity(dto);
        Program updated = programService.updateProgram(id, newData, dto.getInstitutionId());
        return programMapper.toDTO(updated);
    }

    @Override
    public void deleteProgram(Long id) throws BusinessException {
        programService.deleteProgram(id);
    }
}