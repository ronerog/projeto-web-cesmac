package pokemon_web;

public class Anuncio {

	private Long id;
	private String cartaNome;
	private String cartaImagem;
	private String cartaColecao;
	private String cartaRaridade;
	private String cartaNumero;
	private double preco;
	private String condicao;
	private String descricao;
	private String vendedor;
	private String status;

	public Anuncio() {
	}

	public Anuncio(Long id, String cartaNome, String cartaImagem, String cartaColecao, String cartaRaridade,
			String cartaNumero, double preco, String condicao, String descricao, String vendedor, String status) {
		this.id = id;
		this.cartaNome = cartaNome;
		this.cartaImagem = cartaImagem;
		this.cartaColecao = cartaColecao;
		this.cartaRaridade = cartaRaridade;
		this.cartaNumero = cartaNumero;
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

	public String getCartaNome() {
		return cartaNome;
	}

	public void setCartaNome(String cartaNome) {
		this.cartaNome = cartaNome;
	}

	public String getCartaImagem() {
		return cartaImagem;
	}

	public void setCartaImagem(String cartaImagem) {
		this.cartaImagem = cartaImagem;
	}

	public String getCartaColecao() {
		return cartaColecao;
	}

	public void setCartaColecao(String cartaColecao) {
		this.cartaColecao = cartaColecao;
	}

	public String getCartaRaridade() {
		return cartaRaridade;
	}

	public void setCartaRaridade(String cartaRaridade) {
		this.cartaRaridade = cartaRaridade;
	}

	public String getCartaNumero() {
		return cartaNumero;
	}

	public void setCartaNumero(String cartaNumero) {
		this.cartaNumero = cartaNumero;
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
