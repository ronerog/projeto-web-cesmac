package pokemon_web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AnuncioController {

	private final AnuncioService anuncioService;

	public AnuncioController(AnuncioService anuncioService) {
		this.anuncioService = anuncioService;
	}

	@PostMapping("/anuncios")
	public String criar(@ModelAttribute Anuncio anuncio) {
		anuncio.setStatus("a-venda");
		anuncioService.salvar(anuncio);
		return "redirect:/pages/listagem.html";
	}

}
