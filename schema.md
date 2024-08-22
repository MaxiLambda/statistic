# Db Schema

## Player (players)
* id - int PRIMARY KEY
* name - varchar(15) UNIQUE

## Match (matches)
* id - int PRIMARY KEY
* date - timestamp with time zone
* winner - smallint => (0,1,2) => draw, team1, team2
* discipline - varchar(20)
* tag - varchar(20)

## Player-Match (player_matches)
* match_id - int PRIMARY KEY
* player_id - int PRIMARY KEY
* team - smallint => (1,2) => team1, team2