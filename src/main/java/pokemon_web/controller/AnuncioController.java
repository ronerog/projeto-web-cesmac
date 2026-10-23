package pokemon_web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.validation.Valid;
import pokemon_web.domain.CondicaoCarta;
import pokemon_web.exception.ApiCartasIndisponivelException;
import pokemon_web.form.AlteracaoStatusForm;
import pokemon_web.form.AnuncioCadastroForm;
import pokemon_web.form.AnuncioEdicaoForm;
import pokemon_web.model.Anuncio;
import pokemon_web.model.Carta;
import pokemon_web.service.AnuncioService;
import pokemon_web.service.CartaService;

@Controller
public class AnuncioController {

	private static final String PAGINA_LISTAGEM = "pages/listagem";
	private static final String PAGINA_CADASTRO = "pages/cadastro";
	private static final String PAGINA_DETALHES = "pages/detalhes";
	private static final String PAGINA_EDICAO = "pages/edicao";
	private static final String REDIRECIONAR_LISTAGEM = "redirect:/anuncios";
	private static final String ATRIBUTO_ANUNCIO = "anuncio";
	private static final String ATRIBUTO_FORMULARIO = "formulario";
	private static final String ATRIBUTO_MENSAGEM = "mensagem";

	private final AnuncioService anuncioService;
	private final CartaService cartaService;

	public AnuncioController(AnuncioService anuncioService, CartaService cartaService) {
		this.anuncioService = anuncioService;
		this.cartaService = cartaService;
	}

	@ModelAttribute("condicoes")
	public CondicaoCarta[] condicoes() {
		return CondicaoCarta.values();
	}

	@GetMapping("/anuncios")
	public String listar(Model model) {
		model.addAttribute("anuncios", anuncioService.listar());
		return PAGINA_LISTAGEM;
	}

	@GetMapping("/anuncios/novo")
	public String novo(Model model) {
		model.addAttribute(ATRIBUTO_ANUNCIO, new AnuncioCadastroForm());
		return PAGINA_CADASTRO;
	}

	@GetMapping("/anuncios/{id}")
	public String detalhes(@PathVariable Long id, Model model) {
		model.addAttribute(ATRIBUTO_ANUNCIO, anuncioService.buscarPorId(id));
		return PAGINA_DETALHES;
	}

	@GetMapping("/anuncios/{id}/editar")
	public String edicao(@PathVariable Long id, Model model) {
		Anuncio anuncio = anuncioService.buscarPorId(id);
		model.addAttribute(ATRIBUTO_ANUNCIO, anuncio);
		if (anuncio != null) {
			model.addAttribute(ATRIBUTO_FORMULARIO, AnuncioEdicaoForm.de(anuncio));
		}
		return PAGINA_EDICAO;
	}

	@PostMapping("/anuncios")
	public String criar(@Valid @ModelAttribute(ATRIBUTO_ANUNCIO) AnuncioCadastroForm formulario,
			BindingResult validacao, Model model) {
		boolean apiIndisponivel = false;
		if (!validacao.hasErrors()) {
			try {
				if (anuncioService.criar(formulario) != null) {
					return REDIRECIONAR_LISTAGEM;
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
	public String editar(@PathVariable Long id, @Valid @ModelAttribute(ATRIBUTO_FORMULARIO) AnuncioEdicaoForm formulario,
			BindingResult validacao, Model model, RedirectAttributes redirectAttributes) {
		if (validacao.hasErrors()) {
			model.addAttribute(ATRIBUTO_ANUNCIO, anuncioService.buscarPorId(id));
			return PAGINA_EDICAO;
		}

		if (anuncioService.atualizar(id, formulario) != null) {
			redirectAttributes.addFlashAttribute(ATRIBUTO_MENSAGEM, "Alterações salvas com sucesso.");
		}
		return redirecionarEdicao(id);
	}

	@PostMapping("/anuncios/{id}/status")
	public String alterarStatus(@PathVariable Long id, @Valid @ModelAttribute("statusForm") AlteracaoStatusForm statusForm,
			BindingResult validacao, Model model, RedirectAttributes redirectAttributes) {
		Anuncio anuncio = anuncioService.buscarPorId(id);
		if (anuncio == null) {
			return REDIRECIONAR_LISTAGEM;
		}

		if (!validacao.hasErrors()) {
			if (anuncioService.alterarStatus(id, statusForm.getStatus())) {
				redirectAttributes.addFlashAttribute(ATRIBUTO_MENSAGEM, "Status atualizado com sucesso.");
				return redirecionarEdicao(id);
			}
			validacao.rejectValue("status", "status.transicaoInvalida",
					"Um anúncio vendido não pode voltar a ficar à venda nem ser reservado.");
		}

		model.addAttribute(ATRIBUTO_ANUNCIO, anuncio);
		model.addAttribute(ATRIBUTO_FORMULARIO, AnuncioEdicaoForm.de(anuncio));
		return PAGINA_EDICAO;
	}

	@PostMapping("/anuncios/{id}/excluir")
	public String excluir(@PathVariable Long id) {
		anuncioService.excluir(id);
		return REDIRECIONAR_LISTAGEM;
	}

	private String redirecionarEdicao(Long id) {
		return "redirect:/anuncios/" + id + "/editar";
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
