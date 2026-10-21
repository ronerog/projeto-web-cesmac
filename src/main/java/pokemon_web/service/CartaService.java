package pokemon_web.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import pokemon_web.model.Carta;
import pokemon_web.model.PokemonTcgCardDto;
import pokemon_web.model.PokemonTcgCartaRespostaDto;
import pokemon_web.model.PokemonTcgListaRespostaDto;
import pokemon_web.repository.CartaRepository;

@Service
public class CartaService {

	private static final String URL_BASE = "https://api.pokemontcg.io/v2";
	private static final int LIMITE_RESULTADOS = 24;

	private final CartaRepository cartaRepository;
	private final RestClient restClient;

	public CartaService(CartaRepository cartaRepository) {
		this.cartaRepository = cartaRepository;
		this.restClient = RestClient.create(URL_BASE);
	}

	public List<Carta> buscarPorNome(String nome) {
		String termo = nome == null ? "" : nome.trim();
		if (termo.isEmpty()) {
			return List.of();
		}

		String consulta = "name:\"*" + termo + "*\"";
		try {
			PokemonTcgListaRespostaDto resposta = restClient.get()
					.uri(uriBuilder -> uriBuilder.path("/cards")
							.queryParam("q", consulta)
							.queryParam("pageSize", LIMITE_RESULTADOS)
							.build())
					.retrieve()
					.body(PokemonTcgListaRespostaDto.class);

			if (resposta == null || resposta.getData() == null) {
				return cartaRepository.findByNomeContainingIgnoreCase(termo);
			}

			List<Carta> cartas = resposta.getData().stream().map(this::converter).toList();
			return cartaRepository.saveAll(cartas);
		} catch (RestClientException excecao) {
			return cartaRepository.findByNomeContainingIgnoreCase(termo);
		}
	}

	public Carta buscarPorId(String id) {
		Optional<Carta> existente = cartaRepository.findById(id);
		if (existente.isPresent()) {
			return existente.get();
		}

		try {
			PokemonTcgCartaRespostaDto resposta = restClient.get()
					.uri("/cards/{id}", id)
					.retrieve()
					.body(PokemonTcgCartaRespostaDto.class);

			if (resposta == null || resposta.getData() == null) {
				return null;
			}

			return cartaRepository.save(converter(resposta.getData()));
		} catch (RestClientException excecao) {
			return null;
		}
	}

	public List<Carta> listarSalvas() {
		return cartaRepository.findAll();
	}

	private Carta converter(PokemonTcgCardDto dto) {
		PokemonTcgCardDto.Imagens imagens = dto.getImages() != null ? dto.getImages() : new PokemonTcgCardDto.Imagens();
		PokemonTcgCardDto.ColecaoDto colecao = dto.getSet() != null ? dto.getSet() : new PokemonTcgCardDto.ColecaoDto();

		String numeroBase = dto.getNumber() != null ? dto.getNumber() : "-";
		String numero = colecao.getPrintedTotal() != null
				? numeroBase + "/" + colecao.getPrintedTotal()
				: numeroBase;

		return new Carta(
				dto.getId(),
				dto.getName() != null ? dto.getName() : "Carta sem nome",
				imagens.getSmall(),
				imagens.getLarge() != null ? imagens.getLarge() : imagens.getSmall(),
				colecao.getName() != null ? colecao.getName() : "Coleção desconhecida",
				dto.getRarity() != null ? dto.getRarity() : "Não informada",
				numero);
	}

}
