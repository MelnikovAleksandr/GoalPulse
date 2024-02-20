# GoalPulse
A simple Android application for displaying statistics of major football competitions.

## Content
- [Technologies](#technologies)
- [Usage](#usage)
- [Structure](#structure)
  
## Technologies
- [Realm](https://realm.io/)
- [Koin](https://insert-koin.io/)
- [Orbit MVI](https://orbit-mvi.org/)
- [Compose](https://developer.android.com/jetpack/compose)
- [Shared Elements](https://github.com/mxalbert1996/compose-shared-elements?tab=readme-ov-file)
- [Collapsing Toolbar](https://github.com/onebone/compose-collapsing-toolbar)

## Usage
To use the application, obtain keys from the resources and replace them in the file ru.asmelnikov.utils.Constants:
- [Football API](https://www.football-data.org/client/register)
- [News API](https://newsapi.org/register)

```typescript
object Constants {

    const val FOOTBALL_API_KEY = "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX"
    const val NEW_API_KEY = "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX"
}
```

## Structure
<img src="https://github.com/MelnikovAleksandr/GoalPulse/assets/83123472/254a1b2e-67d8-4033-a1ff-b6992933b9ba.png" width="600"> 


