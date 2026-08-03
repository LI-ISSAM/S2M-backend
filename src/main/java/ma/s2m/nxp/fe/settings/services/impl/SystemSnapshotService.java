package ma.s2m.nxp.fe.settings.services.impl;

import ma.s2m.nxp.fe.settings.enums.CustomerInstallmentStatus;
import ma.s2m.nxp.fe.settings.enums.InstallmentPlanStatus;
import ma.s2m.nxp.fe.settings.repositories.*;
import org.springframework.stereotype.Component;

/**
 * Construit un "instantané" du système (quelques compteurs clés) injecté
 * dans le prompt du chatbot, pour qu'il puisse répondre à des questions
 * générales ("combien de clients ?", "combien d'échéances en retard ?")
 * sans halluciner de chiffres.
 *
 * N'utilise QUE des méthodes standard Spring Data (count() / count(Specification))
 * pour rester robuste même si les repositories évoluent — aucune dépendance à
 * des méthodes personnalisées fragiles.
 */
@Component
public class SystemSnapshotService {

    private final CustomerRepository customerRepository;
    private final InstitutionRepository institutionRepository;
    private final OfferRepository offerRepository;
    private final ProgramRepository programRepository;
    private final MerchantRepository merchantRepository;
    private final InstallmentPlanRepository installmentPlanRepository;
    private final CustomerInstallmentRepository customerInstallmentRepository;
    private final OperationRepository operationRepository;

    public SystemSnapshotService(CustomerRepository customerRepository,
                                 InstitutionRepository institutionRepository,
                                 ProgramRepository programRepository,
                                 MerchantRepository merchantRepository,
                                 InstallmentPlanRepository installmentPlanRepository,
                                 CustomerInstallmentRepository customerInstallmentRepository,
                                 OperationRepository operationRepository,
                                 OfferRepository offerRepository
    ) {
        this.customerRepository = customerRepository;
        this.institutionRepository = institutionRepository;
        this.programRepository = programRepository;
        this.merchantRepository = merchantRepository;
        this.installmentPlanRepository = installmentPlanRepository;
        this.customerInstallmentRepository = customerInstallmentRepository;
        this.operationRepository = operationRepository;
        this.offerRepository = offerRepository;
    }

    public String buildSnapshot() {
        StringBuilder sb = new StringBuilder();
        sb.append("Instantané du système (chiffres à jour) :\n");

        safeAppend(sb, "Institutions", () -> institutionRepository.count());
        safeAppend(sb, "Programmes BNPL", () -> programRepository.count());
        safeAppend(sb, "Commerçants", () -> merchantRepository.count());
        safeAppend(sb, "Clients", () -> customerRepository.count());
        safeAppend(sb,"Offers", () -> offerRepository.count());
        safeAppend(sb, "Plans de mensualités", () -> installmentPlanRepository.count());
        safeAppend(sb, "Plans de mensualités ACTIFS", () ->
                installmentPlanRepository.count((root, query, cb) ->
                        cb.equal(root.get("status"), InstallmentPlanStatus.ACTIVE)));
        safeAppend(sb, "Échéances clients au total", () -> customerInstallmentRepository.count());
        safeAppend(sb, "Échéances clients EN RETARD", () ->
                customerInstallmentRepository.count((root, query, cb) ->
                        cb.equal(root.get("status"), CustomerInstallmentStatus.LATE)));
        safeAppend(sb, "Opérations (transactions) enregistrées", () -> operationRepository.count());

        return sb.toString();
    }

    private void safeAppend(StringBuilder sb, String label, java.util.function.Supplier<Long> supplier) {
        try {
            sb.append("- ").append(label).append(" : ").append(supplier.get()).append("\n");
        } catch (Exception e) {
            // Une métrique indisponible ne doit jamais faire planter tout le chatbot.
            sb.append("- ").append(label).append(" : indisponible\n");
        }
    }
}