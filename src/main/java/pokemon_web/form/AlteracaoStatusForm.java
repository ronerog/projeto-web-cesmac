package pokemon_web.form;

import jakarta.validation.constraints.NotNull;
import pokemon_web.domain.StatusAnuncio;

public class AlteracaoStatusForm {

	@NotNull(message = "Informe o novo status.")
	private StatusAnuncio status;

	public StatusAnuncio getStatus() {
		return status;
	}

	public void setStatus(StatusAnuncio status) {
		this.status = status;
	}

}
