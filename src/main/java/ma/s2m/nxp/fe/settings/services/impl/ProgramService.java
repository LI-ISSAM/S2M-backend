package ma.s2m.nxp.fe.settings.services.impl;

import lombok.extern.slf4j.Slf4j;
import ma.s2m.nxp.fe.settings.domain.member.Institution;
import ma.s2m.nxp.fe.settings.domain.program.Program;
import ma.s2m.nxp.fe.settings.exceptions.BusinessException;
import ma.s2m.nxp.fe.settings.repositories.InstitutionRepository;
import ma.s2m.nxp.fe.settings.repositories.OfferRepository;
import ma.s2m.nxp.fe.settings.repositories.ProgramRepository;
import ma.s2m.nxp.fe.settings.repositories.specifications.ProgramSpecifications;
import ma.s2m.nxp.fe.settings.services.IProgramService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
@Transactional
public class ProgramService implements IProgramService {

    private final ProgramRepository programRepository;
    private final InstitutionRepository institutionRepository;
    private final OfferRepository offerRepository;

    public ProgramService(ProgramRepository programRepository, InstitutionRepository institutionRepository,
                          OfferRepository offerRepository) {
        this.programRepository = programRepository;
        this.institutionRepository = institutionRepository;
        this.offerRepository = offerRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Program> getAllPrograms(Pageable pageable, String name, Long institutionId) {
        Specification<Program> spec = (root, query, cb) -> cb.conjunction();

        if (name != null && !name.isBlank()) {
            spec = spec.and(ProgramSpecifications.hasNameLike(name));
        }
        if (institutionId != null) {
            spec = spec.and(ProgramSpecifications.hasInstitutionId(institutionId));
        }

        return programRepository.findAll(spec, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Program> getProgramById(Long id) {
        return programRepository.findById(id);
    }

    @Override
    public Program createProgram(Program program, Long institutionId) throws BusinessException {
        Institution institution = resolveInstitution(institutionId);

        if (programRepository.existsByNameIgnoreCase(program.getName())) {
            throw new BusinessException("PGM_001",
                    "A program with this name already exists", HttpStatus.CONFLICT);
        }

        program.setInstitution(institution);
        log.info("Creating program name={} for institutionId={}", program.getName(), institutionId);
        return programRepository.save(program);
    }

    @Override
    public Program updateProgram(Long id, Program newData, Long institutionId) throws BusinessException {
        Program existing = programRepository.findById(id)
                .orElseThrow(() -> new BusinessException("PGM_002", "Program not found", HttpStatus.NOT_FOUND));

        Institution institution = resolveInstitution(institutionId);

        if (programRepository.existsByNameIgnoreCaseAndIdNot(newData.getName(), id)) {
            throw new BusinessException("PGM_001",
                    "A program with this name already exists", HttpStatus.CONFLICT);
        }

        existing.setName(newData.getName());
        existing.setInstitution(institution);
        existing.setType(newData.getType());
        existing.setStatus(newData.getStatus());
        existing.setEligibility(newData.getEligibility());
        existing.setAllowedSubBins(newData.getAllowedSubBins());
        existing.setFee(newData.getFee());
        existing.setLimit(newData.getLimit());
        existing.setAllowedChannels(newData.getAllowedChannels());

        log.info("Updating program id={}", id);
        return programRepository.save(existing);
    }

    @Override
    public void deleteProgram(Long id) throws BusinessException {
        if (!programRepository.existsById(id)) {
            throw new BusinessException("PGM_002", "Program not found", HttpStatus.NOT_FOUND);
        }

        if (offerRepository.existsByProgram_Id(id)) {
            throw new BusinessException(
                    "PGM_005",
                    "Cannot delete this program: it still has offer(s) attached. Please delete them first.",
                    HttpStatus.CONFLICT
            );
        }

        programRepository.deleteById(id);
        log.info("Deleted program id={}", id);
    }

    private Institution resolveInstitution(Long institutionId) throws BusinessException {
        if (institutionId == null) {
            throw new BusinessException("PGM_003", "Institution is required", HttpStatus.BAD_REQUEST);
        }
        return institutionRepository.findById(institutionId)
                .orElseThrow(() -> new BusinessException("PGM_004", "Institution not found", HttpStatus.NOT_FOUND));
    }
}