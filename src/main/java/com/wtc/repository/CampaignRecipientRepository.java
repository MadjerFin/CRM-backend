package com.wtc.repository;

import com.wtc.entity.CampaignRecipient;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface CampaignRecipientRepository extends MongoRepository<CampaignRecipient, String> {
    List<CampaignRecipient> findByCampaignId(String campaignId);
    List<CampaignRecipient> findByClientId(String clientId);
    Optional<CampaignRecipient> findByCampaignIdAndClientId(String campaignId, String clientId);
}
