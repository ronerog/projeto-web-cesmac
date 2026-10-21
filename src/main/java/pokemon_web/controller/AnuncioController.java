package pokemon_web.controller;

import java.util.stream.Collectors;

import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.validation.Valid;
import pokemon_web.model.AlteracaoStatusForm;
import pokemon_web.model.AnuncioCadastroForm;
import pokemon_web.model.AnuncioEdicaoForm;
import pokemon_web.model.StatusAnuncio;
import pokemon_web.service.AnuncioService;
import pokemon_web.service.ApiCartasIndisponivelException;

@Controller
public class AnuncioController {

	private static final String REDIRECIONAR_CADASTRO = "redirect:/pages/cadastro.html";
	private static final String REDIRECIONAR_EDICAO = "redirect:/pages/edicao.html?id=";

	private final AnuncioService anuncioService;

	public AnuncioController(AnuncioService anuncioService) {
		this.anuncioService = anuncioService;
	}

	@PostMapping("/anuncios")
	public String criar(@Valid @ModelAttribute AnuncioCadastroForm formulario, BindingResult validacao,
			RedirectAttributes redirectAttributes) {
		if (validacao.hasErrors()) {
			redirectAttributes.addFlashAttribute("erro", mensagemDeErro(validacao));
			return REDIRECIONAR_CADASTRO;
		}

		try {
			if (anuncioService.criar(formulario) == null) {
				redirectAttributes.addFlashAttribute("erro",
						"A carta selecionada não foi encontrada na Pokémon TCG API. Busque e selecione a carta novamente.");
				return REDIRECIONAR_CADASTRO;
			}
		} catch (ApiCartasIndisponivelException excecao) {
			redirectAttributes.addFlashAttribute("erro",
					"A Pokémon TCG API não respondeu ao confirmar a carta. Tente publicar novamente em alguns segundos.");
			return REDIRECIONAR_CADASTRO;
		}
		return "redirect:/pages/listagem.html";
	}

	@PostMapping("/anuncios/{id}/editar")
	public String editar(@PathVariable Long id, @Valid @ModelAttribute AnuncioEdicaoForm formulario,
			BindingResult validacao, RedirectAttributes redirectAttributes) {
		if (validacao.hasErrors()) {
			redirectAttributes.addFlashAttribute("erro", mensagemDeErro(validacao));
			return REDIRECIONAR_EDICAO + id;
		}

		anuncioService.atualizar(id, formulario);
		redirectAttributes.addFlashAttribute("mensagem", "Alterações salvas com sucesso.");
		return REDIRECIONAR_EDICAO + id;
	}

	@PostMapping("/anuncios/{id}/status")
	public String alterarStatus(@PathVariable Long id, @Valid @ModelAttribute AlteracaoStatusForm formulario,
			BindingResult validacao, RedirectAttributes redirectAttributes) {
		if (validacao.hasErrors()) {
			redirectAttributes.addFlashAttribute("erro", mensagemDeErro(validacao));
			return REDIRECIONAR_EDICAO + id;
		}

		anuncioService.atualizarStatus(id, StatusAnuncio.porCodigo(formulario.getStatus()).orElseThrow());
		redirectAttributes.addFlashAttribute("mensagem", "Status atualizado com sucesso.");
		return REDIRECIONAR_EDICAO + id;
	}

	@PostMapping("/anuncios/{id}/excluir")
	public String excluir(@PathVariable Long id) {
		anuncioService.excluir(id);
		return "redirect:/pages/listagem.html";
	}

	private String mensagemDeErro(BindingResult validacao) {
		return validacao.getFieldErrors().stream()
				.map(this::mensagemDoCampo)
				.distinct()
				.collect(Collectors.joining(" "));
	}

	private String mensagemDoCampo(FieldError erro) {
		if (!erro.isBindingFailure()) {
			return erro.getDefaultMessage();
		}
		return "preco".equals(erro.getField()) ? "Informe um preço válido." : "Valor inválido no campo " + erro.getField() + ".";
	}

}
