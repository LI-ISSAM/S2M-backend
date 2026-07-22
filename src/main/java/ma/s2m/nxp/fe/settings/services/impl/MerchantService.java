package ma.s2m.nxp.fe.settings.services.impl;

import lombok.extern.slf4j.Slf4j;
import ma.s2m.nxp.fe.settings.domain.member.Institution;
import ma.s2m.nxp.fe.settings.domain.merchant.Merchant;
import ma.s2m.nxp.fe.settings.exceptions.BusinessException;
import ma.s2m.nxp.fe.settings.repositories.InstitutionRepository;
import ma.s2m.nxp.fe.settings.repositories.MerchantRepository;
import ma.s2m.nxp.fe.settings.repositories.specifications.MerchantSpecifications;
import ma.s2m.nxp.fe.settings.services.IMerchantService;
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
public class MerchantService implements IMerchantService {

    private final MerchantRepository merchantRepository;
    private final InstitutionRepository institutionRepository;

    public MerchantService(MerchantRepository merchantRepository, InstitutionRepository institutionRepository) {
        this.merchantRepository = merchantRepository;
        this.institutionRepository = institutionRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Merchant> getAllMerchants(Pageable pageable, String name, String reference, Long institutionId) {
        Specification<Merchant> spec = (root, query, cb) -> cb.conjunction();

        if (name != null && !name.isBlank()) {
            spec = spec.and(MerchantSpecifications.hasNameLike(name));
        }
        if (reference != null && !reference.isBlank()) {
            spec = spec.and(MerchantSpecifications.hasReferenceLike(reference));
        }
        if (institutionId != null) {
            spec = spec.and(MerchantSpecifications.hasInstitutionId(institutionId));
        }

        return merchantRepository.findAll(spec, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Merchant> getMerchantById(Long id) {
        return merchantRepository.findById(id);
    }

    @Override
    public Merchant createMerchant(Merchant merchant, Long institutionId) throws BusinessException {
        Institution institution = resolveInstitution(institutionId);

        if (merchantRepository.existsByReferenceIgnoreCase(merchant.getReference())) {
            throw new BusinessException("MER_001",
                    "A merchant with this reference already exists", HttpStatus.CONFLICT);
        }

        merchant.setInstitution(institution);
        log.info("Creating merchant name={} reference={} for institutionId={}",
                merchant.getName(), merchant.getReference(), institutionId);
        return merchantRepository.save(merchant);
    }

    @Override
    public Merchant updateMerchant(Long id, Merchant newData, Long institutionId) throws BusinessException {
        Merchant existing = merchantRepository.findById(id)
                .orElseThrow(() -> new BusinessException("MER_002", "Merchant not found", HttpStatus.NOT_FOUND));

        Institution institution = resolveInstitution(institutionId);

        if (merchantRepository.existsByReferenceIgnoreCaseAndIdNot(newData.getReference(), id)) {
            throw new BusinessException("MER_001",
                    "A merchant with this reference already exists", HttpStatus.CONFLICT);
        }

        existing.setName(newData.getName());
        existing.setReference(newData.getReference());
        existing.setMccCode(newData.getMccCode());
        existing.setInstitution(institution);
        existing.setType(newData.getType());
        existing.setStatus(newData.getStatus());

        // Merchant Data
        existing.setMerchantId(newData.getMerchantId());
        existing.setCorporateName(newData.getCorporateName());
        existing.setDbaName(newData.getDbaName());
        existing.setCity(newData.getCity());
        existing.setBranch(newData.getBranch());
        existing.setBank(newData.getBank());
        existing.setIdentityFile(newData.getIdentityFile());

        // Merchant Information
        existing.setCategory(newData.getCategory());
        existing.setParentGroup(newData.getParentGroup());
        existing.setSolvability(newData.getSolvability());
        existing.setBusinessType(newData.getBusinessType());
        existing.setContractNumber(newData.getContractNumber());
        existing.setSignatureDate(newData.getSignatureDate());
        existing.setBusinessCreationDate(newData.getBusinessCreationDate());
        existing.setLatePaymentDate(newData.getLatePaymentDate());
        existing.setStatusDate(newData.getStatusDate());
        existing.setOppositionStatus(newData.getOppositionStatus());

        // Merchant Identity
        existing.setLicence(newData.getLicence());
        existing.setSiretNumber(newData.getSiretNumber());
        existing.setFiscalIdentityNumber(newData.getFiscalIdentityNumber());
        existing.setCommercialRegisterNumber(newData.getCommercialRegisterNumber());
        existing.setSocialSecurityNumber(newData.getSocialSecurityNumber());
        existing.setCapital(newData.getCapital());

        // Merchant Parameters
        existing.setMccGroup(newData.getMccGroup());
        existing.setMerchantGroup(newData.getMerchantGroup());
        existing.setMerchantProgram(newData.getMerchantProgram());
        existing.setRiskManagementGroup(newData.getRiskManagementGroup());
        existing.setPaymentMode(newData.getPaymentMode());
        existing.setPeriodicity(newData.getPeriodicity());
        existing.setCheckbookName(newData.getCheckbookName());
        existing.setAllAccount(newData.getAllAccount());
        existing.setDsDecision(newData.getDsDecision());
        existing.setDsChallenge(newData.getDsChallenge());

        // Merchant Currency
        existing.setDefaultCurrency(newData.getDefaultCurrency());

        // Account Routing (valeurs par défaut)
        existing.setDefaultMxpAccount(newData.getDefaultMxpAccount());
        existing.setDefaultBankAccount(newData.getDefaultBankAccount());

        // Merchant Statement
        existing.setFrequency(newData.getFrequency());
        existing.setPeriod(newData.getPeriod());
        existing.setSupport(newData.getSupport());
        existing.setLastStatementDate(newData.getLastStatementDate());

        // Tableaux : remplacement intégral du contenu
        existing.getOwners().clear();
        existing.getOwners().addAll(newData.getOwners());
        existing.getCurrencySupported().clear();
        existing.getCurrencySupported().addAll(newData.getCurrencySupported());
        existing.getAccounts().clear();
        existing.getAccounts().addAll(newData.getAccounts());
        existing.getAccountRoutings().clear();
        existing.getAccountRoutings().addAll(newData.getAccountRoutings());
        existing.getMembershipFees().clear();
        existing.getMembershipFees().addAll(newData.getMembershipFees());
        existing.getCommissions().clear();
        existing.getCommissions().addAll(newData.getCommissions());
        existing.getAddresses().clear();
        existing.getAddresses().addAll(newData.getAddresses());

        log.info("Updating merchant id={}", id);
        return merchantRepository.save(existing);
    }

    @Override
    public void deleteMerchant(Long id) throws BusinessException {
        if (!merchantRepository.existsById(id)) {
            throw new BusinessException("MER_002", "Merchant not found", HttpStatus.NOT_FOUND);
        }
        merchantRepository.deleteById(id);
        log.info("Deleted merchant id={}", id);
    }

    private Institution resolveInstitution(Long institutionId) throws BusinessException {
        if (institutionId == null) {
            throw new BusinessException("MER_003", "Institution is required", HttpStatus.BAD_REQUEST);
        }
        return institutionRepository.findById(institutionId)
                .orElseThrow(() -> new BusinessException("MER_004", "Institution not found", HttpStatus.NOT_FOUND));
    }
}