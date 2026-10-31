-- ====================================================================
-- FinSense: Supabase PostgreSQL Schema & Migration Script
-- Compatible with Supabase PostgREST & Row Level Security (RLS)
-- ====================================================================

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
    beneficiary_type TEXT NOT NULL DEFAULT 'SELF', -- 'SELF' | 'SENIOR_CITIZEN_PARENTS' | 'FAMILY'
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
    status TEXT NOT NULL DEFAULT 'ACTIVE', -- 'ACTIVE' | 'APPLIED' | 'DISMISSED'
    created_at TIMESTAMPTZ NOT NULL DEFAULT timezone('utc'::text, now())
);

-- 4. Copilot Chat History Table
CREATE TABLE IF NOT EXISTS public.copilot_chat (
    id TEXT PRIMARY KEY DEFAULT uuid_generate_v4()::text,
    session_id TEXT NOT NULL DEFAULT 'session_main',
    sender TEXT NOT NULL DEFAULT 'user', -- 'user' | 'copilot'
    message_text TEXT NOT NULL,
    card_type TEXT DEFAULT 'NONE',
    card_data TEXT,
    language_code TEXT NOT NULL DEFAULT 'en-IN',
    created_at TIMESTAMPTZ NOT NULL DEFAULT timezone('utc'::text, now())
);

-- 5. Insurance & Loan Leads / Applications Table
CREATE TABLE IF NOT EXISTS public.insurance_applications (
    id TEXT PRIMARY KEY DEFAULT uuid_generate_v4()::text,
    applicant_name TEXT NOT NULL,
    beneficiary_relation TEXT NOT NULL DEFAULT 'Senior Citizen Parent',
    plan_type TEXT NOT NULL,
    cover_amount TEXT NOT NULL,
    annual_premium TEXT NOT NULL,
    tax_saving_80d TEXT NOT NULL,
    status TEXT NOT NULL DEFAULT 'SUBMITTED', -- 'SUBMITTED' | 'UNDERWRITING' | 'ISSUED'
    created_at TIMESTAMPTZ NOT NULL DEFAULT timezone('utc'::text, now())
);

-- ====================================================================
-- Row Level Security (RLS) Policies
-- Public access enabled for Anon Key in Hackathon Prototype mode
-- ====================================================================

ALTER TABLE public.transactions ENABLE ROW LEVEL SECURITY;
ALTER TABLE public.recommendations ENABLE ROW LEVEL SECURITY;
ALTER TABLE public.copilot_chat ENABLE ROW LEVEL SECURITY;
ALTER TABLE public.insurance_applications ENABLE ROW LEVEL SECURITY;

-- Allow anon read/write for prototype showcase
CREATE POLICY "Allow anon read transactions" ON public.transactions FOR SELECT USING (true);
CREATE POLICY "Allow anon insert transactions" ON public.transactions FOR INSERT WITH CHECK (true);
CREATE POLICY "Allow anon update transactions" ON public.transactions FOR UPDATE USING (true);

CREATE POLICY "Allow anon read recommendations" ON public.recommendations FOR SELECT USING (true);
CREATE POLICY "Allow anon update recommendations" ON public.recommendations FOR UPDATE USING (true);

CREATE POLICY "Allow anon read copilot_chat" ON public.copilot_chat FOR SELECT USING (true);
CREATE POLICY "Allow anon insert copilot_chat" ON public.copilot_chat FOR INSERT WITH CHECK (true);

CREATE POLICY "Allow anon read insurance_applications" ON public.insurance_applications FOR SELECT USING (true);
CREATE POLICY "Allow anon insert insurance_applications" ON public.insurance_applications FOR INSERT WITH CHECK (true);

-- ====================================================================
-- Initial Seed Data
-- ====================================================================

INSERT INTO public.transactions (id, title, subtitle, amount, is_credit, date, category, method, icon_type, beneficiary_type)
VALUES
    ('tx_apollo_01', 'Apollo Pharmacy', 'Healthcare & Medication (Parental Section 80D)', '₹4,200', false, 'Today, 2:30 PM', 'Healthcare', 'UPI • HDFC Bank', 'medical', 'SENIOR_CITIZEN_PARENTS'),
    ('tx_swiggy_02', 'Swiggy Gourmet', 'Dinner delivery', '₹850', false, 'Yesterday, 8:45 PM', 'Food & Dining', 'Paytm Wallet', 'restaurant', 'SELF'),
    ('tx_cred_03', 'Salary Credit', 'Monthly compensation credited', '₹1,25,000', true, '28 Oct, 9:00 AM', 'Income', 'NEFT • HDFC Bank', 'wallet', 'SELF'),
    ('tx_uber_04', 'Uber India', 'Airport ride', '₹720', false, '27 Oct, 6:15 PM', 'Transit', 'Paytm UPI', 'transit', 'SELF')
ON CONFLICT (id) DO NOTHING;

INSERT INTO public.recommendations (id, title, badge, savings_tag, description, target_screen, icon_type, section_80d_eligible, tax_deduction_amount)
VALUES
    ('rec_health_01', 'Section 80D Parental Health Shield', 'High Impact • 80D Tax', 'Save ₹15,600 Tax', 'Recent ₹4,200 Apollo Pharmacy payment detected for dependent senior citizen parent. Claim ₹50,000 tax deduction under Section 80D.', 'HEALTH_COVER_DETAILS', 'health', true, 50000),
    ('rec_savings_02', 'Auto-Sweep Liquid Yield', 'Yield Boost', '+₹1,840/mo', 'Move ₹42,000 idle current account balance to 7.1% overnight liquid fund with instant T+0 liquidity.', 'SMART_SAVINGS_DETAILS', 'yield', false, 0),
    ('rec_insurance_03', 'Comprehensive Two-Wheeler Cover', 'Renewal Due', 'Save ₹850', 'Your vehicle insurance expires in 12 days. Zero-depreciation cover starting at ₹649/yr with cashless claims.', 'INSURANCE_DISCOVERY', 'shield', false, 0)
ON CONFLICT (id) DO NOTHING;
