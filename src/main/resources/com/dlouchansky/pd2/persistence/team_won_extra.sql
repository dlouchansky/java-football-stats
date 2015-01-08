select 
    t.id team,
    count(g.id) isWinner
from teams t
left join gameTeams gt on t.id = gt.teams_id
left join games g on (gt.games_id = g.id ) 
where gt.isWinner = true and g.winGamePart = 1
group by t.id