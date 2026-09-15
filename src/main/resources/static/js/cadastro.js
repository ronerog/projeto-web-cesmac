(function () {
  const formularioBusca = document.querySelector("#formulario-busca");
  const termoBusca = document.querySelector("#termo-busca");
  const botaoBuscar = document.querySelector("#botao-buscar");
  const mensagemBusca = document.querySelector("#mensagem-busca");
  const resultadosBusca = document.querySelector("#resultados-busca");

  const previaCarta = document.querySelector("#previa-carta");
  const previaImagem = document.querySelector("#previa-imagem");
  const previaNome = document.querySelector("#previa-nome");
  const previaDetalhes = document.querySelector("#previa-detalhes");

  const formularioAnuncio = document.querySelector("#formulario-anuncio");
  const mensagemFormulario = document.querySelector("#mensagem-formulario");
  const campoCartaId = document.querySelector("#carta-id");
  const campoCartaImagem = document.querySelector("#carta-imagem");
  const campoCartaImagemPequena = document.querySelector("#carta-imagem-pequena");
  const campoCartaNumero = document.querySelector("#carta-numero");
  const campoCartaNome = document.querySelector("#carta-nome");
  const campoCartaColecao = document.querySelector("#carta-colecao");
  const campoCartaRaridade = document.querySelector("#carta-raridade");
  const campoPreco = document.querySelector("#preco");
  const campoCondicao = document.querySelector("#condicao");
  const campoVendedor = document.querySelector("#vendedor");
  const campoDescricao = document.querySelector("#descricao");
  const contadorDescricao = document.querySelector("#contador-descricao");

  function selecionarCarta(carta, elementoClicado) {
    campoCartaId.value = carta.id;
    campoCartaImagem.value = carta.imagemGrande;
    campoCartaImagemPequena.value = carta.imagemPequena;
    campoCartaNumero.value = carta.numero;
    campoCartaNome.value = carta.nome;
    campoCartaColecao.value = carta.colecao;
    campoCartaRaridade.value = carta.raridade;

    previaImagem.src = carta.imagemGrande || UI.caminho("img/carta-placeholder.svg");
    previaImagem.alt = carta.nome;
    previaNome.textContent = carta.nome;
    previaDetalhes.textContent = carta.colecao + " · " + carta.raridade + " · nº " + carta.numero;
    previaCarta.classList.remove("oculto");

    resultadosBusca.querySelectorAll(".mini-carta").forEach(function (item) {
      item.classList.remove("selecionada");
    });
    elementoClicado.classList.add("selecionada");

    campoCartaNome.closest(".campo").classList.remove("campo-erro");
    UI.exibirMensagem(mensagemFormulario, "", "");
  }

  function criarMiniCarta(carta) {
    const item = document.createElement("button");
    item.type = "button";
    item.className = "mini-carta";
    item.appendChild(UI.criarImagemCarta(carta.imagemPequena || carta.imagemGrande, carta.nome));

    const nome = document.createElement("span");
    nome.className = "mini-carta-nome";
    nome.textContent = carta.nome;

    const colecao = document.createElement("span");
    colecao.className = "mini-carta-set";
    colecao.textContent = carta.colecao + " · " + carta.raridade;

    item.appendChild(nome);
    item.appendChild(colecao);
    item.addEventListener("click", function () {
      selecionarCarta(carta, item);
    });
    return item;
  }

  function renderizarResultados(cartas) {
    resultadosBusca.innerHTML = "";
    cartas.forEach(function (carta) {
      resultadosBusca.appendChild(criarMiniCarta(carta));
    });
  }

  async function buscarCartas(evento) {
    evento.preventDefault();
    const termo = termoBusca.value.trim();
    if (!UI.validarCampoObrigatorio(termoBusca)) {
      UI.exibirMensagem(mensagemBusca, "Digite o nome de uma carta para buscar.", "erro");
      return;
    }

    botaoBuscar.disabled = true;
    botaoBuscar.textContent = "Buscando...";
    UI.exibirMensagem(mensagemBusca, "Consultando a Pokémon TCG API...", "");

    const cartas = await API.buscarCartasPorNome(termo);
    renderizarResultados(cartas);

    if (cartas.length === 0) {
      UI.exibirMensagem(mensagemBusca, 'Nenhuma carta encontrada para "' + termo + '".', "alerta");
    } else if (API.usouFallback()) {
      UI.exibirMensagem(mensagemBusca, "A Pokémon TCG API não respondeu. Exibindo " + cartas.length + " carta(s) da base local de exemplo — tente buscar de novo em alguns segundos para ver o catálogo completo.", "alerta");
    } else {
      UI.exibirMensagem(mensagemBusca, cartas.length + " carta(s) encontrada(s). Clique em uma para selecionar.", "sucesso");
    }

    botaoBuscar.disabled = false;
    botaoBuscar.textContent = "Buscar carta";
  }

  function validarFormulario() {
    const cartaSelecionada = UI.validarCampoObrigatorio(campoCartaNome);
    const precoValido = UI.validarCampoObrigatorio(campoPreco, function () {
      return UI.valorDoCampoMoeda(campoPreco) > 0;
    });
    const condicaoValida = UI.validarCampoObrigatorio(campoCondicao);
    return cartaSelecionada && precoValido && condicaoValida;
  }

  function publicarAnuncio(evento) {
    evento.preventDefault();
    if (!validarFormulario()) {
      UI.exibirMensagem(mensagemFormulario, "Revise os campos destacados: escolha uma carta, informe um preço maior que zero e selecione a condição.", "erro");
      return;
    }

    const anuncio = Storage.salvarAnuncio({
      cartaId: campoCartaId.value,
      cartaNome: campoCartaNome.value,
      cartaImagem: campoCartaImagem.value,
      cartaImagemPequena: campoCartaImagemPequena.value,
      cartaColecao: campoCartaColecao.value,
      cartaRaridade: campoCartaRaridade.value,
      cartaNumero: campoCartaNumero.value,
      preco: UI.valorDoCampoMoeda(campoPreco),
      condicao: campoCondicao.value,
      descricao: campoDescricao.value.trim(),
      vendedor: campoVendedor.value.trim()
    });

    if (!anuncio) {
      UI.exibirMensagem(mensagemFormulario, "Não foi possível salvar o anúncio: este navegador está bloqueando o armazenamento local da página.", "erro");
      return;
    }

    window.location.href = UI.caminho("pages/listagem.html");
  }

  function atualizarContadorDescricao() {
    contadorDescricao.textContent = campoDescricao.value.length;
  }

  formularioBusca.addEventListener("submit", buscarCartas);
  formularioAnuncio.addEventListener("submit", publicarAnuncio);
  campoDescricao.addEventListener("input", atualizarContadorDescricao);
  campoPreco.addEventListener("input", function () {
    campoPreco.closest(".campo").classList.remove("campo-erro");
  });
  campoCondicao.addEventListener("change", function () {
    campoCondicao.closest(".campo").classList.remove("campo-erro");
  });

  UI.aplicarMascaraMoeda(campoPreco);
  UI.avisarSobreArmazenamento();
  atualizarContadorDescricao();
})();
