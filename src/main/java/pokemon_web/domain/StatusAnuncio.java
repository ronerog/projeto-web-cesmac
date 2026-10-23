package pokemon_web.domain;

public enum StatusAnuncio {

	A_VENDA("a-venda", "À venda"),
	RESERVADA("reservada", "Reservada"),
	VENDIDA("vendida", "Vendida");

	private final String codigo;
	private final String descricao;

	StatusAnuncio(String codigo, String descricao) {
		this.codigo = codigo;
		this.descricao = descricao;
	}

	public String getCodigo() {
		return codigo;
	}

	public String getDescricao() {
		return descricao;
	}

}
