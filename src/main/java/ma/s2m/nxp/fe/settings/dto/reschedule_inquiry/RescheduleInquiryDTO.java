package ma.s2m.nxp.fe.settings.dto.reschedule_inquiry;

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
public class RescheduleInquiryDTO {

    private Long id;

    @NotNull(message = "Card is required")
    private Long cardId;

    /** Renvoyé en lecture seule par le backend (dérivé de Card.cardNumber). */
    private String cardNumber;

    @NotBlank(message = "RNN is required")
    private String rnn;

    @NotBlank(message = "Transaction Detail is required")
    private String transactionDetail;

    @NotNull(message = "Reschedule Fee is required")
    @Min(value = 0, message = "Reschedule Fee must be positive")
    private BigDecimal rescheduleFee;

    @NotNull(message = "Outstanding Amount is required")
    @Min(value = 0, message = "Outstanding Amount must be positive")
    private BigDecimal outstandingAmount;
}
