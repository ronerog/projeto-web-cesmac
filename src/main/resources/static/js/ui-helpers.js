const UI = (function () {
  const LIMITE_DIGITOS_PRECO = 11;

  function caminho(destino) {
    return "/" + destino;
  }

  const IMAGEM_PLACEHOLDER = caminho("img/carta-placeholder.svg");

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

  function limparErroDoCampo(campo) {
    const grupo = campo.closest(".campo");
    if (!grupo) {
      return;
    }
    grupo.classList.remove("campo-erro");
    grupo.querySelectorAll(".campo-mensagem-erro").forEach(function (mensagem) {
      mensagem.remove();
    });
  }

  function exibirMensagem(elemento, texto, tipo) {
    elemento.textContent = texto;
    elemento.className = "mensagem";
    if (tipo) {
      elemento.classList.add("mensagem-" + tipo);
    }
    elemento.classList.toggle("oculto", !texto);
  }

  return {
    caminho,
    aplicarMascaraMoeda,
    valorDoCampoMoeda,
    preencherCampoMoeda,
    criarImagemCarta,
    validarCampoObrigatorio,
    limparErroDoCampo,
    exibirMensagem
  };
})();
