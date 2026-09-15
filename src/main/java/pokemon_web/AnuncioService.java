package pokemon_web;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.stereotype.Service;

@Service
public class AnuncioService {

	private final List<Anuncio> anuncios = new ArrayList<>();
	private final AtomicLong proximoId = new AtomicLong(1);

	public AnuncioService() {
		salvar(new Anuncio(null, "Charizard", "https://images.pokemontcg.io/base1/4_hires.png", "Base",
				"Rare Holo", "4/102", 2500, "Excelente",
				"Carta original de 1999, sem dobras, levíssimo desgaste na borda inferior.", "Rônero", "a-venda"));
		salvar(new Anuncio(null, "Pikachu", "https://images.pokemontcg.io/base1/58_hires.png", "Base",
				"Common", "58/102", 89.9, "Boa",
				"Carta bem conservada, guardada em sleeve desde que foi aberta.", "Marina Alves", "reservada"));
		salvar(new Anuncio(null, "Blastoise", "https://images.pokemontcg.io/base1/2_hires.png", "Base",
				"Rare Holo", "2/102", 1750, "Nova/Lacrada",
				"Carta recém-aberta de booster, encaminhada direto para toploader.", "Diego Ramos", "vendida"));
	}

	public List<Anuncio> listar() {
		return anuncios;
	}

	public Anuncio buscarPorId(Long id) {
		return anuncios.stream()
				.filter(anuncio -> anuncio.getId().equals(id))
				.findFirst()
				.orElse(null);
	}

	public Anuncio salvar(Anuncio anuncio) {
		anuncio.setId(proximoId.getAndIncrement());
		anuncios.add(anuncio);
		return anuncio;
	}

	public Anuncio atualizar(Long id, double preco, String condicao, String descricao) {
		Anuncio anuncio = buscarPorId(id);
		if (anuncio != null) {
			anuncio.setPreco(preco);
			anuncio.setCondicao(condicao);
			anuncio.setDescricao(descricao);
		}
		return anuncio;
	}

	public Anuncio atualizarStatus(Long id, String status) {
		Anuncio anuncio = buscarPorId(id);
		if (anuncio != null) {
			anuncio.setStatus(status);
		}
		return anuncio;
	}

	public boolean excluir(Long id) {
		return anuncios.removeIf(anuncio -> anuncio.getId().equals(id));
	}

}
