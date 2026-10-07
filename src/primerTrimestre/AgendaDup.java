package primerTrimestre;

import java.util.List;
import com.google.gson.annotations.SerializedName;

// Ningún cambio en esta clase
public class AgendaDup {
	@SerializedName("agenda")
	private List<ContactoDup> contactoDups = null;
	
	public List<ContactoDup> getContactos() {
		return contactoDups;
	}
	
	public void setContactos(List<ContactoDup> contactoDups) {
		this.contactoDups = contactoDups;
	}
}
