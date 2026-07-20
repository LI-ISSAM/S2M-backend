package ma.s2m.nxp.fe.settings.services;

import ma.s2m.nxp.fe.settings.domain.subscription.Subscription;
import ma.s2m.nxp.fe.settings.exceptions.BusinessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface ISubscriptionService {

    Page<Subscription> getAllSubscriptions(Pageable pageable, String customerEmail);

    Optional<Subscription> getSubscriptionById(Long id);

    Subscription createSubscription(Subscription subscription, Long customerId, Long programId, Long offerId)
            throws BusinessException;

    Subscription updateSubscription(Long id, Subscription newData, Long customerId, Long programId, Long offerId)
            throws BusinessException;

    void deleteSubscription(Long id) throws BusinessException;
}