(function () {
  const mensagem = document.querySelector("#mensagem-edicao");
  const painel = document.querySelector("#painel-edicao");
  const previaImagem = document.querySelector("#previa-imagem");
  const previaNome = document.querySelector("#previa-nome");
  const previaDetalhes = document.querySelector("#previa-detalhes");
  const previaStatus = document.querySelector("#previa-status");

  const formulario = document.querySelector("#formulario-edicao");
  const campoPreco = document.querySelector("#preco");
  const campoCondicao = document.querySelector("#condicao");
  const campoVendedor = document.querySelector("#vendedor");
  const campoDescricao = document.querySelector("#descricao");
  const contadorDescricao = document.querySelector("#contador-descricao");

  const botaoVendida = document.querySelector("#botao-vendida");
  const botaoReservada = document.querySelector("#botao-reservada");
  const botaoExcluir = document.querySelector("#botao-excluir");

  let anuncioAtual = null;

  function atualizarContadorDescricao() {
    contadorDescricao.textContent = campoDescricao.value.length;
  }

  function preencherStatus() {
    previaStatus.innerHTML = "";
    previaStatus.appendChild(UI.criarBadgeStatus(anuncioAtual.status));
    botaoVendida.classList.toggle("oculto", anuncioAtual.status === "vendida");
    botaoReservada.textContent = anuncioAtual.status === "reservada" ? "Voltar para à venda" : "Marcar como reservada";
    botaoReservada.classList.toggle("oculto", anuncioAtual.status === "vendida");
  }

  function preencherFormulario() {
    previaImagem.src = anuncioAtual.cartaImagem || UI.caminho("img/carta-placeholder.svg");
    previaImagem.alt = anuncioAtual.cartaNome;
    previaNome.textContent = anuncioAtual.cartaNome;
    previaDetalhes.textContent = anuncioAtual.cartaColecao + " · " + anuncioAtual.cartaRaridade + " · nº " + anuncioAtual.cartaNumero;

    UI.preencherCampoMoeda(campoPreco, anuncioAtual.preco);
    campoCondicao.value = anuncioAtual.condicao;
    campoVendedor.value = anuncioAtual.vendedor;
    campoDescricao.value = anuncioAtual.descricao;

    atualizarContadorDescricao();
    preencherStatus();
    painel.classList.remove("oculto");
  }

  function validarFormulario() {
    const precoValido = UI.validarCampoObrigatorio(campoPreco, function () {
      return UI.valorDoCampoMoeda(campoPreco) > 0;
    });
    const condicaoValida = UI.validarCampoObrigatorio(campoCondicao);
    return precoValido && condicaoValida;
  }

  function salvarAlteracoes(evento) {
    evento.preventDefault();
    if (!validarFormulario()) {
      UI.exibirMensagem(mensagem, "Informe um preço maior que zero e selecione a condição da carta.", "erro");
      return;
    }

    anuncioAtual = Storage.atualizarAnuncio(anuncioAtual.id, {
      preco: UI.valorDoCampoMoeda(campoPreco),
      condicao: campoCondicao.value,
      descricao: campoDescricao.value.trim()
    });

    UI.exibirMensagem(mensagem, "Alterações salvas com sucesso.", "sucesso");
  }

  function alterarStatus(novoStatus, textoConfirmacao) {
    if (textoConfirmacao && !confirm(textoConfirmacao)) {
      return;
    }
    anuncioAtual = Storage.atualizarAnuncio(anuncioAtual.id, { status: novoStatus });
    preencherStatus();
    UI.exibirMensagem(mensagem, "Status atualizado para " + UI.rotuloStatus(novoStatus) + ".", "sucesso");
  }

  function excluirAnuncio() {
    if (!confirm("Deseja realmente excluir este anúncio?")) {
      return;
    }
    Storage.removerAnuncio(anuncioAtual.id);
    window.location.href = UI.caminho("pages/listagem.html");
  }

  function iniciar() {
    UI.avisarSobreArmazenamento();
    const id = UI.obterParametroUrl("id");
    anuncioAtual = id ? Storage.obterAnuncioPorId(id) : null;

    if (!anuncioAtual) {
      UI.exibirMensagem(mensagem, "Anúncio não encontrado. Volte ao mercado e escolha um anúncio válido.", "erro");
      return;
    }

    UI.aplicarMascaraMoeda(campoPreco);
    preencherFormulario();

    formulario.addEventListener("submit", salvarAlteracoes);
    campoDescricao.addEventListener("input", atualizarContadorDescricao);
    campoPreco.addEventListener("input", function () {
      campoPreco.closest(".campo").classList.remove("campo-erro");
    });
    campoCondicao.addEventListener("change", function () {
      campoCondicao.closest(".campo").classList.remove("campo-erro");
    });
    botaoVendida.addEventListener("click", function () {
      alterarStatus("vendida", "Confirmar venda desta carta?");
    });
    botaoReservada.addEventListener("click", function () {
      alterarStatus(anuncioAtual.status === "reservada" ? "a-venda" : "reservada", "");
    });
    botaoExcluir.addEventListener("click", excluirAnuncio);
  }

  iniciar();
})();
