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

fun getMockStandings(): CompetitionStandings {
    return CompetitionStandings(
        area = Area(
            id = 2072,
            name = "England",
            flag = "https://crests.football-data.org/770.svg"
        ),
        competition = Competition(
            id = 2021,
            name = "Premier League",
            code = "PL",
            emblem = "https://crests.football-data.org/PL.png"
        ),
        standings = listOf(
            Standing(
                stage = Stage.REGULAR_SEASON,
                type = TournamentType.LEAGUE,
                group = Group.NON,
                table = listOf(
                    Table(
                        position = 1,
                        team = Team(
                            id = 64,
                            name = "Liverpool FC",
                            shortName = "Liverpool",
                            tla = "LIV",
                            crest = "https://crests.football-data.org/64.png"
                        ),
                        playedGames = 27,
                        won = 17,
                        draw = 6,
                        lost = 4,
                        goalsFor = 55,
                        goalsAgainst = 28,
                        goalDifference = 27,
                        points = 57,
                        form = "W,D,W,L,W"
                    ),
                    Table(
                        position = 2,
                        team = Team(
                            id = 65,
                            name = "Manchester City FC",
                            shortName = "Man City",
                            tla = "MCI",
                            crest = "https://crests.football-data.org/65.png"
                        ),
                        playedGames = 27,
                        won = 16,
                        draw = 5,
                        lost = 6,
                        goalsFor = 52,
                        goalsAgainst = 31,
                        goalDifference = 21,
                        points = 53,
                        form = "W,W,D,L,W"
                    ),
                    Table(
                        position = 3,
                        team = Team(
                            id = 57,
                            name = "Arsenal FC",
                            shortName = "Arsenal",
                            tla = "ARS",
                            crest = "https://crests.football-data.org/57.png"
                        ),
                        playedGames = 27,
                        won = 15,
                        draw = 7,
                        lost = 5,
                        goalsFor = 51,
                        goalsAgainst = 29,
                        goalDifference = 22,
                        points = 52,
                        form = "W,W,D,W,L"
                    ),
                    Table(
                        position = 4,
                        team = Team(
                            id = 66,
                            name = "Manchester United FC",
                            shortName = "Man United",
                            tla = "MUN",
                            crest = "https://crests.football-data.org/66.png"
                        ),
                        playedGames = 27,
                        won = 14,
                        draw = 8,
                        lost = 5,
                        goalsFor = 48,
                        goalsAgainst = 35,
                        goalDifference = 13,
                        points = 50,
                        form = "W,D,W,W,D"
                    ),
                    Table(
                        position = 5,
                        team = Team(
                            id = 67,
                            name = "Newcastle United FC",
                            shortName = "Newcastle",
                            tla = "NEW",
                            crest = "https://crests.football-data.org/67.png"
                        ),
                        playedGames = 27,
                        won = 14,
                        draw = 6,
                        lost = 7,
                        goalsFor = 45,
                        goalsAgainst = 34,
                        goalDifference = 11,
                        points = 48,
                        form = "L,W,W,D,L"
                    ),
                    Table(
                        position = 6,
                        team = Team(
                            id = 61,
                            name = "Chelsea FC",
                            shortName = "Chelsea",
                            tla = "CHE",
                            crest = "https://crests.football-data.org/61.png"
                        ),
                        playedGames = 27,
                        won = 13,
                        draw = 7,
                        lost = 7,
                        goalsFor = 47,
                        goalsAgainst = 36,
                        goalDifference = 11,
                        points = 46,
                        form = "W,D,L,W,D"
                    ),
                    Table(
                        position = 7,
                        team = Team(
                            id = 73,
                            name = "Tottenham Hotspur FC",
                            shortName = "Tottenham",
                            tla = "TOT",
                            crest = "https://crests.football-data.org/73.png"
                        ),
                        playedGames = 27,
                        won = 12,
                        draw = 7,
                        lost = 8,
                        goalsFor = 48,
                        goalsAgainst = 38,
                        goalDifference = 10,
                        points = 43,
                        form = "L,D,W,L,W"
                    ),
                    Table(
                        position = 8,
                        team = Team(
                            id = 71,
                            name = "Sunderland AFC",
                            shortName = "Sunderland",
                            tla = "SUN",
                            crest = "https://crests.football-data.org/71.png"
                        ),
                        playedGames = 27,
                        won = 12,
                        draw = 7,
                        lost = 8,
                        goalsFor = 39,
                        goalsAgainst = 34,
                        goalDifference = 5,
                        points = 43,
                        form = "W,D,L,W,L"
                    ),
                    Table(
                        position = 9,
                        team = Team(
                            id = 58,
                            name = "Aston Villa FC",
                            shortName = "Aston Villa",
                            tla = "AVL",
                            crest = "https://crests.football-data.org/58.png"
                        ),
                        playedGames = 27,
                        won = 11,
                        draw = 7,
                        lost = 9,
                        goalsFor = 38,
                        goalsAgainst = 37,
                        goalDifference = 1,
                        points = 40,
                        form = "L,W,D,L,W"
                    ),
                    Table(
                        position = 10,
                        team = Team(
                            id = 341,
                            name = "Leeds United FC",
                            shortName = "Leeds United",
                            tla = "LEE",
                            crest = "https://crests.football-data.org/341.png"
                        ),
                        playedGames = 27,
                        won = 11,
                        draw = 7,
                        lost = 9,
                        goalsFor = 42,
                        goalsAgainst = 42,
                        goalDifference = 0,
                        points = 40,
                        form = "W,D,W,L,D"
                    ),
                    Table(
                        position = 11,
                        team = Team(
                            id = 397,
                            name = "Brighton & Hove Albion FC",
                            shortName = "Brighton Hove",
                            tla = "BHA",
                            crest = "https://crests.football-data.org/397.png"
                        ),
                        playedGames = 27,
                        won = 10,
                        draw = 8,
                        lost = 9,
                        goalsFor = 39,
                        goalsAgainst = 40,
                        goalDifference = -1,
                        points = 38,
                        form = "D,W,L,D,L"
                    ),
                    Table(
                        position = 12,
                        team = Team(
                            id = 63,
                            name = "Fulham FC",
                            shortName = "Fulham",
                            tla = "FUL",
                            crest = "https://crests.football-data.org/63.png"
                        ),
                        playedGames = 27,
                        won = 10,
                        draw = 7,
                        lost = 10,
                        goalsFor = 40,
                        goalsAgainst = 41,
                        goalDifference = -1,
                        points = 37,
                        form = "W,L,W,D,L"
                    ),
                    Table(
                        position = 13,
                        team = Team(
                            id = 402,
                            name = "Brentford FC",
                            shortName = "Brentford",
                            tla = "BRE",
                            crest = "https://crests.football-data.org/402.png"
                        ),
                        playedGames = 27,
                        won = 10,
                        draw = 6,
                        lost = 11,
                        goalsFor = 42,
                        goalsAgainst = 42,
                        goalDifference = 0,
                        points = 36,
                        form = "L,W,D,L,W"
                    ),
                    Table(
                        position = 14,
                        team = Team(
                            id = 351,
                            name = "Nottingham Forest FC",
                            shortName = "Nottingham",
                            tla = "NOT",
                            crest = "https://crests.football-data.org/351.png"
                        ),
                        playedGames = 27,
                        won = 8,
                        draw = 7,
                        lost = 12,
                        goalsFor = 30,
                        goalsAgainst = 39,
                        goalDifference = -9,
                        points = 31,
                        form = "L,L,W,D,L"
                    ),
                    Table(
                        position = 15,
                        team = Team(
                            id = 354,
                            name = "Crystal Palace FC",
                            shortName = "Crystal Palace",
                            tla = "CRY",
                            crest = "https://crests.football-data.org/354.png"
                        ),
                        playedGames = 27,
                        won = 7,
                        draw = 9,
                        lost = 11,
                        goalsFor = 29,
                        goalsAgainst = 39,
                        goalDifference = -10,
                        points = 30,
                        form = "D,L,W,D,L"
                    ),
                    Table(
                        position = 16,
                        team = Team(
                            id = 62,
                            name = "Everton FC",
                            shortName = "Everton",
                            tla = "EVE",
                            crest = "https://crests.football-data.org/62.png"
                        ),
                        playedGames = 27,
                        won = 7,
                        draw = 8,
                        lost = 12,
                        goalsFor = 28,
                        goalsAgainst = 40,
                        goalDifference = -12,
                        points = 29,
                        form = "L,D,W,L,D"
                    ),
                    Table(
                        position = 17,
                        team = Team(
                            id = 76,
                            name = "Wolverhampton Wanderers FC",
                            shortName = "Wolverhampton",
                            tla = "WOL",
                            crest = "https://crests.football-data.org/76.png"
                        ),
                        playedGames = 27,
                        won = 7,
                        draw = 6,
                        lost = 14,
                        goalsFor = 35,
                        goalsAgainst = 50,
                        goalDifference = -15,
                        points = 27,
                        form = "L,L,W,D,L"
                    ),
                    Table(
                        position = 18,
                        team = Team(
                            id = 563,
                            name = "West Ham United FC",
                            shortName = "West Ham",
                            tla = "WHU",
                            crest = "https://crests.football-data.org/563.png"
                        ),
                        playedGames = 27,
                        won = 6,
                        draw = 8,
                        lost = 13,
                        goalsFor = 33,
                        goalsAgainst = 48,
                        goalDifference = -15,
                        points = 26,
                        form = "L,D,L,W,L"
                    ),
                    Table(
                        position = 19,
                        team = Team(
                            id = 1044,
                            name = "AFC Bournemouth",
                            shortName = "Bournemouth",
                            tla = "BOU",
                            crest = "https://crests.football-data.org/bournemouth.png"
                        ),
                        playedGames = 27,
                        won = 6,
                        draw = 7,
                        lost = 14,
                        goalsFor = 33,
                        goalsAgainst = 48,
                        goalDifference = -15,
                        points = 25,
                        form = "L,W,L,D,L"
                    ),
                    Table(
                        position = 20,
                        team = Team(
                            id = 328,
                            name = "Burnley FC",
                            shortName = "Burnley",
                            tla = "BUR",
                            crest = "https://crests.football-data.org/328.png"
                        ),
                        playedGames = 27,
                        won = 4,
                        draw = 8,
                        lost = 15,
                        goalsFor = 27,
                        goalsAgainst = 47,
                        goalDifference = -20,
                        points = 20,
                        form = "L,L,D,L,L"
                    )
                )
            )
        )
    )
}

fun getMockScorers(): List<Scorer> {
    return listOf(
        Scorer(
            player = Player(
                id = 38101,
                name = "Erling Haaland",
                firstName = "Erling",
                lastName = "Haaland",
                dateOfBirth = "2000-07-21",
                nationality = "Norway",
                position = PlayerPosition.CENTRE_FORWARD
            ),
            team = Team(
                id = 65,
                name = "Manchester City FC",
                shortName = "Man City",
                tla = "MCI",
                crest = "https://crests.football-data.org/65.png"
            ),
            playedMatches = 27,
            goals = 22,
            assists = 7,
            penalties = 3
        ),
        Scorer(
            player = Player(
                id = 175994,
                name = "Thiago Rodrigues",
                firstName = "",
                lastName = "Thiago",
                dateOfBirth = "2001-06-26",
                nationality = "Brazil",
                position = PlayerPosition.OFFENCE
            ),
            team = Team(
                id = 402,
                name = "Brentford FC",
                shortName = "Brentford",
                tla = "BRE",
                crest = "https://crests.football-data.org/402.png"
            ),
            playedMatches = 27,
            goals = 17,
            assists = 1,
            penalties = 6
        ),
        Scorer(
            player = Player(
                id = 4417,
                name = "Antoine Semenyo",
                firstName = "Antoine",
                lastName = "Semenyo",
                dateOfBirth = "2000-01-07",
                nationality = "Ghana",
                position = PlayerPosition.RIGHT_WINGER
            ),
            team = Team(
                id = 65,
                name = "Manchester City FC",
                shortName = "Man City",
                tla = "MCI",
                crest = "https://crests.football-data.org/65.png"
            ),
            playedMatches = 26,
            goals = 13,
            assists = 4,
            penalties = 1
        ),
        Scorer(
            player = Player(
                id = 103125,
                name = "João Pedro",
                firstName = "",
                lastName = "João Pedro",
                dateOfBirth = "2001-09-26",
                nationality = "Brazil",
                position = PlayerPosition.CENTRE_FORWARD
            ),
            team = Team(
                id = 61,
                name = "Chelsea FC",
                shortName = "Chelsea",
                tla = "CHE",
                crest = "https://crests.football-data.org/61.png"
            ),
            playedMatches = 27,
            goals = 11,
            assists = 4,
            penalties = 0
        ),
        Scorer(
            player = Player(
                id = 152454,
                name = "Hugo Ekitike",
                firstName = "Hugo",
                lastName = "Ekitiké",
                dateOfBirth = "2002-06-20",
                nationality = "France",
                position = PlayerPosition.CENTRE_FORWARD
            ),
            team = Team(
                id = 64,
                name = "Liverpool FC",
                shortName = "Liverpool",
                tla = "LIV",
                crest = "https://crests.football-data.org/64.png"
            ),
            playedMatches = 24,
            goals = 10,
            assists = 2,
            penalties = 0
        ),
        Scorer(
            player = Player(
                id = 8279,
                name = "Viktor Gyökeres",
                firstName = "Viktor",
                lastName = "Gyökeres",
                dateOfBirth = "1998-06-04",
                nationality = "Sweden",
                position = PlayerPosition.CENTRE_FORWARD,
                shirtNumber = 9
            ),
            team = Team(
                id = 57,
                name = "Arsenal FC",
                shortName = "Arsenal",
                tla = "ARS",
                crest = "https://crests.football-data.org/57.png"
            ),
            playedMatches = 26,
            goals = 10,
            assists = 0,
            penalties = 2
        ),
        Scorer(
            player = Player(
                id = 7839,
                name = "Dominic Calvert-Lewin",
                firstName = "Dominic",
                lastName = "Calvert-Lewin",
                dateOfBirth = "1997-03-16",
                nationality = "England",
                position = PlayerPosition.CENTRE_FORWARD
            ),
            team = Team(
                id = 341,
                name = "Leeds United FC",
                shortName = "Leeds United",
                tla = "LEE",
                crest = "https://crests.football-data.org/341.png"
            ),
            playedMatches = 24,
            goals = 10,
            assists = 1,
            penalties = 2
        ),
        Scorer(
            player = Player(
                id = 1684,
                name = "Bruno Guimarães",
                firstName = "",
                lastName = "Bruno Guimarães",
                dateOfBirth = "1997-11-16",
                nationality = "Brazil",
                position = PlayerPosition.DEFENSIVE_MIDFIELD,
                shirtNumber = 39
            ),
            team = Team(
                id = 67,
                name = "Newcastle United FC",
                shortName = "Newcastle",
                tla = "NEW",
                crest = "https://crests.football-data.org/67.png"
            ),
            playedMatches = 24,
            goals = 9,
            assists = 4,
            penalties = 2
        ),
        Scorer(
            player = Player(
                id = 8626,
                name = "Bryan Mbeumo",
                firstName = "Bryan",
                lastName = "Mbeumo",
                dateOfBirth = "1999-08-07",
                nationality = "Cameroon",
                position = PlayerPosition.RIGHT_WINGER
            ),
            team = Team(
                id = 66,
                name = "Manchester United FC",
                shortName = "Man United",
                tla = "MUN",
                crest = "https://crests.football-data.org/66.png"
            ),
            playedMatches = 22,
            goals = 9,
            assists = 3,
            penalties = 0
        ),
        Scorer(
            player = Player(
                id = 3328,
                name = "Danny Welbeck",
                firstName = "Danny",
                lastName = "Welbeck",
                dateOfBirth = "1990-11-26",
                nationality = "England",
                position = PlayerPosition.CENTRE_FORWARD
            ),
            team = Team(
                id = 397,
                name = "Brighton & Hove Albion FC",
                shortName = "Brighton Hove",
                tla = "BHA",
                crest = "https://crests.football-data.org/397.png"
            ),
            playedMatches = 26,
            goals = 9,
            assists = 0,
            penalties = 1
        ),
        Scorer(
            player = Player(
                id = 102603,
                name = "Enzo Fernández",
                firstName = "Enzo",
                lastName = "Fernández",
                dateOfBirth = "2001-01-17",
                nationality = "Argentina",
                position = PlayerPosition.CENTRAL_MIDFIELD
            ),
            team = Team(
                id = 61,
                name = "Chelsea FC",
                shortName = "Chelsea",
                tla = "CHE",
                crest = "https://crests.football-data.org/61.png"
            ),
            playedMatches = 26,
            goals = 8,
            assists = 2,
            penalties = 2
        ),
        Scorer(
            player = Player(
                id = 11414,
                name = "Jarrod Bowen",
                firstName = "Jarrod",
                lastName = "Bowen",
                dateOfBirth = "1996-01-01",
                nationality = "England",
                position = PlayerPosition.RIGHT_WINGER
            ),
            team = Team(
                id = 563,
                name = "West Ham United FC",
                shortName = "West Ham",
                tla = "WHU",
                crest = "https://crests.football-data.org/563.png"
            ),
            playedMatches = 27,
            goals = 8,
            assists = 3,
            penalties = 1
        ),
        Scorer(
            player = Player(
                id = 641,
                name = "Jean-Philippe Mateta",
                firstName = "Jean-Philippe",
                lastName = "Mateta",
                dateOfBirth = "1997-06-28",
                nationality = "France",
                position = PlayerPosition.CENTRE_FORWARD
            ),
            team = Team(
                id = 354,
                name = "Crystal Palace FC",
                shortName = "Crystal Palace",
                tla = "CRY",
                crest = "https://crests.football-data.org/354.png"
            ),
            playedMatches = 23,
            goals = 8,
            assists = 0,
            penalties = 3
        ),
        Scorer(
            player = Player(
                id = 144892,
                name = "Cole Palmer",
                firstName = "Cole",
                lastName = "Palmer",
                dateOfBirth = "2002-05-06",
                nationality = "England",
                position = PlayerPosition.ATTACKING_MIDFIELD
            ),
            team = Team(
                id = 61,
                name = "Chelsea FC",
                shortName = "Chelsea",
                tla = "CHE",
                crest = "https://crests.football-data.org/61.png"
            ),
            playedMatches = 17,
            goals = 8,
            assists = 1,
            penalties = 5
        ),
        Scorer(
            player = Player(
                id = 11777,
                name = "Harry Wilson",
                firstName = "Harry",
                lastName = "Wilson",
                dateOfBirth = "1997-03-22",
                nationality = "Wales",
                position = PlayerPosition.RIGHT_WINGER
            ),
            team = Team(
                id = 63,
                name = "Fulham FC",
                shortName = "Fulham",
                tla = "FUL",
                crest = "https://crests.football-data.org/63.png"
            ),
            playedMatches = 26,
            goals = 8,
            assists = 5,
            penalties = 0
        ),
        Scorer(
            player = Player(
                id = 204241,
                name = "Eli Kroupi",
                firstName = "Eli",
                lastName = "Kroupi",
                dateOfBirth = "2006-06-23",
                nationality = "France",
                position = PlayerPosition.CENTRE_FORWARD
            ),
            team = Team(
                id = 1044,
                name = "AFC Bournemouth",
                shortName = "Bournemouth",
                tla = "BOU",
                crest = "https://crests.football-data.org/bournemouth.png"
            ),
            playedMatches = 27,
            goals = 8,
            assists = 0,
            penalties = 0
        ),
        Scorer(
            player = Player(
                id = 3305,
                name = "Raúl Jiménez",
                firstName = "Raúl",
                lastName = "Jiménez",
                dateOfBirth = "1991-05-05",
                nationality = "Mexico",
                position = PlayerPosition.CENTRE_FORWARD
            ),
            team = Team(
                id = 63,
                name = "Fulham FC",
                shortName = "Fulham",
                tla = "FUL",
                crest = "https://crests.football-data.org/63.png"
            ),
            playedMatches = 26,
            goals = 8,
            assists = 3,
            penalties = 3
        ),
        Scorer(
            player = Player(
                id = 4444,
                name = "Ollie Watkins",
                firstName = "Ollie",
                lastName = "Watkins",
                dateOfBirth = "1995-12-30",
                nationality = "England",
                position = PlayerPosition.CENTRE_FORWARD
            ),
            team = Team(
                id = 58,
                name = "Aston Villa FC",
                shortName = "Aston Villa",
                tla = "AVL",
                crest = "https://crests.football-data.org/58.png"
            ),
            playedMatches = 26,
            goals = 8,
            assists = 1,
            penalties = 0
        ),
        Scorer(
            player = Player(
                id = 82140,
                name = "Morgan Rogers",
                firstName = "Morgan",
                lastName = "Rogers",
                dateOfBirth = "2002-07-26",
                nationality = "England",
                position = PlayerPosition.ATTACKING_MIDFIELD
            ),
            team = Team(
                id = 58,
                name = "Aston Villa FC",
                shortName = "Aston Villa",
                tla = "AVL",
                crest = "https://crests.football-data.org/58.png"
            ),
            playedMatches = 27,
            goals = 8,
            assists = 6,
            penalties = 0
        ),
        Scorer(
            player = Player(
                id = 8133,
                name = "Richarlison",
                firstName = "",
                lastName = "Richarlison",
                dateOfBirth = "1997-05-10",
                nationality = "Brazil",
                position = PlayerPosition.CENTRE_FORWARD
            ),
            team = Team(
                id = 73,
                name = "Tottenham Hotspur FC",
                shortName = "Tottenham",
                tla = "TOT",
                crest = "https://crests.football-data.org/73.png"
            ),
            playedMatches = 22,
            goals = 7,
            assists = 3,
            penalties = 0
        )
    )
}

fun getMockMatches(): Matches {
    return Matches(
        id = "premier_league_matches_2025",
        matchesByTourCompleted = listOf(
            MatchesByTour(
                matchDay = 27,
                seasonType = TournamentType.LEAGUE,
                stage = Stage.REGULAR_SEASON,
                matches = listOf(
                    Match(
                        id = 538046,
                        utcDate = "2026-02-21T15:00:00Z",
                        status = MatchStatus.FINISHED,
                        matchDay = 27,
                        stage = Stage.REGULAR_SEASON,
                        group = Group.NON,
                        homeTeam = MatchTeam(
                            id = 58,
                            name = "Aston Villa FC",
                            shortName = "Aston Villa",
                            tla = "AVL",
                            crest = "https://crests.football-data.org/58.png"
                        ),
                        awayTeam = MatchTeam(
                            id = 341,
                            name = "Leeds United FC",
                            shortName = "Leeds United",
                            tla = "LEE",
                            crest = "https://crests.football-data.org/341.png"
                        ),
                        score = Score(
                            winner = Winner.DRAW,
                            duration = "REGULAR",
                            fullTime = Time(home = 1, away = 1),
                            halfTime = Time(home = 0, away = 1)
                        ),
                        area = Area(
                            id = 2072,
                            name = "England",
                            flag = "https://crests.football-data.org/770.svg"
                        ),
                        competition = Competition(
                            id = 2021,
                            name = "Premier League",
                            code = "PL",
                            emblem = "https://crests.football-data.org/PL.png"
                        )
                    ),
                    Match(
                        id = 538048,
                        utcDate = "2026-02-21T15:00:00Z",
                        status = MatchStatus.FINISHED,
                        matchDay = 27,
                        stage = Stage.REGULAR_SEASON,
                        group = Group.NON,
                        homeTeam = MatchTeam(
                            id = 402,
                            name = "Brentford FC",
                            shortName = "Brentford",
                            tla = "BRE",
                            crest = "https://crests.football-data.org/402.png"
                        ),
                        awayTeam = MatchTeam(
                            id = 397,
                            name = "Brighton & Hove Albion FC",
                            shortName = "Brighton Hove",
                            tla = "BHA",
                            crest = "https://crests.football-data.org/397.png"
                        ),
                        score = Score(
                            winner = Winner.AWAY_TEAM,
                            duration = "REGULAR",
                            fullTime = Time(home = 0, away = 2),
                            halfTime = Time(home = 0, away = 2)
                        ),
                        area = Area(
                            id = 2072,
                            name = "England",
                            flag = "https://crests.football-data.org/770.svg"
                        ),
                        competition = Competition(
                            id = 2021,
                            name = "Premier League",
                            code = "PL",
                            emblem = "https://crests.football-data.org/PL.png"
                        )
                    ),
                    Match(
                        id = 538049,
                        utcDate = "2026-02-21T15:00:00Z",
                        status = MatchStatus.FINISHED,
                        matchDay = 27,
                        stage = Stage.REGULAR_SEASON,
                        group = Group.NON,
                        homeTeam = MatchTeam(
                            id = 61,
                            name = "Chelsea FC",
                            shortName = "Chelsea",
                            tla = "CHE",
                            crest = "https://crests.football-data.org/61.png"
                        ),
                        awayTeam = MatchTeam(
                            id = 328,
                            name = "Burnley FC",
                            shortName = "Burnley",
                            tla = "BUR",
                            crest = "https://crests.football-data.org/328.png"
                        ),
                        score = Score(
                            winner = Winner.DRAW,
                            duration = "REGULAR",
                            fullTime = Time(home = 1, away = 1),
                            halfTime = Time(home = 1, away = 0)
                        ),
                        area = Area(
                            id = 2072,
                            name = "England",
                            flag = "https://crests.football-data.org/770.svg"
                        ),
                        competition = Competition(
                            id = 2021,
                            name = "Premier League",
                            code = "PL",
                            emblem = "https://crests.football-data.org/PL.png"
                        )
                    ),
                    Match(
                        id = 538054,
                        utcDate = "2026-02-21T17:30:00Z",
                        status = MatchStatus.FINISHED,
                        matchDay = 27,
                        stage = Stage.REGULAR_SEASON,
                        group = Group.NON,
                        homeTeam = MatchTeam(
                            id = 563,
                            name = "West Ham United FC",
                            shortName = "West Ham",
                            tla = "WHU",
                            crest = "https://crests.football-data.org/563.png"
                        ),
                        awayTeam = MatchTeam(
                            id = 1044,
                            name = "AFC Bournemouth",
                            shortName = "Bournemouth",
                            tla = "BOU",
                            crest = "https://crests.football-data.org/bournemouth.png"
                        ),
                        score = Score(
                            winner = Winner.DRAW,
                            duration = "REGULAR",
                            fullTime = Time(home = 0, away = 0),
                            halfTime = Time(home = 0, away = 0)
                        ),
                        area = Area(
                            id = 2072,
                            name = "England",
                            flag = "https://crests.football-data.org/770.svg"
                        ),
                        competition = Competition(
                            id = 2021,
                            name = "Premier League",
                            code = "PL",
                            emblem = "https://crests.football-data.org/PL.png"
                        )
                    ),
                    Match(
                        id = 538051,
                        utcDate = "2026-02-21T20:00:00Z",
                        status = MatchStatus.FINISHED,
                        matchDay = 27,
                        stage = Stage.REGULAR_SEASON,
                        group = Group.NON,
                        homeTeam = MatchTeam(
                            id = 65,
                            name = "Manchester City FC",
                            shortName = "Man City",
                            tla = "MCI",
                            crest = "https://crests.football-data.org/65.png"
                        ),
                        awayTeam = MatchTeam(
                            id = 67,
                            name = "Newcastle United FC",
                            shortName = "Newcastle",
                            tla = "NEW",
                            crest = "https://crests.football-data.org/67.png"
                        ),
                        score = Score(
                            winner = Winner.HOME_TEAM,
                            duration = "REGULAR",
                            fullTime = Time(home = 2, away = 1),
                            halfTime = Time(home = 2, away = 1)
                        ),
                        area = Area(
                            id = 2072,
                            name = "England",
                            flag = "https://crests.football-data.org/770.svg"
                        ),
                        competition = Competition(
                            id = 2021,
                            name = "Premier League",
                            code = "PL",
                            emblem = "https://crests.football-data.org/PL.png"
                        )
                    )
                )
            )
        ),
        matchesByTourAhead = listOf(
            MatchesByTour(
                matchDay = 28,
                seasonType = TournamentType.LEAGUE,
                stage = Stage.REGULAR_SEASON,
                matches = listOf(
                    Match(
                        id = 538064,
                        utcDate = "2026-02-27T20:00:00Z",
                        bigDate = "28.02",
                        status = MatchStatus.TIMED,
                        matchDay = 28,
                        stage = Stage.REGULAR_SEASON,
                        group = Group.NON,
                        homeTeam = MatchTeam(
                            id = 76,
                            name = "Wolverhampton Wanderers FC",
                            shortName = "Wolverhampton",
                            tla = "WOL",
                            crest = "https://crests.football-data.org/76.png"
                        ),
                        awayTeam = MatchTeam(
                            id = 58,
                            name = "Aston Villa FC",
                            shortName = "Aston Villa",
                            tla = "AVL",
                            crest = "https://crests.football-data.org/58.png"
                        ),
                        score = Score(
                            winner = Winner.NON,
                            duration = "REGULAR",
                            fullTime = Time(),
                            halfTime = Time()
                        ),
                        area = Area(
                            id = 2072,
                            name = "England",
                            flag = "https://crests.football-data.org/770.svg"
                        ),
                        competition = Competition(
                            id = 2021,
                            name = "Premier League",
                            code = "PL",
                            emblem = "https://crests.football-data.org/PL.png"
                        )
                    ),
                    Match(
                        id = 538055,
                        utcDate = "2026-02-28T12:30:00Z",
                        bigDate = "28.02",
                        status = MatchStatus.TIMED,
                        matchDay = 28,
                        stage = Stage.REGULAR_SEASON,
                        group = Group.NON,
                        homeTeam = MatchTeam(
                            id = 1044,
                            name = "AFC Bournemouth",
                            shortName = "Bournemouth",
                            tla = "BOU",
                            crest = "https://crests.football-data.org/bournemouth.png"
                        ),
                        awayTeam = MatchTeam(
                            id = 71,
                            name = "Sunderland AFC",
                            shortName = "Sunderland",
                            tla = "SUN",
                            crest = "https://crests.football-data.org/71.png"
                        ),
                        score = Score(
                            winner = Winner.NON,
                            duration = "REGULAR",
                            fullTime = Time(),
                            halfTime = Time()
                        ),
                        area = Area(
                            id = 2072,
                            name = "England",
                            flag = "https://crests.football-data.org/770.svg"
                        ),
                        competition = Competition(
                            id = 2021,
                            name = "Premier League",
                            code = "PL",
                            emblem = "https://crests.football-data.org/PL.png"
                        )
                    ),
                    Match(
                        id = 538058,
                        utcDate = "2026-02-28T15:00:00Z",
                        bigDate = "28.02",
                        status = MatchStatus.TIMED,
                        matchDay = 28,
                        stage = Stage.REGULAR_SEASON,
                        group = Group.NON,
                        homeTeam = MatchTeam(
                            id = 328,
                            name = "Burnley FC",
                            shortName = "Burnley",
                            tla = "BUR",
                            crest = "https://crests.football-data.org/328.png"
                        ),
                        awayTeam = MatchTeam(
                            id = 402,
                            name = "Brentford FC",
                            shortName = "Brentford",
                            tla = "BRE",
                            crest = "https://crests.football-data.org/402.png"
                        ),
                        score = Score(
                            winner = Winner.NON,
                            duration = "REGULAR",
                            fullTime = Time(),
                            halfTime = Time()
                        ),
                        area = Area(
                            id = 2072,
                            name = "England",
                            flag = "https://crests.football-data.org/770.svg"
                        ),
                        competition = Competition(
                            id = 2021,
                            name = "Premier League",
                            code = "PL",
                            emblem = "https://crests.football-data.org/PL.png"
                        )
                    ),
                    Match(
                        id = 538060,
                        utcDate = "2026-02-28T15:00:00Z",
                        bigDate = "28.02",
                        status = MatchStatus.TIMED,
                        matchDay = 28,
                        stage = Stage.REGULAR_SEASON,
                        group = Group.NON,
                        homeTeam = MatchTeam(
                            id = 64,
                            name = "Liverpool FC",
                            shortName = "Liverpool",
                            tla = "LIV",
                            crest = "https://crests.football-data.org/64.png"
                        ),
                        awayTeam = MatchTeam(
                            id = 563,
                            name = "West Ham United FC",
                            shortName = "West Ham",
                            tla = "WHU",
                            crest = "https://crests.football-data.org/563.png"
                        ),
                        score = Score(
                            winner = Winner.NON,
                            duration = "REGULAR",
                            fullTime = Time(),
                            halfTime = Time()
                        ),
                        area = Area(
                            id = 2072,
                            name = "England",
                            flag = "https://crests.football-data.org/770.svg"
                        ),
                        competition = Competition(
                            id = 2021,
                            name = "Premier League",
                            code = "PL",
                            emblem = "https://crests.football-data.org/PL.png"
                        )
                    ),
                    Match(
                        id = 538063,
                        utcDate = "2026-02-28T15:00:00Z",
                        bigDate = "28.02",
                        status = MatchStatus.TIMED,
                        matchDay = 28,
                        stage = Stage.REGULAR_SEASON,
                        group = Group.NON,
                        homeTeam = MatchTeam(
                            id = 67,
                            name = "Newcastle United FC",
                            shortName = "Newcastle",
                            tla = "NEW",
                            crest = "https://crests.football-data.org/67.png"
                        ),
                        awayTeam = MatchTeam(
                            id = 62,
                            name = "Everton FC",
                            shortName = "Everton",
                            tla = "EVE",
                            crest = "https://crests.football-data.org/62.png"
                        ),
                        score = Score(
                            winner = Winner.NON,
                            duration = "REGULAR",
                            fullTime = Time(),
                            halfTime = Time()
                        ),
                        area = Area(
                            id = 2072,
                            name = "England",
                            flag = "https://crests.football-data.org/770.svg"
                        ),
                        competition = Competition(
                            id = 2021,
                            name = "Premier League",
                            code = "PL",
                            emblem = "https://crests.football-data.org/PL.png"
                        )
                    )
                )
            )
        )
    )
}

fun getMockHead2Head(): Head2head {
    return Head2head(
        id = 538046,
        aggregates = Aggregates(
            numberOfMatches = 9,
            homeWinsPercentage = 35f,
            awayWinsPercentage = 45f,
            drawsPercentage = 20f,
            awayTeam = TeamH2H(
                draws = 3,
                losses = 1,
                wins = 5
            ),
            homeTeam = TeamH2H(
                draws = 5,
                losses = 1,
                wins = 3
            )
        ),
    )
}