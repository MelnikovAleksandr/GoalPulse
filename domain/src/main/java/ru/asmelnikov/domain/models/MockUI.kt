package ru.asmelnikov.domain.models

fun getMockCompetitionsList(): List<Competition> {
    return listOf(
        Competition(
            id = 2021,
            area = Area(
                id = 2072,
                flag = "https://crests.football-data.org/770.svg",
                name = "England"
            ),
            code = "PL",
            currentSeason = CurrentSeason(
                id = 2403,
                currentMatchDay = 31,
                startDateEndDate = "2025/2026",
                endDate = "2026-05-24",
                startDate = "2025-08-15"
            ),
            emblem = "https://crests.football-data.org/PL.png",
            name = "Premier League"
        ),
        Competition(
            id = 2014,
            area = Area(
                id = 2224,
                flag = "https://crests.football-data.org/760.svg",
                name = "Spain"
            ),
            code = "PD",
            currentSeason = CurrentSeason(
                id = 2429,
                currentMatchDay = 25,
                startDateEndDate = "2025/2026",
                endDate = "2026-05-24",
                startDate = "2025-08-17"
            ),
            emblem = "https://crests.football-data.org/laliga.png",
            name = "Primera Division"
        ),
        Competition(
            id = 2001,
            area = Area(
                id = 2077,
                flag = "https://crests.football-data.org/EUR.svg",
                name = "Europe"
            ),
            code = "CL",
            currentSeason = CurrentSeason(
                id = 2454,
                currentMatchDay = 8,
                startDateEndDate = "2025/2026",
                endDate = "2026-05-30",
                startDate = "2025-09-16"
            ),
            emblem = "https://crests.football-data.org/CL.png",
            name = "UEFA Champions League"
        ),

        Competition(
            id = 2000,
            area = Area(
                id = 2267,
                flag = "",
                name = "World"
            ),
            code = "WC",
            currentSeason = CurrentSeason(
                id = 2398,
                currentMatchDay = 1,
                startDateEndDate = "2026",
                endDate = "2026-07-19",
                startDate = "2026-06-11"
            ),
            emblem = "https://crests.football-data.org/wm26.png",
            name = "FIFA World Cup"
        ),

        Competition(
            id = 2013,
            area = Area(
                id = 2032,
                flag = "https://crests.football-data.org/764.svg",
                name = "Brazil"
            ),
            code = "BSA",
            currentSeason = CurrentSeason(
                id = 2474,
                currentMatchDay = 4,
                startDateEndDate = "2026",
                endDate = "2026-12-02",
                startDate = "2026-01-28"
            ),
            emblem = "https://crests.football-data.org/bsa.png",
            name = "Campeonato Brasileiro Série A"
        )
    )
}