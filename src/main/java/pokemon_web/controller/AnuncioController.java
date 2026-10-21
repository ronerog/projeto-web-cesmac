package pokemon_web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.validation.Valid;
import pokemon_web.model.AlteracaoStatusForm;
import pokemon_web.model.Anuncio;
import pokemon_web.model.AnuncioCadastroForm;
import pokemon_web.model.AnuncioEdicaoForm;
import pokemon_web.model.Carta;
import pokemon_web.model.StatusAnuncio;
import pokemon_web.service.AnuncioService;
import pokemon_web.service.ApiCartasIndisponivelException;
import pokemon_web.service.CartaService;

@Controller
public class AnuncioController {

	private static final String PAGINA_CADASTRO = "pages/cadastro";
	private static final String PAGINA_EDICAO = "pages/edicao";
	private static final String REDIRECIONAR_EDICAO = "redirect:/pages/edicao.html?id=";

	private final AnuncioService anuncioService;
	private final CartaService cartaService;

	public AnuncioController(AnuncioService anuncioService, CartaService cartaService) {
		this.anuncioService = anuncioService;
		this.cartaService = cartaService;
	}

	@PostMapping("/anuncios")
	public String criar(@Valid @ModelAttribute("anuncio") AnuncioCadastroForm formulario, BindingResult validacao,
			Model model) {
		boolean apiIndisponivel = false;
		if (!validacao.hasErrors()) {
			try {
				if (anuncioService.criar(formulario) != null) {
					return "redirect:/pages/listagem.html";
				}
				validacao.rejectValue("cartaId", "carta.naoEncontrada",
						"A carta selecionada não foi encontrada na Pokémon TCG API. Busque e selecione a carta novamente.");
			} catch (ApiCartasIndisponivelException excecao) {
				apiIndisponivel = true;
				validacao.reject("api.indisponivel",
						"A Pokémon TCG API não respondeu ao confirmar a carta. Tente publicar novamente em alguns segundos.");
			}
		}

		model.addAttribute("cartaSelecionada", apiIndisponivel ? null : cartaSelecionada(formulario.getCartaId()));
		return PAGINA_CADASTRO;
	}

	@PostMapping("/anuncios/{id}/editar")
	public String editar(@PathVariable Long id, @Valid @ModelAttribute("formulario") AnuncioEdicaoForm formulario,
			BindingResult validacao, Model model, RedirectAttributes redirectAttributes) {
		if (validacao.hasErrors()) {
			model.addAttribute("anuncio", anuncioService.buscarPorId(id));
			return PAGINA_EDICAO;
		}

		if (anuncioService.atualizar(id, formulario) != null) {
			redirectAttributes.addFlashAttribute("mensagem", "Alterações salvas com sucesso.");
		}
		return REDIRECIONAR_EDICAO + id;
	}

	@PostMapping("/anuncios/{id}/status")
	public String alterarStatus(@PathVariable Long id, @Valid @ModelAttribute("statusForm") AlteracaoStatusForm statusForm,
			BindingResult validacao, Model model, RedirectAttributes redirectAttributes) {
		if (validacao.hasErrors()) {
			Anuncio anuncio = anuncioService.buscarPorId(id);
			model.addAttribute("anuncio", anuncio);
			if (anuncio != null) {
				model.addAttribute("formulario", AnuncioEdicaoForm.de(anuncio));
			}
			return PAGINA_EDICAO;
		}

		StatusAnuncio novoStatus = StatusAnuncio.porCodigo(statusForm.getStatus()).orElseThrow();
		if (anuncioService.atualizarStatus(id, novoStatus) != null) {
			redirectAttributes.addFlashAttribute("mensagem", "Status atualizado com sucesso.");
		}
		return REDIRECIONAR_EDICAO + id;
	}

	@PostMapping("/anuncios/{id}/excluir")
	public String excluir(@PathVariable Long id) {
		anuncioService.excluir(id);
		return "redirect:/pages/listagem.html";
	}

	private Carta cartaSelecionada(String cartaId) {
		if (cartaId == null || cartaId.isBlank()) {
			return null;
		}
		try {
			return cartaService.buscarPorId(cartaId);
		} catch (ApiCartasIndisponivelException excecao) {
			return null;
		}
	}

}
