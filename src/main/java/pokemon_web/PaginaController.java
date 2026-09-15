package pokemon_web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PaginaController {

	private final AnuncioService anuncioService;

	public PaginaController(AnuncioService anuncioService) {
		this.anuncioService = anuncioService;
	}

	@GetMapping({ "/", "/index.html" })
	public String painel() {
		return "index";
	}

	@GetMapping("/pages/listagem.html")
	public String listagem(Model model) {
		model.addAttribute("anuncios", anuncioService.listar());
		return "pages/listagem";
	}

	@GetMapping("/pages/cadastro.html")
	public String cadastro() {
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
