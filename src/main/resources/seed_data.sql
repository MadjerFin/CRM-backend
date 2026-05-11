-- ============================================================
--  WTC Challenge -- Dados de Exemplo
--  Execute no SQL Developer (F5) APÓS o schema.sql
-- ============================================================

-- ============================================================
-- LIMPEZA (ordem inversa das FKs)
-- ============================================================
DELETE FROM WTC_TASKS;
DELETE FROM WTC_AUDIT_LOGS;
DELETE FROM WTC_ANNOTATIONS;
DELETE FROM WTC_CAMPAIGN_RECIPIENTS;
DELETE FROM WTC_CAMPAIGNS;
DELETE FROM WTC_MESSAGES;
DELETE FROM WTC_CLIENTS;
COMMIT;

-- ============================================================
-- CLIENTES CRM (vinculados aos usuários já criados pela API)
-- USER IDs criados: admin2=2, carlos=3, ana=4
-- clientes: joao=5, maria=6, pedro=7, lucia=8, bruno=9, fernanda=10, ricardo=11, patricia=12
-- Segmentos: VIP=1, Leads=3, Inativos=4, Premium=5, Novos=6
-- ============================================================

-- Descobrir os IDs reais dos usuários CLIENT
-- Execute isso primeiro para ver os IDs:
-- SELECT ID, NAME, EMAIL, ROLE FROM WTC_USERS ORDER BY ID;

INSERT INTO WTC_CLIENTS (ID, USER_ID, SEGMENT_ID, TAGS, SCORE, STATUS, PHONE, COMPANY, CREATED_AT, UPDATED_AT)
SELECT SEQ_CLIENTS.NEXTVAL, U.ID, 1, 'vip,fiel', 95.0, 'ACTIVE', '(11)99001-0001', 'Empresa Alpha', SYSTIMESTAMP, SYSTIMESTAMP
FROM WTC_USERS U WHERE U.EMAIL = 'joao@email.com';

INSERT INTO WTC_CLIENTS (ID, USER_ID, SEGMENT_ID, TAGS, SCORE, STATUS, PHONE, COMPANY, CREATED_AT, UPDATED_AT)
SELECT SEQ_CLIENTS.NEXTVAL, U.ID, 5, 'premium,recorrente', 88.5, 'ACTIVE', '(11)99001-0002', 'Beta Corp', SYSTIMESTAMP, SYSTIMESTAMP
FROM WTC_USERS U WHERE U.EMAIL = 'maria@email.com';

INSERT INTO WTC_CLIENTS (ID, USER_ID, SEGMENT_ID, TAGS, SCORE, STATUS, PHONE, COMPANY, CREATED_AT, UPDATED_AT)
SELECT SEQ_CLIENTS.NEXTVAL, U.ID, 3, 'lead,interessado', 72.0, 'ACTIVE', '(11)99001-0003', 'Gamma Ltda', SYSTIMESTAMP, SYSTIMESTAMP
FROM WTC_USERS U WHERE U.EMAIL = 'pedro@email.com';

INSERT INTO WTC_CLIENTS (ID, USER_ID, SEGMENT_ID, TAGS, SCORE, STATUS, PHONE, COMPANY, CREATED_AT, UPDATED_AT)
SELECT SEQ_CLIENTS.NEXTVAL, U.ID, 1, 'vip,corporativo', 91.0, 'ACTIVE', '(11)99001-0004', 'Delta SA', SYSTIMESTAMP, SYSTIMESTAMP
FROM WTC_USERS U WHERE U.EMAIL = 'lucia@email.com';

INSERT INTO WTC_CLIENTS (ID, USER_ID, SEGMENT_ID, TAGS, SCORE, STATUS, PHONE, COMPANY, CREATED_AT, UPDATED_AT)
SELECT SEQ_CLIENTS.NEXTVAL, U.ID, 4, 'inativo', 45.0, 'INACTIVE', '(11)99001-0005', 'Epsilon ME', SYSTIMESTAMP, SYSTIMESTAMP
FROM WTC_USERS U WHERE U.EMAIL = 'bruno@email.com';

INSERT INTO WTC_CLIENTS (ID, USER_ID, SEGMENT_ID, TAGS, SCORE, STATUS, PHONE, COMPANY, CREATED_AT, UPDATED_AT)
SELECT SEQ_CLIENTS.NEXTVAL, U.ID, 6, 'novo,prospecto', 60.0, 'PROSPECT', '(11)99001-0006', 'Zeta Inc', SYSTIMESTAMP, SYSTIMESTAMP
FROM WTC_USERS U WHERE U.EMAIL = 'fernanda@email.com';

INSERT INTO WTC_CLIENTS (ID, USER_ID, SEGMENT_ID, TAGS, SCORE, STATUS, PHONE, COMPANY, CREATED_AT, UPDATED_AT)
SELECT SEQ_CLIENTS.NEXTVAL, U.ID, 3, 'lead,quente', 78.0, 'ACTIVE', '(11)99001-0007', 'Eta Group', SYSTIMESTAMP, SYSTIMESTAMP
FROM WTC_USERS U WHERE U.EMAIL = 'ricardo@email.com';

INSERT INTO WTC_CLIENTS (ID, USER_ID, SEGMENT_ID, TAGS, SCORE, STATUS, PHONE, COMPANY, CREATED_AT, UPDATED_AT)
SELECT SEQ_CLIENTS.NEXTVAL, U.ID, 5, 'premium', 85.0, 'ACTIVE', '(11)99001-0008', 'Theta EIRELI', SYSTIMESTAMP, SYSTIMESTAMP
FROM WTC_USERS U WHERE U.EMAIL = 'patricia@email.com';

COMMIT;

-- ============================================================
-- MENSAGENS
-- ============================================================
INSERT INTO WTC_MESSAGES (ID, SENDER_ID, RECEIVER_ID, CONTENT, TYPE, STATUS, SENT_AT)
SELECT SEQ_MESSAGES.NEXTVAL, OP.ID, CL.ID,
    'Olá João! Temos uma oferta exclusiva para você como cliente VIP.',
    'TEXT', 'READ', SYSTIMESTAMP - INTERVAL '5' DAY
FROM WTC_USERS OP, WTC_USERS CL
WHERE OP.EMAIL = 'admin2@wtc.com' AND CL.EMAIL = 'joao@email.com';

INSERT INTO WTC_MESSAGES (ID, SENDER_ID, RECEIVER_ID, CONTENT, TYPE, STATUS, SENT_AT, READ_AT)
SELECT SEQ_MESSAGES.NEXTVAL, OP.ID, CL.ID,
    'Maria, seu plano Premium está prestes a vencer. Deseja renovar?',
    'PROMO', 'READ', SYSTIMESTAMP - INTERVAL '3' DAY, SYSTIMESTAMP - INTERVAL '2' DAY
FROM WTC_USERS OP, WTC_USERS CL
WHERE OP.EMAIL = 'carlos@wtc.com' AND CL.EMAIL = 'maria@email.com';

INSERT INTO WTC_MESSAGES (ID, SENDER_ID, RECEIVER_ID, CONTENT, TYPE, STATUS, SENT_AT)
SELECT SEQ_MESSAGES.NEXTVAL, OP.ID, CL.ID,
    'Pedro, vi que você tem interesse em nosso produto. Posso te ajudar?',
    'TEXT', 'DELIVERED', SYSTIMESTAMP - INTERVAL '1' DAY
FROM WTC_USERS OP, WTC_USERS CL
WHERE OP.EMAIL = 'ana@wtc.com' AND CL.EMAIL = 'pedro@email.com';

INSERT INTO WTC_MESSAGES (ID, SENDER_ID, RECEIVER_ID, CONTENT, TYPE, STATUS, SENT_AT)
SELECT SEQ_MESSAGES.NEXTVAL, OP.ID, CL.ID,
    'Lucia, temos novidades exclusivas para clientes VIP. Confira!',
    'PROMO', 'SENT', SYSTIMESTAMP - INTERVAL '2' HOUR
FROM WTC_USERS OP, WTC_USERS CL
WHERE OP.EMAIL = 'admin2@wtc.com' AND CL.EMAIL = 'lucia@email.com';

INSERT INTO WTC_MESSAGES (ID, SENDER_ID, RECEIVER_ID, CONTENT, TYPE, STATUS, SENT_AT)
SELECT SEQ_MESSAGES.NEXTVAL, CL.ID, OP.ID,
    'Olá! Gostaria de saber mais sobre os planos disponíveis.',
    'TEXT', 'READ', SYSTIMESTAMP - INTERVAL '4' DAY
FROM WTC_USERS OP, WTC_USERS CL
WHERE OP.EMAIL = 'admin2@wtc.com' AND CL.EMAIL = 'joao@email.com';

INSERT INTO WTC_MESSAGES (ID, SENDER_ID, RECEIVER_ID, CONTENT, TYPE, STATUS, SENT_AT)
SELECT SEQ_MESSAGES.NEXTVAL, CL.ID, OP.ID,
    'Boa tarde! Quando posso esperar o retorno da proposta?',
    'TEXT', 'DELIVERED', SYSTIMESTAMP - INTERVAL '6' HOUR
FROM WTC_USERS OP, WTC_USERS CL
WHERE OP.EMAIL = 'carlos@wtc.com' AND CL.EMAIL = 'ricardo@email.com';

COMMIT;

-- ============================================================
-- CAMPANHAS
-- ============================================================
INSERT INTO WTC_CAMPAIGNS (ID, TITLE, BODY, TYPE, SEGMENT_ID, CREATED_BY, STATUS, SENT_AT, CREATED_AT)
SELECT SEQ_CAMPAIGNS.NEXTVAL,
    'Oferta Exclusiva VIP 🌟',
    'Prezado cliente VIP, preparamos uma oferta exclusiva para você! Acesse agora e aproveite descontos de até 40% em todos os produtos.',
    'PROMO', 1, U.ID, 'SENT', SYSTIMESTAMP - INTERVAL '7' DAY, SYSTIMESTAMP - INTERVAL '8' DAY
FROM WTC_USERS U WHERE U.EMAIL = 'admin2@wtc.com';

INSERT INTO WTC_CAMPAIGNS (ID, TITLE, BODY, TYPE, SEGMENT_ID, CREATED_BY, STATUS, SENT_AT, CREATED_AT)
SELECT SEQ_CAMPAIGNS.NEXTVAL,
    'Renovação Premium - Última Chance',
    'Seu plano Premium vence em breve. Renove agora e ganhe 2 meses grátis! Não perca essa oportunidade.',
    'PROMO', 5, U.ID, 'SENT', SYSTIMESTAMP - INTERVAL '3' DAY, SYSTIMESTAMP - INTERVAL '4' DAY
FROM WTC_USERS U WHERE U.EMAIL = 'carlos@wtc.com';

INSERT INTO WTC_CAMPAIGNS (ID, TITLE, BODY, TYPE, SEGMENT_ID, CREATED_BY, STATUS, CREATED_AT)
SELECT SEQ_CAMPAIGNS.NEXTVAL,
    'Reativação - Sentimos sua falta!',
    'Faz um tempo que não te vemos por aqui. Temos novidades incríveis esperando por você. Volte e ganhe um desconto especial!',
    'INVITE', 4, U.ID, 'DRAFT', SYSTIMESTAMP - INTERVAL '1' DAY
FROM WTC_USERS U WHERE U.EMAIL = 'admin2@wtc.com';

INSERT INTO WTC_CAMPAIGNS (ID, TITLE, BODY, TYPE, SEGMENT_ID, CREATED_BY, STATUS, CREATED_AT)
SELECT SEQ_CAMPAIGNS.NEXTVAL,
    'Bem-vindo à WTC! 🎉',
    'Seja bem-vindo(a)! Estamos felizes em ter você conosco. Explore todos os nossos serviços e aproveite o período de trial gratuito.',
    'COMUNICADO', 6, U.ID, 'DRAFT', SYSTIMESTAMP
FROM WTC_USERS U WHERE U.EMAIL = 'ana@wtc.com';

INSERT INTO WTC_CAMPAIGNS (ID, TITLE, BODY, TYPE, SEGMENT_ID, CREATED_BY, STATUS, CREATED_AT)
SELECT SEQ_CAMPAIGNS.NEXTVAL,
    'Black Friday WTC - 50% OFF',
    'Nossa maior promoção do ano chegou! Todos os planos com 50% de desconto por tempo limitado. Garanta já o seu!',
    'BANNER', 3, U.ID, 'DRAFT', SYSTIMESTAMP
FROM WTC_USERS U WHERE U.EMAIL = 'admin2@wtc.com';

COMMIT;

-- ============================================================
-- DESTINATÁRIOS DAS CAMPANHAS ENVIADAS
-- ============================================================
INSERT INTO WTC_CAMPAIGN_RECIPIENTS (ID, CAMPAIGN_ID, CLIENT_ID, STATUS, SENT_AT, READ_AT)
SELECT SEQ_CAMP_RECIP.NEXTVAL, CAM.ID, CLI.ID, 'READ', SYSTIMESTAMP - INTERVAL '7' DAY, SYSTIMESTAMP - INTERVAL '6' DAY
FROM WTC_CAMPAIGNS CAM, WTC_CLIENTS CLI, WTC_USERS U
WHERE CAM.TITLE LIKE 'Oferta Exclusiva VIP%' AND CLI.USER_ID = U.ID AND U.EMAIL = 'joao@email.com';

INSERT INTO WTC_CAMPAIGN_RECIPIENTS (ID, CAMPAIGN_ID, CLIENT_ID, STATUS, SENT_AT, READ_AT)
SELECT SEQ_CAMP_RECIP.NEXTVAL, CAM.ID, CLI.ID, 'READ', SYSTIMESTAMP - INTERVAL '7' DAY, SYSTIMESTAMP - INTERVAL '5' DAY
FROM WTC_CAMPAIGNS CAM, WTC_CLIENTS CLI, WTC_USERS U
WHERE CAM.TITLE LIKE 'Oferta Exclusiva VIP%' AND CLI.USER_ID = U.ID AND U.EMAIL = 'lucia@email.com';

INSERT INTO WTC_CAMPAIGN_RECIPIENTS (ID, CAMPAIGN_ID, CLIENT_ID, STATUS, SENT_AT)
SELECT SEQ_CAMP_RECIP.NEXTVAL, CAM.ID, CLI.ID, 'DELIVERED', SYSTIMESTAMP - INTERVAL '3' DAY
FROM WTC_CAMPAIGNS CAM, WTC_CLIENTS CLI, WTC_USERS U
WHERE CAM.TITLE LIKE 'Renovação Premium%' AND CLI.USER_ID = U.ID AND U.EMAIL = 'maria@email.com';

INSERT INTO WTC_CAMPAIGN_RECIPIENTS (ID, CAMPAIGN_ID, CLIENT_ID, STATUS, SENT_AT)
SELECT SEQ_CAMP_RECIP.NEXTVAL, CAM.ID, CLI.ID, 'DELIVERED', SYSTIMESTAMP - INTERVAL '3' DAY
FROM WTC_CAMPAIGNS CAM, WTC_CLIENTS CLI, WTC_USERS U
WHERE CAM.TITLE LIKE 'Renovação Premium%' AND CLI.USER_ID = U.ID AND U.EMAIL = 'patricia@email.com';

COMMIT;

-- ============================================================
-- ANOTAÇÕES DOS OPERADORES
-- ============================================================
INSERT INTO WTC_ANNOTATIONS (ID, CLIENT_ID, OPERATOR_ID, CONTENT, CREATED_AT)
SELECT SEQ_ANNOTATIONS.NEXTVAL, CLI.ID, OP.ID,
    'Cliente muito satisfeito com o atendimento. Demonstrou interesse em upgrade para plano Enterprise.',
    SYSTIMESTAMP - INTERVAL '5' DAY
FROM WTC_CLIENTS CLI, WTC_USERS CLI_U, WTC_USERS OP
WHERE CLI.USER_ID = CLI_U.ID AND CLI_U.EMAIL = 'joao@email.com' AND OP.EMAIL = 'admin2@wtc.com';

INSERT INTO WTC_ANNOTATIONS (ID, CLIENT_ID, OPERATOR_ID, CONTENT, CREATED_AT)
SELECT SEQ_ANNOTATIONS.NEXTVAL, CLI.ID, OP.ID,
    'Ligou solicitando suporte técnico. Encaminhado para equipe de TI. Follow-up necessário.',
    SYSTIMESTAMP - INTERVAL '3' DAY
FROM WTC_CLIENTS CLI, WTC_USERS CLI_U, WTC_USERS OP
WHERE CLI.USER_ID = CLI_U.ID AND CLI_U.EMAIL = 'joao@email.com' AND OP.EMAIL = 'carlos@wtc.com';

INSERT INTO WTC_ANNOTATIONS (ID, CLIENT_ID, OPERATOR_ID, CONTENT, CREATED_AT)
SELECT SEQ_ANNOTATIONS.NEXTVAL, CLI.ID, OP.ID,
    'Plano vencendo em 15 dias. Cliente indicou possibilidade de cancelamento. Necessita proposta de retenção.',
    SYSTIMESTAMP - INTERVAL '2' DAY
FROM WTC_CLIENTS CLI, WTC_USERS CLI_U, WTC_USERS OP
WHERE CLI.USER_ID = CLI_U.ID AND CLI_U.EMAIL = 'maria@email.com' AND OP.EMAIL = 'carlos@wtc.com';

INSERT INTO WTC_ANNOTATIONS (ID, CLIENT_ID, OPERATOR_ID, CONTENT, CREATED_AT)
SELECT SEQ_ANNOTATIONS.NEXTVAL, CLI.ID, OP.ID,
    'Lead qualificado. Reunião agendada para próxima semana. Grande potencial de conversão.',
    SYSTIMESTAMP - INTERVAL '1' DAY
FROM WTC_CLIENTS CLI, WTC_USERS CLI_U, WTC_USERS OP
WHERE CLI.USER_ID = CLI_U.ID AND CLI_U.EMAIL = 'pedro@email.com' AND OP.EMAIL = 'ana@wtc.com';

INSERT INTO WTC_ANNOTATIONS (ID, CLIENT_ID, OPERATOR_ID, CONTENT, CREATED_AT)
SELECT SEQ_ANNOTATIONS.NEXTVAL, CLI.ID, OP.ID,
    'Cliente inativo há 60 dias. Tentativa de contato por telefone sem sucesso. Email enviado.',
    SYSTIMESTAMP - INTERVAL '10' DAY
FROM WTC_CLIENTS CLI, WTC_USERS CLI_U, WTC_USERS OP
WHERE CLI.USER_ID = CLI_U.ID AND CLI_U.EMAIL = 'bruno@email.com' AND OP.EMAIL = 'admin2@wtc.com';

INSERT INTO WTC_ANNOTATIONS (ID, CLIENT_ID, OPERATOR_ID, CONTENT, CREATED_AT)
SELECT SEQ_ANNOTATIONS.NEXTVAL, CLI.ID, OP.ID,
    'Novo cliente com alto potencial. Veio por indicação do João Mendes. Atendimento VIP recomendado.',
    SYSTIMESTAMP - INTERVAL '2' HOUR
FROM WTC_CLIENTS CLI, WTC_USERS CLI_U, WTC_USERS OP
WHERE CLI.USER_ID = CLI_U.ID AND CLI_U.EMAIL = 'fernanda@email.com' AND OP.EMAIL = 'ana@wtc.com';

COMMIT;

-- ============================================================
-- TAREFAS
-- ============================================================
INSERT INTO WTC_TASKS (ID, ASSIGNED_TO, CREATED_BY, TITLE, STATUS, CREATED_AT, UPDATED_AT)
SELECT SEQ_TASKS.NEXTVAL, OP1.ID, OP2.ID,
    'Ligar para João Mendes sobre proposta de upgrade',
    'OPEN', SYSTIMESTAMP - INTERVAL '2' DAY, SYSTIMESTAMP - INTERVAL '2' DAY
FROM WTC_USERS OP1, WTC_USERS OP2
WHERE OP1.EMAIL = 'carlos@wtc.com' AND OP2.EMAIL = 'admin2@wtc.com';

INSERT INTO WTC_TASKS (ID, ASSIGNED_TO, CREATED_BY, TITLE, STATUS, CREATED_AT, UPDATED_AT)
SELECT SEQ_TASKS.NEXTVAL, OP1.ID, OP2.ID,
    'Enviar proposta de retenção para Maria Santos',
    'IN_PROGRESS', SYSTIMESTAMP - INTERVAL '1' DAY, SYSTIMESTAMP - INTERVAL '6' HOUR
FROM WTC_USERS OP1, WTC_USERS OP2
WHERE OP1.EMAIL = 'carlos@wtc.com' AND OP2.EMAIL = 'carlos@wtc.com';

INSERT INTO WTC_TASKS (ID, ASSIGNED_TO, CREATED_BY, TITLE, STATUS, CREATED_AT, UPDATED_AT)
SELECT SEQ_TASKS.NEXTVAL, OP1.ID, OP2.ID,
    'Preparar material para reunião com Pedro Oliveira',
    'OPEN', SYSTIMESTAMP - INTERVAL '3' HOUR, SYSTIMESTAMP - INTERVAL '3' HOUR
FROM WTC_USERS OP1, WTC_USERS OP2
WHERE OP1.EMAIL = 'ana@wtc.com' AND OP2.EMAIL = 'ana@wtc.com';

INSERT INTO WTC_TASKS (ID, ASSIGNED_TO, CREATED_BY, TITLE, STATUS, CREATED_AT, UPDATED_AT)
SELECT SEQ_TASKS.NEXTVAL, OP1.ID, OP2.ID,
    'Reativar conta de Bruno Lima - campanha especial',
    'OPEN', SYSTIMESTAMP - INTERVAL '5' DAY, SYSTIMESTAMP - INTERVAL '5' DAY
FROM WTC_USERS OP1, WTC_USERS OP2
WHERE OP1.EMAIL = 'admin2@wtc.com' AND OP2.EMAIL = 'admin2@wtc.com';

INSERT INTO WTC_TASKS (ID, ASSIGNED_TO, CREATED_BY, TITLE, STATUS, CREATED_AT, UPDATED_AT)
SELECT SEQ_TASKS.NEXTVAL, OP1.ID, OP2.ID,
    'Onboarding Fernanda Rocha - boas-vindas e setup inicial',
    'DONE', SYSTIMESTAMP - INTERVAL '1' DAY, SYSTIMESTAMP - INTERVAL '2' HOUR
FROM WTC_USERS OP1, WTC_USERS OP2
WHERE OP1.EMAIL = 'ana@wtc.com' AND OP2.EMAIL = 'admin2@wtc.com';

COMMIT;

-- ============================================================
-- VERIFICAÇÃO FINAL
-- ============================================================
SELECT 'USUARIOS'  AS TABELA, COUNT(*) AS TOTAL FROM WTC_USERS     UNION ALL
SELECT 'SEGMENTOS'          , COUNT(*)           FROM WTC_SEGMENTS  UNION ALL
SELECT 'CLIENTES'           , COUNT(*)           FROM WTC_CLIENTS   UNION ALL
SELECT 'MENSAGENS'          , COUNT(*)           FROM WTC_MESSAGES  UNION ALL
SELECT 'CAMPANHAS'          , COUNT(*)           FROM WTC_CAMPAIGNS UNION ALL
SELECT 'DESTINATARIOS'      , COUNT(*)           FROM WTC_CAMPAIGN_RECIPIENTS UNION ALL
SELECT 'ANOTACOES'          , COUNT(*)           FROM WTC_ANNOTATIONS UNION ALL
SELECT 'TAREFAS'            , COUNT(*)           FROM WTC_TASKS;
