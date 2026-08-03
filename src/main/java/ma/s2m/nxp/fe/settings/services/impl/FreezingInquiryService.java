package ma.s2m.nxp.fe.settings.services.impl;

import lombok.extern.slf4j.Slf4j;
import ma.s2m.nxp.fe.settings.domain.card.Card;
import ma.s2m.nxp.fe.settings.domain.freezing_inquiry.FreezingInquiry;
import ma.s2m.nxp.fe.settings.exceptions.BusinessException;
import ma.s2m.nxp.fe.settings.repositories.CardRepository;
import ma.s2m.nxp.fe.settings.repositories.FreezingInquiryRepository;
import ma.s2m.nxp.fe.settings.repositories.specifications.FreezingInquirySpecifications;
import ma.s2m.nxp.fe.settings.services.IFreezingInquiryService;
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
public class FreezingInquiryService implements IFreezingInquiryService {

    private final FreezingInquiryRepository freezingInquiryRepository;
    private final CardRepository cardRepository;

    public FreezingInquiryService(FreezingInquiryRepository freezingInquiryRepository, CardRepository cardRepository) {
        this.freezingInquiryRepository = freezingInquiryRepository;
        this.cardRepository = cardRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<FreezingInquiry> getAllFreezingInquiries(Pageable pageable, String cardNumber, String rnn) {
        Specification<FreezingInquiry> spec =  (root, query, cb) -> cb.conjunction();


        if (cardNumber != null && !cardNumber.isBlank()) {
            spec = spec.and(FreezingInquirySpecifications.hasCardNumberLike(cardNumber));
        }
        if (rnn != null && !rnn.isBlank()) {
            spec = spec.and(FreezingInquirySpecifications.hasRnnLike(rnn));
        }

        return freezingInquiryRepository.findAll(spec, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<FreezingInquiry> getFreezingInquiryById(Long id) {
        return freezingInquiryRepository.findById(id);
    }

    @Override
    public FreezingInquiry createFreezingInquiry(FreezingInquiry freezingInquiry, Long cardId) throws BusinessException {
        Card card = resolveCard(cardId);

        if (freezingInquiryRepository.existsByCard_Id(cardId)) {
            throw new BusinessException("FZI_001",
                    "This card already has an active freezing inquiry", HttpStatus.CONFLICT);
        }

        freezingInquiry.setCard(card);
        log.info("Creating freezing inquiry rnn={} for cardId={}", freezingInquiry.getRnn(), cardId);
        return freezingInquiryRepository.save(freezingInquiry);
    }

    @Override
    public FreezingInquiry updateFreezingInquiry(Long id, FreezingInquiry newData, Long cardId) throws BusinessException {
        FreezingInquiry existing = freezingInquiryRepository.findById(id)
                .orElseThrow(() -> new BusinessException("FZI_002", "Freezing Inquiry not found", HttpStatus.NOT_FOUND));

        Card card = resolveCard(cardId);

        if (freezingInquiryRepository.existsByCard_IdAndIdNot(cardId, id)) {
            throw new BusinessException("FZI_001",
                    "This card already has an active freezing inquiry", HttpStatus.CONFLICT);
        }

        existing.setCard(card);
        existing.setRnn(newData.getRnn());
        existing.setTransactionDetail(newData.getTransactionDetail());
        existing.setOutstandingAmount(newData.getOutstandingAmount());
        existing.setFreezingFee(newData.getFreezingFee());
        existing.setFreezingPeriod(newData.getFreezingPeriod());

        log.info("Updating freezing inquiry id={}", id);
        return freezingInquiryRepository.save(existing);
    }

    @Override
    public void deleteFreezingInquiry(Long id) throws BusinessException {
        if (!freezingInquiryRepository.existsById(id)) {
            throw new BusinessException("FZI_002", "Freezing Inquiry not found", HttpStatus.NOT_FOUND);
        }
        freezingInquiryRepository.deleteById(id);
        log.info("Deleted freezing inquiry id={}", id);
    }

    private Card resolveCard(Long cardId) throws BusinessException {
        if (cardId == null) {
            throw new BusinessException("FZI_003", "Card is required", HttpStatus.BAD_REQUEST);
        }
        return cardRepository.findById(cardId)
                .orElseThrow(() -> new BusinessException("FZI_004", "Card not found", HttpStatus.NOT_FOUND));
    }
}