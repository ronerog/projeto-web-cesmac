package pokemon_web.service;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import pokemon_web.dto.PokemonTcgCardDto;
import pokemon_web.dto.PokemonTcgCartaRespostaDto;
import pokemon_web.dto.PokemonTcgListaRespostaDto;
import pokemon_web.exception.ApiCartasIndisponivelException;
import pokemon_web.model.Carta;
import pokemon_web.repository.CartaRepository;

@Service
public class CartaService {

	private static final String URL_BASE = "https://api.pokemontcg.io/v2";
	private static final int LIMITE_RESULTADOS = 24;
	private static final int TENTATIVAS = 3;
	private static final long INTERVALO_ENTRE_TENTATIVAS_MS = 600;
	private static final Duration VALIDADE_CACHE = Duration.ofMinutes(10);
	private static final int LIMITE_CACHE = 200;

	private final CartaRepository cartaRepository;
	private final RestClient restClient;
	private final Map<String, BuscaEmCache> cacheBuscas = new ConcurrentHashMap<>();

	public CartaService(CartaRepository cartaRepository) {
		this.cartaRepository = cartaRepository;

		SimpleClientHttpRequestFactory fabrica = new SimpleClientHttpRequestFactory();
		fabrica.setConnectTimeout(Duration.ofSeconds(5));
		fabrica.setReadTimeout(Duration.ofSeconds(20));
		this.restClient = RestClient.builder().baseUrl(URL_BASE).requestFactory(fabrica).build();
	}

	public List<Carta> buscarPorNome(String nome) {
		String termo = nome == null ? "" : nome.trim();
		if (termo.isEmpty()) {
			return List.of();
		}

		String chave = termo.toLowerCase();
		BuscaEmCache emCache = cacheBuscas.get(chave);
		if (emCache != null && emCache.expiraEm().isAfter(Instant.now())) {
			return emCache.cartas();
		}

		String consulta = "name:\"*" + termo + "*\"";
		PokemonTcgListaRespostaDto resposta = consultarComTentativas(() -> restClient.get()
				.uri(uriBuilder -> uriBuilder.path("/cards")
						.queryParam("q", consulta)
						.queryParam("pageSize", LIMITE_RESULTADOS)
						.build())
				.retrieve()
				.body(PokemonTcgListaRespostaDto.class));

		List<Carta> cartas = resposta == null || resposta.getData() == null
				? List.of()
				: resposta.getData().stream().map(this::converter).toList();

		if (cacheBuscas.size() >= LIMITE_CACHE) {
			cacheBuscas.clear();
		}
		cacheBuscas.put(chave, new BuscaEmCache(cartas, Instant.now().plus(VALIDADE_CACHE)));
		return cartas;
	}

	public Carta buscarPorId(String id) {
		Optional<Carta> existente = cartaRepository.findById(id);
		return existente.isPresent() ? existente.get() : buscarNaApiPorId(id);
	}

	public Carta salvarParaAnuncio(String id) {
		if (id == null || id.isBlank()) {
			return null;
		}

		Optional<Carta> existente = cartaRepository.findById(id);
		if (existente.isPresent()) {
			return existente.get();
		}

		Carta carta = buscarNaApiPorId(id);
		return carta != null ? cartaRepository.save(carta) : null;
	}

	private Carta buscarNaApiPorId(String id) {
		try {
			PokemonTcgCartaRespostaDto resposta = consultarComTentativas(() -> restClient.get()
					.uri("/cards/{id}", id)
					.retrieve()
					.body(PokemonTcgCartaRespostaDto.class));

			return resposta == null || resposta.getData() == null ? null : converter(resposta.getData());
		} catch (HttpClientErrorException.NotFound excecao) {
			return null;
		}
	}

	private <T> T consultarComTentativas(Supplier<T> consulta) {
		RestClientException ultimaFalha = null;
		for (int tentativa = 1; tentativa <= TENTATIVAS; tentativa++) {
			try {
				return consulta.get();
			} catch (HttpClientErrorException excecao) {
				throw excecao;
			} catch (RestClientException excecao) {
				ultimaFalha = excecao;
				if (tentativa < TENTATIVAS) {
					esperar(INTERVALO_ENTRE_TENTATIVAS_MS * tentativa);
				}
			}
		}
		throw new ApiCartasIndisponivelException(ultimaFalha);
	}

	private void esperar(long milissegundos) {
		try {
			Thread.sleep(milissegundos);
		} catch (InterruptedException excecao) {
			Thread.currentThread().interrupt();
		}
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

	private record BuscaEmCache(List<Carta> cartas, Instant expiraEm) {
	}

}
