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
        )
    )
}

fun getMockTeam(): TeamInfo {
    return TeamInfo(
        id = "66",
        address = "Sir Matt Busby Way Manchester M16 0RA",
        area = Area(
            id = 2072,
            name = "England",
            flag = "https://crests.football-data.org/770.svg"
        ),
        clubColors = "Red / White",
        coach = Coach(
            id = 7903,
            contract = Contract(
                start = "2026-01",
                until = "2026-06"
            ),
            dateOfBirth = "1980-12-29",
            firstName = "Michael",
            lastName = "Carrick",
            name = "Michael Carrick",
            nationality = "England"
        ),
        crest = "https://crests.football-data.org/66.png",
        founded = 1878,
        name = "Manchester United FC",
        shortName = "Man United",
        squadByPosition = listOf(
            SquadByPosition(
                position = PlayerPosition.GOALKEEPER,
                squad = listOf(
                    Squad(id = 8035, age = "38", name = "Tom Heaton", nationality = "England"),
                    Squad(id = 29518, age = "27", name = "Altay Bayındır", nationality = "Turkey"),
                    Squad(id = 129946, age = "23", name = "Senne Lammens", nationality = "Belgium")
                )
            ),
            SquadByPosition(
                position = PlayerPosition.DEFENCE,
                squad = listOf(
                    Squad(
                        id = 188291,
                        age = "21",
                        name = "Tyler Fredricson",
                        nationality = "England"
                    ),
                    Squad(id = 262921, age = "18", name = "Diego León", nationality = "Paraguay"),
                    Squad(
                        id = 273281,
                        age = "18",
                        name = "Godwill Kukonki",
                        nationality = "England"
                    ),
                    Squad(
                        id = 278168,
                        age = "18",
                        name = "Bendito Mantato",
                        nationality = "England"
                    ),
                    Squad(
                        id = 46451,
                        age = "28",
                        name = "Lisandro Martínez",
                        nationality = "Argentina"
                    ),
                    Squad(id = 15905, age = "26", name = "Diogo Dalot", nationality = "Portugal"),
                    Squad(id = 7898, age = "30", name = "Luke Shaw", nationality = "England"),
                    Squad(
                        id = 7553,
                        age = "28",
                        name = "Noussair Mazraoui",
                        nationality = "Morocco"
                    ),
                    Squad(
                        id = 7549,
                        age = "26",
                        name = "Matthijs de Ligt",
                        nationality = "Netherlands"
                    ),
                    Squad(
                        id = 7467,
                        age = "26",
                        name = "Tyrell Malacia",
                        nationality = "Netherlands"
                    ),
                    Squad(id = 3326, age = "33", name = "Harry Maguire", nationality = "England"),
                    Squad(id = 247644, age = "19", name = "Ayden Heaven", nationality = "England"),
                    Squad(id = 211559, age = "21", name = "Patrick Dorgu", nationality = "Denmark"),
                    Squad(id = 181933, age = "20", name = "Leny Yoro", nationality = "France")
                )
            ),
            SquadByPosition(
                position = PlayerPosition.MIDFIELD,
                squad = listOf(
                    Squad(id = 271941, age = "18", name = "Jack Fletcher", nationality = "England"),
                    Squad(
                        id = 275523,
                        age = "20",
                        name = "Jack Moorhouse",
                        nationality = "Ireland"
                    ),
                    Squad(
                        id = 290139,
                        age = "18",
                        name = "Tyler Fletcher",
                        nationality = "England"
                    ),
                    Squad(id = 28549, age = "24", name = "Manuel Ugarte", nationality = "Uruguay"),
                    Squad(id = 7599, age = "27", name = "Mason Mount", nationality = "England"),
                    Squad(
                        id = 3257,
                        age = "31",
                        name = "Bruno Fernandes",
                        nationality = "Portugal"
                    ),
                    Squad(id = 3231, age = "34", name = "Casemiro", nationality = "Brazil"),
                    Squad(id = 190797, age = "20", name = "Kobbie Mainoo", nationality = "England")
                )
            ),
            SquadByPosition(
                position = PlayerPosition.OFFENCE,
                squad = listOf(
                    Squad(
                        id = 275522,
                        age = "18",
                        name = "Chido Obi-Martin",
                        nationality = "Denmark"
                    ),
                    Squad(id = 289557, age = "18", name = "Shea Lacey", nationality = "England")
                )
            ),
            SquadByPosition(
                position = PlayerPosition.MIDFIELD,
                squad = listOf(
                    Squad(
                        id = 99731,
                        age = "24",
                        name = "Joshua Zirkzee",
                        nationality = "Netherlands"
                    ),
                    Squad(id = 30842, age = "26", name = "Matheus Cunha", nationality = "Brazil"),
                    Squad(id = 8626, age = "26", name = "Bryan Mbeumo", nationality = "Cameroon"),
                    Squad(
                        id = 133584,
                        age = "23",
                        name = "Amad Diallo",
                        nationality = "Ivory Coast"
                    ),
                    Squad(
                        id = 124244,
                        age = "22",
                        name = "Benjamin Šeško",
                        nationality = "Slovenia"
                    )
                )
            )
        ),
        tla = "MUN",
        venue = "Old Trafford",
        website = "http://www.manutd.com"
    )
}

fun getMockMatchesComplete(): List<Match> {
    return listOf(
        Match(
            id = 537793,
            utcDate = "2025-08-17T15:30:00Z",
            bigDate = "2025-08-17T15:30:00Z",
            status = MatchStatus.FINISHED,
            matchDay = 1,
            stage = Stage.REGULAR_SEASON,
            group = Group.NON,
            competition = Competition(
                name = "Premier League"
            ),
            homeTeam = MatchTeam(
                id = 66,
                name = "Manchester United FC",
                shortName = "Man United",
                tla = "MUN",
                crest = "https://crests.football-data.org/66.png"
            ),
            awayTeam = MatchTeam(
                id = 57,
                name = "Arsenal FC",
                shortName = "Arsenal",
                tla = "ARS",
                crest = "https://crests.football-data.org/57.png"
            ),
            score = Score(
                winner = Winner.AWAY_TEAM,
                duration = "REGULAR",
                fullTime = Time(home = 0, away = 1),
                halfTime = Time(home = 0, away = 1)
            ),
            referees = listOf(
                Referee(
                    id = 11430,
                    name = "Simon Hooper",
                    nationality = "England",
                    type = "REFEREE"
                )
            )
        ),
        Match(
            id = 537801,
            utcDate = "2025-08-24T15:30:00Z",
            bigDate = "2025-08-24T15:30:00Z",
            status = MatchStatus.FINISHED,
            matchDay = 2,
            stage = Stage.REGULAR_SEASON,
            group = Group.NON,
            competition = Competition(
                name = "Premier League"
            ),
            homeTeam = MatchTeam(
                id = 63,
                name = "Fulham FC",
                shortName = "Fulham",
                tla = "FUL",
                crest = "https://crests.football-data.org/63.png"
            ),
            awayTeam = MatchTeam(
                id = 66,
                name = "Manchester United FC",
                shortName = "Man United",
                tla = "MUN",
                crest = "https://crests.football-data.org/66.png"
            ),
            score = Score(
                winner = Winner.DRAW,
                duration = "REGULAR",
                fullTime = Time(home = 1, away = 1),
                halfTime = Time(home = 0, away = 0)
            ),
            referees = listOf(
                Referee(
                    id = 11443,
                    name = "Chris Kavanagh",
                    nationality = "England",
                    type = "REFEREE"
                )
            )
        ),
        Match(
            id = 537811,
            utcDate = "2025-08-30T14:00:00Z",
            bigDate = "2025-08-30T14:00:00Z",
            status = MatchStatus.FINISHED,
            matchDay = 3,
            stage = Stage.REGULAR_SEASON,
            group = Group.NON,
            competition = Competition(
                name = "Premier League"
            ),
            homeTeam = MatchTeam(
                id = 66,
                name = "Manchester United FC",
                shortName = "Man United",
                tla = "MUN",
                crest = "https://crests.football-data.org/66.png"
            ),
            awayTeam = MatchTeam(
                id = 328,
                name = "Burnley FC",
                shortName = "Burnley",
                tla = "BUR",
                crest = "https://crests.football-data.org/328.png"
            ),
            score = Score(
                winner = Winner.HOME_TEAM,
                duration = "REGULAR",
                fullTime = Time(home = 3, away = 2),
                halfTime = Time(home = 1, away = 0)
            ),
            referees = listOf(
                Referee(
                    id = 213813,
                    name = "Sam Barrott",
                    nationality = "England",
                    type = "REFEREE"
                )
            )
        ),
        Match(
            id = 537822,
            utcDate = "2025-09-14T15:30:00Z",
            bigDate = "2025-09-14T15:30:00Z",
            status = MatchStatus.FINISHED,
            matchDay = 4,
            stage = Stage.REGULAR_SEASON,
            group = Group.NON,
            competition = Competition(
                name = "Premier League"
            ),
            homeTeam = MatchTeam(
                id = 65,
                name = "Manchester City FC",
                shortName = "Man City",
                tla = "MCI",
                crest = "https://crests.football-data.org/65.png"
            ),
            awayTeam = MatchTeam(
                id = 66,
                name = "Manchester United FC",
                shortName = "Man United",
                tla = "MUN",
                crest = "https://crests.football-data.org/66.png"
            ),
            score = Score(
                winner = Winner.HOME_TEAM,
                duration = "REGULAR",
                fullTime = Time(home = 3, away = 0),
                halfTime = Time(home = 1, away = 0)
            ),
            referees = listOf(
                Referee(
                    id = 11580,
                    name = "Anthony Taylor",
                    nationality = "England",
                    type = "REFEREE"
                )
            )
        ),
        Match(
            id = 537832,
            utcDate = "2025-09-20T16:30:00Z",
            bigDate = "2025-09-20T16:30:00Z",
            status = MatchStatus.FINISHED,
            matchDay = 5,
            stage = Stage.REGULAR_SEASON,
            group = Group.NON,
            competition = Competition(
                name = "Premier League"
            ),
            homeTeam = MatchTeam(
                id = 66,
                name = "Manchester United FC",
                shortName = "Man United",
                tla = "MUN",
                crest = "https://crests.football-data.org/66.png"
            ),
            awayTeam = MatchTeam(
                id = 61,
                name = "Chelsea FC",
                shortName = "Chelsea",
                tla = "CHE",
                crest = "https://crests.football-data.org/61.png"
            ),
            score = Score(
                winner = Winner.HOME_TEAM,
                duration = "REGULAR",
                fullTime = Time(home = 2, away = 1),
                halfTime = Time(home = 2, away = 0)
            ),
            referees = listOf(
                Referee(
                    id = 11309,
                    name = "Peter Bankes",
                    nationality = "England",
                    type = "REFEREE"
                )
            )
        ),
        Match(
            id = 537837,
            utcDate = "2025-09-27T11:30:00Z",
            bigDate = "2025-09-27T11:30:00Z",
            status = MatchStatus.FINISHED,
            matchDay = 6,
            stage = Stage.REGULAR_SEASON,
            group = Group.NON,
            competition = Competition(
                name = "Premier League"
            ),
            homeTeam = MatchTeam(
                id = 402,
                name = "Brentford FC",
                shortName = "Brentford",
                tla = "BRE",
                crest = "https://crests.football-data.org/402.png"
            ),
            awayTeam = MatchTeam(
                id = 66,
                name = "Manchester United FC",
                shortName = "Man United",
                tla = "MUN",
                crest = "https://crests.football-data.org/66.png"
            ),
            score = Score(
                winner = Winner.HOME_TEAM,
                duration = "REGULAR",
                fullTime = Time(home = 3, away = 1),
                halfTime = Time(home = 2, away = 1)
            ),
            referees = listOf(
                Referee(
                    id = 11585,
                    name = "Craig Pawson",
                    nationality = "England",
                    type = "REFEREE"
                )
            )
        ),
        Match(
            id = 537852,
            utcDate = "2025-10-04T14:00:00Z",
            bigDate = "2025-10-04T14:00:00Z",
            status = MatchStatus.FINISHED,
            matchDay = 7,
            stage = Stage.REGULAR_SEASON,
            group = Group.NON,
            competition = Competition(
                name = "Premier League"
            ),
            homeTeam = MatchTeam(
                id = 66,
                name = "Manchester United FC",
                shortName = "Man United",
                tla = "MUN",
                crest = "https://crests.football-data.org/66.png"
            ),
            awayTeam = MatchTeam(
                id = 71,
                name = "Sunderland AFC",
                shortName = "Sunderland",
                tla = "SUN",
                crest = "https://crests.football-data.org/71.png"
            ),
            score = Score(
                winner = Winner.HOME_TEAM,
                duration = "REGULAR",
                fullTime = Time(home = 2, away = 0),
                halfTime = Time(home = 2, away = 0)
            ),
            referees = listOf(
                Referee(
                    id = 11494,
                    name = "Stuart Attwell",
                    nationality = "England",
                    type = "REFEREE"
                )
            )
        ),
        Match(
            id = 537860,
            utcDate = "2025-10-19T15:30:00Z",
            bigDate = "2025-10-19T15:30:00Z",
            status = MatchStatus.FINISHED,
            matchDay = 8,
            stage = Stage.REGULAR_SEASON,
            group = Group.NON,
            competition = Competition(
                name = "Premier League"
            ),
            homeTeam = MatchTeam(
                id = 64,
                name = "Liverpool FC",
                shortName = "Liverpool",
                tla = "LIV",
                crest = "https://crests.football-data.org/64.png"
            ),
            awayTeam = MatchTeam(
                id = 66,
                name = "Manchester United FC",
                shortName = "Man United",
                tla = "MUN",
                crest = "https://crests.football-data.org/66.png"
            ),
            score = Score(
                winner = Winner.AWAY_TEAM,
                duration = "REGULAR",
                fullTime = Time(home = 1, away = 2),
                halfTime = Time(home = 0, away = 1)
            ),
            referees = listOf(
                Referee(
                    id = 11605,
                    name = "Michael Oliver",
                    nationality = "England",
                    type = "REFEREE"
                )
            )
        ),
        Match(
            id = 537872,
            utcDate = "2025-10-25T16:30:00Z",
            bigDate = "2025-10-25T16:30:00Z",
            status = MatchStatus.FINISHED,
            matchDay = 9,
            stage = Stage.REGULAR_SEASON,
            group = Group.NON,
            competition = Competition(
                name = "Premier League"
            ),
            homeTeam = MatchTeam(
                id = 66,
                name = "Manchester United FC",
                shortName = "Man United",
                tla = "MUN",
                crest = "https://crests.football-data.org/66.png"
            ),
            awayTeam = MatchTeam(
                id = 397,
                name = "Brighton & Hove Albion FC",
                shortName = "Brighton Hove",
                tla = "BHA",
                crest = "https://crests.football-data.org/397.png"
            ),
            score = Score(
                winner = Winner.HOME_TEAM,
                duration = "REGULAR",
                fullTime = Time(home = 4, away = 2),
                halfTime = Time(home = 2, away = 0)
            ),
            referees = listOf(
                Referee(
                    id = 11580,
                    name = "Anthony Taylor",
                    nationality = "England",
                    type = "REFEREE"
                )
            )
        ),
        Match(
            id = 537882,
            utcDate = "2025-11-01T15:00:00Z",
            bigDate = "2025-11-01T15:00:00Z",
            status = MatchStatus.FINISHED,
            matchDay = 10,
            stage = Stage.REGULAR_SEASON,
            group = Group.NON,
            competition = Competition(
                name = "Premier League"
            ),
            homeTeam = MatchTeam(
                id = 351,
                name = "Nottingham Forest FC",
                shortName = "Nottingham",
                tla = "NOT",
                crest = "https://crests.football-data.org/351.png"
            ),
            awayTeam = MatchTeam(
                id = 66,
                name = "Manchester United FC",
                shortName = "Man United",
                tla = "MUN",
                crest = "https://crests.football-data.org/66.png"
            ),
            score = Score(
                winner = Winner.DRAW,
                duration = "REGULAR",
                fullTime = Time(home = 2, away = 2),
                halfTime = Time(home = 0, away = 1)
            ),
            referees = listOf(
                Referee(
                    id = 11469,
                    name = "Darren England",
                    nationality = "England",
                    type = "REFEREE"
                )
            )
        )
    )
}

fun getMockMatchesAhead(): List<Match> {
    return listOf(
        Match(
            id = 538062,
            utcDate = "2026-03-01T14:00:00Z",
            bigDate = "28.02",
            status = MatchStatus.TIMED,
            matchDay = 28,
            stage = Stage.REGULAR_SEASON,
            group = Group.NON,
            competition = Competition(
                name = "Premier League"
            ),
            homeTeam = MatchTeam(
                id = 66,
                name = "Manchester United FC",
                shortName = "Man United",
                tla = "MUN",
                crest = "https://crests.football-data.org/66.png"
            ),
            awayTeam = MatchTeam(
                id = 354,
                name = "Crystal Palace FC",
                shortName = "Crystal Palace",
                tla = "CRY",
                crest = "https://crests.football-data.org/354.png"
            ),
            score = Score(
                winner = Winner.NON,
                duration = "REGULAR",
                fullTime = Time(home = 0, away = 0),
                halfTime = Time(home = 0, away = 0)
            ),
            referees = emptyList()
        ),
        Match(
            id = 538072,
            utcDate = "2026-03-04T20:15:00Z",
            bigDate = "28.02",
            status = MatchStatus.TIMED,
            matchDay = 29,
            stage = Stage.REGULAR_SEASON,
            group = Group.NON,
            competition = Competition(
                name = "Premier League"
            ),
            homeTeam = MatchTeam(
                id = 67,
                name = "Newcastle United FC",
                shortName = "Newcastle",
                tla = "NEW",
                crest = "https://crests.football-data.org/67.png"
            ),
            awayTeam = MatchTeam(
                id = 66,
                name = "Manchester United FC",
                shortName = "Man United",
                tla = "MUN",
                crest = "https://crests.football-data.org/66.png"
            ),
            score = Score(
                winner = Winner.NON,
                duration = "REGULAR",
                fullTime = Time(home = 0, away = 0),
                halfTime = Time(home = 0, away = 0)
            ),
            referees = emptyList()
        ),
        Match(
            id = 538082,
            utcDate = "2026-03-15T14:00:00Z",
            bigDate = "28.02",
            status = MatchStatus.TIMED,
            matchDay = 30,
            stage = Stage.REGULAR_SEASON,
            group = Group.NON,
            competition = Competition(
                name = "Premier League"
            ),
            homeTeam = MatchTeam(
                id = 66,
                name = "Manchester United FC",
                shortName = "Man United",
                tla = "MUN",
                crest = "https://crests.football-data.org/66.png"
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
                fullTime = Time(home = 0, away = 0),
                halfTime = Time(home = 0, away = 0)
            ),
            referees = emptyList()
        ),
        Match(
            id = 538085,
            utcDate = "2026-03-20T20:00:00Z",
            bigDate = "28.02",
            status = MatchStatus.TIMED,
            matchDay = 31,
            stage = Stage.REGULAR_SEASON,
            group = Group.NON,
            competition = Competition(
                name = "Premier League"
            ),
            homeTeam = MatchTeam(
                id = 1044,
                name = "AFC Bournemouth",
                shortName = "Bournemouth",
                tla = "BOU",
                crest = "https://crests.football-data.org/bournemouth.png"
            ),
            awayTeam = MatchTeam(
                id = 66,
                name = "Manchester United FC",
                shortName = "Man United",
                tla = "MUN",
                crest = "https://crests.football-data.org/66.png"
            ),
            score = Score(
                winner = Winner.NON,
                duration = "REGULAR",
                fullTime = Time(home = 0, away = 0),
                halfTime = Time(home = 0, away = 0)
            ),
            referees = emptyList()
        ),
        Match(
            id = 538102,
            utcDate = "2026-04-11T14:00:00Z",
            bigDate = "28.02",
            status = MatchStatus.TIMED,
            matchDay = 32,
            stage = Stage.REGULAR_SEASON,
            group = Group.NON,
            competition = Competition(
                name = "Premier League"
            ),
            homeTeam = MatchTeam(
                id = 66,
                name = "Manchester United FC",
                shortName = "Man United",
                tla = "MUN",
                crest = "https://crests.football-data.org/66.png"
            ),
            awayTeam = MatchTeam(
                id = 341,
                name = "Leeds United FC",
                shortName = "Leeds United",
                tla = "LEE",
                crest = "https://crests.football-data.org/341.png"
            ),
            score = Score(
                winner = Winner.NON,
                duration = "REGULAR",
                fullTime = Time(home = 0, away = 0),
                halfTime = Time(home = 0, away = 0)
            ),
            referees = emptyList()
        ),
        Match(
            id = 538108,
            utcDate = "2026-04-18T14:00:00Z",
            bigDate = "28.02",
            status = MatchStatus.TIMED,
            matchDay = 33,
            stage = Stage.REGULAR_SEASON,
            group = Group.NON,
            competition = Competition(
                name = "Premier League"
            ),
            homeTeam = MatchTeam(
                id = 61,
                name = "Chelsea FC",
                shortName = "Chelsea",
                tla = "CHE",
                crest = "https://crests.football-data.org/61.png"
            ),
            awayTeam = MatchTeam(
                id = 66,
                name = "Manchester United FC",
                shortName = "Man United",
                tla = "MUN",
                crest = "https://crests.football-data.org/66.png"
            ),
            score = Score(
                winner = Winner.NON,
                duration = "REGULAR",
                fullTime = Time(home = 0, away = 0),
                halfTime = Time(home = 0, away = 0)
            ),
            referees = emptyList()
        ),
        Match(
            id = 538122,
            utcDate = "2026-04-25T14:00:00Z",
            bigDate = "28.02",
            status = MatchStatus.TIMED,
            matchDay = 34,
            stage = Stage.REGULAR_SEASON,
            group = Group.NON,
            competition = Competition(
                name = "Premier League"
            ),
            homeTeam = MatchTeam(
                id = 66,
                name = "Manchester United FC",
                shortName = "Man United",
                tla = "MUN",
                crest = "https://crests.football-data.org/66.png"
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
                fullTime = Time(home = 0, away = 0),
                halfTime = Time(home = 0, away = 0)
            ),
            referees = emptyList()
        ),
        Match(
            id = 538132,
            utcDate = "2026-05-02T14:00:00Z",
            bigDate = "28.02",
            status = MatchStatus.TIMED,
            matchDay = 35,
            stage = Stage.REGULAR_SEASON,
            group = Group.NON,
            competition = Competition(
                name = "Premier League"
            ),
            homeTeam = MatchTeam(
                id = 66,
                name = "Manchester United FC",
                shortName = "Man United",
                tla = "MUN",
                crest = "https://crests.football-data.org/66.png"
            ),
            awayTeam = MatchTeam(
                id = 64,
                name = "Liverpool FC",
                shortName = "Liverpool",
                tla = "LIV",
                crest = "https://crests.football-data.org/64.png"
            ),
            score = Score(
                winner = Winner.NON,
                duration = "REGULAR",
                fullTime = Time(home = 0, away = 0),
                halfTime = Time(home = 0, away = 0)
            ),
            referees = emptyList()
        ),
        Match(
            id = 538135,
            utcDate = "2026-05-09T14:00:00Z",
            bigDate = "28.02",
            status = MatchStatus.TIMED,
            matchDay = 36,
            stage = Stage.REGULAR_SEASON,
            group = Group.NON,
            competition = Competition(
                name = "Premier League"
            ),
            homeTeam = MatchTeam(
                id = 71,
                name = "Sunderland AFC",
                shortName = "Sunderland",
                tla = "SUN",
                crest = "https://crests.football-data.org/71.png"
            ),
            awayTeam = MatchTeam(
                id = 66,
                name = "Manchester United FC",
                shortName = "Man United",
                tla = "MUN",
                crest = "https://crests.football-data.org/66.png"
            ),
            score = Score(
                winner = Winner.NON,
                duration = "REGULAR",
                fullTime = Time(home = 0, away = 0),
                halfTime = Time(home = 0, away = 0)
            ),
            referees = emptyList()
        ),
        Match(
            id = 538152,
            utcDate = "2026-05-17T14:00:00Z",
            bigDate = "28.02",
            status = MatchStatus.TIMED,
            matchDay = 37,
            stage = Stage.REGULAR_SEASON,
            group = Group.NON,
            competition = Competition(
                name = "Premier League"
            ),
            homeTeam = MatchTeam(
                id = 66,
                name = "Manchester United FC",
                shortName = "Man United",
                tla = "MUN",
                crest = "https://crests.football-data.org/66.png"
            ),
            awayTeam = MatchTeam(
                id = 351,
                name = "Nottingham Forest FC",
                shortName = "Nottingham",
                tla = "NOT",
                crest = "https://crests.football-data.org/351.png"
            ),
            score = Score(
                winner = Winner.NON,
                duration = "REGULAR",
                fullTime = Time(home = 0, away = 0),
                halfTime = Time(home = 0, away = 0)
            ),
            referees = emptyList()
        )
    )
}

fun getMockPlayer(): Person {
    return Person(
        id = 38101,
        currentTeam = CurrentTeam(
            id = 8872,
            address = "Serviceboks 1, Ullevaal stadion Oslo 0840",
            area = Area(
                id = 2173,
                name = "Norway",
                flag = "https://crests.football-data.org/813.svg"
            ),
            clubColors = "Red / White / Navy Blue",
            crest = "https://crests.football-data.org/813.svg",
            founded = 1902,
            name = "Norway",
            shortName = "Norway",
            tla = "NOR",
            venue = "Ullevaal Stadion",
            website = "http://www.fotball.no"
        ),
        age = "25", // Рассчитано из dateOfBirth 2000-07-21
        firstName = "Erling",
        lastName = "Haaland",
        lastUpdated = "2025-04-02T09:41:03Z",
        name = "Erling Haaland",
        nationality = "Norway",
        position = PlayerPosition.OFFENCE,
        section = "Centre-Forward",
        shirtNumber = 22
    )
}