package pokemon_web.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import pokemon_web.domain.StatusAnuncio;
import pokemon_web.model.Anuncio;
import pokemon_web.service.AnuncioService;

@Controller
public class PaginaController {

	private static final int LIMITE_ULTIMOS_ANUNCIOS = 3;

	private final AnuncioService anuncioService;

	public PaginaController(AnuncioService anuncioService) {
		this.anuncioService = anuncioService;
	}

	@GetMapping("/")
	public String painel(Model model) {
		List<Anuncio> anuncios = anuncioService.listar();

		String vendida = StatusAnuncio.VENDIDA.getCodigo();
		List<Anuncio> ativos = anuncios.stream()
				.filter(anuncio -> !vendida.equals(anuncio.getStatus()))
				.toList();
		long totalVendidas = anuncios.stream()
				.filter(anuncio -> vendida.equals(anuncio.getStatus()))
				.count();
		double valorTotal = ativos.stream().mapToDouble(Anuncio::getPreco).sum();

		Anuncio destaque = ativos.stream()
				.max(Comparator.comparingDouble(Anuncio::getPreco))
				.orElse(null);

		List<Anuncio> ultimosAnuncios = new ArrayList<>(anuncios);
		Collections.reverse(ultimosAnuncios);
		if (ultimosAnuncios.size() > LIMITE_ULTIMOS_ANUNCIOS) {
			ultimosAnuncios = ultimosAnuncios.subList(0, LIMITE_ULTIMOS_ANUNCIOS);
		}

		model.addAttribute("destaque", destaque);
		model.addAttribute("totalAtivos", ativos.size());
		model.addAttribute("totalVendidas", totalVendidas);
		model.addAttribute("valorTotal", valorTotal);
		model.addAttribute("ultimosAnuncios", ultimosAnuncios);
		return "index";
	}

}
