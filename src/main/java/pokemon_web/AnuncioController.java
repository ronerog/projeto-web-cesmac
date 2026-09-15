package pokemon_web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
