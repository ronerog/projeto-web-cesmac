package pokemon_web.form;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import pokemon_web.domain.StatusAnuncio;

public class AlteracaoStatusForm {

	@NotBlank(message = "Informe o novo status.")
	@Pattern(regexp = StatusAnuncio.CODIGOS_VALIDOS, message = "Status inválido.")
	private String status;

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}
