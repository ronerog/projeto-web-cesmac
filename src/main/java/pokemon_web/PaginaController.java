package pokemon_web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PaginaController {

	@GetMapping({ "/", "/index.html" })
	public String painel() {
		return "index";
	}

	@GetMapping("/pages/listagem.html")
	public String listagem() {
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
