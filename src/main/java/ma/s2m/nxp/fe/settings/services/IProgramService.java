package ma.s2m.nxp.fe.settings.services;

import ma.s2m.nxp.fe.settings.domain.program.Program;
import ma.s2m.nxp.fe.settings.exceptions.BusinessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface IProgramService {

    Page<Program> getAllPrograms(Pageable pageable, String name, Long institutionId);

    Optional<Program> getProgramById(Long id);

    Program createProgram(Program program, Long institutionId) throws BusinessException;

    Program updateProgram(Long id, Program newData, Long institutionId) throws BusinessException;

    void deleteProgram(Long id) throws BusinessException;
}