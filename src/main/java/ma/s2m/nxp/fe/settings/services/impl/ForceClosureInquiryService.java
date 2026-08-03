package ma.s2m.nxp.fe.settings.services.impl;

import lombok.extern.slf4j.Slf4j;
import ma.s2m.nxp.fe.settings.domain.card.Card;
import ma.s2m.nxp.fe.settings.domain.force_closure_inquiry.ForceClosureInquiry;
import ma.s2m.nxp.fe.settings.exceptions.BusinessException;
import ma.s2m.nxp.fe.settings.repositories.CardRepository;
import ma.s2m.nxp.fe.settings.repositories.ForceClosureInquiryRepository;
import ma.s2m.nxp.fe.settings.repositories.specifications.ForceClosureInquirySpecifications;
import ma.s2m.nxp.fe.settings.services.IForceClosureInquiryService;
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
public class ForceClosureInquiryService implements IForceClosureInquiryService {

    private final ForceClosureInquiryRepository forceClosureInquiryRepository;
    private final CardRepository cardRepository;

    public ForceClosureInquiryService(ForceClosureInquiryRepository forceClosureInquiryRepository,
                                      CardRepository cardRepository) {
        this.forceClosureInquiryRepository = forceClosureInquiryRepository;
        this.cardRepository = cardRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ForceClosureInquiry> getAllForceClosureInquiries(Pageable pageable, String cardNumber, String rnn) {
        Specification<ForceClosureInquiry> spec = (root, query, cb) -> cb.conjunction();

        if (cardNumber != null && !cardNumber.isBlank()) {
            spec = spec.and(ForceClosureInquirySpecifications.hasCardNumberLike(cardNumber));
        }
        if (rnn != null && !rnn.isBlank()) {
            spec = spec.and(ForceClosureInquirySpecifications.hasRnnLike(rnn));
        }

        return forceClosureInquiryRepository.findAll(spec, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ForceClosureInquiry> getForceClosureInquiryById(Long id) {
        return forceClosureInquiryRepository.findById(id);
    }

    @Override
    public ForceClosureInquiry createForceClosureInquiry(ForceClosureInquiry forceClosureInquiry, Long cardId)
            throws BusinessException {
        Card card = resolveCard(cardId);

        if (forceClosureInquiryRepository.existsByCard_Id(cardId)) {
            throw new BusinessException("FCI_001",
                    "This card already has an active force closure inquiry", HttpStatus.CONFLICT);
        }

        forceClosureInquiry.setCard(card);
        log.info("Creating force closure inquiry rnn={} for cardId={}", forceClosureInquiry.getRnn(), cardId);
        return forceClosureInquiryRepository.save(forceClosureInquiry);
    }

    @Override
    public ForceClosureInquiry updateForceClosureInquiry(Long id, ForceClosureInquiry newData, Long cardId)
            throws BusinessException {
        ForceClosureInquiry existing = forceClosureInquiryRepository.findById(id)
                .orElseThrow(() -> new BusinessException("FCI_002", "Force Closure Inquiry not found", HttpStatus.NOT_FOUND));

        Card card = resolveCard(cardId);

        if (forceClosureInquiryRepository.existsByCard_IdAndIdNot(cardId, id)) {
            throw new BusinessException("FCI_001",
                    "This card already has an active force closure inquiry", HttpStatus.CONFLICT);
        }

        existing.setCard(card);
        existing.setRnn(newData.getRnn());
        existing.setOutstandingAmount(newData.getOutstandingAmount());
        existing.setForceClosureFee(newData.getForceClosureFee());

        log.info("Updating force closure inquiry id={}", id);
        return forceClosureInquiryRepository.save(existing);
    }

    @Override
    public void deleteForceClosureInquiry(Long id) throws BusinessException {
        if (!forceClosureInquiryRepository.existsById(id)) {
            throw new BusinessException("FCI_002", "Force Closure Inquiry not found", HttpStatus.NOT_FOUND);
        }
        forceClosureInquiryRepository.deleteById(id);
        log.info("Deleted force closure inquiry id={}", id);
    }

    private Card resolveCard(Long cardId) throws BusinessException {
        if (cardId == null) {
            throw new BusinessException("FCI_003", "Card is required", HttpStatus.BAD_REQUEST);
        }
        return cardRepository.findById(cardId)
                .orElseThrow(() -> new BusinessException("FCI_004", "Card not found", HttpStatus.NOT_FOUND));
    }
}