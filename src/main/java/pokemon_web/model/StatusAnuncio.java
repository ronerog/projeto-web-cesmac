package pokemon_web.model;

import java.util.Arrays;
import java.util.Optional;

public enum StatusAnuncio {

	A_VENDA("a-venda"),
	RESERVADA("reservada"),
	VENDIDA("vendida");

	public static final String CODIGOS_VALIDOS = "a-venda|reservada|vendida";

	private final String codigo;

	StatusAnuncio(String codigo) {
		this.codigo = codigo;
	}

	public String getCodigo() {
		return codigo;
	}

	public static Optional<StatusAnuncio> porCodigo(String codigo) {
		return Arrays.stream(values())
				.filter(status -> status.codigo.equals(codigo))
				.findFirst();
	}

}
