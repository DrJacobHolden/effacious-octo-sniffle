package nz.co.actiontracker.activist;

/**
 * This class is based on the example from Ian Warren. It
 * simply maps an activist to its DTO model and vice-versa.
 */
public class ActivistMapper {

	public static Activist toDomainModel(ActivistDTO dtoActivist) {
		Activist fullActivist;
		if (dtoActivist.get_id() != 0) {
			fullActivist = new Activist(dtoActivist.get_id(),
					dtoActivist.get_username(),
					dtoActivist.get_email(),
					dtoActivist.get_address());
		} else {
			fullActivist = new Activist(dtoActivist.get_username(),
					dtoActivist.get_email(),
					dtoActivist.get_address());
		}
		return fullActivist;
	}
	
	public static ActivistDTO toDTO(Activist activist) {
		return new ActivistDTO(activist.getId(), 
				activist.getUsername(),
				activist.getEmail(), 
				activist.getAddress());
	}
	
}
