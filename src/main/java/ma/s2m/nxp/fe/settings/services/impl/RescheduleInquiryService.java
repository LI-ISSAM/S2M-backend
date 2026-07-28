package ma.s2m.nxp.fe.settings.services.impl;

import lombok.extern.slf4j.Slf4j;
import ma.s2m.nxp.fe.settings.domain.card.Card;
import ma.s2m.nxp.fe.settings.domain.rescheduleinquiry.RescheduleInquiry;
import ma.s2m.nxp.fe.settings.exceptions.BusinessException;
import ma.s2m.nxp.fe.settings.repositories.CardRepository;
import ma.s2m.nxp.fe.settings.repositories.RescheduleInquiryRepository;
import ma.s2m.nxp.fe.settings.repositories.specifications.RescheduleInquirySpecifications;
import ma.s2m.nxp.fe.settings.services.IRescheduleInquiryService;
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
public class RescheduleInquiryService implements IRescheduleInquiryService {

    private final RescheduleInquiryRepository rescheduleInquiryRepository;
    private final CardRepository cardRepository;

    public RescheduleInquiryService(RescheduleInquiryRepository rescheduleInquiryRepository,
                                    CardRepository cardRepository) {
        this.rescheduleInquiryRepository = rescheduleInquiryRepository;
        this.cardRepository = cardRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<RescheduleInquiry> getAllRescheduleInquiries(Pageable pageable, String cardNumber, String rnn) {
        Specification<RescheduleInquiry> spec =  (root, query, cb) -> cb.conjunction();

        if (cardNumber != null && !cardNumber.isBlank()) {
            spec = spec.and(RescheduleInquirySpecifications.hasCardNumberLike(cardNumber));
        }
        if (rnn != null && !rnn.isBlank()) {
            spec = spec.and(RescheduleInquirySpecifications.hasRnnLike(rnn));
        }

        return rescheduleInquiryRepository.findAll(spec, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<RescheduleInquiry> getRescheduleInquiryById(Long id) {
        return rescheduleInquiryRepository.findById(id);
    }

    @Override
    public RescheduleInquiry createRescheduleInquiry(RescheduleInquiry rescheduleInquiry, Long cardId)
            throws BusinessException {
        Card card = resolveCard(cardId);

        if (rescheduleInquiryRepository.existsByCard_Id(cardId)) {
            throw new BusinessException("RSI_001",
                    "This card already has an active reschedule inquiry", HttpStatus.CONFLICT);
        }

        rescheduleInquiry.setCard(card);
        log.info("Creating reschedule inquiry rnn={} for cardId={}", rescheduleInquiry.getRnn(), cardId);
        return rescheduleInquiryRepository.save(rescheduleInquiry);
    }

    @Override
    public RescheduleInquiry updateRescheduleInquiry(Long id, RescheduleInquiry newData, Long cardId)
            throws BusinessException {
        RescheduleInquiry existing = rescheduleInquiryRepository.findById(id)
                .orElseThrow(() -> new BusinessException("RSI_002", "Reschedule Inquiry not found", HttpStatus.NOT_FOUND));

        Card card = resolveCard(cardId);

        if (rescheduleInquiryRepository.existsByCard_IdAndIdNot(cardId, id)) {
            throw new BusinessException("RSI_001",
                    "This card already has an active reschedule inquiry", HttpStatus.CONFLICT);
        }

        existing.setCard(card);
        existing.setRnn(newData.getRnn());
        existing.setTransactionDetail(newData.getTransactionDetail());
        existing.setRescheduleFee(newData.getRescheduleFee());
        existing.setOutstandingAmount(newData.getOutstandingAmount());

        log.info("Updating reschedule inquiry id={}", id);
        return rescheduleInquiryRepository.save(existing);
    }

    @Override
    public void deleteRescheduleInquiry(Long id) throws BusinessException {
        if (!rescheduleInquiryRepository.existsById(id)) {
            throw new BusinessException("RSI_002", "Reschedule Inquiry not found", HttpStatus.NOT_FOUND);
        }
        rescheduleInquiryRepository.deleteById(id);
        log.info("Deleted reschedule inquiry id={}", id);
    }

    private Card resolveCard(Long cardId) throws BusinessException {
        if (cardId == null) {
            throw new BusinessException("RSI_003", "Card is required", HttpStatus.BAD_REQUEST);
        }
        return cardRepository.findById(cardId)
                .orElseThrow(() -> new BusinessException("RSI_004", "Card not found", HttpStatus.NOT_FOUND));
    }
}