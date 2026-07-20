package ma.s2m.nxp.fe.settings.repositories.specifications;

import ma.s2m.nxp.fe.settings.domain.card.Card;
import ma.s2m.nxp.fe.settings.Enums.CardStatus;
import ma.s2m.nxp.fe.settings.Enums.CardType;
import org.springframework.data.jpa.domain.Specification;

public class CardSpecifications {

    private static final String CARD_NUMBER = "cardNumber";
    private static final String CUSTOMER = "customer";
    private static final String FULL_NAME = "fullName";
    private static final String TYPE = "type";
    private static final String STATUS = "status";

    private CardSpecifications() {
    }

    public static Specification<Card> hasCardNumberLike(String cardNumber) {
        return (root, query, cb) ->
                cb.like(cb.lower(root.get(CARD_NUMBER)), "%" + cardNumber.toLowerCase() + "%");
    }

    /**
     * Jointure vers Customer.fullName, requis par le frontend (customerName_like).
     */
    public static Specification<Card> hasCustomerNameLike(String customerName) {
        return (root, query, cb) -> {
            query.distinct(true);
            return cb.like(cb.lower(root.join(CUSTOMER).get(FULL_NAME)), "%" + customerName.toLowerCase() + "%");
        };
    }

    public static Specification<Card> hasCustomerId(Long customerId) {
        return (root, query, cb) -> cb.equal(root.get(CUSTOMER).get("id"), customerId);
    }

    public static Specification<Card> hasType(CardType type) {
        return (root, query, cb) -> cb.equal(root.get(TYPE), type);
    }

    public static Specification<Card> hasStatus(CardStatus status) {
        return (root, query, cb) -> cb.equal(root.get(STATUS), status);
    }
}