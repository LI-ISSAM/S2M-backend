package ma.s2m.nxp.fe.settings.repositories.specifications;

import ma.s2m.nxp.fe.settings.domain.freezinginquiry.FreezingInquiry;
import org.springframework.data.jpa.domain.Specification;

public class FreezingInquirySpecifications {

    private static final String RNN = "rnn";
    private static final String CARD = "card";
    private static final String CARD_NUMBER = "cardNumber";

    private FreezingInquirySpecifications() {
    }

    public static Specification<FreezingInquiry> hasRnnLike(String rnn) {
        return (root, query, cb) ->
                cb.like(cb.lower(root.get(RNN)), "%" + rnn.toLowerCase() + "%");
    }

    /**
     * Jointure vers Card.cardNumber, requis par le frontend (cardNumber_like).
     */
    public static Specification<FreezingInquiry> hasCardNumberLike(String cardNumber) {
        return (root, query, cb) -> {
            query.distinct(true);
            return cb.like(cb.lower(root.join(CARD).get(CARD_NUMBER)), "%" + cardNumber.toLowerCase() + "%");
        };
    }
}