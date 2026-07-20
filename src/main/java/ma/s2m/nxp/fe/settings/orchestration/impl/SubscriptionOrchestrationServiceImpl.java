package ma.s2m.nxp.fe.settings.orchestration.impl;

import ma.s2m.nxp.fe.settings.DTO.subscription.SubscriptionDTO;
import ma.s2m.nxp.fe.settings.domain.subscription.Subscription;
import ma.s2m.nxp.fe.settings.exceptions.BusinessException;
import ma.s2m.nxp.fe.settings.mappers.SubscriptionMapper;
import ma.s2m.nxp.fe.settings.orchestration.ISubscriptionOrchestrationService;
import ma.s2m.nxp.fe.settings.services.ISubscriptionService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SubscriptionOrchestrationServiceImpl implements ISubscriptionOrchestrationService {

    private final ISubscriptionService subscriptionService;
    private final SubscriptionMapper subscriptionMapper;

    public SubscriptionOrchestrationServiceImpl(ISubscriptionService subscriptionService, SubscriptionMapper subscriptionMapper) {
        this.subscriptionService = subscriptionService;
        this.subscriptionMapper = subscriptionMapper;
    }

    @Override
    public SubscriptionsPageResponse getAllSubscriptions(int page, int limit, String customerEmail) {
        int zeroBasedPage = Math.max(page - 1, 0);
        PageRequest pageRequest = PageRequest.of(zeroBasedPage, limit, Sort.by(Sort.Direction.DESC, "subscriptionDate"));

        Page<Subscription> result = subscriptionService.getAllSubscriptions(pageRequest, customerEmail);

        List<SubscriptionDTO> content = result.getContent().stream()
                .map(subscriptionMapper::toDTO)
                .toList();

        return new SubscriptionsPageResponse(content, result.getTotalElements(), result.getTotalPages(), page);
    }

    @Override
    public SubscriptionDTO getSubscriptionById(Long id) throws BusinessException {
        Subscription subscription = subscriptionService.getSubscriptionById(id)
                .orElseThrow(() -> new BusinessException("SUB_002", "Subscription not found", HttpStatus.NOT_FOUND));
        return subscriptionMapper.toDTO(subscription);
    }

    @Override
    public SubscriptionDTO createSubscription(SubscriptionDTO dto) throws BusinessException {
        Subscription subscription = subscriptionMapper.toEntity(dto);
        Subscription saved = subscriptionService.createSubscription(
                subscription, dto.getCustomerId(), dto.getProgramId(), dto.getOfferId());
        return subscriptionMapper.toDTO(saved);
    }

    @Override
    public SubscriptionDTO updateSubscription(Long id, SubscriptionDTO dto) throws BusinessException {
        Subscription newData = subscriptionMapper.toEntity(dto);
        Subscription updated = subscriptionService.updateSubscription(
                id, newData, dto.getCustomerId(), dto.getProgramId(), dto.getOfferId());
        return subscriptionMapper.toDTO(updated);
    }

    @Override
    public void deleteSubscription(Long id) throws BusinessException {
        subscriptionService.deleteSubscription(id);
    }
}