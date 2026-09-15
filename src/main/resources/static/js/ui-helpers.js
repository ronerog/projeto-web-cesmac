const UI = (function () {
  const PASTA_PAGINAS = "pages/";
  const DOCUMENTO_EM_PAGINAS = window.location.pathname.split("/").slice(-2, -1)[0] === "pages";
  const LIMITE_DIGITOS_PRECO = 11;

  function caminho(destino) {
    if (!DOCUMENTO_EM_PAGINAS) {
      return destino;
    }
    return destino.startsWith(PASTA_PAGINAS) ? destino.slice(PASTA_PAGINAS.length) : "../" + destino;
  }

  const IMAGEM_PLACEHOLDER = caminho("img/carta-placeholder.svg");

  const ROTULOS_STATUS = {
    "a-venda": "À venda",
    reservada: "Reservada",
    vendida: "Vendida"
  };

  function formatarPreco(valor) {
    const numero = Number(valor);
    if (isNaN(numero)) {
      return "R$ 0,00";
    }
    return numero.toLocaleString("pt-BR", { style: "currency", currency: "BRL" });
  }

  function formatarCentavos(digitos) {
    return (Number(digitos) / 100).toLocaleString("pt-BR", { minimumFractionDigits: 2, maximumFractionDigits: 2 });
  }

  function apenasDigitos(texto) {
    return texto.replace(/\D/g, "");
  }

  function aplicarMascaraMoeda(campo) {
    campo.addEventListener("input", function () {
      const digitos = apenasDigitos(campo.value).slice(0, LIMITE_DIGITOS_PRECO);
      campo.value = digitos ? formatarCentavos(digitos) : "";
    });
  }

  function valorDoCampoMoeda(campo) {
    const digitos = apenasDigitos(campo.value);
    return digitos ? Number(digitos) / 100 : 0;
  }

  function preencherCampoMoeda(campo, valor) {
    const centavos = Math.round(Number(valor || 0) * 100);
    campo.value = centavos > 0 ? formatarCentavos(String(centavos)) : "";
  }

  function rotuloStatus(status) {
    return ROTULOS_STATUS[status] || status;
  }

  function criarBadgeStatus(status) {
    const badge = document.createElement("span");
    badge.className = "badge badge-" + status;
    badge.textContent = rotuloStatus(status);
    return badge;
  }

  function criarImagemCarta(endereco, descricao) {
    const imagem = document.createElement("img");
    imagem.src = endereco || IMAGEM_PLACEHOLDER;
    imagem.alt = descricao;
    imagem.loading = "lazy";
    imagem.addEventListener("error", function () {
      imagem.src = IMAGEM_PLACEHOLDER;
    });
    return imagem;
  }

  function validarCampoObrigatorio(campo, validador) {
    const grupo = campo.closest(".campo") || campo.parentElement;
    const valor = campo.value.trim();
    const valido = typeof validador === "function" ? validador(valor) : valor !== "";
    grupo.classList.toggle("campo-erro", !valido);
    return valido;
  }

  function exibirMensagem(elemento, texto, tipo) {
    elemento.textContent = texto;
    elemento.className = "mensagem";
    if (tipo) {
      elemento.classList.add("mensagem-" + tipo);
    }
    elemento.classList.toggle("oculto", !texto);
  }

  function obterParametroUrl(nome) {
    return new URLSearchParams(window.location.search).get(nome);
  }

  function formatarData(dataIso) {
    const data = new Date(dataIso);
    if (isNaN(data.getTime())) {
      return "-";
    }
    return data.toLocaleDateString("pt-BR", { day: "2-digit", month: "2-digit", year: "numeric" });
  }

  function criarEstatistica(rotulo, valor) {
    const bloco = document.createElement("div");
    bloco.className = "estatistica";
    const conteudo = document.createElement("span");
    conteudo.className = "estatistica-valor";
    conteudo.textContent = valor;
    const titulo = document.createElement("span");
    titulo.className = "estatistica-rotulo";
    titulo.textContent = rotulo;
    bloco.appendChild(conteudo);
    bloco.appendChild(titulo);
    return bloco;
  }

  function criarEtiqueta(texto) {
    const etiqueta = document.createElement("span");
    etiqueta.className = "etiqueta";
    etiqueta.textContent = texto;
    return etiqueta;
  }

  function criarCartaAnuncio(anuncio) {
    const carta = document.createElement("article");
    carta.className = "carta-anuncio";

    const areaImagem = document.createElement("div");
    areaImagem.className = "carta-imagem";
    areaImagem.appendChild(criarImagemCarta(anuncio.cartaImagemPequena || anuncio.cartaImagem, anuncio.cartaNome));

    const nome = document.createElement("h3");
    nome.className = "carta-nome";
    const link = document.createElement("a");
    link.href = caminho("pages/detalhes.html?id=" + encodeURIComponent(anuncio.id));
    link.textContent = "• " + anuncio.cartaNome + " •";
    nome.appendChild(link);

    const etiquetas = document.createElement("div");
    etiquetas.className = "etiquetas";
    etiquetas.appendChild(criarEtiqueta(anuncio.cartaRaridade));
    etiquetas.appendChild(criarBadgeStatus(anuncio.status));

    const estatisticas = document.createElement("div");
    estatisticas.className = "carta-estatisticas";
    estatisticas.appendChild(criarEstatistica("Preço", formatarPreco(anuncio.preco)));
    estatisticas.appendChild(criarEstatistica("Condição", anuncio.condicao));

    const acao = document.createElement("a");
    acao.className = "botao botao-pequeno botao-largo";
    acao.href = caminho("pages/detalhes.html?id=" + encodeURIComponent(anuncio.id));
    acao.textContent = "Mais detalhes";

    carta.appendChild(areaImagem);
    carta.appendChild(nome);
    carta.appendChild(etiquetas);
    carta.appendChild(estatisticas);
    carta.appendChild(acao);
    return carta;
  }

  function avisarSobreArmazenamento() {
    const diagnostico = Storage.diagnosticar();
    if (diagnostico.disponivel && !diagnostico.isoladoPorArquivo) {
      return;
    }
    const aviso = document.createElement("div");
    aviso.className = "aviso-armazenamento";

    const titulo = document.createElement("strong");
    const texto = document.createElement("span");
    if (diagnostico.disponivel) {
      titulo.textContent = "Neste navegador os anúncios não passam de uma página para a outra.";
      texto.textContent = "Abrindo os arquivos direto do disco (file://), o Firefox dá um armazenamento separado para cada página. Abra o projeto pelo Chrome ou rode um servidor local na pasta do projeto.";
    } else {
      titulo.textContent = "Armazenamento local bloqueado.";
      texto.textContent = "O navegador está impedindo o uso do localStorage nesta página, então nenhum anúncio é salvo. Libere os dados de site ou abra o projeto por um servidor local.";
    }

    aviso.appendChild(titulo);
    aviso.appendChild(texto);
    const conteudo = document.querySelector(".conteudo");
    conteudo.insertBefore(aviso, conteudo.firstChild);
  }

  return {
    caminho,
    formatarPreco,
    aplicarMascaraMoeda,
    valorDoCampoMoeda,
    preencherCampoMoeda,
    criarBadgeStatus,
    criarImagemCarta,
    criarCartaAnuncio,
    criarEtiqueta,
    validarCampoObrigatorio,
    exibirMensagem,
    rotuloStatus,
    obterParametroUrl,
    formatarData,
    avisarSobreArmazenamento
  };
})();
