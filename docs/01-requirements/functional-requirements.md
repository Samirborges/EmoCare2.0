# Documento de Requisitos Funcionais (DRF)
# EmoCare 2.0

**Versão:** 1.0  
**Status:** Aprovado  
**Autor:** Samir Borges  
**Data:** 24/07/2026

---

# 1. Introdução

## 1.1 Objetivo

Este documento descreve os requisitos funcionais do sistema **EmoCare 2.0**, uma plataforma destinada ao gerenciamento de atendimentos terapêuticos, permitindo a interação entre pacientes, profissionais de saúde mental e administradores da plataforma.

## 1.2 Escopo

O sistema deverá fornecer funcionalidades para:

- Cadastro e autenticação de usuários;
- Gerenciamento de perfis (profissionais);
- Agendamento de consultas;
- Gerenciamento de agenda dos profissionais;
- Registro de prontuários e anotações;
- Comunicação entre profissional e paciente;
- Sistema de notificações;
- Administração da plataforma.

---

# 2. Convenções

Cada requisito funcional será identificado pelo padrão:

> **RF-XXX**

Exemplo:

- RF-001
- RF-002
- RF-003

---

# 3. Requisitos Funcionais

---

# 3.1 Autenticação e Autorização

## RF-001 - Cadastro de Paciente

O sistema deve permitir que novos pacientes realizem cadastro informando:

- Nome completo;
- E-mail;
- Senha;
- Telefone;
- Data de nascimento.

---

## RF-002 - Cadastro de Profissional

O sistema deve permitir que profissioais autorizados pelo administrador realize o cadastro contendo as seguintes informções:

- Nome;
- E-mail;
- Senha;
- Registro profissional (CRP);
- Especialidades;
- Biografia;
- Tempo de experiência.

---

## RF-003 - Login

O sistema deve permitir autenticação utilizando e-mail e senha ou provedor externo como Google. Caso o usuário faça login via provedor externo sem a conta ter sido criada, o sistema poderá fazer o cadastro automáticamente. O login para profissionais não poderá ser feito via provedor externo, apenas usando credenciais.

---

## RF-004 - Recuperação de Senha

O sistema deverá permitir recuperação de senha apenas para usuários autenticados por credenciais locais (e-mail e senha). Usuários autenticados via provedores externos (Google) deverão recuperar suas credenciais diretamente junto ao provedor.

---

## RF-005 - Encerramento de Sessão

O sistema deve permitir que o usuário encerre sua sessão.

---

## RF-006 - Controle de Permissões

O sistema deve restringir funcionalidades conforme o perfil do usuário.

Perfis previstos:

- Paciente
- Profissional
- Administrador

---

# 3.2 Gerenciamento de Perfil

## RF-007 - Atualização de Perfil

O sistema deve permitir atualização dos dados cadastrais.

---

## RF-008 - Alteração de Senha

O usuário poderá alterar sua senha após informar a senha atual. Exceto se o usuário estiver autenticado via provedor externo, nesse caso a alteração de senha deverá ser feita diretamente junto ao provedor.

---

## RF-009 - Foto de Perfil

O sistema deverá permitir upload e alteração da foto de perfil.

---

# 3.3 Gerenciamento de Profissionais

## RF-010 - Cadastro de Especialidades

O profissional poderá cadastrar suas especialidades.

---

## RF-011 - Definição de Valor da Consulta

O profissional poderá informar o valor de suas consultas à serem agendadas pelos pacientes.

---

## RF-012 - Configuração da Agenda

O profissional poderá definir através de um calendário:

- Dias disponíveis;
- Horários disponíveis;
- Intervalos (entre consultas max 30 min);
- Duração padrão da consulta.

---

## RF-013 - Bloqueio de Datas

O profissional poderá através do calendário bloquear datas específicas. 

---

## RF-014 - Ausência Temporária

O profissional poderá informar períodos de férias ou indisponibilidade. 

Para o caso de férias será deverá se aprovada antes pelo administrador e indisponibilidade deve ser apresentada dias antes com justificativa obrigatória.

---

# 3.4 Busca de Profissionais

## RF-015 - Listagem de Profissionais

O paciente poderá visualizar todos os profissionais cadastrados e suas informações como especialidades.

---

## RF-016 - Pesquisa

O paciente poderá pesquisar profissionais por:

- Nome;
- Especialidade;
- Abordagem terapêutica.

---

## RF-017 - Visualização de Perfil Profissional

O sistema deverá apresentar:

- Foto;
- Biografia;
- Especialidades;
- Horários disponíveis;
- Valores das consultas.

---

# 3.5 Agendamento de Consultas

## RF-018 - Consulta de Disponibilidade

O paciente poderá visualizar dias e horários disponíveis através de um calendário do profissional que ela selecionou. 

---

## RF-019 - Agendamento

Ao selecionar o profissional disponível o paciente poderá visualizar a agenda dele e realizar agendamento selecionando:

- Data;
- Horário.

---

## RF-020 - Validação de Horários

O sistema deverá impedir conflitos de agenda durante agendamentos e reagendamentos.

---

## RF-021 - Confirmação do Agendamento

Após concluir o agendamento, o sistema deverá gerar confirmação.

---

## RF-022 - Reagendamento

O paciente poderá reagendar consultas futuras em horários disponíveis.

---

## RF-023 - Cancelamento

O paciente poderá cancelar consultas.

---

## RF-024 - Histórico de Consultas

O paciente poderá visualizar:

- Consultas futuras;
- Consultas realizadas;
- Consultas canceladas.

---

# 3.6 Gestão da Agenda do Profissional

## RF-025 - Agenda (Estado da consulta)

O profissional poderá visualizar sua agenda.

O sistema deverá controlar automaticamente o ciclo de vida de cada consulta, utilizando os seguintes estados:

- Agendada
- Confirmada
- Em andamento
- Finalizada
- Cancelada
- Não Compareceu

O estado "Não Compareceu" deverá registrar qual das partes esteve ausente (paciente ou profissional).

---

## RF-026 - Confirmação de Presença

O sistema deverá permitir que o paciente confirme sua presença antes da consulta.

Caso a presença não seja confirmada dentro do prazo configurado pela clínica, o sistema deverá enviar lembretes ao paciente e ao profissional.

A ausência da confirmação não impedirá a realização da consulta.

---


## RF-027 - Visualização por Período

A agenda deverá permitir visualização:

- Diária;
- Semanal;
- Mensal.

### RF-021.1 - Confirmação da Consulta pelo Profissional

O profissional deverá confirmar o atendimento agendado.

Após a confirmação, o estado da consulta será alterado de "Agendada" para "Confirmada".

---

## RF-028 - Consulta dos Agendamentos

O profissional poderá visualizar informações da consulta. Verificar informações do paciente como histórico de consultas e seus prontuários.

---

## RF-029 - Cancelamento pelo Profissional

O profissional poderá cancelar consultas.

---

## RF-030 - Reagendamento pelo Profissional

O profissional poderá alterar data e horário de consultas. 

---

# 3.7 Gestão de Pacientes

## RF-031 - Listagem de Pacientes

O profissional poderá visualizar pacientes atendidos.

---

## RF-032 - Histórico do Paciente

O profissional poderá visualizar o histórico de consultas do paciente. O histórico desse paciente com o profissional.

---

## RF-033 - Dados do Paciente

O sistema deverá disponibilizar os dados cadastrais autorizados do paciente.

---

# 3.8 Prontuário e Evolução Clínica

## RF-034 - Registro de Evolução

O profissional poderá registrar anotações referentes à sessão.

---

## RF-035 - Histórico de Evoluções

Todas as evoluções deverão permanecer armazenadas.

---

## RF-036 - Edição de Evoluções

O profissional poderá editar registros realizados.

---

## RF-037 - Invalidar Evoluções

O profissional poderá invalidar registros quando necessário.

---

## RF-038 - Consulta do Prontuário

O profissional poderá consultar o prontuário completo do paciente.

---

# 3.9 Comunicação

## RF-039 - Mensagens

O sistema deverá permitir troca de mensagens entre paciente e profissional.

---

## RF-040 - Histórico de Conversas

As mensagens deverão permanecer armazenadas.

---

## RF-041 - Notificação de Nova Mensagem

O sistema notificará o destinatário sempre que receber uma nova mensagem através do e-mail ou no próprio sistema.

---

# 3.10 Notificações

## RF-042 - Confirmação de Consulta

O sistema deverá notificar ambas as partes quando uma consulta for agendada via e-mail e/ou no próprio sistema.

---

## RF-043 - Cancelamento

O sistema deverá enviar notificações em caso de cancelamento via e-mail e/ou no próprio sistema.

---

## RF-044 - Reagendamento

O sistema deverá informar alterações de data ou horário via e-mail e/ou no próprio sistema.

---

## RF-045 - Lembrete da Consulta

O sistema deverá enviar lembrete antes da consulta via e-mail e/ou no próprio sistema.

---

## RF-046 - Notificações do Sistema

O administrador poderá enviar comunicados para usuários. Filtrando se é para todos, profissionais ou usuários somente.

---

# 3.11 Administração

## RF-047 - Gerenciamento de Usuários

O administrador poderá:

- Criar;
- Editar;
- Desativar usuários.

Para pacientes que forem desativados, suas consultas ativas serão automaticamente canceladas.

Para profissionais que tiverem agendamento, seus agendamentos serão cancelados e os prontuários de seus pacientes serão invalidados.


Para as mensagens, tanto com relação ao paciente ou profissional, deve substituir o nome pela mensagem "Esse perfil está desativado"

---

## RF-048 - Gerenciamento de Profissionais

O administrador poderá adicionar e-mail para autorização de cadastro profissional na plataforma.

---

## RF-049 - Dashboard Administrativo

O sistema deverá disponibilizar indicadores da plataforma.

Exemplos:

- Quantidade de usuários;
- Consultas realizadas;
- Profissionais ativos;
- Novos cadastros.

---

## RF-050 - Auditoria

O sistema deverá registrar as seguintes ações:

Para pacientes:
- Agendamento
- Cancelamento
- Reagendamento
- Envio de mensagem para o profissional (o sistema não deve mostrar o conteúdo das mensagens, apenas que foi enviado, e data e horário)
- Confirmação de presença

Para Profissionais
- Mostrar alteração do status da consulta 
- Preencheu e publicou prontuário, com informações de somente o nome do paciente, não devendo mostrar o conteúdo do prontuário.
- Cancelar consulta
- Reagendar consulta
- Confirmação de presença
- Requisição de férias ou ausência temporária

---

## RF-051 - Configurações Gerais

O administrador poderá configurar parâmetros gerais da plataforma.

---

# 3.12 Segurança Funcional

## RF-052 - Proteção de Dados Clínicos

As informações clínicas deverão ser acessíveis apenas ao profissional responsável.

---

## RF-053 - Controle de Acesso

Usuários somente poderão acessar recursos autorizados.

---

## RF-054 - Encerramento Automático de Sessão

O sistema poderá encerrar automaticamente sessões inativas.

---

# 4. Resumo dos Requisitos

| Módulo | Quantidade |
|---------|-----------:|
| Autenticação | 6 |
| Perfil | 3 |
| Profissionais | 5 |
| Busca | 3 |
| Agendamento | 7 |
| Agenda | 5 |
| Pacientes | 3 |
| Prontuário | 5 |
| Comunicação | 3 |
| Notificações | 5 |
| Administração | 5 |
| Segurança | 3 |
| **Total** | **53 Requisitos Funcionais** |

---

# 5. Observações

Este documento contempla exclusivamente os requisitos funcionais do sistema. Requisitos não funcionais (desempenho, escalabilidade, segurança, usabilidade, disponibilidade, LGPD, etc.) deverão ser documentados em um documento específico.