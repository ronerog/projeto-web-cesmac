package pokemon_web.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "cartas")
public class Carta {

	@Id
	private String id;
	private String nome;
	private String imagemPequena;
	private String imagemGrande;
	private String colecao;
	private String raridade;
	private String numero;

	public Carta() {
	}

	public Carta(String id, String nome, String imagemPequena, String imagemGrande, String colecao,
			String raridade, String numero) {
		this.id = id;
		this.nome = nome;
		this.imagemPequena = imagemPequena;
		this.imagemGrande = imagemGrande;
		this.colecao = colecao;
		this.raridade = raridade;
		this.numero = numero;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getImagemPequena() {
		return imagemPequena;
	}

	public void setImagemPequena(String imagemPequena) {
		this.imagemPequena = imagemPequena;
	}

	public String getImagemGrande() {
		return imagemGrande;
	}

	public void setImagemGrande(String imagemGrande) {
		this.imagemGrande = imagemGrande;
	}

	public String getColecao() {
		return colecao;
	}

	public void setColecao(String colecao) {
		this.colecao = colecao;
	}

	public String getRaridade() {
		return raridade;
	}

	public void setRaridade(String raridade) {
		this.raridade = raridade;
	}

	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

}
