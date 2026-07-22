package ma.s2m.nxp.fe.settings.domain.customer;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.time.LocalDate;

/**
 * Ligne du tableau "Card" du formulaire client (fiche KYC), distincte du
 * module ma.s2m.nxp.fe.settings.domain.card.Card (carte BNPL rattachée
 * formellement à un Program via customerId/programId). Ici il s'agit d'une
 * simple liste déclarative de cartes détenues, saisie directement dans la
 * fiche client.
 */
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class CustomerCardInfo {

    @Column(name = "CIC_CARD_NUMBER", length = 19)
    private String cardNumber;

    @Column(name = "CIC_CARD_NAME", length = 80)
    private String cardName;

    @Column(name = "CIC_EXPIRATION_DATE", length = 5)
    private String expirationDate;

    @Column(name = "CIC_AUTO_RENEWAL", length = 10)
    private String autoRenewal;

    @Column(name = "CIC_LAST_TRANSACTION_DATE")
    private LocalDate lastTransactionDate;

    @Column(name = "CIC_TYPE", length = 30)
    private String type;

    @Column(name = "CIC_PRODUCT", length = 30)
    private String product;

    @Column(name = "CIC_STATUS", length = 20)
    private String status;
}