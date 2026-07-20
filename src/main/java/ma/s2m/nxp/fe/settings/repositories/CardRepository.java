package ma.s2m.nxp.fe.settings.repositories;

import ma.s2m.nxp.fe.settings.domain.card.Card;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface CardRepository extends JpaRepository<Card, Long>,
        JpaSpecificationExecutor<Card> {

    boolean existsByCardNumber(String cardNumber);

    boolean existsByCardNumberAndIdNot(String cardNumber, Long id);

    boolean existsByCustomer_Id(Long customerId);

    boolean existsByProgram_Id(Long programId);
}