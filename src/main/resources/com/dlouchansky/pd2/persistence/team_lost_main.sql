select 
    t.id team,
    count(g.id) isLoser
from teams t
left join gameTeams gt on t.id = gt.teams_id
left join games g on (gt.games_id = g.id ) 
where gt.isWinner = false and g.winGamePart = 0
group by t.id