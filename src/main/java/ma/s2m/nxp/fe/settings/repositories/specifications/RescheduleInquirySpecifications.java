package ma.s2m.nxp.fe.settings.repositories.specifications;

import ma.s2m.nxp.fe.settings.domain.rescheduleinquiry.RescheduleInquiry;
import org.springframework.data.jpa.domain.Specification;

public class RescheduleInquirySpecifications {

    private static final String RNN = "rnn";
    private static final String CARD = "card";
    private static final String CARD_NUMBER = "cardNumber";

    private RescheduleInquirySpecifications() {
    }

    public static Specification<RescheduleInquiry> hasRnnLike(String rnn) {
        return (root, query, cb) ->
                cb.like(cb.lower(root.get(RNN)), "%" + rnn.toLowerCase() + "%");
    }

    public static Specification<RescheduleInquiry> hasCardNumberLike(String cardNumber) {
        return (root, query, cb) -> {
            query.distinct(true);
            return cb.like(cb.lower(root.join(CARD).get(CARD_NUMBER)), "%" + cardNumber.toLowerCase() + "%");
        };
    }
}