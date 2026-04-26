# 📰 News Headlines
Android app that shows top headlines from a specific news source using the NewsAPI.

This was built as part of a technical challenge. The goal was to implement the required features cleanly and reliably within a limited timeframe, without over-engineering.

## What it does
- Top headlines list (sorted by date)
- Article detail screen (title, description, content, image if available)
- Pull-to-refresh
- Portrait and landscape support (adaptive layout)
- Handles loading and error states
- Image loading and caching
- Multiple build flavors (BBC / ABC)
- Demo mode (no API key required)
- Unit tests and Compose UI tests
- Biometric authentication on app launch when available and configured on the device

---

## Running the app
### **No setup (Demo mode)**
The app runs out-of-the-box without any configuration.

If no API key is provided, it uses a demo data source so the reviewer can still navigate the app and test the UI.

---

### Using real data
Get a free API key from NewsAPI
Add it to your local.properties:
NEWS_API_KEY=your_api_key_here
Sync and run
Build flavors

---

## Build flavors
The project includes two flavors:

bbc → loads BBC News headlines (red branding)
abc → loads ABC News headlines (dark branding)

Each flavor configures its own source and UI branding via resources.

---

## Tech stack
- Kotlin
- Jetpack Compose
- Material 3
- Hilt (Dependency Injection)
- Retrofit + Gson
- Coil (image loading)
- Coroutines
- JUnit + Compose UI Testing

---

## Decisions & Tradeoffs

- Demo mode is used when no API key is provided to ensure the app runs without setup
- Relative time formatting is implemented for UX, although the API mostly returns older articles
- Architecture is kept intentionally simple (no offline cache/paging) to match the scope of the assignment
- No pagination added because the selected source returns a small, finite headline set.
The repository keeps sorting and refresh logic simple to match the assignment scope.

---

## Includes unit tests for:

Includes:

Unit tests for:
date formatting
repository logic
ViewModel behavior
UI tests for:
headlines list rendering
item click behavior
error state
detail screen rendering
back navigation

# Screenshots
## BBC — Headlines (Portrait)
<img src="https://github.com/user-attachments/assets/83cde93c-0d83-4c1d-865c-4d0c8b2ff3b9" width="20%"/>

## BBC — Article Detail
<img src="https://github.com/user-attachments/assets/988d9f2f-d176-455d-923f-fa7f7971fb6a" width="20%"/>

## BBC — Larger Screen Layout
<img src="https://github.com/user-attachments/assets/5b42e1be-724c-4809-ab9c-8330852a93cc" width="20%"/>
<img src="https://github.com/user-attachments/assets/dd3c4198-dea5-4e51-be02-a010d23b7f5a" width="20%"/>
<img src="https://github.com/user-attachments/assets/f2f3484b-eb22-4788-917f-b17068c49e80" width="40%"/>

## ABC — Headlines (Portrait)
<img src="https://github.com/user-attachments/assets/7a87eabc-a977-4c46-9bad-ff7ba8ff8860" width="20%"/>

