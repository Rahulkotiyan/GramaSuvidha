const SUPABASE_URL = "https://uqgeyezfbcwxfoksglrd.supabase.co";
const SUPABASE_ANON_KEY = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6InVxZ2V5ZXpmYmN3eGZva3NnbHJkIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NzgzODg2OTgsImV4cCI6MjA5Mzk2NDY5OH0.VfaJg4O3FF1FWJpR9VZZ2MuAVLUBMv_PmlJXCajOtfI";

// Initialize the Supabase client
const supabaseClient = supabase.createClient(SUPABASE_URL, SUPABASE_ANON_KEY);
