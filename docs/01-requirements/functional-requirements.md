# Documento de Requisitos Funcionais (DRF)
# EmoCare 2.0

**Versão:** 1.0  
**Status:** Em elaboração  
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

O sistema deve permitir que novos pacientes realizem cadastro através do Google ou informando:

- Nome completo;
- E-mail;
- Senha;
- Telefone;
- Data de nascimento.

---

## RF-002 - Cadastro de Profissional

O sistema deve permitir que profissionais realizem cadastro contendo:

- Nome;
- E-mail;
- Senha;
- Registro profissional (CRP);
- Especialidades;
- Biografia;
- Tempo de experiência.

---

## RF-003 - Login

O sistema deve permitir autenticação utilizando:

- E-mail;
- Senha.

Ou via Google.

---

## RF-004 - Recuperação de Senha

O sistema deve permitir recuperação de senha através do e-mail cadastrado.

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

O usuário poderá alterar sua senha após informar a senha atual.

---

## RF-009 - Foto de Perfil

O sistema deverá permitir upload e alteração da foto de perfil.

---

# 3.3 Gerenciamento de Profissionais

## RF-010 - Cadastro de Especialidades

O profissional poderá cadastrar suas especialidades.

---

## RF-011 - Definição de Valor da Consulta

O profissional poderá informar o valor de suas consultas.

---

## RF-012 - Configuração da Agenda

O profissional poderá definir:

- Dias disponíveis;
- Horários disponíveis;
- Intervalos;
- Duração padrão da consulta.

---

## RF-013 - Bloqueio de Datas

O profissional poderá bloquear datas específicas.

---

## RF-014 - Ausência Temporária

O profissional poderá informar períodos de férias ou indisponibilidade.

---

# 3.4 Busca de Profissionais

## RF-015 - Listagem de Profissionais

O paciente poderá visualizar todos os profissionais cadastrados.

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
- Valor da consulta.

---

# 3.5 Agendamento de Consultas

## RF-018 - Consulta de Disponibilidade

O paciente poderá visualizar horários livres.

---

## RF-019 - Agendamento

O paciente poderá realizar agendamento selecionando:

- Profissional;
- Data;
- Horário.

---

## RF-020 - Validação de Horários

O sistema não permitirá agendamentos em horários indisponíveis.

---

## RF-021 - Confirmação do Agendamento

Após concluir o agendamento, o sistema deverá gerar confirmação.

---

## RF-022 - Reagendamento

O paciente poderá reagendar consultas futuras.

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

## RF-025 - Agenda

O profissional poderá visualizar sua agenda.

---

## RF-026 - Visualização por Período

A agenda deverá permitir visualização:

- Diária;
- Semanal;
- Mensal.

---

## RF-027 - Consulta dos Agendamentos

O profissional poderá visualizar informações da consulta.

---

## RF-028 - Cancelamento pelo Profissional

O profissional poderá cancelar consultas.

---

## RF-029 - Reagendamento pelo Profissional

O profissional poderá alterar data e horário de consultas.

---

# 3.7 Gestão de Pacientes

## RF-030 - Listagem de Pacientes

O profissional poderá visualizar pacientes atendidos.

---

## RF-031 - Histórico do Paciente

O profissional poderá visualizar o histórico de consultas do paciente.

---

## RF-032 - Dados do Paciente

O sistema deverá disponibilizar os dados cadastrais autorizados do paciente.

---

# 3.8 Prontuário e Evolução Clínica

## RF-033 - Registro de Evolução

O profissional poderá registrar anotações referentes à sessão.

---

## RF-034 - Histórico de Evoluções

Todas as evoluções deverão permanecer armazenadas.

---

## RF-035 - Edição de Evoluções

O profissional poderá editar registros realizados.

---

## RF-036 - Exclusão de Evoluções

O profissional poderá remover registros quando necessário.

---

## RF-037 - Consulta do Prontuário

O profissional poderá consultar o prontuário completo do paciente.

---

# 3.9 Comunicação

## RF-038 - Mensagens

O sistema deverá permitir troca de mensagens entre paciente e profissional.

---

## RF-039 - Histórico de Conversas

As mensagens deverão permanecer armazenadas.

---

## RF-040 - Notificação de Nova Mensagem

O sistema notificará o destinatário sempre que receber uma nova mensagem.

---

# 3.10 Notificações

## RF-041 - Confirmação de Consulta

O sistema deverá notificar ambas as partes quando uma consulta for agendada.

---

## RF-042 - Cancelamento

O sistema deverá enviar notificações em caso de cancelamento.

---

## RF-043 - Reagendamento

O sistema deverá informar alterações de data ou horário.

---

## RF-044 - Lembrete da Consulta

O sistema deverá enviar lembrete antes da consulta.

---

## RF-045 - Notificações do Sistema

O administrador poderá enviar comunicados para usuários.

---

# 3.11 Administração

## RF-046 - Gerenciamento de Usuários

O administrador poderá:

- Criar;
- Editar;
- Bloquear;
- Excluir usuários.

---

## RF-047 - Gerenciamento de Profissionais

O administrador poderá aprovar ou rejeitar cadastros de profissionais.

---

## RF-048 - Dashboard Administrativo

O sistema deverá disponibilizar indicadores da plataforma.

Exemplos:

- Quantidade de usuários;
- Consultas realizadas;
- Profissionais ativos;
- Novos cadastros.

---

## RF-049 - Auditoria

O sistema deverá registrar ações relevantes realizadas pelos usuários.

---

## RF-050 - Configurações Gerais

O administrador poderá configurar parâmetros gerais da plataforma.

---

# 3.12 Segurança Funcional

## RF-051 - Proteção de Dados Clínicos

As informações clínicas deverão ser acessíveis apenas ao profissional responsável.

---

## RF-052 - Controle de Acesso

Usuários somente poderão acessar recursos autorizados.

---

## RF-053 - Encerramento Automático de Sessão

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