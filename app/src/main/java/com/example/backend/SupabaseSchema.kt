package com.example.backend

object SupabaseSchema {
    const val SQL_MIGRATION_SCRIPT: String = """
-- 1. Enable UUID Extension
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- 2. Transactions Table
CREATE TABLE IF NOT EXISTS public.transactions (
    id TEXT PRIMARY KEY DEFAULT uuid_generate_v4()::text,
    user_id TEXT DEFAULT 'usr_default_01',
    title TEXT NOT NULL,
    subtitle TEXT NOT NULL,
    amount TEXT NOT NULL,
    is_credit BOOLEAN NOT NULL DEFAULT false,
    date TEXT NOT NULL,
    category TEXT NOT NULL,
    method TEXT NOT NULL,
    icon_type TEXT NOT NULL DEFAULT 'shopping',
    beneficiary_type TEXT NOT NULL DEFAULT 'SELF',
    created_at TIMESTAMPTZ NOT NULL DEFAULT timezone('utc'::text, now())
);

-- 3. Recommendations Table
CREATE TABLE IF NOT EXISTS public.recommendations (
    id TEXT PRIMARY KEY DEFAULT uuid_generate_v4()::text,
    user_id TEXT DEFAULT 'usr_default_01',
    title TEXT NOT NULL,
    badge TEXT NOT NULL,
    savings_tag TEXT NOT NULL,
    description TEXT NOT NULL,
    target_screen TEXT NOT NULL,
    icon_type TEXT NOT NULL DEFAULT 'health',
    section_80d_eligible BOOLEAN NOT NULL DEFAULT false,
    tax_deduction_amount NUMERIC NOT NULL DEFAULT 0,
    status TEXT NOT NULL DEFAULT 'ACTIVE',
    created_at TIMESTAMPTZ NOT NULL DEFAULT timezone('utc'::text, now())
);

-- 4. Copilot Chat History Table
CREATE TABLE IF NOT EXISTS public.copilot_chat (
    id TEXT PRIMARY KEY DEFAULT uuid_generate_v4()::text,
    session_id TEXT NOT NULL DEFAULT 'session_main',
    sender TEXT NOT NULL DEFAULT 'user',
    message_text TEXT NOT NULL,
    card_type TEXT DEFAULT 'NONE',
    card_data TEXT,
    language_code TEXT NOT NULL DEFAULT 'en-IN',
    created_at TIMESTAMPTZ NOT NULL DEFAULT timezone('utc'::text, now())
);

-- 5. Insurance & Leads Table
CREATE TABLE IF NOT EXISTS public.insurance_applications (
    id TEXT PRIMARY KEY DEFAULT uuid_generate_v4()::text,
    applicant_name TEXT NOT NULL,
    beneficiary_relation TEXT NOT NULL DEFAULT 'Senior Citizen Parent',
    plan_type TEXT NOT NULL,
    cover_amount TEXT NOT NULL,
    annual_premium TEXT NOT NULL,
    tax_saving_80d TEXT NOT NULL,
    status TEXT NOT NULL DEFAULT 'SUBMITTED',
    created_at TIMESTAMPTZ NOT NULL DEFAULT timezone('utc'::text, now())
);
"""

    val tables = listOf(
        "transactions (id, user_id, title, amount, category, beneficiary_type)",
        "recommendations (id, title, badge, savings_tag, section_80d_eligible)",
        "copilot_chat (id, session_id, sender, message_text, card_type)",
        "insurance_applications (id, applicant_name, beneficiary_relation, plan_type)"
    )
}
