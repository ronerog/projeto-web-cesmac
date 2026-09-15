package pokemon_web;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PaginaController {

	private static final int LIMITE_ULTIMOS_ANUNCIOS = 3;

	private final AnuncioService anuncioService;

	public PaginaController(AnuncioService anuncioService) {
		this.anuncioService = anuncioService;
	}

	@GetMapping({ "/", "/index.html" })
	public String painel(Model model) {
		List<Anuncio> anuncios = anuncioService.listar();

		List<Anuncio> ativos = anuncios.stream()
				.filter(anuncio -> !"vendida".equals(anuncio.getStatus()))
				.toList();
		long totalVendidas = anuncios.stream()
				.filter(anuncio -> "vendida".equals(anuncio.getStatus()))
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

	@GetMapping("/pages/listagem.html")
	public String listagem(Model model) {
		model.addAttribute("anuncios", anuncioService.listar());
		return "pages/listagem";
	}

	@GetMapping("/pages/cadastro.html")
	public String cadastro(Model model) {
		model.addAttribute("anuncio", new Anuncio());
		return "pages/cadastro";
	}

	@GetMapping("/pages/detalhes.html")
	public String detalhes() {
		return "pages/detalhes";
	}

	@GetMapping("/pages/edicao.html")
	public String edicao() {
		return "pages/edicao";
	}

}
