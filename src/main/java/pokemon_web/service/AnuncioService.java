package pokemon_web.service;

import java.util.List;
import java.util.Optional;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import pokemon_web.model.Anuncio;
import pokemon_web.repository.AnuncioRepository;

@Service
public class AnuncioService {

	private final AnuncioRepository anuncioRepository;

	public AnuncioService(AnuncioRepository anuncioRepository) {
		this.anuncioRepository = anuncioRepository;
	}

	@EventListener(ApplicationReadyEvent.class)
	public void popularDadosIniciais() {
		if (anuncioRepository.count() > 0) {
			return;
		}

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
		return anuncioRepository.findAll();
	}

	public Anuncio buscarPorId(Long id) {
		return anuncioRepository.findById(id).orElse(null);
	}

	public Anuncio salvar(Anuncio anuncio) {
		return anuncioRepository.save(anuncio);
	}

	public Anuncio atualizar(Long id, double preco, String condicao, String descricao) {
		Optional<Anuncio> existente = anuncioRepository.findById(id);
		if (existente.isEmpty()) {
			return null;
		}

		Anuncio anuncio = existente.get();
		anuncio.setPreco(preco);
		anuncio.setCondicao(condicao);
		anuncio.setDescricao(descricao);
		return anuncioRepository.save(anuncio);
	}

	public Anuncio atualizarStatus(Long id, String status) {
		Optional<Anuncio> existente = anuncioRepository.findById(id);
		if (existente.isEmpty()) {
			return null;
		}

		Anuncio anuncio = existente.get();
		anuncio.setStatus(status);
		return anuncioRepository.save(anuncio);
	}

	public boolean excluir(Long id) {
		if (!anuncioRepository.existsById(id)) {
			return false;
		}
		anuncioRepository.deleteById(id);
		return true;
	}

}
