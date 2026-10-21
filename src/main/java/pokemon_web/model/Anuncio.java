package pokemon_web.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

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
	private String condicao;
	private String descricao;
	private String vendedor;
	private String status;

	public Anuncio() {
	}

	public Anuncio(Long id, Carta carta, double preco, String condicao, String descricao, String vendedor,
			String status) {
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

	public String getVendedor() {
		return vendedor;
	}

	public void setVendedor(String vendedor) {
		this.vendedor = vendedor;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}
