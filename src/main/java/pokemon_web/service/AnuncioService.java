package pokemon_web.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import pokemon_web.model.Anuncio;
import pokemon_web.model.AnuncioCadastroForm;
import pokemon_web.model.AnuncioEdicaoForm;
import pokemon_web.model.Carta;
import pokemon_web.model.StatusAnuncio;
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

	public Anuncio criar(AnuncioCadastroForm formulario) {
		Carta carta = cartaService.salvarParaAnuncio(formulario.getCartaId());
		if (carta == null) {
			return null;
		}

		Anuncio anuncio = new Anuncio(null, carta, formulario.getPreco(), formulario.getCondicao(),
				formulario.getDescricao(), formulario.getVendedor(), StatusAnuncio.A_VENDA.getCodigo());
		return anuncioRepository.save(anuncio);
	}

	public Anuncio atualizar(Long id, AnuncioEdicaoForm formulario) {
		Optional<Anuncio> existente = anuncioRepository.findById(id);
		if (existente.isEmpty()) {
			return null;
		}

		Anuncio anuncio = existente.get();
		anuncio.setPreco(formulario.getPreco());
		anuncio.setCondicao(formulario.getCondicao());
		anuncio.setDescricao(formulario.getDescricao());
		return anuncioRepository.save(anuncio);
	}

	public Anuncio atualizarStatus(Long id, StatusAnuncio status) {
		Optional<Anuncio> existente = anuncioRepository.findById(id);
		if (existente.isEmpty()) {
			return null;
		}

		Anuncio anuncio = existente.get();
		anuncio.setStatus(status.getCodigo());
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
