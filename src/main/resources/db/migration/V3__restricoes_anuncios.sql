ALTER TABLE anuncios ADD CONSTRAINT ck_anuncios_preco_positivo CHECK (preco > 0);

ALTER TABLE anuncios ADD CONSTRAINT ck_anuncios_condicao
    CHECK (condicao IN ('Nova/Lacrada', 'Excelente', 'Boa', 'Jogada', 'Danificada'));

ALTER TABLE anuncios ADD CONSTRAINT ck_anuncios_status
    CHECK (status IN ('a-venda', 'reservada', 'vendida'));

ALTER TABLE anuncios ALTER COLUMN condicao SET NOT NULL;
