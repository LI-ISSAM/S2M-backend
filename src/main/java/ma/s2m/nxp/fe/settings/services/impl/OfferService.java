package ma.s2m.nxp.fe.settings.services.impl;

import lombok.extern.slf4j.Slf4j;
import ma.s2m.nxp.fe.settings.domain.offer.Offer;
import ma.s2m.nxp.fe.settings.domain.offer.OfferFee;
import ma.s2m.nxp.fe.settings.domain.offer.OfferLimit;
import ma.s2m.nxp.fe.settings.enums.Channel;
import ma.s2m.nxp.fe.settings.domain.program.Program;
import ma.s2m.nxp.fe.settings.exceptions.BusinessException;
import ma.s2m.nxp.fe.settings.repositories.OfferRepository;
import ma.s2m.nxp.fe.settings.repositories.ProgramRepository;
import ma.s2m.nxp.fe.settings.repositories.specifications.OfferSpecifications;
import ma.s2m.nxp.fe.settings.services.IOfferService;
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
public class OfferService implements IOfferService {

    private final OfferRepository offerRepository;
    private final ProgramRepository programRepository;

    public OfferService(OfferRepository offerRepository, ProgramRepository programRepository) {
        this.offerRepository = offerRepository;
        this.programRepository = programRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Offer> getAllOffers(Pageable pageable, String name, Long programId) {
        Specification<Offer> spec = (root, query, cb) -> cb.conjunction();

        if (name != null && !name.isBlank()) {
            spec = spec.and(OfferSpecifications.hasNameLike(name));
        }
        if (programId != null) {
            spec = spec.and(OfferSpecifications.hasProgramId(programId));
        }

        return offerRepository.findAll(spec, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Offer> getOfferById(Long id) {
        return offerRepository.findById(id);
    }

    @Override
    public Offer createOffer(Offer offer, Long programId) throws BusinessException {
        Program program = resolveProgram(programId);
        offer.setProgram(program);

        applyDefaultsIfNeeded(offer, program);

        if (offer.isDefault()) {
            unsetPreviousDefault(programId, null);
        }
        if (offerRepository.existsByName(offer.getName())){
            throw new BusinessException("INST_001", "Offer name already exists", HttpStatus.CONFLICT);

        }

        log.info("Creating offer name={} for programId={}", offer.getName(), programId);
        return offerRepository.save(offer);
    }

    @Override
    public Offer updateOffer(Long id, Offer newData, Long programId) throws BusinessException {
        Offer existing = offerRepository.findById(id)
                .orElseThrow(() -> new BusinessException("OFR_002", "Offer not found", HttpStatus.NOT_FOUND));

        Program program = resolveProgram(programId);

        existing.setName(newData.getName());
        existing.setProgram(program);
        existing.setNumberOfInstallments(newData.getNumberOfInstallments());
        existing.setStartDate(newData.getStartDate());
        existing.setStatus(newData.getStatus());
        existing.setDefault(newData.isDefault());
        existing.setFee(newData.getFee());
        existing.setHasCustomLimit(newData.isHasCustomLimit());
        existing.setLimit(newData.getLimit());

        applyDefaultsIfNeeded(existing, program);

        if (existing.isDefault()) {
            unsetPreviousDefault(programId, id);
        }

        log.info("Updating offer id={}", id);
        return offerRepository.save(existing);
    }

    @Override
    public void deleteOffer(Long id) throws BusinessException {
        if (!offerRepository.existsById(id)) {
            throw new BusinessException("OFR_002", "Offer not found", HttpStatus.NOT_FOUND);
        }
        offerRepository.deleteById(id);
        log.info("Deleted offer id={}", id);
    }

    /**
     * Si l'offre est marquée "par défaut", ses fee/limit sont recopiées depuis
     * le programme parent côté backend (source de vérité), plutôt que de faire
     * confiance aveuglément à ce que le frontend a envoyé.
     */
    private void applyDefaultsIfNeeded(Offer offer, Program program) {
        if (!offer.isDefault()) {
            return;
        }

        if (program.getFee() != null) {
            offer.setFee(OfferFee.builder()
                    .feeType(program.getFee().getFeeType())
                    .value(program.getFee().getAmount())
                    .build());
        }

        offer.setHasCustomLimit(true);
        if (program.getLimit() != null) {
            Channel firstChannel = (program.getAllowedChannels() != null && !program.getAllowedChannels().isEmpty())
                    ? program.getAllowedChannels().iterator().next()
                    : null;

            offer.setLimit(OfferLimit.builder()
                    .maxAmountPerTransaction(program.getLimit().getMaxAmountPerTransaction())
                    .maxTotalAmount(program.getLimit().getMaxTotalAmount())
                    .maxMonthlyInstallment(program.getLimit().getMaxMonthlyInstallment())
                    .mccCode(program.getLimit().getMccCode())
                    .allowedChannel(firstChannel)
                    .build());
        }
    }

    /**
     * Garantit qu'une seule offre par défaut existe par programme :
     * désactive toute autre offre "isDefault=true" du même programme.
     */
    private void unsetPreviousDefault(Long programId, Long excludeOfferId) {
        offerRepository.findAllByProgram_IdAndIsDefaultTrue(programId).forEach(previousDefault -> {
            if (excludeOfferId != null && previousDefault.getId().equals(excludeOfferId)) {
                return;
            }
            previousDefault.setDefault(false);
            offerRepository.save(previousDefault);
            log.info("Unset previous default offer id={} for programId={}", previousDefault.getId(), programId);
        });
    }

    private Program resolveProgram(Long programId) throws BusinessException {
        if (programId == null) {
            throw new BusinessException("OFR_003", "Program is required", HttpStatus.BAD_REQUEST);
        }
        return programRepository.findById(programId)
                .orElseThrow(() -> new BusinessException("OFR_004", "Program not found", HttpStatus.NOT_FOUND));
    }
}