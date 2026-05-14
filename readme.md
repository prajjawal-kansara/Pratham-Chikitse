# Pratham Chikitse

> **"First Aid in Your Pocket"** — An offline emergency first-aid guide for everyone, in Kannada and English.

---

## The Problem

In emergency situations like choking, burns, or snake bites, **the first 5 minutes are the most critical**. People panic, follow wrong home remedies, or simply don't know what to do — because medically accurate, step-by-step guidance isn't available in their local language, especially without internet.

---

## The Solution

Pratham Chikitse is a **100% offline Android app** that guides any person through 20 common medical emergencies using:

- Clear, numbered step-by-step instructions
- Bilingual content in **Kannada + English**
- **Audio mode** that reads instructions aloud so you can focus on the patient
- GPS-sorted list of the **3 nearest emergency hospitals**
- One-tap **108 Ambulance SOS button**

No internet. No login. No ads. Ready in under 500ms.

---

## Screenshots

| Home Screen | Step-by-Step Guide | Hospital Finder |
|---|---|---|
| Emergency tiles with severity color coding | ViewPager2 with Do's & Don'ts | GPS-sorted hospitals with call button |

---

## Features

### 🚨 Emergency SOS
One large red button at the top of the home screen. Single tap calls **108** (the national ambulance number). Works even mid-panic.

### 📋 20 Emergency Types
Each emergency has 3–5 medically accurate steps with:
- English instruction (large, bold, readable under stress)
- Kannada translation below each step
- Color-coded **Do** (green) and **Don't** (red) panels

Emergencies covered include:
`Snake Bite` `Heart Attack` `Burns` `Choking` `Fracture` `Severe Bleeding` `Drowning` `Seizure` `Poisoning` `Heat Stroke` and 10 more.

### 🔊 Audio Mode
A single large button toggles Text-To-Speech. Instructions are read aloud in **Kannada** (falls back to English if Kannada TTS is not installed on the device). Lets the helper focus entirely on the patient.

### 🏥 Hospital Finder
Uses device GPS to sort a list of real Bengaluru hospitals by distance. Shows:
- Distance in km
- Estimated drive time (based on 20 km/h city average)
- 24/7 open status
- Direct call button

Works fully offline — hospital data is stored in the APK. Falls back to Bengaluru city center coordinates if GPS is unavailable or denied.

### 🔍 Search
Real-time search bar filters emergencies by English name or Kannada name.

---

## Tech Stack

| Layer | Technology |
|---|---|
| Language | Java (Android) |
| Min SDK | API 24 (Android 7.0) |
| Target SDK | API 34 (Android 14) |
| UI Components | AndroidX + Material Design 3 |
| Step Navigation | ViewPager2 + FragmentStateAdapter |
| Emergency Grid | RecyclerView + GridLayoutManager |
| JSON Parsing | Gson |
| Text-To-Speech | Android TTS API (`android.speech.tts`) |
| GPS Location | Google Play Services — FusedLocationProviderClient |
| Offline Data | `res/raw/` JSON files bundled in APK |
| Build System | Gradle 8.0 |

---

## Project Structure

```
PrathamChikitse/
├── app/src/main/
│   ├── AndroidManifest.xml              # Permissions, Activity declarations
│   ├── java/com/pratham/chikitse/
│   │   ├── adapters/
│   │   │   ├── EmergencyAdapter.java    # RecyclerView adapter for home grid
│   │   │   ├── StepPagerAdapter.java    # ViewPager2 adapter for step pages
│   │   │   └── HospitalAdapter.java    # RecyclerView adapter for hospital list
│   │   ├── data/
│   │   │   ├── DataManager.java        # Singleton — loads & serves all JSON data
│   │   │   └── TTSManager.java         # Singleton — Kannada/English TTS engine
│   │   ├── models/
│   │   │   ├── Emergency.java          # Data model: emergency type + steps list
│   │   │   ├── Step.java               # Data model: one first-aid step
│   │   │   └── Hospital.java           # Data model: hospital + runtime GPS data
│   │   └── ui/
│   │       ├── home/
│   │       │   ├── SplashActivity.java  # Entry point — preloads data on bg thread
│   │       │   └── MainActivity.java   # Home screen: grid, SOS, search
│   │       ├── steps/
│   │       │   ├── StepDetailActivity.java  # ViewPager2 host + audio control
│   │       │   └── StepFragment.java        # One step page (instruction + Do/Don't)
│   │       └── hospital/
│   │           └── HospitalActivity.java    # GPS sort + hospital list
│   └── res/
│       ├── layout/                     # All XML screen layouts
│       ├── drawable/                   # Vector icons and shape drawables
│       ├── values/                     # colors.xml, themes.xml, strings.xml
│       └── raw/
│           ├── emergencies.json        # All 10+ emergencies with bilingual steps
│           └── hospitals.json          # 8 Bengaluru hospitals with coordinates
└── app/build.gradle                    # Dependencies and SDK config
```

---

## How It Works

### App Launch Flow
```
App opens → SplashActivity
         → Background thread reads emergencies.json + hospitals.json
         → Gson parses JSON into Java objects
         → DataManager stores them as singletons in memory
         → MainActivity starts (< 500ms after splash)
```

### Step-by-Step Flow
```
User taps tile → Emergency ID passed via Intent
              → StepDetailActivity fetches Emergency from DataManager
              → StepPagerAdapter creates one StepFragment per step
              → ViewPager2 handles swipe navigation
              → Audio button → TTSManager.speakStep() reads current step
```

### Hospital Distance Calculation
The app uses the **Haversine formula** to calculate straight-line distance between the user's GPS coordinates and each hospital's stored latitude/longitude. Hospitals are sorted by distance and the nearest 5 are displayed. Estimated drive time is calculated assuming 20 km/h average city traffic speed.

---

## Getting Started

### Prerequisites
- Android Studio Hedgehog (2023.1.1) or newer
- Android SDK API 34
- JDK 8 or newer (bundled with Android Studio)

### Installation

1. Clone the repository
   ```bash
   git clone https://github.com/yourusername/PrathamChikitse.git
   ```

2. Open Android Studio → **Open** → select the `PrathamChikitse` folder

3. Wait for Gradle sync to complete (requires internet for first-time dependency download)

4. Run on emulator or physical device:
   ```
   Run → Run 'app'   (or press Shift+F10)
   ```

### Running on a Physical Device
1. Enable **Developer Options** on your Android phone (Settings → About Phone → tap Build Number 7 times)
2. Enable **USB Debugging** under Developer Options
3. Connect via USB → Android Studio will detect the device automatically
4. Select your device in the device dropdown and press Run

---

## Permissions

| Permission | Reason |
|---|---|
| `CALL_PHONE` | Required to directly dial 108 ambulance with one tap |
| `ACCESS_FINE_LOCATION` | GPS coordinates for sorting hospitals by proximity |
| `ACCESS_COARSE_LOCATION` | Fallback approximate location if precise GPS unavailable |

All permissions are requested at runtime. The app is fully functional even if location is denied (falls back to Bengaluru city center for hospital sorting).

---

## Adding More Emergencies

No code changes needed. Open `app/src/main/res/raw/emergencies.json` and add a new object to the `emergencies` array following this structure:

```json
{
  "id": "unique_id",
  "name": "Emergency Name",
  "nameKannada": "ತುರ್ತು ಹೆಸರು",
  "icon": "🆘",
  "severity": "critical",
  "description": "Short description",
  "descriptionKannada": "ಸಣ್ಣ ವಿವರಣೆ",
  "steps": [
    {
      "stepNumber": 1,
      "title": "Step title in English",
      "titleKannada": "ಹಂತದ ಶೀರ್ಷಿಕೆ",
      "instruction": "Full instruction in English.",
      "instructionKannada": "ಕನ್ನಡದಲ್ಲಿ ಸಂಪೂರ್ಣ ಸೂಚನೆ.",
      "doText": "What to do",
      "doTextKannada": "ಏನು ಮಾಡಬೇಕು",
      "dontText": "What NOT to do",
      "dontTextKannada": "ಏನು ಮಾಡಬಾರದು",
      "drawableIcon": "ic_step_name"
    }
  ]
}
```

Severity options: `"critical"` (red), `"warning"` (orange), `"normal"` (blue)

---

## Adding Hospitals for Other Cities

Open `app/src/main/res/raw/hospitals.json` and add entries to the `hospitals` array:

```json
{
  "name": "Hospital Name",
  "nameKannada": "ಆಸ್ಪತ್ರೆ ಹೆಸರು",
  "address": "Full address",
  "addressKannada": "ಸಂಪೂರ್ಣ ವಿಳಾಸ",
  "phone": "08012345678",
  "latitude": 12.9716,
  "longitude": 77.5946,
  "isOpen24x7": true,
  "type": "government"
}
```

The GPS-based sorting will automatically rank any new hospitals by distance from the user.

---

## Impact Goals

- **Public Safety** — Reducing preventable deaths through accessible medical knowledge
- **Rural Resilience** — Empowering villagers to act in the critical minutes before an ambulance arrives
- **Language Equity** — Making life-saving medical information available in Kannada, the mother tongue of 44 million people

---

## Success Criteria Met

- [x] App loads main content in under 500ms (data preloaded on background thread during splash)
- [x] Step-by-step instructions are clear and readable under stress (large text, bold headings, bilingual)
- [x] Audio mode is toggled with a single large button in the header

---

## License

This project is open-source and available under the [MIT License](LICENSE).

---

## Acknowledgements

Built as part of the **Android App Development using GenAI** project — Pratham Chikitse (Healthcare track).

Medical content is for first-aid guidance only and does not replace professional medical advice. Always call emergency services (108) in life-threatening situations.

---

<div align="center">
  <strong>ಪ್ರಥಮ ಚಿಕಿತ್ಸೆ — Life-saving knowledge in your pocket, always.</strong>
</div>