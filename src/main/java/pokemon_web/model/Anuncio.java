package pokemon_web.model;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import pokemon_web.domain.CondicaoCarta;
import pokemon_web.domain.StatusAnuncio;

@Entity
@Table(name = "anuncios")
public class Anuncio {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@ManyToOne(optional = false)
	@JoinColumn(name = "carta_id", nullable = false)
	private Carta carta;
	private double preco;
	@Enumerated(EnumType.STRING)
	private CondicaoCarta condicao;
	private String descricao;
	private String vendedor;
	@Enumerated(EnumType.STRING)
	private StatusAnuncio status;

	public Anuncio() {
	}

	public Anuncio(Long id, Carta carta, double preco, CondicaoCarta condicao, String descricao, String vendedor,
			StatusAnuncio status) {
		this.id = id;
		this.carta = carta;
		this.preco = preco;
		this.condicao = condicao;
		this.descricao = descricao;
		this.vendedor = vendedor;
		this.status = status;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Carta getCarta() {
		return carta;
	}

	public void setCarta(Carta carta) {
		this.carta = carta;
	}

	public String getCartaNome() {
		return carta != null ? carta.getNome() : null;
	}

	public String getCartaImagem() {
		return carta != null ? carta.getImagemGrande() : null;
	}

	public String getCartaColecao() {
		return carta != null ? carta.getColecao() : null;
	}

	public String getCartaRaridade() {
		return carta != null ? carta.getRaridade() : null;
	}

	public String getCartaNumero() {
		return carta != null ? carta.getNumero() : null;
	}

	public double getPreco() {
		return preco;
	}

	public void setPreco(double preco) {
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

	public StatusAnuncio getStatus() {
		return status;
	}

	public void setStatus(StatusAnuncio status) {
		this.status = status;
	}

	public boolean isVendido() {
		return status == StatusAnuncio.VENDIDA;
	}

}
