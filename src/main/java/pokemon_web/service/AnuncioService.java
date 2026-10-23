package pokemon_web.service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import pokemon_web.domain.StatusAnuncio;
import pokemon_web.dto.ResumoPainelDto;
import pokemon_web.form.AnuncioCadastroForm;
import pokemon_web.form.AnuncioEdicaoForm;
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

	public ResumoPainelDto resumirPainel() {
		List<Anuncio> anuncios = anuncioRepository.findAll();

		List<Anuncio> ativos = anuncios.stream()
				.filter(anuncio -> !anuncio.isVendido())
				.toList();
		long totalVendidas = anuncios.stream()
				.filter(Anuncio::isVendido)
				.count();
		double valorTotal = ativos.stream().mapToDouble(Anuncio::getPreco).sum();

		Anuncio destaque = ativos.stream()
				.max(Comparator.comparingDouble(Anuncio::getPreco))
				.orElse(null);

		return new ResumoPainelDto(destaque, ativos.size(), totalVendidas, valorTotal,
				anuncioRepository.findTop3ByOrderByIdDesc());
	}

	public Anuncio buscarPorId(Long id) {
		return anuncioRepository.findById(id).orElse(null);
	}

	public Anuncio criar(AnuncioCadastroForm formulario) {
		Carta carta = cartaService.salvarParaAnuncio(formulario.getCartaId());
		if (carta == null) {
			return null;
		}

		Anuncio anuncio = new Anuncio(null, carta, formulario.getPreco(), formulario.getCondicao(),
				formulario.getDescricao(), formulario.getVendedor(), StatusAnuncio.A_VENDA);
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

	public boolean alterarStatus(Long id, StatusAnuncio novoStatus) {
		Anuncio anuncio = buscarPorId(id);
		if (anuncio == null) {
			return false;
		}

		if (anuncio.isVendido() && novoStatus != StatusAnuncio.VENDIDA) {
			return false;
		}

		anuncio.setStatus(novoStatus);
		anuncioRepository.save(anuncio);
		return true;
	}

	public boolean excluir(Long id) {
		if (!anuncioRepository.existsById(id)) {
			return false;
		}
		anuncioRepository.deleteById(id);
		return true;
	}

}
