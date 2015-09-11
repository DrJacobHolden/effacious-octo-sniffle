package nz.co.actiontracker.campaign;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import nz.co.actiontracker.activist.ActivistDTO;

@XmlRootElement(name="campaign")
@XmlAccessorType(XmlAccessType.FIELD)
public class CampaignDTO {

	@XmlAttribute(name="id")
	private long _id;
	
	@XmlElement(name="name")
	private String _name;
	
	@XmlElement(name="creator")
	private ActivistDTO _creator;

	public CampaignDTO(String name, ActivistDTO creator) {
		_name = name;
		_creator = creator;
	}
	
	public CampaignDTO(long id, String name, ActivistDTO creator) {
		_id = id;
		_name = name;
		_creator = creator;
	}
	
	protected CampaignDTO() {}
	
	public long get_id() {
		return _id;
	}

	public void set_id(long _id) {
		this._id = _id;
	}

	public String get_name() {
		return _name;
	}

	public void set_name(String _name) {
		this._name = _name;
	}

	public ActivistDTO get_creator() {
		return _creator;
	}

	public void set_creator(ActivistDTO _creator) {
		this._creator = _creator;
	}
	
	
	
}
