package pokemon_web.form;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import pokemon_web.model.Anuncio;

public class AnuncioEdicaoForm {

	@NotNull(message = "Informe o preço.")
	@Positive(message = "O preço deve ser maior que zero.")
	private Double preco;

	@NotBlank(message = "Selecione a condição da carta.")
	@Pattern(regexp = AnuncioCadastroForm.CONDICOES_VALIDAS, message = "Condição inválida.")
	private String condicao;

	@Size(max = 300, message = "A descrição pode ter no máximo 300 caracteres.")
	private String descricao;

	public static AnuncioEdicaoForm de(Anuncio anuncio) {
		AnuncioEdicaoForm formulario = new AnuncioEdicaoForm();
		formulario.setPreco(anuncio.getPreco());
		formulario.setCondicao(anuncio.getCondicao());
		formulario.setDescricao(anuncio.getDescricao());
		return formulario;
	}

	public Double getPreco() {
		return preco;
	}

	public void setPreco(Double preco) {
		this.preco = preco;
	}

	public String getCondicao() {
		return condicao;
	}

	public void setCondicao(String condicao) {
		this.condicao = condicao;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

}
