(function () {
  const LIMITE_ULTIMOS_ANUNCIOS = 3;

  const totalAtivos = document.querySelector("#total-ativos");
  const valorTotal = document.querySelector("#valor-total");
  const totalVendidas = document.querySelector("#total-vendidas");
  const listaUltimos = document.querySelector("#ultimos-anuncios");
  const estadoVazio = document.querySelector("#estado-vazio");
  const botaoExemplos = document.querySelector("#botao-exemplos");

  const destaqueEtiqueta = document.querySelector("#destaque-etiqueta");
  const destaqueNome = document.querySelector("#destaque-nome");
  const destaqueEtiquetas = document.querySelector("#destaque-etiquetas");
  const destaqueDescricao = document.querySelector("#destaque-descricao");
  const destaqueImagem = document.querySelector("#destaque-imagem");
  const destaqueLink = document.querySelector("#destaque-link");

  function escolherDestaque(anuncios) {
    const disponiveis = anuncios.filter(function (anuncio) {
      return anuncio.status !== "vendida";
    });
    const candidatos = disponiveis.length > 0 ? disponiveis : anuncios;
    return candidatos.reduce(function (melhor, anuncio) {
      return !melhor || Number(anuncio.preco) > Number(melhor.preco) ? anuncio : melhor;
    }, null);
  }

  function renderizarDestaque(anuncio) {
    if (!anuncio) {
      return;
    }
    destaqueEtiqueta.textContent = "Carta mais valiosa";
    destaqueNome.textContent = anuncio.cartaNome;
    destaqueDescricao.textContent = anuncio.descricao || "Carta " + anuncio.cartaRaridade + " da coleção " + anuncio.cartaColecao + ", anunciada por " + anuncio.vendedor + ".";
    destaqueImagem.src = anuncio.cartaImagem;
    destaqueImagem.alt = anuncio.cartaNome;
    destaqueLink.href = "pages/detalhes.html?id=" + encodeURIComponent(anuncio.id);

    destaqueEtiquetas.innerHTML = "";
    destaqueEtiquetas.appendChild(UI.criarEtiqueta(anuncio.cartaColecao));
    destaqueEtiquetas.appendChild(UI.criarEtiqueta(anuncio.cartaRaridade));
    destaqueEtiquetas.appendChild(UI.criarEtiqueta(UI.formatarPreco(anuncio.preco)));
    destaqueEtiquetas.appendChild(UI.criarBadgeStatus(anuncio.status));
  }

  function calcularResumo(anuncios) {
    const ativos = anuncios.filter(function (anuncio) {
      return anuncio.status !== "vendida";
    });
    const vendidas = anuncios.filter(function (anuncio) {
      return anuncio.status === "vendida";
    });
    const soma = ativos.reduce(function (acumulado, anuncio) {
      return acumulado + Number(anuncio.preco || 0);
    }, 0);
    return { quantidadeAtivos: ativos.length, quantidadeVendidas: vendidas.length, soma };
  }

  function renderizarPainel() {
    const anuncios = Storage.listarAnuncios();
    const resumo = calcularResumo(anuncios);

    totalAtivos.textContent = resumo.quantidadeAtivos;
    valorTotal.textContent = UI.formatarPreco(resumo.soma);
    totalVendidas.textContent = resumo.quantidadeVendidas;

    renderizarDestaque(escolherDestaque(anuncios));

    listaUltimos.innerHTML = "";
    anuncios.slice(0, LIMITE_ULTIMOS_ANUNCIOS).forEach(function (anuncio) {
      listaUltimos.appendChild(UI.criarCartaAnuncio(anuncio));
    });

    estadoVazio.classList.toggle("oculto", anuncios.length > 0);
    listaUltimos.classList.toggle("oculto", anuncios.length === 0);
  }

  botaoExemplos.addEventListener("click", function () {
    Storage.semearExemplos();
    renderizarPainel();
  });

  UI.avisarSobreArmazenamento();
  Storage.semearAnuncioPadrao();
  renderizarPainel();
})();
