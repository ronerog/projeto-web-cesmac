package pokemon_web.service;

import java.util.List;
import java.util.Optional;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import pokemon_web.model.Anuncio;
import pokemon_web.model.Carta;
import pokemon_web.repository.AnuncioRepository;

@Service
public class AnuncioService {

	private final AnuncioRepository anuncioRepository;
	private final CartaService cartaService;

	public AnuncioService(AnuncioRepository anuncioRepository, CartaService cartaService) {
		this.anuncioRepository = anuncioRepository;
		this.cartaService = cartaService;
	}

	@EventListener(ApplicationReadyEvent.class)
	public void popularDadosIniciais() {
		if (anuncioRepository.count() > 0) {
			return;
		}

		Carta charizard = cartaService.salvar(new Carta("base1-4", "Charizard",
				"https://images.pokemontcg.io/base1/4.png", "https://images.pokemontcg.io/base1/4_hires.png",
				"Base", "Rare Holo", "4/102"));
		Carta pikachu = cartaService.salvar(new Carta("base1-58", "Pikachu",
				"https://images.pokemontcg.io/base1/58.png", "https://images.pokemontcg.io/base1/58_hires.png",
				"Base", "Common", "58/102"));
		Carta blastoise = cartaService.salvar(new Carta("base1-2", "Blastoise",
				"https://images.pokemontcg.io/base1/2.png", "https://images.pokemontcg.io/base1/2_hires.png",
				"Base", "Rare Holo", "2/102"));

		salvar(new Anuncio(null, charizard, 2500, "Excelente",
				"Carta original de 1999, sem dobras, levíssimo desgaste na borda inferior.", "Rônero", "a-venda"));
		salvar(new Anuncio(null, pikachu, 89.9, "Boa",
				"Carta bem conservada, guardada em sleeve desde que foi aberta.", "Marina Alves", "reservada"));
		salvar(new Anuncio(null, blastoise, 1750, "Nova/Lacrada",
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

	/**
	 * Cria o anúncio vinculado à carta escolhida, gravando a carta no banco caso
	 * ainda não exista. Retorna null se a carta não for encontrada na API.
	 */
	public Anuncio criar(Anuncio anuncio, String cartaId) {
		Carta carta = cartaService.salvarParaAnuncio(cartaId);
		if (carta == null) {
			return null;
		}

		anuncio.setCarta(carta);
		anuncio.setStatus("a-venda");
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
