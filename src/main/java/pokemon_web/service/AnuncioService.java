package pokemon_web.service;

import java.util.List;
import java.util.Optional;

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
