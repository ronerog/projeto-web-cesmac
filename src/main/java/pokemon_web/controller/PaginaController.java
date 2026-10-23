package pokemon_web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import pokemon_web.dto.ResumoPainelDto;
import pokemon_web.service.AnuncioService;

@Controller
public class PaginaController {

	private final AnuncioService anuncioService;

	public PaginaController(AnuncioService anuncioService) {
		this.anuncioService = anuncioService;
	}

	@GetMapping("/")
	public String painel(Model model) {
		ResumoPainelDto resumo = anuncioService.resumirPainel();

		model.addAttribute("destaque", resumo.destaque());
		model.addAttribute("totalAtivos", resumo.totalAtivos());
		model.addAttribute("totalVendidas", resumo.totalVendidas());
		model.addAttribute("valorTotal", resumo.valorTotal());
		model.addAttribute("ultimosAnuncios", resumo.ultimosAnuncios());
		return "index";
	}

}
