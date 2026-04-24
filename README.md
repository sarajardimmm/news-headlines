# 📰 News Headlines
Android app that shows top headlines from a specific news source using the NewsAPI.

This was built as part of a technical challenge. The goal was to implement the required features cleanly and reliably within a limited timeframe, without over-engineering.

## What it does
Shows a list of top headlines (sorted by date)
Each item displays title + image
Tap a headline to see details (image, description, content)
Supports portrait and landscape layouts
Pull-to-refresh
Handles loading and error states

## Running the app
**No setup (Demo mode)**
The app runs out-of-the-box without any configuration.

If no API key is provided, it uses a demo data source so the reviewer can still navigate the app and test the UI.

Using real data
Get a free API key from NewsAPI
Add it to your local.properties:
NEWS_API_KEY=your_api_key_here
Sync and run
Build flavors

The project includes different flavors to support multiple news sources (as requested in the assignment).
Running a different flavor will load a different source.

Tech choices
Kotlin + Jetpack Compose
Hilt for dependency injection
Retrofit for networking
Coil for image loading
Coroutines for async work

## Decisions & Tradeoffs

- Demo mode is used when no API key is provided to ensure the app runs without setup
- Relative time formatting is implemented for UX, although the API mostly returns older articles
- Architecture is kept intentionally simple (no offline cache/paging) to match the scope of the assignment
- No pagination added because the selected source returns a small, finite headline set.
The repository keeps sorting and refresh logic simple to match the assignment scope.

## Includes unit tests for:

Repository behavior
Mapping logic
ViewModel state

