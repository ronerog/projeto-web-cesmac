package pokemon_web.form;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import pokemon_web.domain.CondicaoCarta;

public class AnuncioCadastroForm {

	@NotBlank(message = "Selecione uma carta na busca.")
	@Size(max = 64, message = "Identificador de carta inválido.")
	private String cartaId;

	@NotNull(message = "Informe o preço.")
	@Positive(message = "O preço deve ser maior que zero.")
	private Double preco;

	@NotNull(message = "Selecione a condição da carta.")
	private CondicaoCarta condicao;

	@Size(max = 300, message = "A descrição pode ter no máximo 300 caracteres.")
	private String descricao;

	@Size(max = 40, message = "O nome do vendedor pode ter no máximo 40 caracteres.")
	private String vendedor;

	public String getCartaId() {
		return cartaId;
	}

	public void setCartaId(String cartaId) {
		this.cartaId = cartaId;
	}

	public Double getPreco() {
		return preco;
	}

	public void setPreco(Double preco) {
		this.preco = preco;
	}

	public CondicaoCarta getCondicao() {
		return condicao;
	}

	public void setCondicao(CondicaoCarta condicao) {
		this.condicao = condicao;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public String getVendedor() {
		return vendedor;
	}

	public void setVendedor(String vendedor) {
		this.vendedor = vendedor;
	}

}
