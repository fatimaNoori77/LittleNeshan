# 🚗 Navigation Task App

A sample Android application developed as part of a technical interview task.
The app displays a map, 
shows the user's location, 
allows selecting a destination, 
draws the route using Neshan APIs, 
and provides a simple driving mode with dynamic instructions.

---

## 📱 Features

- Display user’s current location on Neshan map
- Allow user to select a destination by searching addresses
- Fetch and display route from current location to destination using Neshan Routing API
- Driving mode with real-time instructions
- Save recent search result in Room database
- Built with Java using MVVM architecture
- Network communication via Retrofit

---

## 🛠️ Technologies Used

- Java
- Android SDK
- Neshan SDK
- Retrofit
- Room
- Foreground service
- MVVM (ViewModel, LiveData, Repository)
- FusedLocationProviderClient

---

## 📸 Screenshots

<img src="asset/home-screen.png" width="250"/>
<img src="asset/search-address.png" width="250"/>
<img src="asset/navigation.png" width="250"/>

---

## 📂 Project Structure

```plaintext
- data/
  - local/
  - model/
  - network/
  - repository/
  - service/
- ui/
  - direction/
  - main/
  - search/
- utils/
