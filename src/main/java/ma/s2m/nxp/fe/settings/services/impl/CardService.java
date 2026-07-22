package ma.s2m.nxp.fe.settings.services.impl;

import lombok.extern.slf4j.Slf4j;
import ma.s2m.nxp.fe.settings.domain.card.Card;
import ma.s2m.nxp.fe.settings.domain.customer.Customer;
import ma.s2m.nxp.fe.settings.domain.program.Program;
import ma.s2m.nxp.fe.settings.exceptions.BusinessException;
import ma.s2m.nxp.fe.settings.repositories.CardRepository;
import ma.s2m.nxp.fe.settings.repositories.CustomerRepository;
import ma.s2m.nxp.fe.settings.repositories.ProgramRepository;
import ma.s2m.nxp.fe.settings.repositories.specifications.CardSpecifications;
import ma.s2m.nxp.fe.settings.services.ICardService;
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
public class CardService implements ICardService {

    private final CardRepository cardRepository;
    private final CustomerRepository customerRepository;
    private final ProgramRepository programRepository;

    public CardService(CardRepository cardRepository, CustomerRepository customerRepository,
                       ProgramRepository programRepository) {
        this.cardRepository = cardRepository;
        this.customerRepository = customerRepository;
        this.programRepository = programRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Card> getAllCards(Pageable pageable, String cardNumber, String customerName) {
        Specification<Card> spec  = (root, query, cb) -> cb.conjunction();

        if (cardNumber != null && !cardNumber.isBlank()) {
            spec = spec.and(CardSpecifications.hasCardNumberLike(cardNumber));
        }
        if (customerName != null && !customerName.isBlank()) {
            spec = spec.and(CardSpecifications.hasCustomerNameLike(customerName));
        }

        return cardRepository.findAll(spec, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Card> getCardById(Long id) {
        return cardRepository.findById(id);
    }

    @Override
    public Card createCard(Card card, Long customerId, Long programId) throws BusinessException {
        Customer customer = resolveCustomer(customerId);
        Program program = resolveProgramIfPresent(programId);

        if (cardRepository.existsByCardNumber(card.getCardNumber())) {
            throw new BusinessException("CRD_001",
                    "A card with this number already exists", HttpStatus.CONFLICT);
        }

        card.setCustomer(customer);
        card.setProgram(program);

        log.info("Creating card cardNumber={} for customerId={}", card.getCardNumber(), customerId);
        return cardRepository.save(card);
    }

    @Override
    public Card updateCard(Long id, Card newData, Long customerId, Long programId) throws BusinessException {
        Card existing = cardRepository.findById(id)
                .orElseThrow(() -> new BusinessException("CRD_002", "Card not found", HttpStatus.NOT_FOUND));

        Customer customer = resolveCustomer(customerId);
        Program program = resolveProgramIfPresent(programId);

        if (cardRepository.existsByCardNumberAndIdNot(newData.getCardNumber(), id)) {
            throw new BusinessException("CRD_001",
                    "A card with this number already exists", HttpStatus.CONFLICT);
        }

        existing.setCardNumber(newData.getCardNumber());
        existing.setNameOnCard(newData.getNameOnCard());
        existing.setCustomer(customer);
        existing.setProgram(program);
        existing.setType(newData.getType());
        existing.setStatus(newData.getStatus());
        existing.setExpiryDate(newData.getExpiryDate());
        existing.setBranch(newData.getBranch());
        existing.setCustomerData(newData.getCustomerData());
        existing.setCardInfo(newData.getCardInfo());
        existing.setAdditionalData(newData.getAdditionalData());
        existing.setCommission(newData.getCommission());
        existing.setCardFees(newData.getCardFees());
        existing.setReplacementData(newData.getReplacementData());
        existing.setRenewData(newData.getRenewData());
        existing.setRecalculPin(newData.getRecalculPin());
        existing.setPersonalizationData(newData.getPersonalizationData());

        log.info("Updating card id={}", id);
        return cardRepository.save(existing);
    }

    @Override
    public void deleteCard(Long id) throws BusinessException {
        if (!cardRepository.existsById(id)) {
            throw new BusinessException("CRD_002", "Card not found", HttpStatus.NOT_FOUND);
        }
        cardRepository.deleteById(id);
        log.info("Deleted card id={}", id);
    }

    private Customer resolveCustomer(Long customerId) throws BusinessException {
        if (customerId == null) {
            throw new BusinessException("CRD_003", "Customer is required", HttpStatus.BAD_REQUEST);
        }
        return customerRepository.findById(customerId)
                .orElseThrow(() -> new BusinessException("CRD_004", "Customer not found", HttpStatus.NOT_FOUND));
    }

    private Program resolveProgramIfPresent(Long programId) throws BusinessException {
        if (programId == null) {
            return null;
        }
        return programRepository.findById(programId)
                .orElseThrow(() -> new BusinessException("CRD_005", "Program not found", HttpStatus.NOT_FOUND));
    }
}