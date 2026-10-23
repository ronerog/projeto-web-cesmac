package pokemon_web.dto;

import java.util.List;

import pokemon_web.model.Anuncio;

public record ResumoPainelDto(Anuncio destaque, long totalAtivos, long totalVendidas, double valorTotal,
		List<Anuncio> ultimosAnuncios) {
}
