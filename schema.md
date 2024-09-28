# Db Schema

## Space (spaces)
* id - int PRIMARY KEY
* name - varchar(20) UNIQUE
* view_password - varchar(20)
* edit_password - varchar(20)

## Player (players)
* id - int PRIMARY KEY
* name - varchar(15) UNIQUE => (combined with space_id)
* space_id - int REFERENCES spaces

## Match (matches)
* id - int PRIMARY KEY
* space_id - int REFERENCES spaces
* date - timestamp with time zone
* winner - smallint => (0,1,2) => draw, team1, team2
* discipline - varchar(20)
* tag - varchar(20)

## Player-Match (player_matches)
* match_id - int PRIMARY KEY REFERENCES matches
* player_id - int PRIMARY KEY REFERENCES players
* team - smallint => (1,2) => team1, team2

# Domain Objects

## Space
* a Space groups Matches and Players in a context
* Players are unique in a space
* each Space as separate authorization

## Player
* a Player is a real World actor who can participate in Matches

## Match

* a Match is an Event, where two teams compete