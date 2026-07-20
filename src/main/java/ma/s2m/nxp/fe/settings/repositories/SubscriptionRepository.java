package ma.s2m.nxp.fe.settings.repositories;

import ma.s2m.nxp.fe.settings.domain.subscription.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription, Long>,
        JpaSpecificationExecutor<Subscription> {

    boolean existsBySubscriptionId(String subscriptionId);
    boolean existsByCustomerId(Long customerId);

}