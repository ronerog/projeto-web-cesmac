(function () {
  const ITENS_POR_PAGINA = 9;

  const grade = document.querySelector("#grade-anuncios");
  const chipsStatus = document.querySelector("#chips-status");
  const filtroRaridade = document.querySelector("#filtro-raridade");
  const filtroNome = document.querySelector("#filtro-nome");
  const formularioFiltros = document.querySelector("#formulario-filtros");
  const contadorResultados = document.querySelector("#contador-resultados");
  const estadoVazio = document.querySelector("#estado-vazio");
  const textoEstadoVazio = document.querySelector("#texto-estado-vazio");
  const paginacao = document.querySelector("#paginacao");
  const indicadorPagina = document.querySelector("#indicador-pagina");
  const paginaAnterior = document.querySelector("#pagina-anterior");
  const paginaProxima = document.querySelector("#pagina-proxima");

  Storage.semearAnuncioPadrao();
  const anuncios = Storage.listarAnuncios();
  let statusSelecionado = "todos";
  let paginaAtual = 1;

  function preencherFiltroRaridade() {
    const raridades = [];
    anuncios.forEach(function (anuncio) {
      if (!raridades.includes(anuncio.cartaRaridade)) {
        raridades.push(anuncio.cartaRaridade);
      }
    });
    raridades.sort().forEach(function (raridade) {
      const opcao = document.createElement("option");
      opcao.value = raridade;
      opcao.textContent = raridade;
      filtroRaridade.appendChild(opcao);
    });
  }

  function filtrarAnuncios() {
    const raridade = filtroRaridade.value;
    const nome = filtroNome.value.trim().toLowerCase();
    return anuncios.filter(function (anuncio) {
      const combinaStatus = statusSelecionado === "todos" || anuncio.status === statusSelecionado;
      const combinaRaridade = raridade === "todas" || anuncio.cartaRaridade === raridade;
      const combinaNome = nome === "" || anuncio.cartaNome.toLowerCase().includes(nome);
      return combinaStatus && combinaRaridade && combinaNome;
    });
  }

  function renderizarGrade() {
    const filtrados = filtrarAnuncios();
    const totalPaginas = Math.max(1, Math.ceil(filtrados.length / ITENS_POR_PAGINA));
    paginaAtual = Math.min(paginaAtual, totalPaginas);
    const inicio = (paginaAtual - 1) * ITENS_POR_PAGINA;

    grade.innerHTML = "";
    filtrados.slice(inicio, inicio + ITENS_POR_PAGINA).forEach(function (anuncio) {
      grade.appendChild(UI.criarCartaAnuncio(anuncio));
    });

    contadorResultados.textContent = filtrados.length === 1 ? "1 anúncio" : filtrados.length + " anúncios";
    textoEstadoVazio.textContent = anuncios.length === 0
      ? "Nenhum anúncio cadastrado ainda."
      : "Nenhum anúncio corresponde aos filtros selecionados.";
    estadoVazio.classList.toggle("oculto", filtrados.length > 0);
    paginacao.classList.toggle("oculto", totalPaginas === 1);
    indicadorPagina.textContent = paginaAtual + " / " + totalPaginas;
    paginaAnterior.disabled = paginaAtual === 1;
    paginaProxima.disabled = paginaAtual === totalPaginas;
  }

  function selecionarStatus(evento) {
    const chip = evento.target.closest(".chip");
    if (!chip) {
      return;
    }
    chipsStatus.querySelectorAll(".chip").forEach(function (item) {
      item.classList.toggle("ativo", item === chip);
    });
    statusSelecionado = chip.dataset.status;
    paginaAtual = 1;
    renderizarGrade();
  }

  function trocarPagina(passo) {
    paginaAtual += passo;
    renderizarGrade();
    window.scrollTo({ top: 0, behavior: "smooth" });
  }

  chipsStatus.addEventListener("click", selecionarStatus);
  filtroRaridade.addEventListener("change", function () {
    paginaAtual = 1;
    renderizarGrade();
  });
  filtroNome.addEventListener("input", function () {
    paginaAtual = 1;
    renderizarGrade();
  });
  formularioFiltros.addEventListener("submit", function (evento) {
    evento.preventDefault();
    renderizarGrade();
  });
  paginaAnterior.addEventListener("click", function () {
    trocarPagina(-1);
  });
  paginaProxima.addEventListener("click", function () {
    trocarPagina(1);
  });

  UI.avisarSobreArmazenamento();
  preencherFiltroRaridade();
  renderizarGrade();
})();
