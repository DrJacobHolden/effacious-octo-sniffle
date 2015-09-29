package nz.co.actiontracker.campaign;

import nz.co.actiontracker.activist.ActivistMapper;

/**
 * A simple class to map between a Campaign and its DTO
 * and vice-versa.
 */
public class CampaignMapper {

	public static Campaign toDomainModel(CampaignDTO dtoCampaign) {
		Campaign fullCampaign = new Campaign(dtoCampaign.get_id(),
				dtoCampaign.get_name(),
				ActivistMapper.toDomainModel(dtoCampaign.get_creator()));
		return fullCampaign;
	}
	
	public static CampaignDTO toDTO(Campaign campaign) {
		return new CampaignDTO(campaign.getId(), 
				campaign.getName(),
				ActivistMapper.toDTO(campaign.getCreator()));
	}
	
}
