ALTER TABLE anuncios DROP CONSTRAINT ck_anuncios_status;
ALTER TABLE anuncios DROP CONSTRAINT ck_anuncios_condicao;

UPDATE anuncios SET status = CASE status
    WHEN 'a-venda'   THEN 'A_VENDA'
    WHEN 'reservada' THEN 'RESERVADA'
    WHEN 'vendida'   THEN 'VENDIDA'
END;

UPDATE anuncios SET condicao = CASE condicao
    WHEN 'Nova/Lacrada' THEN 'NOVA_LACRADA'
    WHEN 'Excelente'    THEN 'EXCELENTE'
    WHEN 'Boa'          THEN 'BOA'
    WHEN 'Jogada'       THEN 'JOGADA'
    WHEN 'Danificada'   THEN 'DANIFICADA'
END;

ALTER TABLE anuncios ADD CONSTRAINT ck_anuncios_status
    CHECK (status IN ('A_VENDA', 'RESERVADA', 'VENDIDA'));

ALTER TABLE anuncios ADD CONSTRAINT ck_anuncios_condicao
    CHECK (condicao IN ('NOVA_LACRADA', 'EXCELENTE', 'BOA', 'JOGADA', 'DANIFICADA'));
