package pokemon_web.domain;

public enum CondicaoCarta {

	NOVA_LACRADA("Nova/Lacrada"),
	EXCELENTE("Excelente"),
	BOA("Boa"),
	JOGADA("Jogada"),
	DANIFICADA("Danificada");

	private final String descricao;

	CondicaoCarta(String descricao) {
		this.descricao = descricao;
	}

	public String getDescricao() {
		return descricao;
	}

}
