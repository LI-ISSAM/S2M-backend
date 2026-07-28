package ma.s2m.nxp.fe.settings.repositories.specifications;

import ma.s2m.nxp.fe.settings.domain.forceclosureinquiry.ForceClosureInquiry;
import org.springframework.data.jpa.domain.Specification;

public class ForceClosureInquirySpecifications {

    private static final String RNN = "rnn";
    private static final String CARD = "card";
    private static final String CARD_NUMBER = "cardNumber";

    private ForceClosureInquirySpecifications() {
    }

    public static Specification<ForceClosureInquiry> hasRnnLike(String rnn) {
        return (root, query, cb) ->
                cb.like(cb.lower(root.get(RNN)), "%" + rnn.toLowerCase() + "%");
    }

    public static Specification<ForceClosureInquiry> hasCardNumberLike(String cardNumber) {
        return (root, query, cb) -> {
            query.distinct(true);
            return cb.like(cb.lower(root.join(CARD).get(CARD_NUMBER)), "%" + cardNumber.toLowerCase() + "%");
        };
    }
}