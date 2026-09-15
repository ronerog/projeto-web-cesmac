(function () {
  const container = document.querySelector("#detalhe-anuncio");
  const mensagem = document.querySelector("#mensagem-detalhes");

  function criarItemDado(rotulo, valor) {
    const bloco = document.createElement("div");
    const titulo = document.createElement("dt");
    titulo.textContent = rotulo;
    const descricao = document.createElement("dd");
    descricao.textContent = valor;
    bloco.appendChild(titulo);
    bloco.appendChild(descricao);
    return bloco;
  }

  function renderizarAnuncio(anuncio) {
    const areaImagem = document.createElement("div");
    areaImagem.className = "detalhe-imagem";
    areaImagem.appendChild(UI.criarImagemCarta(anuncio.cartaImagem, anuncio.cartaNome));

    const areaDados = document.createElement("div");
    areaDados.className = "detalhe-dados";

    const nome = document.createElement("h2");
    nome.textContent = anuncio.cartaNome;

    const subtitulo = document.createElement("p");
    subtitulo.className = "campo-ajuda";
    subtitulo.textContent = anuncio.cartaColecao + " · nº " + anuncio.cartaNumero;

    const preco = document.createElement("p");
    preco.className = "detalhe-preco";
    preco.textContent = UI.formatarPreco(anuncio.preco);

    const dados = document.createElement("dl");
    dados.className = "lista-dados";
    dados.appendChild(criarItemDado("Raridade", anuncio.cartaRaridade));
    dados.appendChild(criarItemDado("Condição", anuncio.condicao));
    dados.appendChild(criarItemDado("Vendedor", anuncio.vendedor));
    dados.appendChild(criarItemDado("Anunciado em", UI.formatarData(anuncio.criadoEm)));

    const descricao = document.createElement("p");
    descricao.className = "descricao";
    descricao.textContent = anuncio.descricao || "O vendedor não escreveu uma descrição para esta carta.";

    const acoes = document.createElement("div");
    acoes.className = "grupo-botoes";
    const linkEdicao = document.createElement("a");
    linkEdicao.className = "botao";
    linkEdicao.href = UI.caminho("pages/edicao.html?id=" + encodeURIComponent(anuncio.id));
    linkEdicao.textContent = "Editar anúncio";
    const linkVoltar = document.createElement("a");
    linkVoltar.className = "botao botao-secundario";
    linkVoltar.href = UI.caminho("pages/listagem.html");
    linkVoltar.textContent = "Ver outros anúncios";
    acoes.appendChild(linkEdicao);
    acoes.appendChild(linkVoltar);

    const etiquetas = document.createElement("div");
    etiquetas.className = "etiquetas";
    etiquetas.appendChild(UI.criarEtiqueta(anuncio.cartaColecao));
    etiquetas.appendChild(UI.criarEtiqueta(anuncio.cartaRaridade));
    etiquetas.appendChild(UI.criarBadgeStatus(anuncio.status));

    areaDados.appendChild(etiquetas);
    areaDados.appendChild(nome);
    areaDados.appendChild(subtitulo);
    areaDados.appendChild(preco);
    areaDados.appendChild(dados);
    areaDados.appendChild(descricao);
    areaDados.appendChild(acoes);

    container.appendChild(areaImagem);
    container.appendChild(areaDados);
  }

  function iniciar() {
    UI.avisarSobreArmazenamento();
    const id = UI.obterParametroUrl("id");
    const anuncio = id ? Storage.obterAnuncioPorId(id) : null;

    if (!anuncio) {
      UI.exibirMensagem(mensagem, "Anúncio não encontrado. Ele pode ter sido excluído ou o link está incorreto.", "erro");
      return;
    }
    renderizarAnuncio(anuncio);
  }

  iniciar();
})();
