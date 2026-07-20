package ma.s2m.nxp.fe.settings.services.impl;

import lombok.extern.slf4j.Slf4j;
import ma.s2m.nxp.fe.settings.domain.member.Institution;
import ma.s2m.nxp.fe.settings.exceptions.BusinessException;
import ma.s2m.nxp.fe.settings.repositories.InstitutionRepository;
import ma.s2m.nxp.fe.settings.repositories.ProgramRepository;
import ma.s2m.nxp.fe.settings.repositories.specifications.InstitutionSpecifications;
import ma.s2m.nxp.fe.settings.services.IInstitutionService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ma.s2m.nxp.fe.settings.repositories.MerchantRepository;

import java.util.Optional;

@Slf4j
@Service
@Transactional
public class InstitutionService implements IInstitutionService {

    private final InstitutionRepository institutionRepository;
    private final ProgramRepository programRepository;
    private final MerchantRepository merchantRepository;

    public InstitutionService(InstitutionRepository institutionRepository,MerchantRepository merchantRepository, ProgramRepository programRepository) {
        this.institutionRepository = institutionRepository;
        this.programRepository = programRepository;
        this.merchantRepository = merchantRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Institution> getAllInstitutions(Pageable pageable, String name, String reference , String tag) {
        Specification<Institution> spec = (root, query, cb) -> cb.conjunction();

        if (name != null && !name.isBlank()) {
            spec = spec.and(InstitutionSpecifications.hasNameLike(name));
        }
        if (tag != null && !tag.isBlank()) {
            spec = spec.and(InstitutionSpecifications.hasTag(tag));
        }
        if(reference != null && !reference.isBlank()){
            spec = spec.and(InstitutionSpecifications.hasReferenceLike(reference));
        }

        return institutionRepository.findAll(spec, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Institution> getInstitutionById(Long id) {
        return institutionRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Institution> getInstitutionByReference(String reference) {
        return institutionRepository.findByReference(reference);
    }

    @Override
    public Institution createInstitution(Institution institution) throws BusinessException {
        if (institutionRepository.existsByReference(institution.getReference())) {
            throw new BusinessException("INST_001", "Institution reference already exists", HttpStatus.CONFLICT);
        }
        if (institutionRepository.existsByNameIgnoreCase(institution.getName())) {
            throw new BusinessException("INST_002", "Institution name already exists", HttpStatus.CONFLICT);
        }
        log.info("Creating institution with reference={}", institution.getReference());
        return institutionRepository.save(institution);
    }

    @Override
    public Institution updateInstitution(Long id, Institution newData) throws BusinessException {
        Institution existing = institutionRepository.findById(id)
                .orElseThrow(() -> new BusinessException("INST_003", "Institution not found", HttpStatus.NOT_FOUND));

        existing.setName(newData.getName());
        existing.setType(newData.getType());
        existing.setStatus(newData.getStatus());
        existing.setLogo(newData.getLogo());
        existing.setContact(newData.getContact());
        existing.setDescription(newData.getDescription());
        existing.setOnboardingDate(newData.getOnboardingDate());
        existing.setTags(newData.getTags());
        existing.setReference((newData.getReference()));

        log.info("Updating institution id={}", id);
        return institutionRepository.save(existing);
    }

    @Override
    public void deleteInstitution(Long id) throws BusinessException {
        if (!institutionRepository.existsById(id)) {
            throw new BusinessException("INST_003", "Institution not found", HttpStatus.NOT_FOUND);
        }

        long linkedProgramsCount = programRepository.countByInstitution_Id(id);
        if (linkedProgramsCount > 0) {
            throw new BusinessException(
                    "INST_004",
                    "Cannot delete this institution: it still has " + linkedProgramsCount
                            + " program(s) attached. Please delete or reassign them first.",
                    HttpStatus.CONFLICT
            );
        }

            if (merchantRepository.existsByInstitution_Id(id)) {
                throw new BusinessException(
                        "INST_005",
                        "Cannot delete this institution: it still has merchant(s) attached. "
                                + "Please delete or reassign them first.",
                        HttpStatus.CONFLICT
                );
        };


        institutionRepository.deleteById(id);
        log.info("Deleted institution id={}", id);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByReference(String reference) {
        return institutionRepository.existsByReference(reference);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByName(String name) {
        return institutionRepository.existsByNameIgnoreCase(name);
    }
}