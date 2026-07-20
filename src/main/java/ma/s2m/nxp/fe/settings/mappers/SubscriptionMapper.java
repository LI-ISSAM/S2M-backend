package ma.s2m.nxp.fe.settings.mappers;

import ma.s2m.nxp.fe.settings.DTO.subscription.EligibilityDTO;
import ma.s2m.nxp.fe.settings.DTO.subscription.SubscriptionDTO;
import ma.s2m.nxp.fe.settings.Enums.OnboardingMode;
import ma.s2m.nxp.fe.settings.Enums.SubscriptionStatus;
import ma.s2m.nxp.fe.settings.domain.subscription.Subscription;
import ma.s2m.nxp.fe.settings.domain.subscription.SubscriptionEligibility;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface SubscriptionMapper {

    @Mapping(target = "customerId", source = "customer.id")
    @Mapping(target = "programId", source = "program.id")
    @Mapping(target = "offerId", source = "offer.id")
    @Mapping(target = "mode", expression = "java(subscription.getMode() != null ? subscription.getMode().name() : null)")
    @Mapping(target = "status", expression = "java(subscription.getStatus() != null ? subscription.getStatus().name() : null)")
    SubscriptionDTO toDTO(Subscription subscription);

    EligibilityDTO toDTO(SubscriptionEligibility eligibility);

    // customer / program / offer sont résolus et assignés dans SubscriptionService,
    // à partir de customerId / programId / offerId (pattern identique à Program.institutionId)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "subscriptionId", ignore = true)
    @Mapping(target = "customerEmail", ignore = true)
    @Mapping(target = "customer", ignore = true)
    @Mapping(target = "program", ignore = true)
    @Mapping(target = "offer", ignore = true)
    @Mapping(target = "mode", expression = "java(dto.getMode() != null ? ma.s2m.nxp.fe.settings.Enums.OnboardingMode.valueOf(dto.getMode()) : null)")
    @Mapping(target = "status", expression = "java(dto.getStatus() != null ? ma.s2m.nxp.fe.settings.Enums.SubscriptionStatus.valueOf(dto.getStatus()) : null)")
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Subscription toEntity(SubscriptionDTO dto);

    SubscriptionEligibility toEntity(EligibilityDTO dto);
}