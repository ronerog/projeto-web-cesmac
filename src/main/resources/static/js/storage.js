const Storage = (function () {
  const CHAVE_ANUNCIOS = "poketplace-anuncios";
  const CHAVE_TESTE = "poketplace-teste-armazenamento";
  const CHAVE_SEMEADO = "poketplace-exemplos-semeados";
  const STATUS_PADRAO = "a-venda";

  const CAMPOS_ANTIGOS = {
    cardApiId: "cartaId",
    cardName: "cartaNome",
    cardImage: "cartaImagem",
    cardSet: "cartaColecao",
    cardRarity: "cartaRaridade",
    cardNumber: "cartaNumero"
  };

  const CHAVES_ANTIGAS = ["pokemon-tcg-anuncios"];

  const ANUNCIOS_EXEMPLO = [
    {
      cartaId: "base1-4",
      cartaNome: "Charizard",
      cartaImagem: "https://images.pokemontcg.io/base1/4_hires.png",
      cartaImagemPequena: "https://images.pokemontcg.io/base1/4.png",
      cartaColecao: "Base",
      cartaRaridade: "Rare Holo",
      cartaNumero: "4/102",
      preco: 2500,
      condicao: "Excelente",
      descricao: "Carta original de 1999, sem dobras, levíssimo desgaste na borda inferior.",
      vendedor: "Rônero",
      status: "a-venda"
    },
    {
      cartaId: "base1-58",
      cartaNome: "Pikachu",
      cartaImagem: "https://images.pokemontcg.io/base1/58_hires.png",
      cartaImagemPequena: "https://images.pokemontcg.io/base1/58.png",
      cartaColecao: "Base",
      cartaRaridade: "Common",
      cartaNumero: "58/102",
      preco: 89.9,
      condicao: "Boa",
      descricao: "Carta bem conservada, guardada em sleeve desde que foi aberta.",
      vendedor: "Marina Alves",
      status: "reservada"
    },
    {
      cartaId: "base1-2",
      cartaNome: "Blastoise",
      cartaImagem: "https://images.pokemontcg.io/base1/2_hires.png",
      cartaImagemPequena: "https://images.pokemontcg.io/base1/2.png",
      cartaColecao: "Base",
      cartaRaridade: "Rare Holo",
      cartaNumero: "2/102",
      preco: 1750,
      condicao: "Nova/Lacrada",
      descricao: "Carta recém-aberta de booster, encaminhada direto para toploader.",
      vendedor: "Diego Ramos",
      status: "vendida"
    }
  ];

  function armazenamentoDisponivel() {
    try {
      localStorage.setItem(CHAVE_TESTE, "1");
      localStorage.removeItem(CHAVE_TESTE);
      return true;
    } catch (erro) {
      return false;
    }
  }

  function armazenamentoIsoladoPorArquivo() {
    return window.location.protocol === "file:" && navigator.userAgent.includes("Firefox");
  }

  function diagnosticar() {
    return {
      disponivel: armazenamentoDisponivel(),
      isoladoPorArquivo: armazenamentoIsoladoPorArquivo()
    };
  }

  function normalizarAnuncio(anuncio) {
    const convertido = Object.assign({}, anuncio);
    Object.keys(CAMPOS_ANTIGOS).forEach(function (campoAntigo) {
      if (convertido[campoAntigo] !== undefined) {
        convertido[CAMPOS_ANTIGOS[campoAntigo]] = convertido[campoAntigo];
        delete convertido[campoAntigo];
      }
    });
    if (!convertido.cartaImagemPequena) {
      convertido.cartaImagemPequena = (convertido.cartaImagem || "").replace("_hires.png", ".png");
    }
    return convertido;
  }

  function lerChave(chave) {
    try {
      const conteudo = JSON.parse(localStorage.getItem(chave));
      return Array.isArray(conteudo) ? conteudo : [];
    } catch (erro) {
      return [];
    }
  }

  function lerBase() {
    const atuais = lerChave(CHAVE_ANUNCIOS);
    const antigos = CHAVES_ANTIGAS.reduce(function (acumulado, chave) {
      return acumulado.concat(lerChave(chave));
    }, []);
    return atuais.concat(antigos).map(normalizarAnuncio);
  }

  function gravarBase(anuncios) {
    try {
      localStorage.setItem(CHAVE_ANUNCIOS, JSON.stringify(anuncios));
      CHAVES_ANTIGAS.forEach(function (chave) {
        localStorage.removeItem(chave);
      });
      return true;
    } catch (erro) {
      return false;
    }
  }

  function gerarId() {
    return "anuncio-" + Date.now() + "-" + Math.floor(Math.random() * 1000);
  }

  function listarAnuncios() {
    return lerBase().sort(function (primeiro, segundo) {
      return new Date(segundo.criadoEm) - new Date(primeiro.criadoEm);
    });
  }

  function salvarAnuncio(dados) {
    const anuncio = {
      id: gerarId(),
      cartaId: dados.cartaId,
      cartaNome: dados.cartaNome,
      cartaImagem: dados.cartaImagem,
      cartaImagemPequena: dados.cartaImagemPequena || (dados.cartaImagem || "").replace("_hires.png", ".png"),
      cartaColecao: dados.cartaColecao,
      cartaRaridade: dados.cartaRaridade,
      cartaNumero: dados.cartaNumero,
      preco: Number(dados.preco),
      condicao: dados.condicao,
      descricao: dados.descricao || "",
      vendedor: dados.vendedor || "Anônimo",
      status: dados.status || STATUS_PADRAO,
      criadoEm: new Date().toISOString()
    };
    const anuncios = lerBase();
    anuncios.push(anuncio);
    return gravarBase(anuncios) ? anuncio : null;
  }

  function obterAnuncioPorId(id) {
    return lerBase().find(function (anuncio) {
      return anuncio.id === id;
    }) || null;
  }

  function atualizarAnuncio(id, alteracoes) {
    const anuncios = lerBase();
    const posicao = anuncios.findIndex(function (anuncio) {
      return anuncio.id === id;
    });
    if (posicao === -1) {
      return null;
    }
    const atualizado = Object.assign({}, anuncios[posicao], alteracoes, { id: id });
    if (alteracoes.preco !== undefined) {
      atualizado.preco = Number(alteracoes.preco);
    }
    anuncios[posicao] = atualizado;
    return gravarBase(anuncios) ? atualizado : null;
  }

  function removerAnuncio(id) {
    const anuncios = lerBase();
    const restantes = anuncios.filter(function (anuncio) {
      return anuncio.id !== id;
    });
    if (restantes.length === anuncios.length) {
      return false;
    }
    return gravarBase(restantes);
  }

  function marcarComoSemeado() {
    try {
      localStorage.setItem(CHAVE_SEMEADO, "1");
    } catch (erro) {
      return;
    }
  }

  function jaFoiSemeado() {
    try {
      return localStorage.getItem(CHAVE_SEMEADO) === "1";
    } catch (erro) {
      return true;
    }
  }

  function semearExemplos() {
    marcarComoSemeado();
    return ANUNCIOS_EXEMPLO.map(salvarAnuncio);
  }

  function semearAnuncioPadrao() {
    if (jaFoiSemeado() || lerBase().length > 0) {
      return null;
    }
    marcarComoSemeado();
    return salvarAnuncio(ANUNCIOS_EXEMPLO[0]);
  }

  return {
    listarAnuncios,
    salvarAnuncio,
    obterAnuncioPorId,
    atualizarAnuncio,
    removerAnuncio,
    semearExemplos,
    semearAnuncioPadrao,
    diagnosticar
  };
})();
