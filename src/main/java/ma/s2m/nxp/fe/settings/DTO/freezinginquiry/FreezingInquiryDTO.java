package ma.s2m.nxp.fe.settings.DTO.freezinginquiry;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class FreezingInquiryDTO {

    private Long id;

    @NotNull(message = "Card is required")
    private Long cardId;

    /** Renvoyé en lecture seule par le backend (dérivé de Card.cardNumber). */
    private String cardNumber;

    @NotBlank(message = "RNN is required")
    private String rnn;

    @NotBlank(message = "Transaction Detail is required")
    private String transactionDetail;

    @NotNull(message = "Outstanding Amount is required")
    @Min(value = 0, message = "Outstanding Amount must be positive")
    private BigDecimal outstandingAmount;

    @NotNull(message = "Freezing Fee is required")
    @Min(value = 0, message = "Freezing Fee must be positive")
    private BigDecimal freezingFee;

    @NotNull(message = "Freezing Period is required")
    @Min(value = 1, message = "Freezing Period must be at least 1 day")
    private Integer freezingPeriod;
}