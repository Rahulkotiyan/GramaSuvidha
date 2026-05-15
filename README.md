# GramaSuvidha 

**GramaSuvidha** is a comprehensive digital platform designed to bridge the gap between local governance and citizens in rural areas. It allows village administrations to track developmental projects, share important notices, and receive direct feedback from the community.

##  Mobile Application (Citizen Portal)
A modern Android application built for citizens to stay updated with their village's progress.

### Features
*   **Project Tracking**: View ongoing and completed developmental projects with real-time progress bars.
*   **Visual Evidence**: See "Before" and "After" photos of project sites to track actual growth.
*   **Notice Board**: Stay informed with categorized notices (Health, Agriculture, Education, General) with priority indicators.
*   **Bilingual Support**: Full support for both **English** and **Kannada** languages.
*   **Feedback System**: Citizens can provide ratings and detailed feedback on specific projects, supporting anonymous submissions for privacy.
*   **Pull-to-Refresh**: Native Material 3 integration for seamless data synchronization.

### Tech Stack
*   **Language**: Kotlin
*   **UI Framework**: Jetpack Compose (Material 3)
*   **Architecture**: MVVM (ViewModel, StateFlow)
*   **Networking**: Retrofit & OkHttp
*   **Image Loading**: Coil
*   **Backend**: Supabase (PostgreSQL)

---

##  Admin Dashboard (Governance Portal)
A sleek, responsive web interface for administrators to manage the village data.

### Features
*   **Dashboard Overview**: High-level statistics on notices, projects, and community satisfaction.
*   **Notice Management**: Create, edit, and delete notices with multi-language support.
*   **Project Management**: Track budgets, update progress percentages, and manage site photos.
*   **Integrated Storage**: Direct image upload to Supabase Storage, eliminating the need for external hosting.
*   **Feedback Moderation**: Review citizen feedback and update status (Pending, Reviewed, Resolved).

### Tech Stack
*   **Frontend**: HTML5, CSS3 (Vanilla), JavaScript (ES6+)
*   **Icons**: Lucide Icons
*   **Database & Auth**: Supabase JS SDK
*   **Hosting**: Compatible with Vercel, Netlify, or GitHub Pages.

---

##  Setup & Configuration

### Prerequisites
*   Android Studio (Ladybug or newer)
*   Supabase Project

### Backend Setup
1.  Create a new Supabase project.
2.  Run the provided SQL schemas for `projects`, `notices`, and `feedbacks` tables.
3.  Create a Public Bucket in **Storage** named `project-images`.
4.  Configure RLS (Row Level Security) policies to allow authenticated/public access as needed.

### Mobile App Configuration
Update your `RetrofitClient.kt` or `Constants.kt` with your Supabase URL and Anon Key:
```kotlin
val BASE_URL = "https://your-project-id.supabase.co/rest/v1/"
val SUPABASE_KEY = "your-anon-key"
```

### Dashboard Configuration
Update `supabase-config.js`:
```javascript
const SUPABASE_URL = 'https://your-project-id.supabase.co';
const SUPABASE_KEY = 'your-anon-key';
```

### Downloadable APK file link
https://drive.google.com/file/d/1ITQ7WTXtyaNn22CawIT1jU8qCHPvBFPT/view?usp=drive_link
---



##  License
This project is licensed under the MIT License - see the LICENSE file for details.

---
*Developed for rural empowerment.*
