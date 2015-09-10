package nz.co.actiontracker.activist;

public class ActivistMapper {

	static Activist toDomainModel(ActivistDTO dtoActivist) {
		Activist fullActivist = new Activist(dtoActivist.get_id(),
				dtoActivist.get_username(),
				dtoActivist.get_email(),
				dtoActivist.get_address());
		return fullActivist;
	}
	
	static ActivistDTO toDTO(Activist activist) {
		return new ActivistDTO(activist.getId(), 
				activist.getUsername(),
				activist.getEmail(), 
				activist.getAddress());
	}
	
}
