package ma.s2m.nxp.fe.settings.orchestration;

import ma.s2m.nxp.fe.settings.DTO.program.ProgramDTO;
import ma.s2m.nxp.fe.settings.exceptions.BusinessException;
import ma.s2m.nxp.fe.settings.orchestration.impl.ProgramsPageResponse;

public interface IProgramOrchestrationService {

    ProgramsPageResponse getAllPrograms(int page, int limit, String name, Long institutionId);

    ProgramDTO getProgramById(Long id) throws BusinessException;

    ProgramDTO createProgram(ProgramDTO dto) throws BusinessException;

    ProgramDTO updateProgram(Long id, ProgramDTO dto) throws BusinessException;

    void deleteProgram(Long id) throws BusinessException;
}