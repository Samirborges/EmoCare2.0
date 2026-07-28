-- =====================================================
-- EXTENSÕES
-- =====================================================

CREATE EXTENSION IF NOT EXISTS pgcrypto;

-- =====================================================
-- ENUMS
-- =====================================================

CREATE TYPE user_role AS ENUM (
    'PATIENT',
    'PROFESSIONAL',
    'ADMIN'
);

CREATE TYPE auth_provider AS ENUM (
    'LOCAL',
    'GOOGLE'
);

CREATE TYPE professional_status AS ENUM (
    'PENDING',
    'ACTIVE',
    'INACTIVE',
    'SUSPENDED'
);

CREATE TYPE weekday_enum AS ENUM (
    'MONDAY',
    'TUESDAY',
    'WEDNESDAY',
    'THURSDAY',
    'FRIDAY',
    'SATURDAY',
    'SUNDAY'
);

CREATE TYPE absence_type AS ENUM (
    'VACATION',
    'TEMPORARY_UNAVAILABILITY'
);

CREATE TYPE absence_status AS ENUM (
    'PENDING',
    'APPROVED',
    'REJECTED',
    'CANCELLED'
);

CREATE TYPE appointment_status AS ENUM (
    'SCHEDULED',
    'CONFIRMED',
    'IN_PROGRESS',
    'COMPLETED',
    'CANCELLED',
    'NO_SHOW'
);

CREATE TYPE cancelled_by_type AS ENUM (
    'PATIENT',
    'PROFESSIONAL',
    'ADMIN',
    'SYSTEM'
);

CREATE TYPE no_show_type AS ENUM (
    'PATIENT',
    'PROFESSIONAL'
);

CREATE TYPE message_type AS ENUM (
    'TEXT'
);

CREATE TYPE notification_type AS ENUM (
    'APPOINTMENT_CREATED',
    'APPOINTMENT_CONFIRMED',
    'APPOINTMENT_CANCELLED',
    'APPOINTMENT_RESCHEDULED',
    'APPOINTMENT_REMINDER',
    'NEW_MESSAGE',
    'SYSTEM'
);

CREATE TYPE notification_reference_type AS ENUM (
    'APPOINTMENT',
    'MESSAGE',
    'SYSTEM'
);

CREATE TYPE audit_action AS ENUM (
    'CREATE',
    'UPDATE',
    'DELETE',
    'LOGIN',
    'LOGOUT',
    'SCHEDULE_APPOINTMENT',
    'CANCEL_APPOINTMENT',
    'RESCHEDULE_APPOINTMENT',
    'CONFIRM_APPOINTMENT',
    'CHANGE_APPOINTMENT_STATUS',
    'CREATE_MEDICAL_RECORD',
    'UPDATE_MEDICAL_RECORD',
    'INVALIDATE_MEDICAL_RECORD',
    'SEND_MESSAGE',
    'REQUEST_ABSENCE'
);

-- =====================================================
-- TABELA USERS
-- =====================================================

CREATE TABLE public.users (

    -- Mesmo UUID da tabela auth.users do Supabase
    id UUID PRIMARY KEY,

    role user_role NOT NULL,

    full_name VARCHAR(150) NOT NULL,

    email VARCHAR(255) NOT NULL UNIQUE,

    phone VARCHAR(20),

    birth_date DATE,

    photo_url TEXT,

    provider auth_provider NOT NULL DEFAULT 'LOCAL',

    active BOOLEAN NOT NULL DEFAULT TRUE,

    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),

    updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW()

);

-- =====================================================
-- FOREIGN KEY COM AUTH.USERS
-- =====================================================

ALTER TABLE public.users
ADD CONSTRAINT fk_users_auth
FOREIGN KEY (id)
REFERENCES auth.users(id)
ON DELETE CASCADE;

-- =====================================================
-- ÍNDICES
-- =====================================================

CREATE INDEX idx_users_role
ON public.users(role);

CREATE INDEX idx_users_active
ON public.users(active);

CREATE INDEX idx_users_name
ON public.users(full_name);

-- =====================================================
-- COMENTÁRIOS
-- =====================================================

COMMENT ON TABLE public.users IS
'Tabela principal contendo informações comuns a todos os usuários da plataforma.';

COMMENT ON COLUMN public.users.id IS
'Mesmo UUID gerado pelo Supabase Auth (auth.users.id).';

COMMENT ON COLUMN public.users.role IS
'Perfil do usuário: PATIENT, PROFESSIONAL ou ADMIN.';

COMMENT ON COLUMN public.users.provider IS
'Provedor utilizado para autenticação.';

COMMENT ON COLUMN public.users.active IS
'Indica se o usuário está ativo na plataforma.';


-- =====================================================
-- TABELA PATIENTS
-- =====================================================

CREATE TABLE public.patients (

    user_id UUID PRIMARY KEY,

    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),

    updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),

    CONSTRAINT fk_patients_user
        FOREIGN KEY (user_id)
        REFERENCES public.users(id)
        ON DELETE CASCADE

);

-- =====================================================
-- COMENTÁRIOS
-- =====================================================

COMMENT ON TABLE public.patients IS
'Tabela contendo informações específicas dos pacientes.';

COMMENT ON COLUMN public.patients.user_id IS
'Identificador do usuário (users.id).';


-- =====================================================
-- TABELA PROFESSIONALS
-- =====================================================

CREATE TABLE public.professionals (

    user_id UUID PRIMARY KEY,

    crp VARCHAR(30) NOT NULL UNIQUE,

    biography TEXT,

    therapeutic_approach VARCHAR(150),

    experience_years SMALLINT,

    status professional_status
        NOT NULL DEFAULT 'ACTIVE',

    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),

    updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),

    CONSTRAINT fk_professionals_user
        FOREIGN KEY (user_id)
        REFERENCES public.users(id)
        ON DELETE CASCADE,

    CONSTRAINT chk_experience_years
        CHECK (experience_years >= 0)

);

-- =====================================================
-- ÍNDICES
-- =====================================================

CREATE INDEX idx_professionals_status
ON public.professionals(status);

CREATE INDEX idx_professionals_crp
ON public.professionals(crp);

CREATE INDEX idx_professionals_approach
ON public.professionals(therapeutic_approach);

-- =====================================================
-- COMENTÁRIOS
-- =====================================================

COMMENT ON TABLE public.professionals IS
'Tabela contendo informações específicas dos profissionais.';

COMMENT ON COLUMN public.professionals.user_id IS
'Identificador do usuário (users.id).';

COMMENT ON COLUMN public.professionals.crp IS
'Registro profissional do psicólogo.';

COMMENT ON COLUMN public.professionals.biography IS
'Biografia apresentada no perfil público.';

COMMENT ON COLUMN public.professionals.therapeutic_approach IS
'Abordagem terapêutica utilizada pelo profissional.';

COMMENT ON COLUMN public.professionals.experience_years IS
'Tempo de experiência profissional em anos.';

COMMENT ON COLUMN public.professionals.status IS
'Status atual do profissional na plataforma.';


-- =====================================================
-- TABELA SPECIALTIES
-- =====================================================

CREATE TABLE public.specialties (

    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),

    name VARCHAR(100) NOT NULL,

    description TEXT,

    active BOOLEAN NOT NULL DEFAULT TRUE,

    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),

    updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),

    CONSTRAINT uk_specialties_name
        UNIQUE (name)

);

-- =====================================================
-- ÍNDICES
-- =====================================================

CREATE INDEX idx_specialties_name
ON public.specialties(name);

CREATE INDEX idx_specialties_active
ON public.specialties(active);

-- =====================================================
-- COMENTÁRIOS
-- =====================================================

COMMENT ON TABLE public.specialties IS
'Catálogo de especialidades disponíveis para os profissionais.';

COMMENT ON COLUMN public.specialties.id IS
'Identificador único da especialidade.';

COMMENT ON COLUMN public.specialties.name IS
'Nome da especialidade.';

COMMENT ON COLUMN public.specialties.description IS
'Descrição opcional da especialidade.';

COMMENT ON COLUMN public.specialties.active IS
'Indica se a especialidade está disponível para utilização.';


-- =====================================================
-- TABELA PROFESSIONAL_SPECIALTIES
-- =====================================================

CREATE TABLE public.professional_specialties (

    professional_id UUID NOT NULL,

    specialty_id UUID NOT NULL,

    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),

    CONSTRAINT pk_professional_specialties
        PRIMARY KEY (professional_id, specialty_id),

    CONSTRAINT fk_professional_specialties_professional
        FOREIGN KEY (professional_id)
        REFERENCES public.professionals(user_id)
        ON DELETE CASCADE,

    CONSTRAINT fk_professional_specialties_specialty
        FOREIGN KEY (specialty_id)
        REFERENCES public.specialties(id)
        ON DELETE RESTRICT

);

-- =====================================================
-- ÍNDICES
-- =====================================================

CREATE INDEX idx_professional_specialties_professional
ON public.professional_specialties(professional_id);

CREATE INDEX idx_professional_specialties_specialty
ON public.professional_specialties(specialty_id);

-- =====================================================
-- COMENTÁRIOS
-- =====================================================

COMMENT ON TABLE public.professional_specialties IS
'Tabela de relacionamento entre profissionais e especialidades.';

COMMENT ON COLUMN public.professional_specialties.professional_id IS
'Profissional associado à especialidade.';

COMMENT ON COLUMN public.professional_specialties.specialty_id IS
'Especialidade vinculada ao profissional.';


-- =====================================================
-- TABELA CONSULTATION_TYPES
-- =====================================================

CREATE TABLE public.consultation_types (

    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),

    professional_id UUID NOT NULL,

    name VARCHAR(100) NOT NULL,

    description TEXT,

    duration_minutes SMALLINT NOT NULL,

    price NUMERIC(10,2) NOT NULL,

    active BOOLEAN NOT NULL DEFAULT TRUE,

    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),

    updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),

    CONSTRAINT fk_consultation_types_professional
        FOREIGN KEY (professional_id)
        REFERENCES public.professionals(user_id)
        ON DELETE CASCADE,

    CONSTRAINT chk_duration_minutes
        CHECK (duration_minutes > 0),

    CONSTRAINT chk_price
        CHECK (price >= 0),

    CONSTRAINT uk_consultation_type_name
        UNIQUE (professional_id, name)

);

-- =====================================================
-- ÍNDICES
-- =====================================================

CREATE INDEX idx_consultation_types_professional
ON public.consultation_types(professional_id);

CREATE INDEX idx_consultation_types_active
ON public.consultation_types(active);

CREATE INDEX idx_consultation_types_name
ON public.consultation_types(name);

-- =====================================================
-- COMENTÁRIOS
-- =====================================================

COMMENT ON TABLE public.consultation_types IS
'Tipos de consulta oferecidos por cada profissional.';

COMMENT ON COLUMN public.consultation_types.professional_id IS
'Profissional proprietário do tipo de consulta.';

COMMENT ON COLUMN public.consultation_types.name IS
'Nome do tipo de consulta (Individual, Casal, Infantil, etc.).';

COMMENT ON COLUMN public.consultation_types.description IS
'Descrição opcional do tipo de consulta.';

COMMENT ON COLUMN public.consultation_types.duration_minutes IS
'Duração da consulta em minutos.';

COMMENT ON COLUMN public.consultation_types.price IS
'Valor cobrado pelo profissional para este tipo de consulta.';

COMMENT ON COLUMN public.consultation_types.active IS
'Indica se o tipo de consulta está disponível para agendamento.';


-- =====================================================
-- TABELA PROFESSIONAL_AVAILABILITY
-- =====================================================

CREATE TABLE public.professional_availability (

    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),

    professional_id UUID NOT NULL,

    weekday weekday_enum NOT NULL,

    start_time TIME NOT NULL,

    end_time TIME NOT NULL,

    active BOOLEAN NOT NULL DEFAULT TRUE,

    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),

    updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),

    CONSTRAINT fk_professional_availability_professional
        FOREIGN KEY (professional_id)
        REFERENCES public.professionals(user_id)
        ON DELETE CASCADE,

    CONSTRAINT chk_availability_time
        CHECK (start_time < end_time)

);

-- =====================================================
-- ÍNDICES
-- =====================================================

CREATE INDEX idx_professional_availability_professional
ON public.professional_availability(professional_id);

CREATE INDEX idx_professional_availability_weekday
ON public.professional_availability(weekday);

CREATE INDEX idx_professional_availability_active
ON public.professional_availability(active);

-- =====================================================
-- COMENTÁRIOS
-- =====================================================

COMMENT ON TABLE public.professional_availability IS
'Períodos recorrentes de disponibilidade do profissional para atendimento.';

COMMENT ON COLUMN public.professional_availability.professional_id IS
'Profissional proprietário da disponibilidade.';

COMMENT ON COLUMN public.professional_availability.weekday IS
'Dia da semana em que a disponibilidade se repete.';

COMMENT ON COLUMN public.professional_availability.start_time IS
'Horário inicial do período disponível.';

COMMENT ON COLUMN public.professional_availability.end_time IS
'Horário final do período disponível.';

COMMENT ON COLUMN public.professional_availability.active IS
'Indica se o período está disponível para agendamentos.';


-- =====================================================
-- TABELA BLOCKED_DATES
-- =====================================================

CREATE TABLE public.blocked_dates (

    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),

    professional_id UUID NOT NULL,

    blocked_date DATE NOT NULL,

    start_time TIME,

    end_time TIME,

    is_full_day BOOLEAN NOT NULL DEFAULT TRUE,

    reason VARCHAR(255),

    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),

    updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),

    CONSTRAINT fk_blocked_dates_professional
        FOREIGN KEY (professional_id)
        REFERENCES public.professionals(user_id)
        ON DELETE CASCADE,

    CONSTRAINT chk_blocked_time
        CHECK (
            (is_full_day = TRUE AND start_time IS NULL AND end_time IS NULL)
            OR
            (is_full_day = FALSE
                AND start_time IS NOT NULL
                AND end_time IS NOT NULL
                AND start_time < end_time)
        )

);

-- =====================================================
-- ÍNDICES
-- =====================================================

CREATE INDEX idx_blocked_dates_professional
ON public.blocked_dates(professional_id);

CREATE INDEX idx_blocked_dates_date
ON public.blocked_dates(blocked_date);

CREATE INDEX idx_blocked_dates_full_day
ON public.blocked_dates(is_full_day);

-- =====================================================
-- COMENTÁRIOS
-- =====================================================

COMMENT ON TABLE public.blocked_dates IS
'Bloqueios pontuais na agenda do profissional para um dia inteiro ou um período específico.';

COMMENT ON COLUMN public.blocked_dates.professional_id IS
'Profissional responsável pelo bloqueio.';

COMMENT ON COLUMN public.blocked_dates.blocked_date IS
'Data em que o bloqueio ocorrerá.';

COMMENT ON COLUMN public.blocked_dates.start_time IS
'Horário inicial do bloqueio quando não for um bloqueio de dia inteiro.';

COMMENT ON COLUMN public.blocked_dates.end_time IS
'Horário final do bloqueio quando não for um bloqueio de dia inteiro.';

COMMENT ON COLUMN public.blocked_dates.is_full_day IS
'Indica se o bloqueio é para o dia inteiro.';

COMMENT ON COLUMN public.blocked_dates.reason IS
'Motivo opcional do bloqueio.';


-- =====================================================
-- TABELA ABSENCES
-- =====================================================

CREATE TABLE public.absences (

    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),

    professional_id UUID NOT NULL,

    type absence_type NOT NULL,

    start_date DATE NOT NULL,

    end_date DATE NOT NULL,

    reason TEXT,

    status absence_status NOT NULL DEFAULT 'PENDING',

    reviewed_by UUID,

    reviewed_at TIMESTAMPTZ,

    review_notes TEXT,

    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),

    updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),

    CONSTRAINT fk_absence_professional
        FOREIGN KEY (professional_id)
        REFERENCES public.professionals(user_id)
        ON DELETE CASCADE,

    CONSTRAINT fk_absence_reviewed_by
        FOREIGN KEY (reviewed_by)
        REFERENCES public.users(id)
        ON DELETE SET NULL,

    CONSTRAINT chk_absence_dates
        CHECK (start_date <= end_date)

);

-- =====================================================
-- ÍNDICES
-- =====================================================

CREATE INDEX idx_absences_professional
ON public.absences(professional_id);

CREATE INDEX idx_absences_status
ON public.absences(status);

CREATE INDEX idx_absences_type
ON public.absences(type);

CREATE INDEX idx_absences_period
ON public.absences(start_date, end_date);

-- =====================================================
-- COMENTÁRIOS
-- =====================================================

COMMENT ON TABLE public.absences IS
'Registra solicitações de férias e ausências temporárias dos profissionais.';

COMMENT ON COLUMN public.absences.professional_id IS
'Profissional solicitante da ausência.';

COMMENT ON COLUMN public.absences.type IS
'Tipo da ausência: férias ou indisponibilidade temporária.';

COMMENT ON COLUMN public.absences.start_date IS
'Data inicial da ausência.';

COMMENT ON COLUMN public.absences.end_date IS
'Data final da ausência.';

COMMENT ON COLUMN public.absences.reason IS
'Justificativa informada pelo profissional.';

COMMENT ON COLUMN public.absences.status IS
'Status da solicitação.';

COMMENT ON COLUMN public.absences.reviewed_by IS
'Administrador responsável pela análise da solicitação.';

COMMENT ON COLUMN public.absences.reviewed_at IS
'Data e hora da análise da solicitação.';

COMMENT ON COLUMN public.absences.review_notes IS
'Observações do administrador durante a análise.';


-- =====================================================
-- TABELA APPOINTMENTS
-- =====================================================

CREATE TABLE public.appointments (

    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),

    patient_id UUID NOT NULL,

    professional_id UUID NOT NULL,

    consultation_type_id UUID NOT NULL,

    consultation_name VARCHAR(100) NOT NULL,

    consultation_duration SMALLINT NOT NULL,

    consultation_price NUMERIC(10,2) NOT NULL,

    start_datetime TIMESTAMPTZ NOT NULL,

    end_datetime TIMESTAMPTZ NOT NULL,

    status appointment_status NOT NULL DEFAULT 'SCHEDULED',

    patient_confirmed BOOLEAN NOT NULL DEFAULT FALSE,

    patient_confirmed_at TIMESTAMPTZ,

    professional_confirmed BOOLEAN NOT NULL DEFAULT FALSE,

    professional_confirmed_at TIMESTAMPTZ,

    cancelled_by cancelled_by_type,

    cancelled_at TIMESTAMPTZ,

    cancellation_reason TEXT,

    no_show_by no_show_type,

    notes TEXT,

    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),

    updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),

    CONSTRAINT fk_appointment_patient
        FOREIGN KEY (patient_id)
        REFERENCES public.patients(user_id)
        ON DELETE RESTRICT,

    CONSTRAINT fk_appointment_professional
        FOREIGN KEY (professional_id)
        REFERENCES public.professionals(user_id)
        ON DELETE RESTRICT,

    CONSTRAINT fk_appointment_consultation_type
        FOREIGN KEY (consultation_type_id)
        REFERENCES public.consultation_types(id)
        ON DELETE RESTRICT,

    CONSTRAINT chk_appointment_datetime
        CHECK (start_datetime < end_datetime),

    CONSTRAINT chk_consultation_duration
        CHECK (consultation_duration > 0),

    CONSTRAINT chk_consultation_price
        CHECK (consultation_price >= 0)

);

-- =====================================================
-- ÍNDICES
-- =====================================================

CREATE INDEX idx_appointments_patient
ON public.appointments(patient_id);

CREATE INDEX idx_appointments_professional
ON public.appointments(professional_id);

CREATE INDEX idx_appointments_status
ON public.appointments(status);

CREATE INDEX idx_appointments_start
ON public.appointments(start_datetime);

CREATE INDEX idx_appointments_professional_start
ON public.appointments(professional_id, start_datetime);

CREATE INDEX idx_appointments_patient_start
ON public.appointments(patient_id, start_datetime);

COMMENT ON TABLE public.appointments IS
'Agendamentos de consultas entre pacientes e profissionais.';

COMMENT ON COLUMN public.appointments.consultation_name IS
'Nome do tipo de consulta no momento do agendamento.';

COMMENT ON COLUMN public.appointments.consultation_duration IS
'Duração da consulta em minutos no momento do agendamento.';

COMMENT ON COLUMN public.appointments.consultation_price IS
'Preço da consulta no momento do agendamento.';

COMMENT ON COLUMN public.appointments.status IS
'Estado atual da consulta.';

COMMENT ON COLUMN public.appointments.patient_confirmed IS
'Indica se o paciente confirmou presença.';

COMMENT ON COLUMN public.appointments.professional_confirmed IS
'Indica se o profissional confirmou a consulta.';

COMMENT ON COLUMN public.appointments.cancelled_by IS
'Quem realizou o cancelamento da consulta.';

COMMENT ON COLUMN public.appointments.no_show_by IS
'Parte que não compareceu à consulta.';


-- =====================================================
-- TABELA MEDICAL_RECORDS
-- =====================================================

CREATE TABLE public.medical_records (

    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),

    appointment_id UUID NOT NULL,

    patient_id UUID NOT NULL,

    professional_id UUID NOT NULL,

    evolution_number INTEGER NOT NULL DEFAULT 1,

    clinical_notes TEXT NOT NULL,

    is_valid BOOLEAN NOT NULL DEFAULT TRUE,

    invalidated_reason TEXT,

    invalidated_by UUID,

    invalidated_at TIMESTAMPTZ,

    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),

    updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),

    CONSTRAINT fk_medical_record_appointment
        FOREIGN KEY (appointment_id)
        REFERENCES public.appointments(id)
        ON DELETE CASCADE,

    CONSTRAINT fk_medical_record_patient
        FOREIGN KEY (patient_id)
        REFERENCES public.patients(user_id)
        ON DELETE RESTRICT,

    CONSTRAINT fk_medical_record_professional
        FOREIGN KEY (professional_id)
        REFERENCES public.professionals(user_id)
        ON DELETE RESTRICT,

    CONSTRAINT fk_medical_record_invalidated_by
        FOREIGN KEY (invalidated_by)
        REFERENCES public.users(id)
        ON DELETE SET NULL,

    CONSTRAINT chk_evolution_number
        CHECK (evolution_number > 0)

);

-- =====================================================
-- ÍNDICES
-- =====================================================

CREATE INDEX idx_medical_records_patient
ON public.medical_records(patient_id);

CREATE INDEX idx_medical_records_professional
ON public.medical_records(professional_id);

CREATE INDEX idx_medical_records_appointment
ON public.medical_records(appointment_id);

CREATE INDEX idx_medical_records_valid
ON public.medical_records(is_valid);

COMMENT ON TABLE public.medical_records IS
'Evoluções clínicas registradas pelos profissionais após as consultas.';

COMMENT ON COLUMN public.medical_records.appointment_id IS
'Consulta à qual esta evolução pertence.';

COMMENT ON COLUMN public.medical_records.patient_id IS
'Paciente da evolução clínica.';

COMMENT ON COLUMN public.medical_records.professional_id IS
'Profissional responsável pelo registro.';

COMMENT ON COLUMN public.medical_records.evolution_number IS
'Número sequencial da evolução clínica para a consulta.';

COMMENT ON COLUMN public.medical_records.clinical_notes IS
'Conteúdo da evolução clínica.';

COMMENT ON COLUMN public.medical_records.is_valid IS
'Indica se a evolução está válida ou foi invalidada.';

COMMENT ON COLUMN public.medical_records.invalidated_reason IS
'Motivo da invalidação da evolução clínica.';


-- =====================================================
-- TABELA CONVERSATIONS
-- =====================================================

CREATE TABLE public.conversations (

    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),

    patient_id UUID NOT NULL,

    professional_id UUID NOT NULL,

    last_message_at TIMESTAMPTZ,

    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),

    updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),

    CONSTRAINT fk_conversation_patient
        FOREIGN KEY (patient_id)
        REFERENCES public.patients(user_id)
        ON DELETE CASCADE,

    CONSTRAINT fk_conversation_professional
        FOREIGN KEY (professional_id)
        REFERENCES public.professionals(user_id)
        ON DELETE CASCADE,

    CONSTRAINT uk_conversation
        UNIQUE (patient_id, professional_id)

);

-- =====================================================
-- ÍNDICES
-- =====================================================

CREATE INDEX idx_conversations_patient
ON public.conversations(patient_id);

CREATE INDEX idx_conversations_professional
ON public.conversations(professional_id);

CREATE INDEX idx_conversations_last_message
ON public.conversations(last_message_at);

COMMENT ON TABLE public.conversations IS
'Representa um canal de conversa entre um paciente e um profissional.';

COMMENT ON COLUMN public.conversations.patient_id IS
'Paciente participante da conversa.';

COMMENT ON COLUMN public.conversations.professional_id IS
'Profissional participante da conversa.';

COMMENT ON COLUMN public.conversations.last_message_at IS
'Data e hora da última mensagem enviada na conversa.';


-- =====================================================
-- TABELA MESSAGES
-- =====================================================

CREATE TABLE public.messages (

    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),

    conversation_id UUID NOT NULL,

    sender_id UUID NOT NULL,

    receiver_id UUID NOT NULL,

    message_type message_type NOT NULL DEFAULT 'TEXT',

    content TEXT NOT NULL,

    is_read BOOLEAN NOT NULL DEFAULT FALSE,

    read_at TIMESTAMPTZ,

    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),

    updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),

    CONSTRAINT fk_messages_conversation
        FOREIGN KEY (conversation_id)
        REFERENCES public.conversations(id)
        ON DELETE CASCADE,

    CONSTRAINT fk_messages_sender
        FOREIGN KEY (sender_id)
        REFERENCES public.users(id)
        ON DELETE RESTRICT,

    CONSTRAINT fk_messages_receiver
        FOREIGN KEY (receiver_id)
        REFERENCES public.users(id)
        ON DELETE RESTRICT,

    CONSTRAINT chk_sender_receiver
        CHECK (sender_id <> receiver_id)

);

-- =====================================================
-- ÍNDICES
-- =====================================================

CREATE INDEX idx_messages_conversation
ON public.messages(conversation_id);

CREATE INDEX idx_messages_sender
ON public.messages(sender_id);

CREATE INDEX idx_messages_receiver
ON public.messages(receiver_id);

CREATE INDEX idx_messages_created
ON public.messages(created_at);

CREATE INDEX idx_messages_unread
ON public.messages(receiver_id, is_read);

COMMENT ON TABLE public.messages IS
'Mensagens trocadas entre pacientes e profissionais.';

COMMENT ON COLUMN public.messages.conversation_id IS
'Conversa à qual a mensagem pertence.';

COMMENT ON COLUMN public.messages.sender_id IS
'Usuário que enviou a mensagem.';

COMMENT ON COLUMN public.messages.receiver_id IS
'Usuário destinatário da mensagem.';

COMMENT ON COLUMN public.messages.message_type IS
'Tipo da mensagem.';

COMMENT ON COLUMN public.messages.content IS
'Conteúdo da mensagem.';

COMMENT ON COLUMN public.messages.is_read IS
'Indica se a mensagem foi lida pelo destinatário.';

COMMENT ON COLUMN public.messages.read_at IS
'Data e hora em que a mensagem foi lida.';


-- =====================================================
-- TABELA NOTIFICATIONS
-- =====================================================

CREATE TABLE public.notifications (

    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),

    user_id UUID NOT NULL,

    type notification_type NOT NULL,

    title VARCHAR(150) NOT NULL,

    message TEXT NOT NULL,

    reference_type notification_reference_type,

    reference_id UUID,

    read_at TIMESTAMPTZ,

    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),

    updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),

    CONSTRAINT fk_notifications_user
        FOREIGN KEY (user_id)
        REFERENCES public.users(id)
        ON DELETE CASCADE

);

-- =====================================================
-- ÍNDICES
-- =====================================================

CREATE INDEX idx_notifications_user
ON public.notifications(user_id);

CREATE INDEX idx_notifications_created_at
ON public.notifications(created_at DESC);

CREATE INDEX idx_notifications_unread
ON public.notifications(user_id, read_at);

CREATE INDEX idx_notifications_reference
ON public.notifications(reference_type, reference_id);

-- =====================================================
-- COMENTÁRIOS
-- =====================================================

COMMENT ON TABLE public.notifications IS
'Notificações enviadas aos usuários da plataforma.';

COMMENT ON COLUMN public.notifications.user_id IS
'Usuário destinatário da notificação.';

COMMENT ON COLUMN public.notifications.type IS
'Tipo da notificação.';

COMMENT ON COLUMN public.notifications.title IS
'Título da notificação.';

COMMENT ON COLUMN public.notifications.message IS
'Conteúdo da notificação.';

COMMENT ON COLUMN public.notifications.reference_type IS
'Tipo do recurso relacionado à notificação.';

COMMENT ON COLUMN public.notifications.reference_id IS
'Identificador do recurso relacionado.';

COMMENT ON COLUMN public.notifications.read_at IS
'Data e hora em que a notificação foi visualizada.';


-- =====================================================
-- TABELA AUDIT_LOGS
-- =====================================================

CREATE TABLE public.audit_logs (

    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),

    user_id UUID NOT NULL,

    action audit_action NOT NULL,

    entity_name VARCHAR(50) NOT NULL,

    entity_id UUID,

    description TEXT,

    ip_address INET,

    user_agent TEXT,

    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),

    CONSTRAINT fk_audit_logs_user
        FOREIGN KEY (user_id)
        REFERENCES public.users(id)
        ON DELETE RESTRICT

);

-- =====================================================
-- ÍNDICES
-- =====================================================

CREATE INDEX idx_audit_logs_user
ON public.audit_logs(user_id);

CREATE INDEX idx_audit_logs_action
ON public.audit_logs(action);

CREATE INDEX idx_audit_logs_entity
ON public.audit_logs(entity_name, entity_id);

CREATE INDEX idx_audit_logs_created_at
ON public.audit_logs(created_at DESC);

-- =====================================================
-- COMENTÁRIOS
-- =====================================================

COMMENT ON TABLE public.audit_logs IS
'Tabela de auditoria das ações relevantes realizadas pelos usuários.';

COMMENT ON COLUMN public.audit_logs.entity_name IS
'Nome da entidade afetada (appointments, messages, medical_records etc.).';

COMMENT ON COLUMN public.audit_logs.entity_id IS
'UUID da entidade afetada.';


-- =====================================================
-- TABELA AUTHORIZED_PROFESSIONAL_EMAILS
-- =====================================================

CREATE TABLE public.authorized_professional_emails (

    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),

    email VARCHAR(255) NOT NULL UNIQUE,

    authorized_by UUID NOT NULL,

    authorized_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),

    used BOOLEAN NOT NULL DEFAULT FALSE,

    used_at TIMESTAMPTZ,

    created_user_id UUID,

    observations TEXT,

    CONSTRAINT fk_authorized_professional_emails_admin
        FOREIGN KEY (authorized_by)
        REFERENCES public.users(id)
        ON DELETE RESTRICT,

    CONSTRAINT fk_authorized_professional_emails_created_user
        FOREIGN KEY (created_user_id)
        REFERENCES public.users(id)
        ON DELETE SET NULL

);

-- =====================================================
-- ÍNDICES
-- =====================================================

CREATE INDEX idx_authorized_professional_email
ON public.authorized_professional_emails(email);

CREATE INDEX idx_authorized_professional_used
ON public.authorized_professional_emails(used);

-- =====================================================
-- COMENTÁRIOS
-- =====================================================

COMMENT ON TABLE public.authorized_professional_emails IS
'Lista de e-mails autorizados para cadastro de profissionais.';

COMMENT ON COLUMN public.authorized_professional_emails.authorized_by IS
'Administrador responsável pela autorização do e-mail.';

COMMENT ON COLUMN public.authorized_professional_emails.created_user_id IS
'Usuário criado após utilizar este e-mail autorizado.';


-- =====================================================
-- TABELA SYSTEM_SETTINGS
-- =====================================================

CREATE TABLE public.system_settings (

    key VARCHAR(100) PRIMARY KEY,

    value TEXT NOT NULL,

    description TEXT,

    updated_by UUID,

    updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),

    CONSTRAINT fk_system_settings_updated_by
        FOREIGN KEY (updated_by)
        REFERENCES public.users(id)
        ON DELETE SET NULL

);

-- =====================================================
-- COMENTÁRIOS
-- =====================================================

COMMENT ON TABLE public.system_settings IS
'Configurações gerais da plataforma.';

COMMENT ON COLUMN public.system_settings.key IS
'Nome único da configuração.';

COMMENT ON COLUMN public.system_settings.value IS
'Valor da configuração armazenado em formato texto.';

COMMENT ON COLUMN public.system_settings.updated_by IS
'Administrador que realizou a última alteração.';

-- =====================================================
-- CONFIGURAÇÕES INICIAIS
-- =====================================================

INSERT INTO public.system_settings (key, value, description)
VALUES
('appointment_confirmation_hours', '24', 'Prazo para confirmação da presença na consulta'),

('appointment_reminder_hours', '24', 'Antecedência para envio do lembrete da consulta'),

('max_cancel_hours', '24', 'Prazo mínimo para cancelamento da consulta'),

('max_reschedule_hours', '24', 'Prazo mínimo para reagendamento da consulta'),

('session_timeout_minutes', '30', 'Tempo máximo de inatividade da sessão'),

('email_notifications_enabled', 'true', 'Habilita envio de notificações por e-mail'),

('system_name', 'EmoCare 2.0', 'Nome da plataforma');