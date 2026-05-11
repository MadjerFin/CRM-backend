-- ============================================================
--  WTC Challenge -- Migração v2: Campos empresariais em WTC_CLIENTS
--  Execute no SQL Developer (F5)
-- ============================================================

ALTER TABLE WTC_CLIENTS ADD (
    EMPLOYEE_COUNT NUMBER(10),
    SECTOR         VARCHAR2(30),
    COMPANY_LEVEL  VARCHAR2(20),
    WEBSITE        VARCHAR2(500),
    CITY           VARCHAR2(100),
    STATE          VARCHAR2(2)
);

ALTER TABLE WTC_CLIENTS ADD (
    CONSTRAINT ck_clients_sector
        CHECK (SECTOR IN ('FINANCEIRO','INDUSTRIAL','COMERCIO','TECNOLOGIA',
                          'SAUDE','EDUCACAO','SERVICOS','AGRONEGOCIO','OUTROS')),
    CONSTRAINT ck_clients_level
        CHECK (COMPANY_LEVEL IN ('STARTUP','PEQUENA','MEDIA','GRANDE','ENTERPRISE'))
);

-- Atualizar dados existentes com valores de exemplo
UPDATE WTC_CLIENTS C SET
    EMPLOYEE_COUNT = 1200,
    SECTOR         = 'FINANCEIRO',
    COMPANY_LEVEL  = 'GRANDE',
    CITY           = 'São Paulo',
    STATE          = 'SP'
WHERE C.USER_ID = (SELECT ID FROM WTC_USERS WHERE EMAIL = 'joao@email.com');

UPDATE WTC_CLIENTS C SET
    EMPLOYEE_COUNT = 350,
    SECTOR         = 'TECNOLOGIA',
    COMPANY_LEVEL  = 'MEDIA',
    CITY           = 'Campinas',
    STATE          = 'SP'
WHERE C.USER_ID = (SELECT ID FROM WTC_USERS WHERE EMAIL = 'maria@email.com');

UPDATE WTC_CLIENTS C SET
    EMPLOYEE_COUNT = 80,
    SECTOR         = 'COMERCIO',
    COMPANY_LEVEL  = 'PEQUENA',
    CITY           = 'São Paulo',
    STATE          = 'SP'
WHERE C.USER_ID = (SELECT ID FROM WTC_USERS WHERE EMAIL = 'pedro@email.com');

UPDATE WTC_CLIENTS C SET
    EMPLOYEE_COUNT = 5000,
    SECTOR         = 'INDUSTRIAL',
    COMPANY_LEVEL  = 'ENTERPRISE',
    CITY           = 'Santos',
    STATE          = 'SP'
WHERE C.USER_ID = (SELECT ID FROM WTC_USERS WHERE EMAIL = 'lucia@email.com');

UPDATE WTC_CLIENTS C SET
    EMPLOYEE_COUNT = 12,
    SECTOR         = 'SERVICOS',
    COMPANY_LEVEL  = 'STARTUP',
    CITY           = 'São Paulo',
    STATE          = 'SP'
WHERE C.USER_ID = (SELECT ID FROM WTC_USERS WHERE EMAIL = 'bruno@email.com');

UPDATE WTC_CLIENTS C SET
    EMPLOYEE_COUNT = 45,
    SECTOR         = 'SAUDE',
    COMPANY_LEVEL  = 'PEQUENA',
    CITY           = 'Guarulhos',
    STATE          = 'SP'
WHERE C.USER_ID = (SELECT ID FROM WTC_USERS WHERE EMAIL = 'fernanda@email.com');

UPDATE WTC_CLIENTS C SET
    EMPLOYEE_COUNT = 150,
    SECTOR         = 'AGRONEGOCIO',
    COMPANY_LEVEL  = 'MEDIA',
    CITY           = 'Ribeirão Preto',
    STATE          = 'SP'
WHERE C.USER_ID = (SELECT ID FROM WTC_USERS WHERE EMAIL = 'ricardo@email.com');

UPDATE WTC_CLIENTS C SET
    EMPLOYEE_COUNT = 280,
    SECTOR         = 'EDUCACAO',
    COMPANY_LEVEL  = 'MEDIA',
    CITY           = 'São Paulo',
    STATE          = 'SP'
WHERE C.USER_ID = (SELECT ID FROM WTC_USERS WHERE EMAIL = 'patricia@email.com');

COMMIT;

-- Verificar resultado
SELECT U.NAME, C.COMPANY, C.SECTOR, C.COMPANY_LEVEL, C.EMPLOYEE_COUNT, C.CITY
FROM WTC_CLIENTS C
JOIN WTC_USERS U ON U.ID = C.USER_ID
ORDER BY C.ID;
