package nz.co.actiontracker.event;

import java.util.HashSet;
import java.util.Set;

import nz.co.actiontracker.activist.Activist;
import nz.co.actiontracker.campaign.Campaign;

public class EventMapper {
	
	public static Event toDomainModel(EventDTO dtoEvent, Activist creator, Campaign campaign) {
		return new Event(
				dtoEvent.get_id(),
				dtoEvent.get_name(),
				dtoEvent.get_eventDate(),
				dtoEvent.get_location(),
				creator,
				campaign				
				);
	}
	
	public static EventDTO toDTO(Event event) {
		Set<String> attendees = new HashSet<String>();
		for (Activist a : event.getAttendees()) {
			attendees.add(a.getUsername());
		}
		
		return new EventDTO(
				event.getId(),
				event.getName(),
				event.getDate(),
				event.getLocation(),
				event.getCreator().getId(),
				event.getCampaign().getId(),
				attendees
				);
	}
	
}
