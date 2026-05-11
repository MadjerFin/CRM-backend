package com.wtc.repository;

import com.wtc.entity.CampaignRecipient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface CampaignRecipientRepository extends JpaRepository<CampaignRecipient, Long> {
    List<CampaignRecipient> findByCampaignId(Long campaignId);
    List<CampaignRecipient> findByClientId(Long clientId);
    Optional<CampaignRecipient> findByCampaignIdAndClientId(Long campaignId, Long clientId);
}
