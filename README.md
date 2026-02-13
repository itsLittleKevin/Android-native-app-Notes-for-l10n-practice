# Notes - Android Native App for L10n Practice

A functional Android note-taking application designed as a training ground for software localization (l10n) and internationalization (i18n).

## Project Purpose

This repository contains a fully working Android application built with Jetpack Compose. While the app is feature-complete—supporting note creation, deletion, tagging, search, and appearance settings—it has been intentionally developed with numerous "localization pitfalls."

The primary goal for students is to identify these hard-coded elements and refactor the codebase to support multiple languages, regions, and cultural conventions following Android's best practices.

<table style="width: 100%;">
  <tr>
    <td align="center" width="33%">
      <img src="https://github.com/user-attachments/assets/44984430-072d-4ee4-93cb-ffe6c8f3b690" alt="Icon" style="width:100%;" >
    </td>
    <td align="center" width="33%">
      <img src="https://github.com/user-attachments/assets/3122b771-68dc-4cff-be17-ee8eba3e947b" alt="Homepage" style="width:100%;" >
    </td>
    <td align="center" width="33%">
      <img src="https://github.com/user-attachments/assets/df3bee2a-ed4a-41b3-8b7b-ebb350364649" alt="Settings" style="width:100%;" >
    </td>
  </tr>
</table>






## Features

- **Note Management:** Create, edit, and delete notes.
- **Search & Filter:** Real-time search and multiple sorting options.
- **Organization:** Tagging system for categorizing notes.
- **Customization:** Adjustable font sizes, theme colors, and Dark Mode support.
- **View Modes:** Toggle between List and Grid views.
- **Import/Export:** Placeholder functionality for data management.

## Training Objectives

Students working on this project will gain experience in:
- Extracting hard-coded UI strings to resource files.
- Implementing plural support for different languages.
- Handling locale-aware date and time formatting.
- Managing localized assets and adaptive icons.
- Adapting layouts for text expansion and RTL (Right-to-Left) languages.
- Decoupling logic-critical strings from presentation layers.

## Getting Started

1. Clone the repository.
2. Open the project in Android Studio.
3. Build and run the app on an emulator or physical device.
4. Begin the localization audit!

---
*Developed for educational purposes*
