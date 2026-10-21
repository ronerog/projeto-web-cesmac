package pokemon_web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import pokemon_web.model.Anuncio;
import pokemon_web.service.AnuncioService;
import pokemon_web.service.ApiCartasIndisponivelException;

@Controller
public class AnuncioController {

	private final AnuncioService anuncioService;

	public AnuncioController(AnuncioService anuncioService) {
		this.anuncioService = anuncioService;
	}

	@PostMapping("/anuncios")
	public String criar(@RequestParam(defaultValue = "") String cartaId, @ModelAttribute Anuncio anuncio,
			RedirectAttributes redirectAttributes) {
		try {
			if (anuncioService.criar(anuncio, cartaId) == null) {
				redirectAttributes.addFlashAttribute("erro",
						"A carta selecionada não foi encontrada na Pokémon TCG API. Busque e selecione a carta novamente.");
				return "redirect:/pages/cadastro.html";
			}
		} catch (ApiCartasIndisponivelException excecao) {
			redirectAttributes.addFlashAttribute("erro",
					"A Pokémon TCG API não respondeu ao confirmar a carta. Tente publicar novamente em alguns segundos.");
			return "redirect:/pages/cadastro.html";
		}
		return "redirect:/pages/listagem.html";
	}

	@PostMapping("/anuncios/{id}/editar")
	public String editar(@PathVariable Long id, @ModelAttribute Anuncio anuncio, RedirectAttributes redirectAttributes) {
		anuncioService.atualizar(id, anuncio.getPreco(), anuncio.getCondicao(), anuncio.getDescricao());
		redirectAttributes.addFlashAttribute("mensagem", "Alterações salvas com sucesso.");
		return "redirect:/pages/edicao.html?id=" + id;
	}

	@PostMapping("/anuncios/{id}/status")
	public String alterarStatus(@PathVariable Long id, @RequestParam String status, RedirectAttributes redirectAttributes) {
		anuncioService.atualizarStatus(id, status);
		redirectAttributes.addFlashAttribute("mensagem", "Status atualizado com sucesso.");
		return "redirect:/pages/edicao.html?id=" + id;
	}

	@PostMapping("/anuncios/{id}/excluir")
	public String excluir(@PathVariable Long id) {
		anuncioService.excluir(id);
		return "redirect:/pages/listagem.html";
	}

}
