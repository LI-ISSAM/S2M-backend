package ma.s2m.nxp.fe.settings.orchestration;

import ma.s2m.nxp.fe.settings.DTO.subscription.SubscriptionDTO;
import ma.s2m.nxp.fe.settings.exceptions.BusinessException;
import ma.s2m.nxp.fe.settings.orchestration.impl.SubscriptionsPageResponse;

public interface ISubscriptionOrchestrationService {

    SubscriptionsPageResponse getAllSubscriptions(int page, int limit, String customerEmail);

    SubscriptionDTO getSubscriptionById(Long id) throws BusinessException;

    SubscriptionDTO createSubscription(SubscriptionDTO dto) throws BusinessException;

    SubscriptionDTO updateSubscription(Long id, SubscriptionDTO dto) throws BusinessException;

    void deleteSubscription(Long id) throws BusinessException;
}